package com.quirijngrootbluemink.iptools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class WifiScanReciever extends BroadcastReceiver {
//	  private static final String TAG = "WiFiScanReceiver";
	  WifiMain wifiMain;

	public WifiScanReciever(WifiMain wifiDemo) {
		super();
	    this.wifiMain = wifiDemo;
	}

	@Override
	public void onReceive(Context arg0, Intent arg1) {
	    wifiMain.setScanResults(wifiMain.wifi.getScanResults());
	    wifiMain.updateNetworks();
		
	}

}

