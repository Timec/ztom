package com.ztom.vo;

public class ConnectorVO {
	private String docRoot = "";
	private String port = "";
	
	public ConnectorVO(String docRoot, String port){
		this.docRoot = docRoot;
		this.port = port;
	}
	
	public String getDocRoot() {
		return docRoot;
	}
	public void setDocRoot(String docRoot) {
		this.docRoot = docRoot;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
}