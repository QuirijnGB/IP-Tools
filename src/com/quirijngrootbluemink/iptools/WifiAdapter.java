package com.quirijngrootbluemink.iptools;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WifiAdapter extends ArrayAdapter<WifiListItem> {
    private ArrayList<WifiListItem> items;

    public WifiAdapter(Context context, int textViewResourceId, ArrayList<WifiListItem> items) {
            super(context, textViewResourceId, items);
            this.items = items;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi =  (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.wifi_list_item, null);
            }
            WifiListItem o = items.get(position);
            if (o != null) {
                    TextView tt = (TextView) v.findViewById(R.id.txtWifiName);
                    if (tt != null) {
                          tt.setText(o.getSsid());                            
                      }
                    ImageView signal_strength = (ImageView) v.findViewById(R.id.signal_strength);
                    if (signal_strength != null) {
                    	if(o.getSignal_strength() > -50){                    		
                    		signal_strength.setImageResource(R.drawable.ic_signal_strength_4);  
                    	}else if(o.getSignal_strength() > -70){
                    		signal_strength.setImageResource(R.drawable.ic_signal_strength_3);  
                    	}else if(o.getSignal_strength() > -85){
                    		signal_strength.setImageResource(R.drawable.ic_signal_strength_2);  
                    	}else if(o.getSignal_strength() > -90){
                    		signal_strength.setImageResource(R.drawable.ic_signal_strength_1);  
                    	}else{
                    		signal_strength.setImageResource(R.drawable.ic_signal_strength);  
                    	}
                    }
                    ImageView current_network = (ImageView) v.findViewById(R.id.current_network);
                    if (current_network != null) {
                    	if(o.isConnected()){                    		
                    		current_network.setImageResource(R.drawable.ic_star);  
                    	}                        
                    }
            }
            return v;
    }

}
