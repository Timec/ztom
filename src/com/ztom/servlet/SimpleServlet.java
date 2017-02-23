package com.ztom.servlet;

import com.ztom.servlet.http.HttpRequest;
import com.ztom.servlet.http.HttpResponse;

public interface SimpleServlet {
	public void service(HttpRequest req, HttpResponse res);
}
