package com.quirijngrootbluemink.iptools;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class WifiMain extends Activity {
	List<ScanResult> scanResults;
	Button btnScan;
	ProgressBar pb;
	ListView wifiNetworksList;
	WifiManager wifi;
	BroadcastReceiver receiver;
	WifiInfo info;

	Handler handlr;
	Runnable mUpdateScanTask;
	Intent chartIntent;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.wifi_main_menu, menu);
	    return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.charts:
	    	showChannelChart();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	private void setActionBar(){
        ActionBar actionBar = (ActionBar) findViewById(R.id.actionBar);
        actionBar.setTitle(getString(R.string.app_name));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.addAction(new Action() {
			
			@Override
			public void performAction(View view) {
				wifi.startScan();
				if(scanResults != null)
					updateNetworks();
			}
			
			@Override
			public int getDrawable() {
				return R.drawable.ic_menu_refresh;
			}
		});
        actionBar.addAction(new IntentAction(this, new Intent(this, WifiChannelChart.class), R.drawable.ic_charts));
        actionBar.setOnTitleClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goUp();
				
			}
		});
	}
	private void goUp(){
		this.finish();
	}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_main);

		chartIntent = new Intent(this.getApplicationContext(), WifiChannelChart.class);
        registerReciever();
        setActionBar();
     	handlr = new Handler();
     	
     	mUpdateScanTask = new Runnable() {
     	   public void run() {
				wifi.startScan();
				if(scanResults != null)
					updateNetworks();
				handlr.postDelayed(this, 5000);
     	   }
     	};
     	
     	handlr.removeCallbacks(mUpdateScanTask);
     	handlr.postDelayed(mUpdateScanTask, 500);
		// Setup WiFi
		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		wifiNetworksList = (ListView) findViewById(R.id.wifiNetworksList);
		scanResults = wifi.getScanResults();
		info = wifi.getConnectionInfo();		
		
		if(scanResults != null)
			updateNetworks();

    }
    private void registerReciever(){
        // Register Broadcast Receiver
     		if (receiver == null)
     			receiver = new WifiScanReciever(this);

     		registerReceiver(receiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }
    private void unregisterReciever(){
        // Register Broadcast Receiver
     		if (receiver != null)
     			unregisterReceiver(receiver);
    }
    
    @Override
    protected void onPause(){
        try {
    		handlr.removeCallbacks(mUpdateScanTask);
    		   unregisterReciever();
    	} catch (Exception e) {
    	}
       super.onPause();

    }    
    @Override
    protected void onDestroy(){
       try {
		handlr.removeCallbacks(mUpdateScanTask);
		   unregisterReciever();
	} catch (Exception e) {
	}
       super.onDestroy();

    }    
    @Override
    protected void onStop(){
        try {
    		handlr.removeCallbacks(mUpdateScanTask);
    		   unregisterReciever();
    	} catch (Exception e) {
    	}
       super.onStop();

    } 
    @Override
    protected void onResume(){
        registerReciever();
		handlr.postDelayed(mUpdateScanTask, 10000);
        super.onResume();

     }
    public List<ScanResult> getScanResults() {
		return scanResults;
	}

	public void setScanResults(List<ScanResult> scanResults) {
		this.scanResults = scanResults;
	}

	public void updateNetworks(){
		
		ArrayList<WifiListItem> ssids = new ArrayList<WifiListItem>();		
		for (ScanResult s : scanResults) {
			WifiListItem w = new WifiListItem(s.SSID, s.level, false);
			if(s.SSID.equals(info.getSSID()))
				w.setConnected(true);
			
			ssids.add(w);					
			
			
		}
		
		wifiNetworksList.setAdapter(new WifiAdapter(this, R.layout.wifi_list_item, ssids));
		wifiNetworksList.setClickable(true);
		wifiNetworksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int arg2,
					long arg3) {
				
				showWifiDetails(arg2);
			}
			
		});

    }
	private void showChannelChart(){
		startActivity(chartIntent);
	}
    public void showWifiDetails(int id){
		ScanResult s = scanResults.get(id);
		Intent newIntent = new Intent(this.getApplicationContext(), WifiDetails.class);
		newIntent.putExtra("Wifi", s);
		startActivity(newIntent);
    	
    }
    public void showMe(String s){
    	Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
}
