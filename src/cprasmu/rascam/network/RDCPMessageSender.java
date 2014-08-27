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
package cprasmu.rascam.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cprasmu.rascam.camera.RazCamServerImpl;
import cprasmu.util.SystemUtilities;

public class RDCPMessageSender {

	private static final String  UDP_MULTICAST_ADDRESS = "226.1.1.1";
	//private static final String RDCP_VERSION = "[1,1]";
	private String hostAddress = "";
	//private static final String RDCP_DISPLAY_NAME = "RazCam";
	//private static final int RDCP_PORT = 23117;
	
	private static final Integer UDP_TX_PORT = 15555;
	private static final Integer UDP_RX_PORT = 15556;
	
	private int imagePort = 23116;
	private InetAddress group;
	private MulticastSocket s;
	private Thread listener;
	private Timer sender  = null;
	private RazCamServerImpl parent;
	
	public RDCPMessageSender(RazCamServerImpl parent){

		init();
		this.parent = parent;
		sender = new Timer("RDCP Sender",true); 
		sender.schedule(new SenderTask(), 100,1000);
		
	}
	
	public void start(long delay){
		if(sender==null){
			sender = new Timer("RDCP Sender",true); 
			sender.schedule(new SenderTask(), 100,delay);
		}
	}
	
	private boolean init(){
		boolean result = false;
		try{
			hostAddress = SystemUtilities.getNetworkAddress();
			group = InetAddress.getByName(UDP_MULTICAST_ADDRESS);
			s = new MulticastSocket(UDP_RX_PORT);
			s.joinGroup(group);
			
			listener = new Thread(new Listener());
			listener.start();
		 
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
		
		return result;
	}
	
	
	private class SenderTask extends TimerTask {
        public void run() {
        	try{
        			String message = parent.getAdvertData();
        			DatagramPacket dp = new DatagramPacket(message.getBytes(), message.length(),  group, UDP_TX_PORT);
        			s.send(dp);
    		}
    		catch (Exception ex){
    			ex.printStackTrace();
    		}
        }
    }
	
	public void setImagePort(int port){
		
		if ((port>0) && (port <65535)){
			this.imagePort = port;
		}
	}
	
	public int getImagePort(){
		return imagePort;
	}
	
	private class Listener implements Runnable {
		public void run(){
			while(true){
				 byte[] buf = new byte[1024];
				 DatagramPacket recv = new DatagramPacket(buf, buf.length);
				 try {
					s.receive(recv);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
	}

}
