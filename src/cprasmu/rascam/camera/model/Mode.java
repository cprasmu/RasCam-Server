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
/**
 * 
 */
package cprasmu.rascam.camera.model;


/**
 * @author peterrasmussen
 *
 */
public enum Mode {
	WAITING (0,"Waiting","Waiting"),
	SINGLE_SHOT (1,"Single Shot","Single Shot"),
	TIME_LAPSE (2,"Time Lapse","Time Lapse"),
	VIDEO (3,"Video","Video");
	
	public int mode;
	public String name;
	public String displayName;
	
	private Mode(int mode, String name,String displayName) {
	        this.mode = mode;
	        this.name = name;
	        this.displayName = displayName;
	    }
	
	public static Mode fromInt(int code) {
	    switch(code) {
	    case 0:
	        return WAITING;
	    case 1:
	        return SINGLE_SHOT;
	    case 2:
	        return TIME_LAPSE;
	    case 3:
	        return VIDEO;
	    }
	    
	    return null;
	}
	
	public Mode next(){
		
		if(this.equals(SINGLE_SHOT)){
			return TIME_LAPSE;
		} else if(this.equals(TIME_LAPSE)){
			return VIDEO;
		} else if(this.equals(VIDEO)){
			return SINGLE_SHOT;
		} else {
			return null;
		}
	}
	
	public Mode previous(){
		
		if(this.equals(SINGLE_SHOT)){
			return VIDEO;
		} else if(this.equals(VIDEO)){
			return TIME_LAPSE;
		} else if(this.equals(TIME_LAPSE)){
			return SINGLE_SHOT;
		} else {
			return null;
		}
	}
	
}
