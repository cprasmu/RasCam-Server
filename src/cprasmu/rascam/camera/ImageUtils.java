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

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;

public class ImageUtils {
	private final static String TAG = ImageUtils.class.getSimpleName();
	
	public static  void saveScaledImage(String filePath,String outputFile){
		System.out.println( "saveScaledImage");
	    try {

	        BufferedImage sourceImage = ImageIO.read(new File(filePath));
	        int width = sourceImage.getWidth();
	        int height = sourceImage.getHeight();

	        if(width>height){
	            float extraSize=    height-100;
	            float percentHight = (extraSize/height)*100;
	            float percentWidth = width - ((width/100)*percentHight);
	            BufferedImage img = new BufferedImage((int)percentWidth, 100, BufferedImage.TYPE_INT_RGB);
	            Image scaledImage = sourceImage.getScaledInstance((int)percentWidth, 100, Image.SCALE_SMOOTH);
	            img.createGraphics().drawImage(scaledImage, 0, 0, null);
	            BufferedImage img2 = new BufferedImage(100, 100 ,BufferedImage.TYPE_INT_RGB);
	            img2 = img.getSubimage((int)((percentWidth-100)/2), 0, 100, 100);
	            try{
	            	ImageIO.write(img2, "jpg", new File(outputFile));    
	            } catch (Exception ex){
	            	
	            }
	        }else{
	            float extraSize=    width-100;
	            float percentWidth = (extraSize/width)*100;
	            float  percentHight = height - ((height/100)*percentWidth);
	            BufferedImage img = new BufferedImage(100, (int)percentHight, BufferedImage.TYPE_INT_RGB);
	            Image scaledImage = sourceImage.getScaledInstance(100,(int)percentHight, Image.SCALE_SMOOTH);
	            img.createGraphics().drawImage(scaledImage, 0, 0, null);
	            BufferedImage img2 = new BufferedImage(100, 100 ,BufferedImage.TYPE_INT_RGB);
	            img2 = img.getSubimage(0, (int)((percentHight-100)/2), 100, 100);

	            try{
	            	ImageIO.write(img2, "jpg", new File(outputFile));    
	            } catch (Exception ex){
	            	
	            }
	        }

	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }

	}
	
	public static synchronized void createVideoThumbnail(Object parent,String outputFile) {
		
		//InputStream inputStream = parent.getClass().getClassLoader().getResourceAsStream("/images/h.264-logo.jpg");
		InputStream inputStream = null;
		OutputStream outputStream = null;
		File f =new File("");
		
		try {
			 inputStream = new FileInputStream(new File("h.264-logo.jpg"));
			 outputStream = new FileOutputStream(new File(outputFile));
			
			int read = 0;
			byte[] bytes = new byte[1024];
	 
			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
		} catch (IOException ex){
			System.err.println(ex.toString());
			
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					// outputStream.flush();
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	 
			}
		}
	}
	
	public static synchronized Process createVideoFromImages(String filePath, String outputFile){
		
		String command = "avconv -r 10 -i " +filePath + "IMG_%04d.jpg -r 10 -vcodec libx264 -crf 20 -g 15 -vf crop=2592:1458,scale=1280:720 " + outputFile + ".mp4";
		Process p=null;
		try {
	
			 p = Runtime.getRuntime().exec(command.split(" "));
			 
			 BufferedReader inp = new BufferedReader( new InputStreamReader(p.getErrorStream()) );
			 String line="";
			 while((line=inp.readLine())!=null){
 
			//	 if(line.contains(name)){
					System.err.println(line);
					// result= true;
					// break;
				 }
			// }
			 
			 inp.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return p;
	}
	public static synchronized Process createThumbnail(String filePath, String outputFile, int percentage,int delay){
		System.out.println( "createThumbnail");
		String  command = ("epeg -w " + percentage + "% -h " + percentage + "% " + filePath  + " " + outputFile );
		//System.out.println( command);
		Process p=null;
		try {
			 Thread.sleep(delay);
			 p = Runtime.getRuntime().exec(command.split(" "));
			 
			 BufferedReader inp = new BufferedReader( new InputStreamReader(p.getErrorStream()) );
			 String line="";
			 while((line=inp.readLine())!=null){
 
			//	 if(line.contains(name)){
					System.err.println(line);
					// result= true;
					// break;
				 }
			// }
			 
			 inp.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p;
	}
}
