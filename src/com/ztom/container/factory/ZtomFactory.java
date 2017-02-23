package com.ztom.container.factory;

public class ZtomFactory{
	private ZtomConfigFactory config;
	private ZtomServletFactory servlet;
	
	public ZtomFactory(){
		config = new ZtomConfigFactory();
		servlet = new ZtomServletFactory();
	}
	
	public ZtomConfigFactory getConfig() {
		return config;
	}
	public void setConfig(ZtomConfigFactory config) {
		this.config = config;
	}
	public ZtomServletFactory getServlet() {
		return servlet;
	}
	public void setServlet(ZtomServletFactory servlet) {
		this.servlet = servlet;
	}
}
