package com.ztom.vo;

public class HostVO {
	private String domain = "";
	private String root = "";
	private String errorPage_403 = "";
	private String errorPage_404 = "";
	private String errorPage_500 = "";
	
	public HostVO(){}
	
	public HostVO(String domain, String root, String errorPage_403, String errorPage_404, String errorPage_500){
		this.domain = domain;
		this.root = root;
		this.errorPage_403 = errorPage_403;
		this.errorPage_404 = errorPage_404;
		this.errorPage_500 = errorPage_500;
	}
	
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getRoot() {
		return root;
	}
	public void setRoot(String root) {
		this.root = root;
	}
	public String getErrorPage_403() {
		return errorPage_403;
	}
	public void setErrorPage_403(String errorPage_403) {
		this.errorPage_403 = errorPage_403;
	}
	public String getErrorPage_404() {
		return errorPage_404;
	}
	public void setErrorPage_404(String errorPage_404) {
		this.errorPage_404 = errorPage_404;
	}
	public String getErrorPage_500() {
		return errorPage_500;
	}
	public void setErrorPage_500(String errorPage_500) {
		this.errorPage_500 = errorPage_500;
	}
}