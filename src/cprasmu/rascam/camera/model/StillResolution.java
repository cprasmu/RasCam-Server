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

public enum StillResolution {

	MAX(0,2592,1944,"5MP MAX",10),
	SXGA(1,1280,1024,"1.3MP SXGA",20),
	WXGA(2,1280,768,"1MP WXGA",20),
	SVGA(3,800,600,"0.5MP SVGA",32),
	WVGA(4,768,480,"0.5MP WVGA",35),
	VGA(5,640,480,"0.3MP VGA",40);
	
	public int width;
	public int height;
	public String displayName;
	public int mode;
	public int thumbscale;
	
	private StillResolution(int mode,int width, int height,String displayName,int thumbscale) {
			this.mode = mode;
	        this.width = width;
	        this.height = height;
	        this.displayName = displayName;
	        this.thumbscale = thumbscale;
	}
	
	public static StillResolution fromInt(int code) {
	    switch(code) {
	    case 0:
	        return MAX;
	    case 1:
	        return SXGA;
	    case 2:
	        return WXGA;
	    case 3:
	        return SVGA;
	    case 4:
	        return WVGA;
	    case 5:
	        return VGA;
	    }
	    
	    return null;
	}
	
	public StillResolution next(){
		
		if(this.equals(MAX)){
			return SXGA;
		} else if(this.equals(SXGA)){
			return WXGA;
		} else if(this.equals(WXGA)){
			return SVGA;
		} else if(this.equals(SVGA)){
			return WVGA;
		} else if(this.equals(WVGA)){
			return VGA;
		} else if(this.equals(VGA)){
			return MAX;
		} else {
			return null;
		}
	}
	
	public StillResolution previous(){
		
		if(this.equals(VGA)){
			return WVGA;
		} else if(this.equals(WVGA)){
			return SVGA;
		} else if(this.equals(SVGA)){
			return WXGA;
		} else if(this.equals(WXGA)){	
			return SXGA;
		} else if(this.equals(SXGA)){
			return MAX;
		} else if(this.equals(MAX)){
			return VGA;
		} else {
			return null;
		}
	}
	
	
	
}
