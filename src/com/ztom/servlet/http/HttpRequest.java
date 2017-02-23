package com.ztom.servlet.http;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
	private Map<String, Object> req;
	
	public HttpRequest(){
		req = new HashMap<>();
	}
	
	public HttpRequest(String data){
		req = new HashMap<>();
		if(data.indexOf("?") != -1){
			data = data.substring(data.indexOf("?")+1);
			parameter2RequestMap(data);			
		}
	}
	
	public String getMethod(){
		return String.valueOf(req.get("method"));
	}
	
	public Object getParameter(String key){
		return String.valueOf(req.get(key));
	}
	
	public String getRemoteAddr(){
		return String.valueOf(req.get("remoteAddr"));
	}
	
	private void parameter2RequestMap(String data){
		String[] params = data.split("&");
		
		String[] elements;
		for(String ele: params){
			elements = ele.split("=");
			req.put(elements[0], elements[1]);
		}
	}
}
