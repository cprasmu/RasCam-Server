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

public enum MeteringMode {
	
	
	AVERAGE (0,"average","Average"),
	SPOT (1,"spot","Spot"),
	BACKLIT (2,"backlit","Backlit"),
	MATRIX (3,"matrix","Matrix");
	
	public int mode;
	public String name;
	public String displayName;
	
	private MeteringMode(int mode, String name,String displayName) {
	        this.mode = mode;
	        this.name = name;
	        this.displayName=displayName;
	    }
	
	public static MeteringMode fromInt(int code) {
	    switch(code) {
	    case 0:
	        return AVERAGE;
	    case 1:
	        return SPOT;
	    case 2:
	        return BACKLIT;
	    case 3:
	        return MATRIX;
	    }
	    
	    return null;
	}
	
	public MeteringMode next(){
		
		if(this.equals(AVERAGE)){
			return SPOT;
		} else if(this.equals(SPOT)){
			return BACKLIT;
		} else if(this.equals(BACKLIT)){
			return MATRIX;
		} else if(this.equals(MATRIX)){	
			return AVERAGE;
		} else {
			return null;
		}
	}
	
	public MeteringMode previous(){
		
		if(this.equals(MATRIX)){	
			return BACKLIT;
		} else if(this.equals(BACKLIT)){
			return SPOT;
		} else if(this.equals(SPOT)){
			return AVERAGE;
		} else if(this.equals(AVERAGE)){
			return MATRIX;
		} else {
			return null;
		}
	}
	
}
