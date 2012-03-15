package com.quirijngrootbluemink.iptools;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.markupartist.android.widget.ActionBar;

public class WifiDetails extends Activity{

	WifiInfo info;
	WifiManager wifi;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_details);
		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		info = wifi.getConnectionInfo();
        
        setActionBar();
        
        ScanResult s = (ScanResult) this.getIntent().getParcelableExtra("Wifi");
 
        if(s.SSID.equals(info.getSSID())){
            TextView txtHiddenSSID = (TextView) findViewById(R.id.txtHiddenSSID);
            TextView txtLinkSpeed = (TextView) findViewById(R.id.txtLinkSpeed);
            TextView txtRSSI = (TextView) findViewById(R.id.txtRSSI);
            TextView txtIP= (TextView) findViewById(R.id.txtIP);
            
            txtHiddenSSID.setText((info.getHiddenSSID()) ? getString(R.string.yes) : getString(R.string.no));            
            txtLinkSpeed.setText(info.getLinkSpeed()+getString(R.string.mbps));            
            txtRSSI.setText(info.getRssi()+getString(R.string.dbm));
            txtIP.setText(intToIp(info.getIpAddress()));

            TextView lblHiddenSSID =  (TextView) findViewById(R.id.lblHiddenSSID);
            TextView lblLinkSpeed =  (TextView) findViewById(R.id.lblLinkSpeed);
            TextView lblRSSI =  (TextView) findViewById(R.id.lblRSSI);
            TextView lblIP =  (TextView) findViewById(R.id.lblIP);

            txtHiddenSSID.setVisibility(0);            
            txtLinkSpeed.setVisibility(0);          
            txtRSSI.setVisibility(0);
            txtIP.setVisibility(0);

            lblHiddenSSID.setVisibility(0);
            lblLinkSpeed.setVisibility(0);
            lblRSSI.setVisibility(0);
            lblIP.setVisibility(0);
            
            
        }
        TextView txtSSID = (TextView) findViewById(R.id.txtSSID);
        TextView txtBSSID = (TextView) findViewById(R.id.txtBSSID);
        TextView txtFreq = (TextView) findViewById(R.id.txtFreq);
        TextView txtStrength = (TextView) findViewById(R.id.txtStrength);
        TextView txtInfo = (TextView) findViewById(R.id.txtInfo);
        TextView txtChan = (TextView) findViewById(R.id.txtChan);
        

        txtSSID.setText(s.SSID+"");
        txtBSSID.setText(s.BSSID+"");
        txtFreq.setText(s.frequency+getString(R.string.hz));
        txtChan.setText(calculateChannel(s.frequency)+"");
        txtStrength.setText(s.level+getString(R.string.dbm));
        
        
        
        if(s.capabilities.equals("")){
            txtInfo.setText("Open network");
        }else{
            txtInfo.setText(s.capabilities);
        }
        
    }	
	public String intToIp(int i) {

		   return ((i ) & 0xFF ) + "." + ((i >> 8 ) & 0xFF) + "." +((i >> 16 ) & 0xFF) + "." +( i  >> 24 & 0xFF) ;
	}
	public void showMe(String s){
    	Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
	private void setActionBar(){
        ActionBar actionBar = (ActionBar) findViewById(R.id.actionBar);
        actionBar.setTitle(getString(R.string.app_name));
        actionBar.setDisplayHomeAsUpEnabled(true);
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
    private int calculateChannel(int freq){
    	int channel = -1;
    	switch (freq) {
		case 2412:
			channel = 1;
			break;
		case 2417:
			channel = 2;
			break;
		case 2422:
			channel = 3;
			break;
		case 2427:
			channel = 4;
			break;
		case 2432:
			channel = 5;
			break;
		case 2437:
			channel = 6;
			break;
		case 2442:
			channel = 7;
			break;
		case 2447:
			channel = 8;
			break;
		case 2452:
			channel = 9;
			break;
		case 2457:
			channel = 10;
			break;
		case 2462:
			channel = 11;
			break;
		case 2467:
			channel = 12;
			break;
		case 2472:
			channel = 13;
			break;
		case 2484:
			channel = 14;
			break;

		default:
			channel = -1;
		}
    	return channel;
    }
	

}
