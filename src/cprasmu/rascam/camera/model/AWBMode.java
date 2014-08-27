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

public enum AWBMode {
	
	
	OFF (0,"off","Off"),
	AUTO (1,"auto","Auto"),
	SUN (2,"sun","Sun"),
	CLOUD (3,"cloud","Cloud"),
	SHADE (4,"shade","Shade"),
	TUNGSTEN (5,"tungsten","Tungsten"),
	FLOURESCENT (6,"fluorescent","Fluorescent"),
	INCANDESENT (7,"incandescent","Incandescent"),
	FLASH (8,"flash","Flash"),
	HORIZON (9,"horizon","Horizon");
	
	public int mode;
	public String name;
	public String displayName;
	
	private AWBMode(int mode, String name,String displayName) {
	        this.mode = mode;
	        this.name = name;
	        this.displayName = displayName;
	}
	
	
	public AWBMode next(){
		
		if (this.equals(AWBMode.OFF)){
			return AWBMode.AUTO;
		} else if(this.equals(AWBMode.AUTO)){
			return AWBMode.SUN;
		} else if(this.equals(AWBMode.SUN)){
			return AWBMode.CLOUD;
		} else if(this.equals(AWBMode.CLOUD)){
			return AWBMode.SHADE;
		} else if(this.equals(AWBMode.SHADE)){
			return AWBMode.TUNGSTEN;
		} else if(this.equals(AWBMode.TUNGSTEN)){
			return AWBMode.FLOURESCENT;
		} else if(this.equals(AWBMode.FLOURESCENT)){
			return AWBMode.INCANDESENT;
		} else if(this.equals(AWBMode.INCANDESENT)){
			return AWBMode.FLASH;
		} else if(this.equals(AWBMode.FLASH)){
			return AWBMode.HORIZON;
		} else if(this.equals(AWBMode.HORIZON)){
			return AWBMode.OFF;
		} else {
			return null;
		}
		
	}
	
	public AWBMode previous() {
	
		if (this.equals(AWBMode.OFF)){
			return AWBMode.HORIZON;
		} else if(this.equals(AWBMode.HORIZON)){
			return AWBMode.FLASH;
		} else if(this.equals(AWBMode.FLASH)){
			return AWBMode.INCANDESENT;
		} else if(this.equals(AWBMode.INCANDESENT)){
			return AWBMode.FLOURESCENT;
		} else if(this.equals(AWBMode.FLOURESCENT)){
			return AWBMode.TUNGSTEN;
		} else if(this.equals(AWBMode.TUNGSTEN)){
			return AWBMode.SHADE;
		} else if(this.equals(AWBMode.SHADE)){
			return AWBMode.CLOUD;
		} else if(this.equals(AWBMode.CLOUD)){
			return AWBMode.SUN;
		} else if(this.equals(AWBMode.SUN)){
			return AWBMode.AUTO;
		} else if(this.equals(AWBMode.AUTO)){
			return AWBMode.OFF;
		} else {
			return null;
		}
	}
}
