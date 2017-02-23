package com.ztom.vo;

import java.util.ArrayList;
import java.util.List;

public class ConfigVO {
	private List<HostVO> host = new ArrayList<HostVO>();
	private ConnectorVO connector;
	
	public ConnectorVO getConnector() {
		return connector;
	}
	public void setConnector(ConnectorVO connector) {
		this.connector = connector;
	}
	public List<HostVO> getHost() {
		return host;
	}
	public HostVO getHost(String domain) {
		for(HostVO host : host){
			if(null != host.getDomain() && domain.equalsIgnoreCase(host.getDomain())){
				return host;
			}
		}
		return null;
	}
	public void setHost(List<HostVO> host) {
		this.host = host;
	}
	public void appendHost(HostVO host) {
		this.host.add(host);
	}
}