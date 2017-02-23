package com.ztom.container.factory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

public class ZtomServletFactory{
	private static final Logger logger = (Logger) LoggerFactory.getLogger(ZtomServletFactory.class);
	private static final String SERVICE_ROOT_PATH = "ztom/webapp/service/";
	private Map<String, Object> map;
	private List<String> fileList;
	private List<String> servletConfig;
	private URL[] jarUrlList;
	
	public ZtomServletFactory(){
		servletConfig = new ArrayList<>();
		map = new HashMap<>();
		
		this.setServiceList(SERVICE_ROOT_PATH);
		this.setJarUrlList();
		
		for(String fileName : fileList){
			this.setServletConfig(fileName);			
		}
		
		for(String json : servletConfig){
			this.readServlet(json);			
		}
	}

	// webapp/service ������ jar���� �о����
	public void setServiceList(String source){
		fileList = new ArrayList<>();
		File dir = new File(source); 
		
		for(File file : dir.listFiles()){
			if(file.isFile()){
				fileList.add(file.getName());
			}
		}
	}

	// �������� ���� jar ������ url setting -> URL[] jarUrlList
	public void setJarUrlList(){
		jarUrlList = new URL[this.getJarFileList().size()];
		File jarFile = null;
		String jarFileName;
		try {
			for(int i=0;i<jarUrlList.length;i++){
				jarFileName = this.getJarFileList().get(i);
				jarFile = new File("ztom/webapp/service/"+jarFileName);
				jarUrlList[i] = (new URL("jar:" + jarFile.toURI().toURL() + "!/"));
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			logger.error("Ztom Jar file binding fail",e);
		}
	}
	// jar ���� url getter
	public URL[] getJarUrlList(){
		return this.jarUrlList;
	}

	//servlet���� json data �߰�
	public void appendServletConfig(String json){
		this.servletConfig.add(json);
	}
	// jar���� ��� ��ü getter
	public List<String> getJarFileList(){
		return this.fileList;
	}

	//�о���� jar ���Ͽ��� servlet config�� �����͸� �о����
	public void setServletConfig(String fileName){
		JarFile jarFile;
		try {
			jarFile = new JarFile(SERVICE_ROOT_PATH+fileName);
			JarEntry entry = jarFile.getJarEntry("conf/servlet.json");
			InputStreamReader in = new InputStreamReader(jarFile.getInputStream(entry), "UTF-8");
			BufferedReader reader = new BufferedReader(in);
			
    		StringBuffer data = new StringBuffer();
    		
    	    String line;
    	    while ((line = reader.readLine()) != null){
    	    	data.append(line.trim());
    	    }
    	    reader.close();
    	    this.appendServletConfig(data.toString());
    	    logger.debug("Ztom Service Servlet Config Append : "+fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Ztom Service Servlet binding fail",e);
		}
	}

	//json�� ���õ� servlet ������ �Ľ��ؼ� map�� ������.
	public void readServlet(String json){
		JSONParser parser = new JSONParser();
        
		try {
			Object obj = parser.parse(json);
			
			JSONObject jsonObject = (JSONObject) obj;
			 
			// loop array
			JSONArray servlets = (JSONArray) jsonObject.get("servlet");
			
			Object[] servlet = servlets.toArray();
			for(Object ele : servlet){
				JSONObject json_obj = (JSONObject) ele;
				setReadSetting(String.valueOf(json_obj.get("url")), String.valueOf(json_obj.get("class")));
				logger.debug("Ztom Service Servlet registered  : "+json_obj.get("url") + ":" +json_obj.get("class"));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.error("Ztom Service Servlet binding fail",e);
		}
	}

	//servlet ��� ��ü setter
	public void setReadSettingList(Map<String, Object> map){
		this.map = map;
	}
	//��ϵ� servlet ��� ��ü getter
	public Map<String, Object> getReadSettingList(){
		return this.map;
	}
	//��ϵ� servlet ��� �ܰ� getter
	public Object getReadSetting(String key){
		return map.get(key);
	}
	//��ϵ� servlet ��Ͽ� �ܰ� �߰�
	public Object setReadSetting(String key, Object val){
		return map.put(key, val);
	}
}
