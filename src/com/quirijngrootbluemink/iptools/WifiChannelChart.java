package com.quirijngrootbluemink.iptools;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.androidplot.Plot;
import com.androidplot.series.XYSeries;
import com.androidplot.ui.AnchorPosition;
import com.androidplot.ui.SizeLayoutType;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.LineAndPointRenderer;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XLayoutStyle;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYStepMode;
import com.androidplot.xy.YLayoutStyle;
import com.markupartist.android.widget.ActionBar;


public class WifiChannelChart extends Activity {

    private XYPlot channelChart;
    private List<ScanResult> scanResults ;
    private List<XYSeries> currentSeries;
	BroadcastReceiver receiver;
	ListView wifiNetworksList;

	WifiManager wifi;
	Handler handlr;
	Runnable mUpdateScanTask;
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
    private double calculateY(double x,double strength, double channel){
    	double v = 100-strength;
    	
    	double vertical = 100-v;
    	double stretch=(0.25)*v;
    	double formula = -(Math.pow((x-channel),2));
    	return ((stretch)*(formula)-(vertical));
    }
    private List<List<Double>> getCoordinates(double channel, double strength){
    	List<List<Double>> coords = new ArrayList<List<Double>>();
    	
        List<Double> x2 = new ArrayList<Double>();
        double chan = channel;
        double step = 0.1;
        double startpoint = chan-2;
        double endpoint = chan+2.1;
        double counter = startpoint;
        
        
        while (counter <= endpoint) {
        	x2.add(counter);
        	counter += step;
		}        
        counter = startpoint;
        List<Double> y2 = new ArrayList<Double>();
        while (counter <= (endpoint)) {    
        	double v = calculateY(counter,Math.abs(strength),chan);
        	y2.add(v);
        	counter += step;
		}

        coords.add( x2);
        coords.add( y2);
		return coords;
    	
    }
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_channel_graph);
        setActionBar();
        currentSeries = new ArrayList<XYSeries>();

     	handlr = new Handler();
     	
     	mUpdateScanTask = new Runnable() {
     	   public void run() {
				wifi.startScan();
				updateNetworks();
				handlr.postDelayed(this, 10000);
     	   }
     	};
     	
     	handlr.removeCallbacks(mUpdateScanTask);
     	handlr.postDelayed(mUpdateScanTask, 1000);
		// Setup WiFi
		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		wifiNetworksList = (ListView) findViewById(R.id.wifiNetworksList);
		scanResults = wifi.getScanResults();

        setupChart();
        addChartSeries();
        
 

    }
	public void addChartSeries(){
		for (XYSeries series : currentSeries) {	
			
			channelChart.removeSeries(series);
		       //Log.d("YEPLA Chart", "Cleared series: "+series.getTitle());
		}
		currentSeries.clear();
		channelChart.invalidate();
	   int i = 0;
	
	   for (ScanResult scanResult : scanResults) {
	       List<List<Double>> opdenbierg = getCoordinates(calculateChannel(scanResult.frequency), scanResult.level);
	       SimpleXYSeries series2 = new SimpleXYSeries(opdenbierg.get(0),opdenbierg.get(1),scanResult.SSID);
	       currentSeries.add(series2);
	       channelChart.addSeries(series2, LineAndPointRenderer.class, new LineAndPointFormatter(getColor(i), null,getColor(i)));
	
	       i++;
	       //Log.d("YEPLA Chart", "Adding series: "+scanResult);
		}
	}
	@SuppressWarnings("deprecation")
	private void setupChart(){
	    // initialize our XYPlot reference:
        channelChart = (XYPlot) findViewById(R.id.channelChart);
        channelChart.setBorderPaint(null);
        channelChart.setPlotPadding(15, 20, 20, 50);
        channelChart.setPlotMargins(0, 0, 0, 0);
        channelChart.setBorderStyle(Plot.BorderStyle.SQUARE, null, null);
        channelChart.setTitle(getString(R.string.chart_title));
        channelChart.getTitleWidget().setWidth(1, SizeLayoutType.RELATIVE);
        
        //Set x axis
        channelChart.setDomainBoundaries(0, 14, BoundaryMode.FIXED );
        channelChart.setDomainValueFormat(new DecimalFormat("#"));
        channelChart.setDomainLabel(getString(R.string.channel));
        channelChart.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 1);
        
        
        //Set y axis
        channelChart.setRangeBoundaries(-100, -30, BoundaryMode.GROW );
        channelChart.setRangeValueFormat(new DecimalFormat("#"));
        channelChart.setRangeLabel(getString(R.string.signal_strength_unit));
        channelChart.setRangeStep(XYStepMode.INCREMENT_BY_VAL, 2);
        channelChart.getRangeLabelWidget().setHeight(1, SizeLayoutType.RELATIVE);
    
        
 
        // reduce the number of range labels
        channelChart.getGraphWidget().setRangeTicksPerLabel(4);

 
        // add a semi-transparent black background to the legend
        // so it's easier to see overlaid on top of our plot:
        Paint bgPaint = new Paint();
        bgPaint.setColor(Color.BLACK);
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setAlpha(0);
        channelChart.getLegendWidget().setBackgroundPaint(bgPaint);
 
        // adjust the padding of the legend widget to look a little nicer:
 
        // reposition the grid so that it rests above the top-right
        // edge of the graph widget:
        
        channelChart.getLegendWidget().setWidth(1, SizeLayoutType.RELATIVE);
        channelChart.getLegendWidget().setHeight(50, SizeLayoutType.ABSOLUTE);
        //channelChart.getLegendWidget().setTableModel(new DynamicTableModel(1, 1, TableOrder.COLUMN_MAJOR));
        channelChart.position(
                channelChart.getLegendWidget(),
                0,
                XLayoutStyle.ABSOLUTE_FROM_LEFT,
                -50,
                YLayoutStyle.ABSOLUTE_FROM_BOTTOM,
                AnchorPosition.LEFT_BOTTOM);
        channelChart.disableAllMarkup();
	}
	private int getColor(int number){
	
    	switch (number) {
		case 0:
			return Color.argb(50, 255, 0, 0);
		case 1:
			return Color.argb(50, 0, 255, 0);
		case 2:
			return Color.argb(50, 0, 0, 255);
		case 3:
			return Color.argb(50, 255, 255, 0);
		case 4:
			return Color.argb(50, 255, 0, 255);
		case 5:
			return Color.argb(50, 0, 255, 255);
		default:
			Random rnd = new Random();			
			return Color.argb(50,rnd.nextInt(255),rnd.nextInt(255),rnd.nextInt(255));
    	}
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
    private void registerReciever(){
        // Register Broadcast Receiver
     		if (receiver == null)
     			receiver = new WifiScanChartReciever(this);

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
	public void updateNetworks(){
		
		List<String> ssids = new ArrayList<String>();		
		for (ScanResult s : scanResults) {
			ssids.add(s.SSID);
			
		}
		

    }
	public List<ScanResult> getScanResults() {
		return scanResults;
	}
	public void setScanResults(List<ScanResult> scanResults) {
		this.scanResults = scanResults;
	       //Log.d("YEPLA Chart", "New scan results!");
	}
	
	
}
