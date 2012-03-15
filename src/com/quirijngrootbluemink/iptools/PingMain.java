package com.quirijngrootbluemink.iptools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.markupartist.android.widget.ActionBar;

public class PingMain extends Activity{
	Button btnPing;
	EditText txtAddress;
	LinearLayout pingResultsLayout;
	List<String> pingResults;
	ProgressDialog dialog;	
	int numberOfPings;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ping_main);

        setActionBar();

        btnPing = (Button) findViewById(R.id.btnPing);
        txtAddress = (EditText) findViewById(R.id.txtAddress);
        pingResultsLayout = (LinearLayout) findViewById(R.id.pingResults);        
        pingResults = new ArrayList<String>();
        
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        numberOfPings = Integer.parseInt(prefs.getString("numPings","6"));
        
        txtAddress.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_GO){
					if(checkIpDomain(txtAddress.getText().toString())){
						new PingTask().execute(txtAddress.getText().toString());
					}else{
						showMe(getString(R.string.invalid_ip_domain));
					}
				}
				return true;
			}
		});
		
        btnPing.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(checkIpDomain(txtAddress.getText().toString())){
					new PingTask().execute(txtAddress.getText().toString());
				}else{
					showMe(getString(R.string.invalid_ip_domain));
				}
					

			}
		});
        
        String s = (String) this.getIntent().getStringExtra("Address");
        if(s != null){
    		Log.d("PingMain", "Extra is not null");
    		Log.d("PingMain", "IP " + s);
        	txtAddress.setText(s);
        	if(pingResults.isEmpty())
        		new PingTask().execute(txtAddress.getText().toString());
        }
	}
	public void showMe(String s){
    	Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
	private boolean checkIpDomain(String value){
		String IprRegex = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
		Pattern patt = Pattern.compile(IprRegex);
		Matcher matcher = patt.matcher(value);
		if(matcher.matches()){
			Log.d("UtilitiesMain","Valid IP");
			return true;
		}else{
			Log.d("UtilitiesMain","Invalid IP");
			//
			IprRegex = "^[a-z0-9]*\\.[a-z]*$";
			patt = Pattern.compile(IprRegex);
			 matcher = patt.matcher(value);
			if(matcher.matches()){
				Log.d("UtilitiesMain","Valid Domain");
				return true;
			}else{
				Log.d("UtilitiesMain","Invalid Domain");
				return false;
			}
		}
		
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
	private void showProgressDialog(){
		dialog = ProgressDialog.show(PingMain.this, "", getString(R.string.pinging) + " " + getString(R.string.please_wait), true);
		Log.d("PingMain", "Show progress");
	}
	private void hideProgressDialog(){
		dialog.dismiss();
		Log.d("PingMain", "Hide progress");
	}
	private void showResults(){

		for (String s : pingResults) {
		    TextView tv = new TextView(this);
			tv.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
			tv.setText(s);
			pingResultsLayout.addView(tv);
			
		}
	}

	private class PingTask extends AsyncTask<String, Void, List<String>>{
		private List<String> sendPing(String address){

			List<String> results = new ArrayList<String>();
			try {
				Log.d("PingMain", "Sending ping");
				
		        Runtime r = Runtime.getRuntime();
				String pingCmd = "ping -c" + numberOfPings + " " + txtAddress.getText().toString();
				Process p;
				
				p = r.exec(pingCmd);
				
				BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()),8000);
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					Log.d("PingMain", "Reading ping: " + inputLine);					
					results.add(inputLine);
				}
				in.close();
				if(results.isEmpty()){
					Log.d("PingMain", "No data");					
					results.add(getString(R.string.ping_no_host) + " "+address+". " + getString(R.string.check_name));
				}
				Log.d("PingMain", "Reader closed");
				return results;

			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		@Override
		protected List<String> doInBackground(String... params) {			
			return sendPing(params[0]);
		}
		@Override
		protected void onPostExecute(List<String> result) {
			pingResults = result;
			showResults();
			hideProgressDialog();
		}
		@Override
		protected void onPreExecute() {  
			if(pingResults != null)
				pingResults.clear();
				pingResultsLayout.removeAllViews();
			showProgressDialog();
		}
		
		
		
	}
}
