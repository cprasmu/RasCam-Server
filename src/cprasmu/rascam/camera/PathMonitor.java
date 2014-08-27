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

import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class PathMonitor extends Thread{

	private String path = "./";
	private FileCreationMonitor parent;
	private WatchService watchService;
	private boolean valid = true;
	
	public PathMonitor(FileCreationMonitor parent, String path) {
		this.setName("WATCHER " +path);
		this.path = path;
		this.parent = parent;
	}
	
	public String getPath(){
		return path;
	}
	
	public void close(){
		try {
			watchService.close();
			valid=false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run(){
		try{
			
			Path imageFolder = Paths.get(path);
			System.out.println("Watching " + path);
			watchService = FileSystems.getDefault().newWatchService();
			//imageFolder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY );
			imageFolder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE);
			
		  do {
		    WatchKey watchKey = watchService.take();

		    for (WatchEvent<?> event : watchKey.pollEvents()) {
	
			//WatchEvent.Kind kind = event.kind();
		      String fileName = event.context().toString();
		      
		      if (StandardWatchEventKinds.ENTRY_CREATE.equals(event.kind())) {
		    	  System.err.println(event.kind().name()+ fileName);	
		    	  parent.fileCreated(path, fileName);
		    	  
		      } else if (StandardWatchEventKinds.ENTRY_DELETE.equals(event.kind())) {
		    	  System.err.println(event.kind().name() + " : " + fileName);	
			  		parent.fileDeleted(path, fileName);
			  		
			  }  else if (StandardWatchEventKinds.ENTRY_MODIFY.equals(event.kind())) {
				  System.err.println(event.kind().name() + " : " + fileName);	
				  	parent.fileModified(path, fileName);
				  	
			  }  else if (StandardWatchEventKinds.OVERFLOW.equals(event.kind())) {
				  System.err.println(event.kind().name() + " : " + fileName);	
			  }
		    }
		    valid = watchKey.reset();

		  } while (valid);
		  
		} catch (IOException ioex){
			
		} catch(InterruptedException ie){
			System.err.println("Stopped watching : "+path);
			
		} catch(ClosedWatchServiceException cwe){
			System.err.println("Stopped watching -closed  : "+path);
		}

	}
  
}
