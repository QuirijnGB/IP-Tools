package com.quirijngrootbluemink.iptools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class WifiScanChartReciever extends BroadcastReceiver {
//	  private static final String TAG = "WiFiScanChartReceiver";
	  WifiChannelChart wifiChart;

	public WifiScanChartReciever(WifiChannelChart wifiChart) {
		super();
	    this.wifiChart = wifiChart;
	}

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		wifiChart.setScanResults(wifiChart.wifi.getScanResults());
		wifiChart.addChartSeries();
		
	}

}

