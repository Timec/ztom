package com.ztom.servlet.http;

import java.io.*;
import java.net.Socket;

public class HttpResponse {
	Writer writer;
	
	public HttpResponse(){
	}
	
	public HttpResponse(Socket connection){
		try {
			OutputStream raw = new BufferedOutputStream(connection.getOutputStream());
			this.writer = new OutputStreamWriter(raw);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Writer getWriter(){
		return writer;
	}
}
