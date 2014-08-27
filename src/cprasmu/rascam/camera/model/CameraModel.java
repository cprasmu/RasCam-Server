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
package cprasmu.rascam.camera.model;

import java.util.Date;

import org.json.simple.JSONObject;

import cprasmu.util.SystemUtilities;

/** 
 * @version 1.0
 * @author Christian P Rasmussen cprasmussen@hotmail.com
 * Camera model to support Raspberry-Pi Camera functions
 */
public class CameraModel {
	
	//Default model values;
	public static final int DEFAULT_SATURATION = 0;//
	public static final int DEFAULT_BRIGHTNESS = 50;//
	public static final int DEFAULT_SHARPNESS = 0;//
	public static final int DEFAULT_CONTRAST  = 0;//
	public static final int DEFAULT_QUALITY = 75;
	public static final Mode DEFAULT_CAMERA_MODE = Mode.SINGLE_SHOT;
	public static final AWBMode DEFAULT_AWB_MODE = AWBMode.AUTO;
	public static final MeteringMode DEFAULT_METERING_MODE = MeteringMode.AVERAGE;
	public static final ExposureMode DEFAULT_EXPOSURE_MODE  = ExposureMode.AUTO;
	public static final ImageEffect DEFAULT_IMAGE_EFFECT  = ImageEffect.NONE;
	public static final int  DEFAULT_ISO = 100;
	public static final int  DEFAULT_EV = 0;
	public static final boolean DEFAULT_HFLIP = false;//
	public static final boolean DEFAULT_VFLIP = false;//
	public static final boolean DEFAULT_VSTAB = false;//
	public static final Rotation DEFAULT_ROTATION = Rotation.ROT000;
	public static final int DEFAULT_TIMELAPSE_DELAY = 4;//
	public static final int DEFAULT_FPS = 30;
	public static final VideoResolution DEFAULT_VIDEO_RESOLUTION = VideoResolution.V1080P;
	public static final StillResolution DEFAULT_STILL_RESOLUTION = StillResolution.MAX;
	public static final int DEFAULT_SHUTTER_SPEED =10000;
	
	
	private Mode cameraMode = DEFAULT_CAMERA_MODE;		//UI
	private AWBMode awbMode = DEFAULT_AWB_MODE;					//UI
	private MeteringMode meteringMode = DEFAULT_METERING_MODE;	//UI
	private ExposureMode exposureMode = DEFAULT_EXPOSURE_MODE;	//UI
	private ImageEffect imageEffect = DEFAULT_IMAGE_EFFECT;		//UI
	private ISO iso = new ISO(DEFAULT_ISO);
	private EV ev = new EV(DEFAULT_EV);
	private long videoStart = -1;
	private Saturation saturation = new Saturation(DEFAULT_SATURATION);
	private Brightness brightness = new Brightness(DEFAULT_BRIGHTNESS);
	private Sharpness sharpness = new Sharpness( DEFAULT_SHARPNESS);
	private Contrast contrast = new Contrast( DEFAULT_CONTRAST);
	private boolean hFlip = DEFAULT_HFLIP;
	private boolean vFlip = DEFAULT_VFLIP;						//UI
	private boolean vStab = DEFAULT_VSTAB;
	private Rotation rotation = DEFAULT_ROTATION;
	private Quality quality = new Quality(DEFAULT_QUALITY);
	private Date lastModified = new Date();
	private TimelapseDelay timelapseDelay = new TimelapseDelay (DEFAULT_TIMELAPSE_DELAY);
	private FPS fps = new FPS(DEFAULT_FPS);
	private VideoResolution videoResolution = DEFAULT_VIDEO_RESOLUTION;
	private StillResolution stillResolution = DEFAULT_STILL_RESOLUTION;
	private ShutterSpeed shutterSpeed = new ShutterSpeed(DEFAULT_SHUTTER_SPEED);
	
	
	public CameraModel(){
		this.shutterSpeed.setEnabled(false);
	}
	
	public Date getLastModified(){
		return lastModified;
		
	}
	public Mode getCameraMode() {
		return cameraMode;
	}

	public void setCameraMode(Mode cameraMode) {
		this.cameraMode = cameraMode;
	}

	public VideoResolution getVideoResolution(){
		return videoResolution;
	}
	
	public void setVideoResolution(VideoResolution videoResolution) {
		this.videoResolution = videoResolution;
	}
	
	public StillResolution getStillResolution(){
		return stillResolution;
	}
	
	public void setStillResolution(StillResolution stillResolution) {
		this.stillResolution = stillResolution;
	}
	
	public AWBMode getAwbMode() {
		return awbMode;
	}

	public void setAwbMode(AWBMode awbMode) {
		this.awbMode = awbMode;
	}

	public Brightness getBrightness() {
		return brightness;
	}

	public void setBrightness(Brightness brightness) {
		this.brightness = brightness;
		lastModified = new Date();
	}

	public EV getEv() {
		return ev;
	}

	public void setEv(EV ev) {
		this.ev = ev;
		lastModified = new Date();
	}

	public ExposureMode getExposureMode() {
		return exposureMode;
	}

	public void setExposureMode(ExposureMode exposureMode) {
		this.exposureMode = exposureMode;
		lastModified = new Date();
	}

	public ImageEffect getImageEffect() {
		return imageEffect;
	}

	public void setImageEffect(ImageEffect imageEffect) {
		this.imageEffect = imageEffect;
		lastModified = new Date();
	}

	public ISO getIso() {
		return iso;
	}

	public void setIso(ISO iso) {
		this.iso = iso;
		lastModified = new Date();
	}
	
	public Quality getQuality() {
		return quality;
	}

	public void setQuality(Quality quality) {
		this.quality = quality;
		lastModified = new Date();
	}
	
	public ShutterSpeed getShutterSpeed() {
		return shutterSpeed;
	}

	public void setShutterSpeed(ShutterSpeed speed) {
		this.shutterSpeed = speed;
		lastModified = new Date();
	}
	
	public MeteringMode getMeteringMode() {
		return meteringMode;
	}

	public void setMeteringMode(MeteringMode meteringMode) {
		this.meteringMode = meteringMode;
		lastModified = new Date();
	}

	public Saturation getSaturation() {
		return saturation;
	}

	public void setSaturation(Saturation saturation) {
		this.saturation = saturation;
		lastModified = new Date();
	}

	public Sharpness getSharpness() {
		return sharpness;
	}

	public void setSharpness(Sharpness sharpness) {
		this.sharpness = sharpness;
		lastModified = new Date();
	}
	
	public Contrast getContrast() {
		return contrast;
	}

	public void setContrast(Contrast contrast) {
		this.contrast = contrast;
		lastModified = new Date();
	}
	
	public boolean ishFlip() {
		return hFlip;
	}

	public void sethFlip(boolean hFlip) {
		this.hFlip = hFlip;
		lastModified = new Date();
	}

	public boolean isvFlip() {
		return vFlip;
	}

	public void setvFlip(boolean vFlip) {
		this.vFlip = vFlip;
		lastModified = new Date();
	}

	public boolean isvStab() {
		return vStab;
	}

	public void setvStab(boolean vStab) {
		this.vStab = vStab;
		lastModified = new Date();
	}

	public Rotation getRotation() {
		return rotation;
	}

	public void setRotation(Rotation rotation) {
		this.rotation = rotation;
		lastModified = new Date();
	}

	
	/**
	 * Converts the model to JSON format.
	 * 
	 * @return a JSON representation of the current camera model.
	 */
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(){
		
		JSONObject obj = new JSONObject();

		obj.put("CameraMode",cameraMode.displayName);
		obj.put("AWBMode" , awbMode.displayName);
		obj.put("ExposureMode" , exposureMode.displayName);
		obj.put("ImageEffect" , imageEffect.displayName);
		obj.put("MeteringMode" , meteringMode.displayName);
		obj.put("Rotation" , rotation.displayName);
		obj.put("ISO" , iso.getValue());
		obj.put("EV" , ev.getValue());
		obj.put("Brightness" , brightness.getValue());
		obj.put("Saturation" , saturation.getValue());
		obj.put("Sharpness" , sharpness.getValue());
		obj.put("Contrast" , contrast.getValue());
		obj.put("HFlip" , hFlip);
		obj.put("VFlip" , vFlip);
		obj.put("VStab" , vStab);
		obj.put("Duration" , getVideoDuration());
		obj.put("Quality",quality.getValue());
		obj.put("TimelapseDelay",timelapseDelay.getValue());
		obj.put("FPS",fps.getValue());
		obj.put("VideoResolution", videoResolution.displayName);
		obj.put("StillResolution", stillResolution.displayName);
		obj.put("ShutterSpeed", shutterSpeed.getValue());
		obj.put("ShutterSpeedEnabled", shutterSpeed.isEnabled());
		return obj;
		
	}
	
	/**
	 * Converts the model to string format.
	 * 
	 * @return a String representation of the current camera model.
	 */
	public String toString(){
		
		//long tmp = getVideoDuration() / 1000;
		//long seconds = tmp % 60;
		//long minutes = tmp / 60;
		
		String tmp = SystemUtilities.getDurationHMS( getVideoDuration());
		
		return  "CameraMode : " + cameraMode.displayName + "\n" + 
				"AWBMode : " + awbMode.displayName + "\n" + 
				"ExposureMode : " + exposureMode.displayName + "\n" + 
				"ImageEffect : " + imageEffect.displayName + "\n" + 
				"MeteringMode : " + meteringMode.displayName + "\n" + 
				"Rotation : " + rotation.displayName + "\n" + 
				"ISO : " + iso.getValue() + "\n" +
				"EV : " + ev.getValue() + "\n" +
				"Brightness : " + brightness.getValue() + "\n" +
				"Sharpness : " + sharpness.getValue() + "\n" +
				"Contrast : " + contrast.getValue() + "\n" +
				"Saturation : " + saturation.getValue() + "\n" +
				"HFlip : " + hFlip + "\n" +
				"VFlip : " + vFlip + "\n" +
				"VStab : " + vStab + "\n" +
				"Duration : " +tmp +"\n" + // + minutes + ":" +seconds + "\n" +
				"Quality : " + quality.getValue() + "\n" +
				"TimelapseDelay : " + timelapseDelay.getValue()  + "\n" +
				"FPS : " + fps.getValue()+ "\n" +
				"VideoResolution : " + videoResolution.displayName + "\n" +
				"StillResolution : " + stillResolution.displayName + "\n" +
				"ShutterSpeed : " + shutterSpeed.getValue() + "\n" +
				"ShutterSpeedEnabled : "  + shutterSpeed.isEnabled();
		
 	}
	
	
	/*public static void main(String [] args){
		
		CameraModel cm = new CameraModel();
		System.out.println(cm.toJSON());
		cm.setAwbMode(AWBMode.FLOURESCENT);
	}
*/
	public long getVideoStart() {
		return videoStart;
	}

	public void startVideoCounter(){
		this.videoStart =  System.currentTimeMillis();
		//lastModified = new Date();
	}
	
	public void resetVideoCounter(){
		this.videoStart = -1;
		//lastModified = new Date();
	}
	
	public long getVideoDuration() {
		if(this.videoStart>-1){
			return System.currentTimeMillis() - this.videoStart;
		} else {
			return 0;
		}
	}
	public TimelapseDelay getTimelapseDelay() {
		return timelapseDelay;
	}
	public void setTimelapseDelay(TimelapseDelay timelapseDelay) {
		this.timelapseDelay = timelapseDelay;
		lastModified = new Date();
	}
	public FPS getFps() {
		return fps;
	}
	public void setFps(FPS fps) {
		this.fps = fps;
	}
}
