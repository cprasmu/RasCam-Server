/*
 * copyright (C) 2013 Christian P Rasmussen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cprasmu.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SystemUtilities {
	
	public static void shutdownOS() {
		String command = ("sudo shutdown -h now");
		//sendMessage(command);
		System.out.println(command);
		try {
			 Runtime.getRuntime().exec(command.split(" "));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void rebootOS() {
		String command = ("sudo shutdown -r now");
		//sendMessage(command);
		System.out.println(command);
		try {
			 Runtime.getRuntime().exec(command.split(" "));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * iterate interfaces to find the interface address.
	 * @return the String representation of the interface address.
	 */
	public static String getNetworkAddress(){
		
		Enumeration<NetworkInterface> networkInterfaces;
		try {
			networkInterfaces = NetworkInterface.getNetworkInterfaces();
		
			while (networkInterfaces.hasMoreElements())
			{
			    NetworkInterface networkInterface = (NetworkInterface) networkInterfaces.nextElement();
	
			    List<InterfaceAddress> ia = networkInterface.getInterfaceAddresses();
			    
			    if (networkInterface.getDisplayName().equals("wlan0") || networkInterface.getDisplayName().equals("en1")) {
			    	for(int i=0;i<ia.size();i++){
			    		InterfaceAddress address = ia.get(i);
			    		if (address.getAddress() instanceof Inet4Address){
			    			System.err.println("Using address : " + address.getAddress().getHostAddress() );
			    			return address.getAddress().getHostAddress();
			    		}
			    	}
			    }
			}
		
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		System.err.println("Failed to get address!");
		
		return null;
	}
	
	private static boolean isRunnging(String name){
		
		boolean result=false;

		try {
			 Process p = Runtime.getRuntime().exec("ps -ef".split(" "));
			 BufferedReader inp = new BufferedReader( new InputStreamReader(p.getInputStream()) );
			 String line="";
			 while((line=inp.readLine())!=null){
 
				 if(line.contains(name)){
					System.out.println(line);
					 result= true;
					 break;
				 }
			 }
			 
			 inp.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	private static void killProcess(String processName){
		
		String  command = ("killall " + processName);
		
		try {
			 Process p = Runtime.getRuntime().exec(command.split(" "));
			 p.waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static long calculateUsage(String filePath){
		File f= new File(filePath);
		return  (long) (  ((double)(f.getTotalSpace()-f.getFreeSpace()) / (double)f.getTotalSpace())*100d);
		
	}
	
	public static String humanReadableByteCount(long bytes, boolean si) {
	    int unit = si ? 1000 : 1024;
	    if (bytes < unit) return bytes + " B";
	    int exp = (int) (Math.log(bytes) / Math.log(unit));
	    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
	    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
	
	
	public static String getDurationBreakdown(long millis)
	{
	    String[] units = {" Days ", " Hours ", " Minutes ", " Seconds "};
	    Long[] values = new Long[units.length];
	    if(millis < 0)
	    {
	        throw new IllegalArgumentException("Duration must be greater than zero!");
	    }

	    values[0] = TimeUnit.MILLISECONDS.toDays(millis);
	    millis -= TimeUnit.DAYS.toMillis(values[0]);
	    values[1] = TimeUnit.MILLISECONDS.toHours(millis);
	    millis -= TimeUnit.HOURS.toMillis(values[1]);
	    values[2] = TimeUnit.MILLISECONDS.toMinutes(millis);
	    millis -= TimeUnit.MINUTES.toMillis(values[2]);
	    values[3] = TimeUnit.MILLISECONDS.toSeconds(millis);

	    StringBuilder sb = new StringBuilder(64);
	    boolean startPrinting = false;
	    for(int i = 0; i < units.length; i++){
	        if( !startPrinting && values[i] != 0)
	            startPrinting = true;
	        if(startPrinting){
	            sb.append(values[i]);
	            sb.append(units[i]);
	        }
	    }

	    return(sb.toString());
	}
	
	public static String getDurationHMS(long millis){
		String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
	            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
	            TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
		return hms;
	}
}
