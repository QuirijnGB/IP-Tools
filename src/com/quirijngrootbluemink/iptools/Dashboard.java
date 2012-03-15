package com.quirijngrootbluemink.iptools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.markupartist.android.widget.ActionBar;

public class Dashboard extends Activity {
	RelativeLayout btnIpCalc, btnUtilities, btnWifi;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        btnIpCalc = (RelativeLayout) findViewById(R.id.IPCalc);
        btnUtilities = (RelativeLayout) findViewById(R.id.Utilities);
        btnWifi = (RelativeLayout) findViewById(R.id.Wifi);

        setActionBar();
        btnIpCalc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), IpCalcMain.class);
                startActivity(myIntent);
			}
		});        
        btnUtilities.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), UtilitiesMain.class);
                startActivity(myIntent);
			}
		});       
        btnWifi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
				if(wifi.isWifiEnabled()){
	                Intent myIntent = new Intent(v.getContext(), WifiMain.class);
	                startActivityForResult(myIntent, 0);
				}else{
					showWifiDialog();

					
				}
			}
		});
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_menu, menu);
	    return true;
	}	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.settings:
	    	showSettings();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	private void showSettings(){
		Intent newIntent = new Intent(this.getApplicationContext(), Preferences.class);
		startActivity(newIntent);
		
	}
	private void setActionBar(){
        ActionBar actionBar = (ActionBar) findViewById(R.id.actionBar);
        actionBar.setTitle("IP Tools");
        //actionBar.addAction(new IntentAction(this, homeIntent, R.drawable.ic_title_export_default));
	}
    public void showWifiDialog(){
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		        	startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
		            break;

		        case DialogInterface.BUTTON_NEGATIVE:
	                dialog.cancel();
		            break;
		        }
		    }
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("WiFi is disconnected, Go to Settings?").setPositiveButton("Yes", dialogClickListener)
		    .setNegativeButton("No", dialogClickListener).show();
    }
    public void showMe(String s){
    	Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
    
}