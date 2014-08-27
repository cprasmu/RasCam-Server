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
package cprasmu.rascam.camera;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import cprasmu.util.SystemUtilities;

public class FileSystemModel implements FileCreationMonitor {
	
	private final static String TAG = FileCreationMonitor.class.getSimpleName();
	private final static String RE_DCIM_DIRECTORY = "([1-9][0-9]{2})([A-Z_0-9]{5})(\\..+)?";
	private final static String RE_DCIM_FILE = "([A-Z_0-9]{4})([0-9]{4})(\\..+)";
	
	private String initialPath = "/DCIM";
	//private List<File> fileList = new ArrayList<File>();
	//private Map<String,File> imageFileMap = new HashMap <String,File>();
	private String filePrefix = "DSC_";
	private PathMonitor pathMonitor;
	private FileCreationMonitor delegate =null;

	private int dirCount = 0;
	
	private static final int MAX_DIR_COUNT = 999;
	private static final int MAX_FILE_COUNT = 9999;
	private static final String DIR_REGEX = "^[0-9]{3}[A-Z]{5}";
	private static final String FILENAME_REGEX = "^[A-Z]{5}[0-9]{4}.JPG";
	private String lastFilename = "";
	
	public String getNextFilename(){
		
		int fileNumber = getFileCount();
		String newFilename = initialPath  + filePrefix + fileNumber ;
		return newFilename;
	}

	public boolean delete(String filename) {
		
		File tmp = new File(filename);
		//boolean result = false;
		if(tmp.isDirectory()){
			
			File[] fileList=tmp.listFiles();
			
			
			for(File f : fileList){
				f.delete();
			}
		}
		
		return tmp.delete();
		
	}
	
	
	//public FileSystemModel( FileCreationMonitor delegate){
		public FileSystemModel( ){
		
		//this.delegate=delegate;

		
		File f = new File(initialPath);
		
		if(!f.isDirectory()) {
			File newDir = new File (initialPath);
			if (!f.mkdir()){
				System.out.println("Failed to create directory : " + f.getAbsolutePath());
			}
		}
	//	getDirectoryCount();
		
	//	getFileCount();
	//	
	//	pathMonitor = new PathMonitor(this, initialPath);
	//	pathMonitor.start();
		
	}
	
	private int getDirectoryCount(String path){
		
		int count=0;
		File [] f = new File(path).listFiles(); 
		
		for (File thisFile:f){
			
			if(thisFile.isDirectory()){
				String name = thisFile.getName();
				if(name.matches(DIR_REGEX)){
					
					int tmp = Integer.parseInt(thisFile.getName().substring(0, 3));
					if(tmp>count ){
						count = tmp;
					}
					
				}else {
					//invalid Dir!!
				}
			} else if (thisFile.isFile()){
				//delete this?
			}
		}
		return count;
	}
	
	
	public int getFileCount() {
		
		int count=-1;
		File f = new File(initialPath);
		//imageFileMap.clear();
		//totalFileCount = 0;
		//fileDataSize = 0;
		
		if (f.isDirectory()) {
			
			File[] files = f.listFiles();

			Arrays.sort(files, new Comparator<File>(){
			    public int compare(File f1, File f2) {
			        return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
			    } 
			});
			
			for(File file:files){
				if (file.getName().endsWith(".jpg")|| file.getName().endsWith(".h264")) {
					if(file.getName().startsWith("drone_")){
						
						/*
						if(!imageFileMap.containsKey(file.getName())){
							imageFileMap.put(file.getName(), file);
							fileList.add(file);
						}
						*/
						
						String name=file.getName();
						name = name.replace("drone_", "");
						
						if (name.contains("_")){
							name = name.split("_")[0];
						} else {
							if (name.contains(".")){
								name = name.split("[.]")[0];
							}
						}
						if(Integer.parseInt(name) > count){
							count = Integer.parseInt(name);
						}
					}
				}
			}
		}
		
		return count+1;
		
	}
		
		
		/*
	@SuppressWarnings("unchecked")
	private JSONArray getJSONFilesArrayOLD(){
		
		JSONObject obj = new JSONObject();
		
		JSONArray jsonFiles = new JSONArray();
		
		synchronized(fileList){
			StringBuffer json=new StringBuffer("");
			
		//	obj.put("count", fileList.size());
			
			for (File file:fileList){
				
				String tmpName = file.getName().replace(initialPath, "");
				if(json.length()>0){
					json.append( ",");
				}
				JSONObject fileContainer = new JSONObject();
				fileContainer.put("fileName", tmpName);
				fileContainer.put("fileSize",  SystemUtilities.humanReadableByteCount (file.length(),false) );
				
				jsonFiles.add(fileContainer);
				json.append( tmpName +":" +  SystemUtilities.humanReadableByteCount (file.length(),false) );

			}
			//obj.put("files", jsonFiles);
			//sendMessage("JSON:" + json );
		}
		
		return jsonFiles;
	}
	*/
		
	private synchronized JSONArray getJSONFilesArray(String path){

		
		JSONArray jsonFiles = new JSONArray();
		
		File [] files = new File(path).listFiles();
		File [] tmpfiles;

		Arrays.sort(files, new Comparator<File>(){
		    public int compare(File f1, File f2) {
		    	if(!f1.getName().startsWith(".") && !f2.getName().startsWith(".") ) {
			    	try{
			    		Long v1 = Long.parseLong(f1.getName().substring(4, 8));
			    		Long v2 = Long.parseLong(f2.getName().substring(4, 8));
			        	return Long.valueOf(v1).compareTo(v2);
			    	}
			    	catch(NumberFormatException nfe){
			    		return -1;
			    	}
		        
		    	} else {
		    		return 0;
		    	}
		    } 
		});
		
		for (File file:files){
			if(!file.getName().endsWith(".thm")){
				JSONObject fileContainer = new JSONObject();
				fileContainer.put("fileName", file.getName());
				fileContainer.put("fileSize",  SystemUtilities.humanReadableByteCount (file.length(),false) );
				jsonFiles.add(fileContainer);
			}
		}

		return jsonFiles;
	}
	
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(String path){
		
		JSONObject obj = new JSONObject();
		obj.put("Usage", calculateUsage());
		obj.put("Files", getJSONFilesArray(path));
		return obj;
	}
	
	public String toString(){
		return "";
	}
	
	public long calculateUsage(){
		
		File f = new File(initialPath);
		return  (long) ( ((double)(f.getTotalSpace()-f.getFreeSpace()) / (double)f.getTotalSpace())*100d);
		
	}
	
	public static String humanReadableByteCount(long bytes, boolean si) {
	    int unit = si ? 1000 : 1024;
	    if (bytes < unit) return bytes + " B";
	    int exp = (int) (Math.log(bytes) / Math.log(unit));
	    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
	    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
	
	public static void main(String [] args){
		System.out.println(DCIMHelper.getCurrentDirectory());
		
		System.out.println(DCIMHelper.getDirectoryForNewImage());
		//FileSystemModel fsm = new FileSystemModel(null);
		//System.out.println(fsm.toJSON());
	}
	
	@Override
	public void fileCreated(String path, String filename) {
		
		try{
			//if(delegate!=null){
			//	delegate.fileCreated(path, filename);
			//}
			File file= new File(path + "/" +filename);
			System.out.println("seen : " + filename);
			 
			if(filename.endsWith(".jpg")){
				setLastFilename(filename);
				//imageFileMap.put(filename, file);
				//fileList.add(file);
				//Call a delegate
				//ImageUtils.createThumbnail(path + filename,path+ (filename.replace("drone_", "thumb_")),40,550);
				//ImageUtils.createThumbnail(path + filename,path+ (filename.replace(".jpg", ".thm")),5,550);

				
			} else if(filename.endsWith(".h264")){
				setLastFilename(filename);
				//imageFileMap.put(filename, file);
				//fileList.add(file);
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	public void fileDeleted(String path, String filename) {
		try{
			//if(imageFileMap.containsKey(filename)){
			//	fileList.remove(imageFileMap.get(filename));
			//	imageFileMap.remove(filename);
			//}
			//if(delegate!=null){
			//	delegate.fileDeleted(path, filename);
			//}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}



	public String getLastFilename() {
		return lastFilename;
	}



	private void setLastFilename(String lastFilename) {
		this.lastFilename = lastFilename;
	}

	@Override
	public void fileModified(String path, String filename) {
		// TODO Auto-generated method stub
		
	}
	
}
