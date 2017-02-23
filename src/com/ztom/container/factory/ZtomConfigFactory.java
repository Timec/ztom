package com.ztom.container.factory;

import com.ztom.vo.ConfigVO;
import com.ztom.vo.ConnectorVO;
import com.ztom.vo.HostVO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class ZtomConfigFactory{
	private ConfigVO configVo;
	public ZtomConfigFactory(){
		configVo = new ConfigVO();
		this.readConfig();
	}
	
	public void readConfig(){
        try {
            JSONParser parser = new JSONParser();
            
            Object obj = parser.parse(new FileReader("ztom/conf/conf.json"));
            
    		JSONObject jsonObject = (JSONObject) obj;
     
    		JSONObject connector = (JSONObject) jsonObject.get("Connector");
    		
    		configVo.setConnector(new ConnectorVO(String.valueOf(connector.get("docRoot")), String.valueOf(connector.get("port"))));
     
    		// loop array
    		JSONArray host = (JSONArray) jsonObject.get("Host");
    		
    		Object[] host_ele = host.toArray();
    		HostVO hostVo;
    		for(Object ele : host_ele){
    			JSONObject json_obj = (JSONObject) ele;
    			hostVo = new HostVO();
    			hostVo.setDomain(String.valueOf(json_obj.get("domain")));
    			hostVo.setRoot(String.valueOf(json_obj.get("domain")));
    			hostVo.setErrorPage_403(String.valueOf(json_obj.get("errorPage_403")));
    			hostVo.setErrorPage_404(String.valueOf(json_obj.get("errorPage_404")));
    			hostVo.setErrorPage_500(String.valueOf(json_obj.get("errorPage_500")));
    			configVo.appendHost(hostVo);
    		}
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Usage: java JHTTP docroot port");
            return;
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ConfigVO getConfigVO(){
		return configVo;
	}
}
