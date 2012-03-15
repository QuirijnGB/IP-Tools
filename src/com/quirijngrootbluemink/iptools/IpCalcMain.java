package com.quirijngrootbluemink.iptools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

import com.markupartist.android.widget.ActionBar;

public class IpCalcMain extends Activity {
	Button btnCalc;
	EditText txtURL;
	TextView txtNetworkClass, txtBinaryAddress, txtNetworkAddress,
			txtBroadcastAddress, txtAddressRange, txtNumberOfHosts;
	IpCalc ip;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ip_calc_main);
		btnCalc = (Button) findViewById(R.id.btnCalc);
		txtURL = (EditText) findViewById(R.id.txtAddress);
		txtNetworkClass = (TextView) findViewById(R.id.txtNetworkClass);
		txtBinaryAddress = (TextView) findViewById(R.id.txtBinaryAddress);
		txtNetworkAddress = (TextView) findViewById(R.id.txtNetworkAddress);
		txtBroadcastAddress = (TextView) findViewById(R.id.txtBroadcastAddress);
		txtAddressRange = (TextView) findViewById(R.id.txtAddressRange);
		txtNumberOfHosts = (TextView) findViewById(R.id.txtNumberOfHosts);
		
		txtURL.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_GO){
					try {
						////Log.d("IP Calc Main", "Clicked request");
						String temp = txtURL.getText().toString();
						String[] parts = temp.split("/");
						if (Integer.parseInt(parts[1]) > 32) {
							throw new Exception(getString(R.string.min_subnet));
						}
						if(!checkIp(parts[0])){
							throw new Exception(getString(R.string.invalid_ip));
						}
						ip = new IpCalc(parts[0], parts[1]);
						displayInfo();
					} catch (Exception e) {
						txtURL.requestFocus();
						showMe(e.getMessage());
					}
				}
				return true;
			}
		});
		setActionBar();
		btnCalc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					////Log.d("IP Calc Main", "Clicked request");
					String temp = txtURL.getText().toString();
					String[] parts = temp.split("/");
					if (Integer.parseInt(parts[1]) > 32) {
						throw new Exception(getString(R.string.min_subnet));
					}
					if(!checkIp(parts[0])){
						throw new Exception(getString(R.string.invalid_ip));
					}
					ip = new IpCalc(parts[0], parts[1]);
					displayInfo();
				} catch (Exception e) {
					txtURL.requestFocus();
					showMe(e.getMessage());
				}
			}
		});
	}

	private boolean checkIp(String value) {
		String IprRegex = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
		Pattern patt = Pattern.compile(IprRegex);
		Matcher matcher = patt.matcher(value);
		if (matcher.matches()) {
			////Log.d("UtilitiesMain", "Valid IP");
			return true;
		} else {
			return false;
		}

	}

	public void showMe(String s) {
		Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
	}

	private void displayInfo() {
		txtNetworkClass.setText(ip.getNetworkClass());
		txtBinaryAddress.setText(ip.getBinaryAddress());
		txtNetworkAddress.setText(ip.getDecimalNetworkAddress());
		txtBroadcastAddress.setText(ip.getDecimalBroadcastAddress());
		txtAddressRange.setText(ip.getValidIpRange());
		txtNumberOfHosts.setText("" + ip.getNumberOfHosts());
	}

	private void setActionBar() {
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

	private void goUp() {
		this.finish();
	}

}
