package com.quirijngrootbluemink.iptools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.markupartist.android.widget.ActionBar;

public class UtilitiesMain extends Activity{
	RelativeLayout btnNsLookup,  btnPing ;
	TextView txtAddress;
    DhcpInfo d;
    WifiManager wifii;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.utilities_main);	
		btnPing = (RelativeLayout) findViewById(R.id.btnPing);
		btnNsLookup = (RelativeLayout) findViewById(R.id.btnNsLookup);
		txtAddress = (TextView) findViewById(R.id.txtAddress);
		setActionBar();
		btnPing.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(checkIpDomain(txtAddress.getText().toString())){
					sendPing();
				}else{
					//Log.d("UtilitiesMain","Not an IP");
					showMe(getString(R.string.invalid_ip_domain));
				}
					
			}
		});
		
		btnNsLookup.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				sendNSLookup();
			}
		});
		getDefaultGateway();
	}
	private void getDefaultGateway(){
        wifii= (WifiManager) getSystemService(Context.WIFI_SERVICE);
        d=wifii.getDhcpInfo();
        txtAddress.setText(intToIp(d.gateway));
        
	}
	public String intToIp(int i) {

		   return ((i ) & 0xFF ) + "." + ((i >> 8 ) & 0xFF) + "." +((i >> 16 ) & 0xFF) + "." +( i  >> 24 & 0xFF) ;
	}
	private boolean checkIpDomain(String value){
		String IprRegex = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
		Pattern patt = Pattern.compile(IprRegex);
		Matcher matcher = patt.matcher(value);
		if(matcher.matches()){
			//Log.d("UtilitiesMain","Valid IP");
			return true;
		}else{
			//Log.d("UtilitiesMain","Invalid IP");
			//
			IprRegex = "^[a-z0-9]*\\.[a-z]*$";
			patt = Pattern.compile(IprRegex);
			 matcher = patt.matcher(value);
			if(matcher.matches()){
				//Log.d("UtilitiesMain","Valid Domain");
				return true;
			}else{
				//Log.d("UtilitiesMain","Invalid Domain");
				return false;
			}
		}
		
	}
	private void sendPing(){
		Intent newIntent = new Intent(this.getApplicationContext(), PingMain.class);
		newIntent.putExtra("Address", txtAddress.getText().toString());
		startActivity(newIntent);		
	}

	private void sendNSLookup(){
		Intent newIntent = new Intent(this.getApplicationContext(), NSLookupMain.class);
		newIntent.putExtra("Address", txtAddress.getText().toString());
		startActivity(newIntent);		
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
	public void showMe(String s){
    	Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
}


