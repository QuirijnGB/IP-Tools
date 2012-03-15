package com.quirijngrootbluemink.iptools;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.http.conn.util.InetAddressUtils;

public class IpCalc {

	String ipAddress, binaryNetmask, broadcastAddress, networkAddress;
	int maskSuffix;
	InetAddress inet;
	

	public IpCalc(String ipAddress, String maskSuffix) {
		super();
		this.ipAddress = ipAddress;
		this.maskSuffix = Integer.parseInt(maskSuffix);
		setUp();
	}
	private void setUp(){
		if(InetAddressUtils.isIPv4Address(ipAddress)){
			try {
				inet = InetAddress.getByName(ipAddress);
//				byte[] bytes = inet.getAddress();
//				
////				for (byte b : bytes) {
////					//Log.d("IPCalc","ip:" + b);
////					
////				}
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public String getNetworkClass(){
		String address = ipAddress; 
		String[] addressParts = address.split("\\.");
		if(Integer.parseInt(addressParts[0]) < 128){
			return "A";
		}else if(Integer.parseInt(addressParts[0]) < 192 && Integer.parseInt(addressParts[0]) > 127){
			return "B";
		}else if(Integer.parseInt(addressParts[0]) < 224 && Integer.parseInt(addressParts[0]) > 191){
			return "C";
		}else if(Integer.parseInt(addressParts[0]) < 240 && Integer.parseInt(addressParts[0]) > 223){
			return "D";
		}else if(Integer.parseInt(addressParts[0]) > 239){
			return "E";
		}
		
		return "";
	}
	public int getNumberOfHosts(){
		return (int) (Math.pow(2, (32-maskSuffix))-2);
	}
	public String getValidIpRange(){
		String network = getDecimalNetworkAddress();
		String[] networkParts = network.split("\\.");
		networkParts[3] = ""+(Integer.parseInt(networkParts[3])+1);
		
		String broadcast = getDecimalBroadcastAddress();
		String[] broadcastParts = broadcast.split("\\.");
		broadcastParts[3] = ""+(Integer.parseInt(broadcastParts[3])-1);
		return networkParts[0]+"."+networkParts[1]+"."+networkParts[2]+"."+networkParts[3]+" - "+broadcastParts[0]+"."+broadcastParts[1]+"."+broadcastParts[2]+"."+broadcastParts[3];
	}
	public String getBinaryNetmask(){
		String netmask = "";
		for (int i = 1; i <= 32; i++) {
			
			if(i<=maskSuffix){
				netmask += "1";
			}else{
				netmask += "0";
			}
			if(i%8==0 && i!=32){
				netmask += ".";
			}
			
		}
		return netmask;
	}
	public String getDecimalNetworkAddress(){
		String networkAddress = "";
		String address = getBinaryAddress();
		String netmask = getBinaryNetmask();

		String[] addressParts = address.split("\\.");
		String[] netmaskParts = netmask.split("\\.");
		for (int i = 0; i < addressParts.length; i++) {
			networkAddress += (Integer.parseInt(addressParts[i],2) &  Integer.parseInt(netmaskParts[i],2))+"";
			if(i<addressParts.length-1)
				networkAddress += ".";
			
		}
		return networkAddress;
	}
	public String getDecimalBroadcastAddress(){
		String networkAddress = "";
		String address = getBinaryAddress();
		String netmask = getBinaryNetmask();

		//Log.d("IpCalc", "Address: " + address);
		//Log.d("IpCalc", "Mask: " + netmask);
		String[] addressParts = address.split("\\.");
		String[] netmaskParts = netmask.split("\\.");
		addLeadingZeros(addressParts);
		for (int i = 0; i < addressParts.length; i++) {
			String part = (Integer.parseInt(addressParts[i],2)|(Integer.parseInt("11111111",2)^Integer.parseInt(netmaskParts[i],2)))+"";
			networkAddress +=  part;
			if(i==2){
			//Log.d("IpCalc", "Part of binary address: " + addressParts[i]);
			//Log.d("IpCalc", "Part of binary mask: " + netmaskParts[i]);
			
			//Log.d("IpCalc", "Part of binary broadcast: " + Integer.toBinaryString(t));
			}
			if(i<addressParts.length-1)
				networkAddress += ".";
			
		}
		return networkAddress;
	}
	public String[] addLeadingZeros(String[] value){
		for (String string : value) {
			for (int i = 0; i < string.length(); i++) {
				if(string.length() < 8){
					String temp = "";
					for (int j = 0; j < 8-string.length(); j++) {
						temp += "0";
					}
					string = temp+string;
				}			
			}
		}
		return value;

	}
	public static String getDecimal(String address){
		//Log.d("IpCalc", "Get decimal of: " + address);
		String[] parts = address.split("\\.");
		String decnetmask = "";
		for (int i = 0; i < parts.length; i++) {
			decnetmask += Integer.parseInt(parts[i],2)+"";
			if(i<parts.length-1)
				decnetmask += ".";
			
		}		
		//Log.d("IpCalc", "The decimal:  " + decnetmask);
		return decnetmask;
	}
	public String getBinaryAddress(){
		//Log.d("IPCalc","Calculate binary");
		String binary = "";
		
		String[] parts = inet.getHostAddress().split("\\.");
		int points = 0;
		for (String s : parts) {
			int i = Integer.parseInt(s);
			if(points >2){
				binary += Integer.toBinaryString(i);	
			}else{
				binary += Integer.toBinaryString(i)+".";				
			}
			points++;		
		}	
		//Log.d("IPCalc","binary ip:" + binary);	
		return binary;
		
	}
	public String getHexAddress(){
		//Log.d("IPCalc","Calculate hex");
		String binary = "";
		
		String[] parts = inet.getHostAddress().split("\\.");
		int points = 0;
		for (String s : parts) {
			int i = Integer.parseInt(s);
			if(points >2){
				binary += Integer.toHexString(i);	
			}else{
				binary += Integer.toHexString(i)+".";				
			}
			points++;	
		}	
		//Log.d("IPCalc","hex ip:" + binary);	
		return binary;
		
	}
	public String getOctalAddress(){
		//Log.d("IPCalc","Calculate hex");
		String binary = "";
		
		String[] parts = inet.getHostAddress().split("\\.");
		int points = 0;
		for (String s : parts) {
			int i = Integer.parseInt(s);
			if(points >2){
				binary += Integer.toOctalString(i);	
			}else{
				binary += Integer.toOctalString(i)+".";				
			}
			points++;	
		}	
		//Log.d("IPCalc","hex ip:" + binary);	
		return binary;
		
	}
	
	
	
}
