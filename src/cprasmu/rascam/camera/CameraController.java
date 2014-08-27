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

import java.util.Date;

import org.json.simple.JSONObject;

import cprasmu.rascam.camera.model.AWBMode;
import cprasmu.rascam.camera.model.Brightness;
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
/** 
 * {@code CameraController} is an interface to provide typical user controls for the camera.
 * @author peterrasmussen
 *
 */
public interface CameraController {

	//public CameraModel getCameraModel() ;
	public boolean isRecording();
	public String singleShotPhoto();
	public String timelapsePhoto();
	public String video();
	
	public Date getLastModified();
	
	public String getCameraMode();
	public void setCameraMode(Mode cameraMode);
	public void nextCameraMode();
	public void previousCameraMode();
	public void resetCameraMode();
	
	public String getVideoResolution();
	public void setVideoResolution(VideoResolution videoResolution);
	public void nextVideoResolution();
	public void previousVideoResolution();
	public void resetVideoResolution();
	
	public String getStillResolution();
	public void setStillResolution(StillResolution stillResolution);
	public void nextStillResolution();
	public void previousStillResolution();
	public void resetStillResolution();
	
	
	public String getAwbMode() ;
	public void setAwbMode(AWBMode awbMode);
	public void nextAwbMode() ;
	public void previousAwbMode() ;
	public void resetAwbMode();
	
	public Brightness getBrightness();
	public void setBrightness(Brightness brightness);
	public void nextBrightness();
	public void previousBrightness();
	public void resetBrightness();
	
	public EV getEv() ;
	public void setEv(EV ev);
	public void resetEv();
	
	public String getExposureMode() ;
	public void setExposureMode(ExposureMode exposureMode);
	public void nextExposureMode();
	public void previousExposureMode();
	public void resetExposureMode();
	
	public String getImageEffect() ;
	public void setImageEffect(ImageEffect imageEffect) ;
	public void nextImageEffect();
	public void previousImageEffect();
	public void resetImageEffect();
	
	public ISO getIso() ;
	public void setIso(ISO iso) ;
	public void resetIso();
	
	public Quality getQuality() ;
	public void setQuality(Quality quality) ;
	public void resetQuality();
	
	public ShutterSpeed getShutterSpeed();
	public void setShutterSpeed(ShutterSpeed speed) ;
	public void nextShutterSpeed() ;
	public void previousShutterSpeed() ;
	public void resetShutterSpeed();
	public void enableShutterSpeed();
	public void disableShutterSpeed();
	
	public String getMeteringMode() ;
	public void setMeteringMode(MeteringMode meteringMode) ;
	public void nextMeteringMode();
	public void previousMeteringMode();
	public void resetMeteringMode();
	
	public Saturation getSaturation() ;
	public void setSaturation(Saturation saturation) ;
	public void resetSaturation();
	
	public Sharpness getSharpness() ;
	public void setSharpness(Sharpness sharpness) ;
	public void resetSharpness();
	
	public Contrast getContrast() ;
	public void setContrast(Contrast contrast) ;
	public void resetContrast();
	
	public boolean isHFlip() ;
	public void setHFlip(boolean hFlip) ;
	public void resetHFlip();
	
	public boolean isVFlip() ;
	public void setVFlip(boolean vFlip);
	public void resetVFlip();
	
	public boolean isVStab() ;
	public void setVStab(boolean vStab) ;
	public void resetVStab();
	
	public String getRotation() ;
	public void setRotation(Rotation rotation);
	public void nextRotation();
	public void previousRotation();
	public void resetRotation();
	
	public long getVideoStart();

	public void startVideoCounter();
	public void resetVideoCounter();
	
	public long getVideoDuration() ;
	
	public TimelapseDelay getTimelapseDelay() ;
	public void setTimelapseDelay(TimelapseDelay timelapseDelay);
	public void resetTimelapseDelay() ;
	
	public FPS getFps() ;
	public void setFps(FPS fps) ;
	public void resetFps() ;
	
	public JSONObject toJSON();
	
	public void reset();
	
	public void stop();
	
	public String getLastFilename();
	public long getFileSystemUsage();
	
}
