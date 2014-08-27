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
package cprasmu.os;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Environment {

	public static  File getExternalStorageDirectory(){
		
		return new File("/");
		
	}
	
	public static long calculateUsage(String filePath){
		File f= new File(filePath);
		return  (long) (  ((double)(f.getTotalSpace()-f.getFreeSpace()) / (double)f.getTotalSpace())*100d);
		
	}
	
	public static void shutdown() {
		String command = ("shutdown -h now");
		//sendMessage(command);
		System.out.println(command);
		try {
			 Runtime.getRuntime().exec(command.split(" "));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean isRunnging(String name){
		
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
	
	public static void killProcess(String processName){
		
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
}
