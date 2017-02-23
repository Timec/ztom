package com.ztom.container;

import ch.qos.logback.classic.Logger;
import com.ztom.container.factory.ZtomConfigFactory;
import com.ztom.container.factory.ZtomFactory;
import com.ztom.container.factory.ZtomServletFactory;
import com.ztom.servlet.SimpleServlet;
import com.ztom.servlet.http.HttpRequest;
import com.ztom.servlet.http.HttpResponse;
import com.ztom.vo.ConfigVO;
import com.ztom.vo.HostVO;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.URLClassLoader;
import java.nio.file.Files;

public class ZtomContainer implements Runnable{
	private static final Logger logger = (Logger) LoggerFactory.getLogger(ZtomContainer.class);
	private final Socket connection;
	private final ZtomServletFactory servletFactory;
	private final ZtomConfigFactory configFactory;
	private final ConfigVO configVo;
	
	public ZtomContainer(Socket connection, ZtomFactory ztomFactory){
		this.connection = connection;
		this.servletFactory = ztomFactory.getServlet();
		this.configFactory = ztomFactory.getConfig();
		this.configVo = configFactory.getConfigVO();
	}
	
	@Override
	public void run() {
		Class<? extends Object> clazz;
		OutputStream raw = null;
		HostVO host = null;
		URLClassLoader classLoader = null;
        try {
        	logger.debug("User Connect from : "+connection.getInetAddress().getHostAddress());
        	raw = new BufferedOutputStream(connection.getOutputStream());
        	Reader in = new InputStreamReader(new BufferedInputStream(connection.getInputStream()), "UTF-8");
    		StringBuffer requestData = new StringBuffer();
    		
    		while(true){
    			int c = in.read();
    			if(c=='\r' || c == '\n')
    				break;
    			requestData.append((char) c);
    		}
    		
    		String data = requestData.toString();
    		String[] tokens = data.split("\\s");
    		String fileName = tokens[1];
    		String url = fileName;
    		
    		if(fileName.indexOf("?") != -1){
    			url = fileName.substring(0, fileName.indexOf("?"));
    		}
    		
    		host = configVo.getHost(connection.getInetAddress().getHostAddress());
    		
    		if(null == host){
    			host = configVo.getHost("default");
    		}

			//403 error
			if(url.endsWith(".exe") || url.indexOf("../") > 0){
				goErrorPage(raw, host.getErrorPage_403());
				connection.close();
				return;
			}
    		
    		//404 error
    		Object servletClazz = servletFactory.getReadSetting(url);
    		if(null == servletClazz){
    			goErrorPage(raw, host.getErrorPage_404());
    			connection.close();
    			return;
    		}
    		
    		//html transfer
    		if(url.endsWith(".html")){
    			goHtmlPage(raw, url);
    			connection.close();
    			return;
    		}
            classLoader = new URLClassLoader(servletFactory.getJarUrlList());
            clazz = classLoader.loadClass(servletClazz.toString());
            Method servletMethod = clazz.getDeclaredMethod("service", HttpRequest.class, HttpResponse.class);
            SimpleServlet o = (SimpleServlet)clazz.newInstance();
			servletMethod.invoke(o, new HttpRequest(fileName), new HttpResponse(connection));

		} catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
				InstantiationException | InvocationTargetException| IOException e) {
			logger.error(e.getMessage(), e);
			goErrorPage(raw, host.getErrorPage_500());
		} catch (NullPointerException e){
			logger.error(e.getMessage(), e);
			goErrorPage(raw, host.getErrorPage_404());
		} finally{
			try {
                if(connection != null) {
					connection.close();
				}
                if(classLoader != null) {
					classLoader.close();
				}
            } catch (IOException e) {
            	logger.error(e.getMessage(), e);
                goErrorPage(raw, host.getErrorPage_500());
            }
        }
	}
	// error �������� ������
	public void goErrorPage(OutputStream raw, String fileName)throws NullPointerException{
		goPage(raw, fileName);
    }
	//�Ϲ� html ������ ����
	public void goHtmlPage(OutputStream raw, String fileName)throws NullPointerException{
		goPage(raw, fileName);
    }

    public void goPage(OutputStream raw, String fileName)throws NullPointerException{
		try {
			File theFile = new File(fileName);

			if (theFile.canRead()) {
				byte[] theData = Files.readAllBytes(theFile.toPath());
				raw.write(theData);
				raw.flush();
			}
		} catch (IOException | NullPointerException e) {
			throw new NullPointerException(e.getMessage());
		}
	}
}
