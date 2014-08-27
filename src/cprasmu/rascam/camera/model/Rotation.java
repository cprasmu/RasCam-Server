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

public enum Rotation {

	ROT000(0,"0","0"),
	ROT090(90,"90","90"),
	ROT180(180,"180","180"),
	ROT270(270,"270","270");
	
	
	public int rotation;
	public String name;
	public String displayName;
	
	private Rotation(int rotation, String name,String displayName) {
	        this.rotation = rotation;
	        this.name = name;
	        this.displayName = displayName;
	}
	
	
	public Rotation next(){
		
		if (this.equals(ROT000)){
			return ROT090;
		} else if(this.equals(ROT090)){
			return ROT180;
		} else if(this.equals(ROT180)){
			return ROT270;
		} else if(this.equals(ROT270)){
			return ROT000;
		} else {
			return null;
		}
	}
	
	public Rotation previous(){
		if(this.equals(ROT270)){
			return ROT180;
		} else if(this.equals(ROT180)){
			return ROT090;
		} else if(this.equals(ROT090)){
			return ROT000;
		} else if (this.equals(ROT000)){
			return ROT270;
		} else {
			return null;
		}
	}
}
