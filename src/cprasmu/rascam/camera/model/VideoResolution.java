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

public enum VideoResolution {

	
	
/*
    1080p = 1920x1080 pixels (16:9) 
    960p = 1280x960 pixels (4:3) 
    720p = 1280x720 pixels (16:9)
    WVGA = 848x480 pixels (16:9)
*/
	V1080P (0,1920,1080,"1080p"),
	V960P (1,1280,960,"960p"),
	V720P (2,1280,720,"720p"),
	VWVGA (3,848,480,"WVGA");
	

	public int width;
	public int height;
	public String displayName;
	public int mode;
	
	private VideoResolution(int mode,int width, int height,String displayName) {
			this.mode = mode;
	        this.width = width;
	        this.height = height;
	        this.displayName = displayName;
	}
	
	public static VideoResolution fromInt(int code) {
	    switch(code) {
	    case 0:
	        return V1080P;
	    case 1:
	        return V960P;
	    case 2:
	        return V720P;
	    case 3:
	        return VWVGA;
	    }
	    
	    return null;
	}
	
	public VideoResolution next(){
		
		if(this.equals(V1080P)){
			return V960P;
		} else if(this.equals(V960P)){
			return V720P;
		} else if(this.equals(V720P)){
			return VWVGA;
		} else if(this.equals(VWVGA)){
			return V1080P;
			
		} else {
			return null;
		}
	}
	
	public VideoResolution previous(){
		
		if(this.equals(VWVGA)){
			return V720P;
		} else if(this.equals(V720P)){
			return V960P;
		} else if(this.equals(V960P)){
			return V1080P;
		} else if(this.equals(V1080P)){	
			return VWVGA;
		} else {
			return null;
		}
	}
	
	
}
