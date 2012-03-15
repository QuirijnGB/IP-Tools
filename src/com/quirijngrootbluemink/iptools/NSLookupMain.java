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
import android.os.AsyncTask;
import android.os.Bundle;
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

public class NSLookupMain extends Activity{
	Button btnSearch;
	EditText txtAddress;
	LinearLayout resultsLayout;
	List<String> results;
	ProgressDialog dialog;	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nslookup_main);

        setActionBar();

        btnSearch = (Button) findViewById(R.id.btnSearch);
        txtAddress = (EditText) findViewById(R.id.txtAddress);
        resultsLayout = (LinearLayout) findViewById(R.id.results);
        
        String s = (String) this.getIntent().getStringExtra("Address");
        if(s != null){
    		//Log.d("NsLookupMain", "Extra is not null");
    		//Log.d("NsLookupMain", "IP " + s);
        	txtAddress.setText(s);
			new NSLookupTask().execute(txtAddress.getText().toString());
        }
        
        results = new ArrayList<String>();
        txtAddress.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_GO){
					if(checkIpDomain(txtAddress.getText().toString())){
						new NSLookupTask().execute(txtAddress.getText().toString());
					}else{
						showMe(getString(R.string.invalid_ip_domain));
					}
				}
				return true;
			}
		});
		
        btnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {			
//				txtAddress.setInputType(InputType.TYPE_NULL);
				if(checkIpDomain(txtAddress.getText().toString())){
					new NSLookupTask().execute(txtAddress.getText().toString());
				}else{
					showMe(getString(R.string.invalid_ip_domain));
				}

			}
		});
	}
	public void showMe(String s){
    	Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
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
	private void goUp(){
		this.finish();
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
	private void showProgressDialog(){
		dialog = ProgressDialog.show(NSLookupMain.this, "", getString(R.string.searching) + " " +getString(R.string.please_wait), true);
		//Log.d("NsLookupMain", "Show progress");
	}
	private void hideProgressDialog(){
		dialog.dismiss();
		//Log.d("NsLookupMain", "Hide progress");
	}
	private void showResults(){

		if(results != null){
			for (String s : results) {
			    TextView tv = new TextView(this);
				tv.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
				if(s == null)
					s = getString(R.string.no_data_found);
					
				tv.setText(s);
				resultsLayout.addView(tv);
				
			}
		}else{
		    TextView tv = new TextView(this);
			tv.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));				
			tv.setText( getString(R.string.no_data_found));
			resultsLayout.addView(tv);
		}
	}

	private class NSLookupTask extends AsyncTask<String, Void, List<String>>{
		private List<String> sendPing(String address){

			List<String> results = new ArrayList<String>();
			try {
				//Log.d("NsLookupMain", "Sending ping");
				
		        Runtime r = Runtime.getRuntime();
				String pingCmd = "nslookup " + txtAddress.getText().toString();
				Process p;
				
				p = r.exec(pingCmd);
				
				BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()),8000);
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					//Log.d("NsLookupMain", "Reading ping: " + inputLine);					
					results.add(inputLine);
				}
				in.close();
				//Log.d("NsLookupMain", "Reader closed");
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
			results = result;
			showResults();
			hideProgressDialog();
		}
		@Override
		protected void onPreExecute() {
			if(results != null)
				results.clear();
				resultsLayout.removeAllViews();
			showProgressDialog();
		}
		
		
		
	}
}
