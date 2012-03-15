package com.quirijngrootbluemink.iptools;

public class WifiListItem {
	private String ssid;
	private int signal_strength;
	private boolean isConnected;
	
	public WifiListItem(String ssid, int signal_strength, boolean isConnected) {
		super();
		this.ssid = ssid;
		this.signal_strength = signal_strength;
		this.isConnected = isConnected;
	}
	public String getSsid() {
		return ssid;
	}
	public void setSsid(String ssid) {
		this.ssid = ssid;
	}
	public int getSignal_strength() {
		return signal_strength;
	}
	public void setSignal_strength(int signal_strength) {
		this.signal_strength = signal_strength;
	}
	public boolean isConnected() {
		return isConnected;
	}
	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}
	
	
	

}
