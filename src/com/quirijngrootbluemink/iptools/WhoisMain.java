package com.quirijngrootbluemink.iptools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.markupartist.android.widget.ActionBar;

public class WhoisMain extends Activity {
	Button btnRequest;
	EditText txtURL;
	LinearLayout pingResultsLayout;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.whois_main);
        btnRequest = (Button) findViewById(R.id.btnRequest);
        txtURL = (EditText) findViewById(R.id.txtURL);
        pingResultsLayout = (LinearLayout) findViewById(R.id.pingResults);
        setActionBar();
        btnRequest.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Log.d("Whois Main", "Clicked request");
				checkWhois();
			}
		});
	}
	private void setActionBar(){
        ActionBar actionBar = (ActionBar) findViewById(R.id.actionBar);
        actionBar.setTitle("IP Tools");
        //actionBar.addAction(new IntentAction(this, homeIntent, R.drawable.ic_title_export_default));
	}
	private void checkWhois(){
		List<String> results = new ArrayList<String>();
		try {
			//Log.d("Whois Main", "Sending whois request");
			
	        Runtime r = Runtime.getRuntime();
			String pingCmd = "nslookup 65.55.175.254" ;
			Process p;
			
			p = r.exec(pingCmd);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()),8000);
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				//Log.d("Whois Main", "Reading whois: " + inputLine);					
				results.add(inputLine);
			}
			in.close();
			//Log.d("Whois Main", "Reader closed");		
			for (String s : results) {
			    TextView tv = new TextView(this);
				tv.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
				tv.setText(s);
				pingResultsLayout.addView(tv);
				
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
