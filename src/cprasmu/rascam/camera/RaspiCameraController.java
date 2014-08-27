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
import java.util.Date;

import org.json.simple.JSONObject;

/**usage: RaspiStill [options]
//
//Image parameter commands
//
//-?, --help : This help information
//-w, --width : Set image width <size>
//-h, --height : Set image height <size>
//-q, --quality : Set jpeg quality <0 to 100>
//-r, --raw : Add raw bayer data to jpeg metadata
//*-o, --output : Output filename <filename> (to write to stdout, use '-o -'). If not specified, no file is saved
//-v, --verbose : Output verbose information during run
//-t, --timeout : Time (in ms) before takes picture and shuts down (if not specified, set to 5s)
//-th, --thumb : Set thumbnail parameters (x:y:quality)
//-d, --demo : Run a demo mode (cycle through range of camera options, no capture)
//-e, --encoding : Encoding to use for output file (jpg, bmp, gif, png)
//*-x, --exif : EXIF tag to apply to captures (format as 'key=value')
//*-tl, --timelapse : Timelapse mode. Takes a picture every <t>ms
//
//Preview parameter commands
//
//-p, --preview : Preview window settings <'x,y,w,h'>
//-f, --fullscreen : Fullscreen preview mode
//-n, --nopreview : Do not display a preview window
//
//Image parameter commands
//
//-sh, --sharpness : Set image sharpness (-100 to 100)
//-co, --contrast : Set image contrast (-100 to 100)
//-br, --brightness : Set image brightness (0 to 100)
//-sa, --saturation : Set image saturation (-100 to 100)
//-ISO, --ISO : Set capture ISO
//-vs, --vstab : Turn on video stablisation
//-ev, --ev : Set EV compensation
//*-ex, --exposure : Set exposure mode (see Notes)
//*-awb, --awb : Set AWB mode (see Notes)
//*-ifx, --imxfx : Set image effect (see Notes)
//-cfx, --colfx : Set colour effect (U:V)
//*-mm, --metering : Set metering mode (see Notes)
//-rot, --rotation : Set image rotation (0-359)
//*-hf, --hflip : Set horizontal flip
-vf, --vflip : Set vertical flip 


 */

import cprasmu.os.Environment;
import cprasmu.rascam.camera.model.AWBMode;
import cprasmu.rascam.camera.model.Brightness;
import cprasmu.rascam.camera.model.CameraModel;
import cprasmu.rascam.camera.model.Contrast;
import cprasmu.rascam.camera.model.EV;
import cprasmu.rascam.camera.model.ExposureMode;
import cprasmu.rascam.camera.model.FPS;
import cprasmu.rascam.camera.model.ISO;
import cprasmu.rascam.camera.model.ImageEffect;
import cprasmu.rascam.camera.model.MeteringMode;
import cprasmu.rascam.camera.model.Mode;
import cprasmu.rascam.camera.model.Quality;
import cprasmu.rascam.camera.model.Rotation;
import cprasmu.rascam.camera.model.Saturation;
import cprasmu.rascam.camera.model.Sharpness;
import cprasmu.rascam.camera.model.ShutterSpeed;
import cprasmu.rascam.camera.model.StillResolution;
import cprasmu.rascam.camera.model.TimelapseDelay;
import cprasmu.rascam.camera.model.VideoResolution;
import cprasmu.util.Log;
import cprasmu.util.SystemUtilities;



public class RaspiCameraController implements CameraController,FileCreationMonitor {
	
	private final static String TAG = RaspiCameraController.class.getSimpleName();
	public int singleShotTimeout = 1000;

	private static final String APP_STILL = "raspistill";
	private static final String APP_VIDEO = "raspivid";
	private String lastFilename="";
	private CameraModel cameraModel = new CameraModel();
	private Process process;
	private boolean recording = false;
	private PathMonitor pathMonitor;
	
	public RaspiCameraController() {
		killProcesses();
		MonitorFolder("/DCIM/");
	}
	
	public boolean isRecording(){
		return recording;
	} 
	
	public String getLastFilename(){
		return lastFilename;
	}
	
	public synchronized void createExternalProcessInThread(final String command,final String name){

		new Thread() {
	         public void run() {
	        	 	Thread.currentThread().setName("RECORD_THREAD_" +name.toUpperCase());
	            	try {
	            		cameraModel.startVideoCounter();	
	            		recording = true;
	            		process = Runtime.getRuntime().exec(command.split(" "));
	            		process.waitFor();
	            		Log.e(TAG, "Exit value : " + process.exitValue());
	            	} catch (IOException e) {
	        			// TODO Auto-generated catch block
	        			e.printStackTrace();
	        		} catch (InterruptedException e) {
	        			// TODO Auto-generated catch block
	        			e.printStackTrace();
	        		} finally {
	        			recording = false;
	            		cameraModel.resetVideoCounter();
	        		}
	            }
	    }.start();
		
	}
	
	public  String singleShotPhoto( ){
		
		String filename = DCIMHelper.getDirectoryForNewImage() + "/" + DCIMHelper.getNameForNewImage() + ".jpg";
		
		MonitorFolder( DCIMHelper.getDirectoryForNewImage() + "/");
		
		String  command = ( APP_STILL + " -w " + cameraModel.getStillResolution().width +  " -h " + cameraModel.getStillResolution().height +  " -n -sh " + cameraModel.getSharpness() + " -co " + cameraModel.getContrast() + " -br " + cameraModel.getBrightness() + " -sa " + cameraModel.getSaturation() + " -ISO " + cameraModel.getIso() + " -ev " + cameraModel.getEv() + " -mm " + cameraModel.getMeteringMode().name + " -ifx " + cameraModel.getImageEffect().name + " -awb " + cameraModel.getAwbMode().name + " -ex " + cameraModel.getExposureMode().name + " -t " + singleShotTimeout + " -x createdBy=ArDronePi -q " + cameraModel.getQuality() + " -o " + filename );
		
		
		if(cameraModel.getShutterSpeed().isEnabled()) {
			
			command+=" -ss " + cameraModel.getShutterSpeed().getValue();
		}
		
		if(cameraModel.ishFlip()){
			command+=" -hf";
		}
		
		if(cameraModel.isvFlip()){
			command+=" -vf";
		}
		
		if(cameraModel.isvStab()){
			command+=" -vs";
		}
		
		Log.e(TAG, command);
		
		createExternalProcessInThread(command,filename);

		return filename;
	}
	
	public  String timelapsePhoto(){
		
		if ( !DCIMHelper.getNameForNewImage().equals("IMG_0001") || !DCIMHelper.getNameForNewVideo().equals("VID_0001")){
			DCIMHelper.makeNewDirectory();
		}
		MonitorFolder( DCIMHelper.getDirectoryForNewImage() + "/");
		String filename = DCIMHelper.getDirectoryForNewImage() + "/IMG_%04d.jpg";
		
		String  command = (APP_STILL + " -w " + cameraModel.getStillResolution().width +  " -h " + cameraModel.getStillResolution().height +  " -n -sh " + cameraModel.getSharpness() + " -co " + cameraModel.getContrast() + " -br " + cameraModel.getBrightness() + " -sa " + cameraModel.getSaturation() + " -ISO " + cameraModel.getIso() + " -ev " + cameraModel.getEv() + " -mm " + cameraModel.getMeteringMode().name + " -ifx " + cameraModel.getImageEffect().name + " -awb " + cameraModel.getAwbMode().name + " -ex " + cameraModel.getExposureMode().name + " -t " + 99999999 + " -x createdBy=ArDronePi -tl " + ( cameraModel.getTimelapseDelay().getValue() * 1000) + " -q " + cameraModel.getQuality() + " -o " +filename );
		
		if(cameraModel.getShutterSpeed().isEnabled()) {
			
			command+=" -ss " + cameraModel.getShutterSpeed().getValue();
		}

		if(cameraModel.ishFlip()){
			command+=" -hf";
		}
		
		if(cameraModel.isvFlip()){
			command+=" -vf";
		}
		
		if(cameraModel.isvStab()){
			command+=" -vs";
		}
		
		Log.e(TAG, command);

		createExternalProcessInThread(command,filename);
		
		return filename;
	}
	
	public String video( ){
		String filename = DCIMHelper.getDirectoryForNewImage() + "/" + DCIMHelper.getNameForNewVideo() + ".mov" ;
		MonitorFolder( DCIMHelper.getDirectoryForNewImage() + "/");
		String command = ( APP_VIDEO + " -w " + cameraModel.getVideoResolution().width +  " -h " + cameraModel.getVideoResolution().height + " -n -sh " + cameraModel.getSharpness() + " -co " + cameraModel.getContrast() + " -br " + cameraModel.getBrightness() + " -sa " + cameraModel.getSaturation() + " -ISO " + cameraModel.getIso() + " -ev " + cameraModel.getEv() + " -mm " + cameraModel.getMeteringMode().name + " -ifx " + cameraModel.getImageEffect().name + " -awb " + cameraModel.getAwbMode().name + " -ex " + cameraModel.getExposureMode().name + " -fps " + cameraModel.getFps().getValue() + " -t 9999999 -o " + filename );
		
		if(cameraModel.getShutterSpeed().isEnabled()) {
			
			command+=" -ss " + cameraModel.getShutterSpeed().getValue();
		}
		
		if(cameraModel.ishFlip()){
			command+=" -hf";
		}
		
		if(cameraModel.isvFlip()){
			command+=" -vf";
		}
		
		if(cameraModel.isvStab()){
			command+=" -vs";
		}
		
		Log.e(TAG, command);
		
		createExternalProcessInThread(command,filename);
		
		return filename;
	}
	
	private void MonitorFolder(String path){
		
		if((pathMonitor==null) || (!pathMonitor.getPath().equals(path))){
			if (!(pathMonitor==null)) {
				pathMonitor.close();
			}
			
			pathMonitor = new PathMonitor(this, path);
			pathMonitor.start();
		}
	}
	
	
	public boolean killProcesses(){
		
		System.out.println("killProcesses");
		
		cameraModel.resetVideoCounter();
		
		if (Environment.isRunnging("raspi")) {
			Environment.killProcess("raspistill");
			Environment.killProcess("raspivid");
			return true;
		}
		return false;
	}
	
	private Process getProcess(){
		return process;
	}

	@Override
	public Date getLastModified() {

		return cameraModel.getLastModified();
	}

	@Override
	public String getCameraMode() {

		return cameraModel.getCameraMode().displayName;
	}

	@Override
	public void setCameraMode(Mode cameraMode) {
		if(!isRecording()) {
			cameraModel.setCameraMode(cameraMode);
		}
	}
	
	@Override
	public void nextCameraMode() {
		if(!isRecording()) {
			cameraModel.setCameraMode(cameraModel.getCameraMode().next());
		}
	}

	@Override
	public void previousCameraMode() {
		if(!isRecording()) {
			cameraModel.setCameraMode(cameraModel.getCameraMode().previous());
		}
	}
	
	
	@Override
	public String getAwbMode() {
		return cameraModel.getAwbMode().displayName;
	}
	
	@Override
	public void setAwbMode(AWBMode awbMode) {
		if(!isRecording()) {
			cameraModel.setAwbMode(awbMode);
		}
	}
	
	@Override
	public void nextAwbMode() {
		if(!isRecording()) {
			cameraModel.setAwbMode(cameraModel.getAwbMode().next());
		}
	}

	@Override
	public void previousAwbMode() {
		if(!isRecording()) {
			cameraModel.setAwbMode(cameraModel.getAwbMode().previous());
		}
	}
	
	@Override
	public Brightness getBrightness() {
		return cameraModel.getBrightness();
	}

	@Override
	public void setBrightness(Brightness brightness) {
		if(!isRecording()) {
			cameraModel.setBrightness(brightness);
		}
	}

	@Override
	public EV getEv() {
		return cameraModel.getEv();
	}

	@Override
	public void setEv(EV ev) {
		if(!isRecording()) {
			cameraModel.setEv(ev);
		}
	}

	@Override
	public String getExposureMode() {
	
		return cameraModel.getExposureMode().displayName;
	}

	@Override
	public void setExposureMode(ExposureMode exposureMode) {
		if(!isRecording()) {
			cameraModel.setExposureMode(exposureMode);
		}
	}

	@Override
	public String getImageEffect() {
		return cameraModel.getImageEffect().displayName;
	}

	@Override
	public void setImageEffect(ImageEffect imageEffect) {
		if(!isRecording()) {
			cameraModel.setImageEffect(imageEffect);
		}
	}
	
	@Override
	public void nextImageEffect() {
		if(!isRecording()) {
			cameraModel.setImageEffect(cameraModel.getImageEffect().next());
		}
	}

	@Override
	public void previousImageEffect() {
		if(!isRecording()) {
			cameraModel.setImageEffect(cameraModel.getImageEffect().previous());
		}
	}
	
	@Override
	public void resetImageEffect() {
		cameraModel.setImageEffect(CameraModel.DEFAULT_IMAGE_EFFECT);
		
	}
	
	@Override
	public ISO getIso() {
		return cameraModel.getIso();
	}

	@Override
	public void setIso(ISO iso) {
		if(!isRecording()) {
			cameraModel.setIso(iso);
		}
	}

	@Override
	public Quality getQuality() {
		return cameraModel.getQuality();
	}

	@Override
	public void setQuality(Quality quality) {
		if(!isRecording()) {
			cameraModel.setQuality(quality);
		}
	}

	
	
	
	
	@Override
	public String getMeteringMode() {
		return cameraModel.getMeteringMode().displayName;
	}

	@Override
	public void setMeteringMode(MeteringMode meteringMode) {
		if(!isRecording()) {
			cameraModel.setMeteringMode(meteringMode);
		}
	}
	
	@Override
	public void nextMeteringMode() {
		if(!isRecording()) {
			cameraModel.setMeteringMode(cameraModel.getMeteringMode().next());
		}
	}

	@Override
	public void previousMeteringMode() {
		if(!isRecording()) {
			cameraModel.setMeteringMode(cameraModel.getMeteringMode().previous());
		}
		
	}
	
	@Override
	public void resetMeteringMode() {
		cameraModel.setMeteringMode(cameraModel.DEFAULT_METERING_MODE);
		
	}
	
	@Override
	public Saturation getSaturation() {
		return cameraModel.getSaturation();
	}

	@Override
	public void setSaturation(Saturation saturation) {
		if(!isRecording()) {
			cameraModel.setSaturation(saturation);
		}
	}

	@Override
	public Sharpness getSharpness() {
	
		return cameraModel.getSharpness();
	}

	@Override
	public void setSharpness(Sharpness sharpness) {
		if(!isRecording()) {
			cameraModel.setSharpness(sharpness);
		}
	}

	@Override
	public Contrast getContrast() {
		return cameraModel.getContrast();
	}

	@Override
	public void setContrast(Contrast contrast) {
		if(!isRecording()) {
			cameraModel.setContrast(contrast);
		}
	}

	@Override
	public boolean isHFlip() {
		return cameraModel.ishFlip();
	}

	@Override
	public void setHFlip(boolean hFlip) {
		if(!isRecording()) {
			cameraModel.sethFlip(hFlip);
		}
	}

	@Override
	public boolean isVFlip() {
		return cameraModel.isvFlip();
	}

	@Override
	public void setVFlip(boolean vFlip) {
		if(!isRecording()) {
			cameraModel.setvFlip(vFlip);
		}
	}

	@Override
	public boolean isVStab() {
		return cameraModel.isvStab();
	}

	@Override
	public void setVStab(boolean vStab) {
		if(!isRecording()) {
			cameraModel.setvStab(vStab);
		}
	}

	@Override
	public String getRotation() {
		return cameraModel.getRotation().displayName;
	}

	@Override
	public void setRotation(Rotation rotation) {
		if(!isRecording()) {
			cameraModel.setRotation(rotation);
		}
	}
	
	@Override
	public void nextRotation() {
		if(!isRecording()) {
			cameraModel.setRotation(cameraModel.getRotation().next());
		}
	}

	@Override
	public void previousRotation() {
		if(!isRecording()) {
			cameraModel.setRotation(cameraModel.getRotation().previous());
		}
	}

	@Override
	public long getVideoStart() {
		return cameraModel.getVideoStart();
	}

	@Override
	public void startVideoCounter() {
		if(!isRecording()) {
			cameraModel.startVideoCounter();
		}
	}

	@Override
	public void resetVideoCounter() {
		cameraModel.resetVideoCounter();
	}

	@Override
	public long getVideoDuration() {
		return cameraModel.getVideoDuration();
	}

	@Override
	public TimelapseDelay getTimelapseDelay() {
		return cameraModel.getTimelapseDelay();
	}

	@Override
	public void setTimelapseDelay(TimelapseDelay timelapseDelay) {
		if(!isRecording()) {
			cameraModel.setTimelapseDelay(timelapseDelay);
		}
	}

	@Override
	public FPS getFps() {
		return cameraModel.getFps();
	}

	@Override
	public void setFps(FPS fps) {
		if(!isRecording()) {
			cameraModel.setFps(fps);
		}
	}

	@Override
	public JSONObject toJSON() {
		return cameraModel.toJSON();
	}
	
	public String toString(){
		return cameraModel.toString();
	}

	@Override
	public void resetCameraMode() {
		cameraModel.setCameraMode(CameraModel.DEFAULT_CAMERA_MODE);		
	}

	@Override
	public void resetAwbMode() {
		cameraModel.setAwbMode(CameraModel.DEFAULT_AWB_MODE);
	}

	@Override
	public void resetBrightness() {
		cameraModel.setBrightness(new Brightness(CameraModel.DEFAULT_BRIGHTNESS));
	}

	@Override
	public void resetEv() {
		cameraModel.setEv(new EV(CameraModel.DEFAULT_EV));
	}
	
	@Override
	public void nextExposureMode() {
		if(!isRecording()) {
			cameraModel.setExposureMode(cameraModel.getExposureMode().next());
		}
	}

	@Override
	public void previousExposureMode() {
		if(!isRecording()) {
			cameraModel.setExposureMode(cameraModel.getExposureMode().previous());
		}
	}
	
	@Override
	public void resetExposureMode() {
		cameraModel.setExposureMode(CameraModel.DEFAULT_EXPOSURE_MODE);
	}

	@Override
	public void resetIso() {
		cameraModel.setIso(new ISO(CameraModel.DEFAULT_ISO));
		
	}

	@Override
	public void resetQuality() {
		cameraModel.setQuality(new Quality(CameraModel.DEFAULT_QUALITY));
	}
	

	@Override
	public void resetSaturation() {
		cameraModel.setSaturation(new Saturation(CameraModel.DEFAULT_SATURATION));
		
	}

	@Override
	public void resetSharpness() {
		cameraModel.setSharpness(new Sharpness(CameraModel.DEFAULT_SHARPNESS));
	}

	@Override
	public void resetContrast() {
		cameraModel.setContrast(new Contrast(CameraModel.DEFAULT_CONTRAST));
	}

	@Override
	public void resetHFlip() {
		cameraModel.sethFlip(CameraModel.DEFAULT_HFLIP);
	}

	@Override
	public void resetVFlip() {
		cameraModel.setvFlip(CameraModel.DEFAULT_VFLIP);
	}

	@Override
	public void resetVStab() {
		cameraModel.setvStab(CameraModel.DEFAULT_VSTAB);
	}

	@Override
	public void resetRotation() {
		cameraModel.setRotation(cameraModel.DEFAULT_ROTATION);
	}

	@Override
	public void resetTimelapseDelay() {
		cameraModel.setTimelapseDelay(new TimelapseDelay(cameraModel.DEFAULT_TIMELAPSE_DELAY));
	}

	@Override
	public void resetFps() {
		cameraModel.setFps(new FPS(CameraModel.DEFAULT_FPS));
	}

	@Override
	public void reset() {
		if (!isRecording()) {
			this.resetAwbMode();
			this.resetBrightness();
			this.resetCameraMode();
			this.resetContrast();
			this.resetEv();
			this.resetExposureMode();
			this.resetFps();
			this.resetHFlip();
			this.resetImageEffect();
			this.resetIso();
			this.resetMeteringMode();
			this.resetQuality();
			this.resetRotation();
			this.resetSaturation();
			this.resetSharpness();
			this.resetTimelapseDelay();
			this.resetVFlip();
			this.resetVStab();
			this.resetVideoResolution();
			this.resetStillResolution();
		}
	}

	@Override
	public void stop() {
		killProcesses();
	}

	@Override
	public void nextBrightness() {
		if (!isRecording()){
			cameraModel.getBrightness().increment();
		}
	}

	@Override
	public void previousBrightness() {
		if (!isRecording()){
			cameraModel.getBrightness().decrement();
		}
	}

	@Override
	public long getFileSystemUsage() {
		return SystemUtilities.calculateUsage("/DCIM/");
	}

	@Override
	public void fileCreated(String path, String filename) {
		// TODO Auto-generated method stub
		if (filename.endsWith(".jpg")) {
		
			ImageUtils.createThumbnail(path + filename,path+ filename.replace(".jpg", ".thm"), cameraModel.getStillResolution().thumbscale, cameraModel.getStillResolution().thumbscale);
			
		} else if (filename.endsWith(".mov")){
			System.out.println("CREATE "+filename.replace(".mov", ".thm"));
			ImageUtils.createVideoThumbnail(this,path+filename.replace(".mov", ".thm"));
			
		}
		
		if (!filename.contains("~") && (!filename.endsWith(".thm"))){
			lastFilename = path + filename;
		}
	}

	@Override
	public void fileDeleted(String path, String filename) {
		//TODO: do we need to delete anything else or update something?
		System.out.println(filename);
	}

	@Override
	public void fileModified(String path, String filename) {
		
		
	}

	@Override
	public String getVideoResolution() {
		// TODO Auto-generated method stub
		return cameraModel.getVideoResolution().displayName;
	}

	@Override
	public void setVideoResolution(VideoResolution videoResolution) {
		if(!isRecording()) {
			cameraModel.setVideoResolution(videoResolution);
		}
	}

	@Override
	public void nextVideoResolution() {
		if(!isRecording()) {
			cameraModel.setVideoResolution(cameraModel.getVideoResolution().next());
		}
	}

	@Override
	public void previousVideoResolution() {
		if(!isRecording()) {
			cameraModel.setVideoResolution(cameraModel.getVideoResolution().previous());
		}
	}

	@Override
	public void resetVideoResolution() {
		if (!isRecording()) {
			cameraModel.setVideoResolution(CameraModel.DEFAULT_VIDEO_RESOLUTION);
		}
	}

	@Override
	public String getStillResolution() {
		return cameraModel.getStillResolution().displayName;
	}

	@Override
	public void setStillResolution(StillResolution stillResolution) {
		if (!isRecording()) {
			cameraModel.setStillResolution(stillResolution);
		}
	}

	@Override
	public void nextStillResolution() {
		if (!isRecording()) {
			cameraModel.setStillResolution(cameraModel.getStillResolution().next());
		}
	}

	@Override
	public void previousStillResolution() {
		if (!isRecording()) {
			cameraModel.setStillResolution(cameraModel.getStillResolution().previous());
		}
	}

	@Override
	public void resetStillResolution() {
		if (!isRecording()) {
			cameraModel.setStillResolution(CameraModel.DEFAULT_STILL_RESOLUTION);
		}
	}

	@Override
	public ShutterSpeed getShutterSpeed() {
		// TODO Auto-generated method stub
		return cameraModel.getShutterSpeed();
	}

	@Override
	public void setShutterSpeed(ShutterSpeed speed) {
		// TODO Auto-generated method stub
		if (!isRecording()) {
			cameraModel.setShutterSpeed(speed);
		}
	}

	@Override
	public void resetShutterSpeed() {
		// TODO Auto-generated method stub
		if (!isRecording()) {
			cameraModel.setShutterSpeed(new ShutterSpeed(CameraModel.DEFAULT_SHUTTER_SPEED));
			
		}
	}

	@Override
	public void enableShutterSpeed() {
		// TODO Auto-generated method stub
		if (!isRecording()){
			cameraModel.getShutterSpeed().setEnabled(true);
		}
	}

	@Override
	public void disableShutterSpeed() {
		// TODO Auto-generated method stub
		if (!isRecording()){
			cameraModel.getShutterSpeed().setEnabled(false);
		}
	}

	@Override
	public void nextShutterSpeed() {
		// TODO Auto-generated method stub
		if (!isRecording()){
			cameraModel.getShutterSpeed().increment();
		}
	}

	@Override
	public void previousShutterSpeed() {
		// TODO Auto-generated method stub
		if (!isRecording()){
			cameraModel.getShutterSpeed().decrement();
		}
	}

}
