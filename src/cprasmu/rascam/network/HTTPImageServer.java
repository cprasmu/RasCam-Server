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

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.FileDataSourceImpl;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.tracks.H264TrackImpl;
import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsServer;

import cprasmu.rascam.camera.CameraController;
import cprasmu.rascam.camera.ImageUtils;
import cprasmu.rascam.camera.RazCamServerImpl;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;



public class HTTPImageServer {

	private HttpServer server;
	private static String location;
	private static String context;
	//private static CameraController delegate;
	private RazCamServerImpl parent;
	//private static final String FOLDER_REGEX="";
	//private static boolean secure = false;
	private final static Logger LOGGER = Logger.getLogger(HTTPImageServer.class .getName()); 
	
	private static final String LOGO = "<img width=44px height=46px src=\"data:image/png;base64,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\">";

	public HTTPImageServer( RazCamServerImpl parent, String host,int port, String context, String location, String keyStore, String passphrase,String realm, final Map<String,String> userDb){
		
		HTTPImageServer.location = location;
		HTTPImageServer.context = context;
		//HTTPImageServer.delegate = delegate;
		this.parent = parent;
		InetSocketAddress addr = new InetSocketAddress(host, port);
		LOGGER.setLevel(Level.INFO); 
		FileHandler fileTxt;
		try {
			fileTxt = new FileHandler("RasCam_Access.log");
			 // create txt Formatter
			SimpleFormatter formatterTxt = new SimpleFormatter();
		    fileTxt.setFormatter(formatterTxt);
		    LOGGER.addHandler(fileTxt);
			
		} catch (SecurityException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		try {
			server = HttpsServer.create( addr, 100);
			HttpsServer secServer = (HttpsServer)server;
			KeyStore ks = KeyStore.getInstance("JKS");
			
			//ks.load(new FileInputStream("/opt/rascam.keystore"), passphrase);
			ks.load(new FileInputStream(keyStore), passphrase.toCharArray());
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(ks, passphrase.toCharArray());

			TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
			tmf.init(ks);

			SSLContext ssl = SSLContext.getInstance("TLS");
			ssl.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
			   	
			HttpsConfigurator configurator = new HttpsConfigurator(ssl);
			secServer.setHttpsConfigurator(configurator);
			
			HttpContext cc  = secServer.createContext(context, new MyHandler(parent));
			if(!userDb.isEmpty()){
				cc.setAuthenticator(new BasicAuthenticator(realm) {
			        @Override
			        public boolean checkCredentials(String user, String pwd) {
			        	LOGGER.info("AUTH REQ : " + user + "," +pwd);
			        	if (userDb.containsKey(user)){
			        		try{
			        			return userDb.get(user).equals(pwd);
			        		}
			        		catch(Exception ex){
			        			LOGGER.warning(ex.toString());
			        		}
			        	}
	
			        	return false;
			        }
			    });
			}
			
			secServer.setExecutor(null); // creates a default executor
			secServer.start();
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecoverableKeyException e1) 	{ e1.printStackTrace();
		} catch (KeyStoreException e1) 			{ e1.printStackTrace();
		} catch (NoSuchAlgorithmException e1) 	{ e1.printStackTrace();
		} catch (CertificateException e1) 		{ e1.printStackTrace();
		} catch (KeyManagementException e1) 	{ e1.printStackTrace();
		}
	
	}
	
	public HTTPImageServer( RazCamServerImpl parent, String host,int port, String context, String location,String realm, final Map<String,String> userDb){
		
		HTTPImageServer.location = location;
		HTTPImageServer.context = context;
		//HTTPImageServer.delegate = delegate;
		this.parent=parent;
		
		InetSocketAddress addr = new InetSocketAddress(host, port);
		LOGGER.setLevel(Level.INFO); 
		FileHandler fileTxt;
		try {
			fileTxt = new FileHandler("RasCam_Access.log");
			 // create txt Formatter
			SimpleFormatter formatterTxt = new SimpleFormatter();
			
		    fileTxt.setFormatter(formatterTxt);
		    LOGGER.addHandler(fileTxt);
			
		} catch (SecurityException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		try {
			server = HttpServer.create( addr, 100);
			
			HttpContext cc  = server.createContext(context, new MyHandler(parent));
			
			if(!userDb.isEmpty()){
				cc.setAuthenticator(new BasicAuthenticator(realm) {
			        @Override
			        public boolean checkCredentials(String user, String pwd) {
			        	LOGGER.info("AUTH REQ : " + user + "," +pwd);
			        	if (userDb.containsKey(user)){
			        		try{
			        			return userDb.get(user).equals(pwd);
			        		}
			        		catch(Exception ex){
			        			LOGGER.warning(ex.toString());
			        		}
			        	}
			        	
			        	return false;
			        }
			    });
			}
			
	        server.setExecutor(null); // creates a default executor
	        server.start();
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}
	
	 class MyHandler implements HttpHandler {
		 
		//private  static final int BUFFSIZE = 10241024; //10 MB
		private  static final int BUFFSIZE = 10241024/4; //10 MB
		private RazCamServerImpl parent;
		MyHandler(RazCamServerImpl parent){
			this.parent=parent;
		}
		
        public void handle(final HttpExchange exchange)  {
        	
        	new Thread(new Runnable() {
        	    public void run() {
        	String requestMethod = exchange.getRequestMethod();
        	String requestIp = exchange.getRemoteAddress() + "	" + exchange.getLocalAddress() + "	" +exchange.getRequestURI()+ "	" +exchange.getRequestHeaders().values().toString() ;
        	
        	LOGGER.info(requestIp);
        	
        	URI uri = exchange.getRequestURI();
        	Headers responseHeaders = exchange.getResponseHeaders();
        	responseHeaders.set("Server", "DroneCam/1.0.22");
        	 
        	if (requestMethod.equalsIgnoreCase("GET")) {
        		
        		  OutputStream responseBody = exchange.getResponseBody();
        		  
        		  if (uri.getPath().endsWith(".jpg")) {
        			  responseHeaders.set("Content-Type", "image/jpeg");
        		  } else if (uri.getPath().endsWith(".thm")){
        			  responseHeaders.set("Content-Type", "image/jpeg");
        		  } else if (uri.getPath().endsWith(".mov")) {
        			  responseHeaders.set("Content-Type", "video/mp4");
        		  } else if (uri.getPath().endsWith(".mp4")) {
        			  responseHeaders.set("Content-Type", "video/mp4");
        		  } else {
        			  responseHeaders.set("Content-Type", "text/html");
        		  }
        		  
        		  if(uri.getPath().endsWith(".mov")) {
        			  String filename = uri.getPath().replace(context, location);
        			  
        			//H264TrackImpl h264Track;
        			
					try {
						File tf= new File(filename.replace(".mov", ".mp4"));
						
						if(!tf.exists()) {
						
							H264TrackImpl h264Track = new H264TrackImpl(new FileDataSourceImpl(filename));
						
	        		        //AACTrackImpl aacTrack = new AACTrackImpl(new FileInputStream("/home/sannies2/Downloads/lv.aac").getChannel());
	        		        Movie m = new Movie();
	        		        m.addTrack(h264Track);
	        		        Container out = new DefaultMp4Builder().build(m);
	        		        
	        		        
	        	            FileOutputStream fos = new FileOutputStream(new File(filename.replace(".mov", ".mp4")));
	        	            FileChannel fc = fos.getChannel();
	        	            out.writeContainer(fc);
	        	            fos.close();
        	            
						}
						
						 try{
		        			 
		     				  File xfile = new File(filename.replace(".mov", ".mp4"));
		 
		     				  long fileLength = xfile.length();
		     				  responseHeaders.set("Content-Length", "" + xfile.length());
		     				  exchange.sendResponseHeaders(200, xfile.length());
		     				  
		     				  byte [] fileData;
		     				  DataInputStream dis = new DataInputStream(new FileInputStream(xfile));
		     				  
		     				  if(fileLength > BUFFSIZE){
		     					  fileData = new byte[BUFFSIZE];
		     					  long bufCount = fileLength / BUFFSIZE;
		     					  int remainder = (int) (fileLength % BUFFSIZE);
		     					  
		     					  for(int z=0;z<bufCount;z++){
		     						  dis.readFully(fileData);
		     						  responseBody.write(fileData);
		     						  System.out.println("Buffer " + BUFFSIZE + " sent");
		     					  }
		     					  fileData = new byte[remainder];
		
		     				  } else {
		     					  fileData = new byte[(int)xfile.length()];
		     				  }
		     				  
		     				  dis.readFully(fileData);
		     				  responseBody.write(fileData);
		     				  dis.close();
		     				 
		     			  } catch(IOException ex){
		     				  ex.printStackTrace();
		     			  }
        	            
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        			  
        			  
        		  } else  if(uri.getPath().contains(".")) {
        			  
	        		  String filename = uri.getPath().replace(context, location);
	        		  try{
	        			 
	     				  File xfile = new File(filename);
	     				  
	     				  
	     				  if(!xfile.exists() && xfile.getName().endsWith(".thm")) {
	     					//missing thumbnail?
	     					  
	     					  
	     				  }
	     				  
	     				  long fileLength = xfile.length();
	     				  responseHeaders.set("Content-Length", "" + xfile.length());
	     				  exchange.sendResponseHeaders(200, xfile.length());
	     				  
	     				  byte [] fileData;
	     				  DataInputStream dis = new DataInputStream(new FileInputStream(xfile));
	     				  
	     				  if(fileLength > BUFFSIZE){
	     					  fileData = new byte[BUFFSIZE];
	     					  long bufCount = fileLength / BUFFSIZE;
	     					  int remainder = (int) (fileLength % BUFFSIZE);
	     					  
	     					  for(int z=0;z<bufCount;z++){
	     						  dis.readFully(fileData);
	     						  responseBody.write(fileData);
	     						  System.out.println("Buffer " + BUFFSIZE + " sent");
	     					  }
	     					  fileData = new byte[remainder];
	
	     				  } else {
	     					  fileData = new byte[(int)xfile.length()];
	     				  }
	     				  
	     				  dis.readFully(fileData);
	     				  responseBody.write(fileData);
	     				  dis.close();
	     				 
	     			  } catch(IOException ex){
	     				  ex.printStackTrace();
	     			  }
        		  

        		} else {
        			  try{
        				  StringBuffer sb = new StringBuffer();
        				  boolean getFileCounts=false;
        				  
        					  sb.append("<!DOCTYPE html><html><head><title>" + parent.getInstanceName() + "</title></head><body><a href=\""+ exchange.getHttpContext().getPath() +"\">" + LOGO + "</a><h1>Index of "+uri.toString()+"</h1>");
        					  
        					  
        					  if (!uri.toString().equals(context)) {
        						  sb.append("<table><tr><td>Name</td><td>Last Modified</td><td>Size</td><td>Description</td></tr><tr><td><a href=\""+ context + "\">Parent Directory</a></td><td>-</td><td>-</td></tr>");
        					  } else {
        						  sb.append( "<br><img src=\"" + parent.getCameraController().getLastFilename().replace(".jpg", ".thm") + "\" ><br>" + parent.getCameraController().getLastFilename() + "<br><table><tr><td>Name</td><td>Last Modified</td><td>Size</td><td>Description</td></tr>");
        						  getFileCounts = true;
        					  }
        					  File [] dirs = new File(uri.toString()).listFiles();
        					  Arrays.sort(dirs, new Comparator<File>(){
        						    public int compare(File f1, File f2) {
        						        return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
        						    } 
        					  });
        					  for(File dir:dirs){
        						  if(getFileCounts){
        							  if(dir.isDirectory()){
        								  String[] tmp = new File(dir.getAbsolutePath()).list();
        								  sb.append("<tr><td><a href=\"" + dir.getAbsolutePath() + "\">" + dir.getName() +"</a></td><td>"+new Date(dir.lastModified())+"</td><td>" + dir.length() + "</td><td>"+tmp.length+" files</td></tr>");
        							  }
        						  }else{
        							  sb.append("<tr><td><a href=\"" + dir.getAbsolutePath() + "\">" + dir.getName() +"</a></td><td>"+new Date(dir.lastModified())+"</td><td>" + dir.length() + "</td></tr>");
        						  }
        					  }
        					  sb.append("</table><br><hr><br>" + parent.getCameraController().toString().replace("\n", "<br>")+"</body></html>");
        					  
        					  exchange.sendResponseHeaders(200, sb.length());
        					  responseBody.write(sb.toString().getBytes());

        			  }catch(IOException ex){
            			  
            		  }
        		  }
        		  try{
        			  responseBody.close();
        		  }catch(IOException ex){
        			  ex.printStackTrace();
        			  
        		  }
        		 
        	    } else if (requestMethod.equalsIgnoreCase("POST")) {
        	    	OutputStream responseBody = exchange.getResponseBody();
        	    	responseHeaders.set("Content-Type", "application/json");
        	    	 
					BufferedReader in = new BufferedReader(new InputStreamReader( exchange.getRequestBody()));
					String line;
    	    	    try {
    	    	    	exchange.sendResponseHeaders(200, 0);
    	    	    	 
						while ((line = in.readLine()) != null) {
						    responseBody.write( parent.parseCommand(line).getBytes());
						}
						try{
		        			responseBody.close();
		        		} catch (IOException ex) {
		        			ex.printStackTrace();
		        		}
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        	    }
        }
        }).start();
        	
    }}
}
