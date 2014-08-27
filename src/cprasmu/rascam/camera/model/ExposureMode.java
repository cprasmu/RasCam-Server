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

/**
 * 
 * @author peterrasmussen
 *
 */
public enum ExposureMode {
	OFF (0,"off","Off"),
	AUTO (1,"auto","Auto"),
	NIGHT (2,"night","Night"),
	NIGHTPREVIEW (3,"nightpreview","Night-Preview"),
	BACKLIGHT(4,"backlight","Backlight"),
	SPOTLIGHT(5,"spotlight","Spotlight"),
	SPORTS(6,"sports","Sports"),
	SNOW(7,"snow","Snow"),
	BEACH(8,"beach","Beach"),
	VERYLONG(9,"verylong","Very-Long"),
	FIXEDFPS(10,"fixedfps","Fixed-FPS"),
	ANTISHAKE(11,"antishake","Anti-Shake"),
	FIREWORKS(12,"fireworks","Fireworks");
	
	public int mode;
	public String name;
	public String displayName;
	
	private ExposureMode(int mode, String name,String displayName) {
	        this.mode = mode;
	        this.name = name;
	        this.displayName = displayName;
	    }
	
	public static ExposureMode fromInt(int code) {
	    switch(code) {
	    case 0:
	        return OFF;
	    case 1:
	        return AUTO;
	    case 2:
	        return NIGHT;
	    case 3:
	        return NIGHTPREVIEW;
	    case 4:
	    	return BACKLIGHT;
	    case 5:
	    	return SPOTLIGHT;
	    case 6:
	    	return SPORTS;
	    case 7:
	    	return SNOW;
	    case 8:
	    	return BEACH;
	    case 9:
	    	return VERYLONG;
	    case 10:
	    	return FIXEDFPS;
	    case 11:
	    	return ANTISHAKE;
	    case 12:
	    	return FIREWORKS;
	    case 13:
	    
	    }
	    
	    return null;
	}
	
	public ExposureMode next(){
		
		if (this.equals(OFF)){
			return AUTO;
		} else if(this.equals(AUTO)){
			return NIGHT;
		} else if(this.equals(NIGHT)){
			return NIGHTPREVIEW;
		} else if(this.equals(NIGHTPREVIEW)){
			return BACKLIGHT;
		} else if(this.equals(BACKLIGHT)){
			return SPOTLIGHT;
		} else if(this.equals(SPOTLIGHT)){
			return SPORTS;
		} else if(this.equals(SPORTS)){
			return SNOW;
		} else if(this.equals(SNOW)){
			return BEACH;
		} else if(this.equals(BEACH)){
			return VERYLONG;
		} else if(this.equals(VERYLONG)){
			return FIXEDFPS;
		} else if(this.equals(FIXEDFPS)){
			return ANTISHAKE;
		} else if(this.equals(ANTISHAKE)){
			return FIREWORKS;
		} else if(this.equals(FIREWORKS)){
			return OFF;
		} else {
			return null;
		}
	}
	public ExposureMode previous(){
		
		if (this.equals(FIREWORKS)){
			return ANTISHAKE;
		} else if(this.equals(ANTISHAKE)){
			return FIXEDFPS;
		} else if(this.equals(FIXEDFPS)){
			return VERYLONG;
		} else if(this.equals(VERYLONG)){
			return BEACH;
		} else if(this.equals(BEACH)){
			return SNOW;
		} else if(this.equals(SNOW)){
			return SPORTS;
		} else if(this.equals(SPORTS)){
			return SPOTLIGHT;
		} else if(this.equals(SPOTLIGHT)){
			return BACKLIGHT;
		} else if(this.equals(BACKLIGHT)){
			return NIGHTPREVIEW;
		} else if(this.equals(NIGHTPREVIEW)){
			return NIGHT;
		} else if(this.equals(NIGHT)){
			return AUTO;
		} else if(this.equals(AUTO)){
			return OFF;
		} else if (this.equals(OFF)){
			return FIREWORKS;
		} else {
			return null;
		}
	}
}
