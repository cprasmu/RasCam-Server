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

public enum ImageEffect {
	
	NONE (0,"none","None"),
	NEGATIVE (1,"negative","Negative"),
	SOLARISE (2,"solarise","Solarise"),
	SKETCH (3,"sketch","Sketch"),
	DENOISE(4,"denoise","De-noise"),
	EMBOSS(5,"emboss","Emboss"),
	OILPAINT(6,"oilpaint","Oil-Paint"),
	HATCH(7,"hatch","Hatch"),
	GPEN(8,"gpen","G-Pen"),
	PASTEL(9,"pastel","Pastel"),
	WATERCOLOUR(10,"watercolour","Water-Colour"),
	FILM(11,"film","Film"),
	BLUR(12,"blur","Blur"),
	SATURATION(13,"saturation","Saturation"),
	COLOURSWAP(14,"colourswap","Colour-Swap"),
	WASHEDOUT(15,"washedout","Washed-Out"),
	POSTERISE(16,"posterise","Posterise"),
	COLOURPAINT(17,"colourpoint","Colour-Point"),
	COLOURBALANCE(18,"colourbalance","Colour-Balance"),
	CARTOON(19,"cartoon","Cartoon");
	
	public int mode;
	public String name;
	public String displayName;
	
	private ImageEffect(int mode, String name,String displayName) {
	        this.mode = mode;
	        this.name = name;
	        this.displayName = displayName;
	}
	
	public ImageEffect next(){
		
		if(this.equals(NONE)){
			return NEGATIVE;
		} else if(this.equals(NEGATIVE)){
			return SOLARISE;
		} else if(this.equals(SOLARISE)){
			return SKETCH;
		} else if(this.equals(SKETCH)){
			return DENOISE;
		} else if(this.equals(DENOISE)){
			return EMBOSS;
		} else if(this.equals(EMBOSS)){
			return OILPAINT;
		} else if(this.equals(OILPAINT)){
			return HATCH;
		} else if(this.equals(HATCH)){
			return GPEN;
		} else if(this.equals(GPEN)){
			return PASTEL;
		} else if(this.equals(PASTEL)){
			return WATERCOLOUR;
		} else if(this.equals(WATERCOLOUR)){
			return FILM;
		} else if(this.equals(FILM)){
			return BLUR;
		} else if(this.equals(BLUR)){
			return SATURATION;
		} else if(this.equals(SATURATION)){
			return COLOURSWAP;
		} else if(this.equals(COLOURSWAP)){
			return WASHEDOUT;
		} else if(this.equals(WASHEDOUT)){
			return POSTERISE;
		} else if(this.equals(ImageEffect.POSTERISE)){
			return COLOURPAINT;
		} else if(this.equals(COLOURPAINT)){
			return COLOURBALANCE;
		} else if(this.equals(COLOURBALANCE)){
			return CARTOON;
		} else if(this.equals(CARTOON)){
			return NONE;	
		} else {
			return null;
		}
	}
	
	public ImageEffect previous(){
		
		if(this.equals(CARTOON)){
			return COLOURBALANCE;
		} else if(this.equals(COLOURBALANCE)){
			return COLOURPAINT;
		} else if(this.equals(COLOURPAINT)){
			return POSTERISE;
		} else if(this.equals(ImageEffect.POSTERISE)){
			return WASHEDOUT;
		} else if(this.equals(WASHEDOUT)){
			return COLOURSWAP;
		} else if(this.equals(COLOURSWAP)){
			return SATURATION;
		} else if(this.equals(SATURATION)){
			return BLUR;
		} else if(this.equals(BLUR)){
			return FILM;
		} else if(this.equals(FILM)){
			return WATERCOLOUR;
		} else if(this.equals(WATERCOLOUR)){
			return PASTEL;
		} else if(this.equals(PASTEL)){
			return GPEN;
		} else if(this.equals(GPEN)){
			return HATCH;
		} else if(this.equals(HATCH)){
			return OILPAINT;
		} else if(this.equals(OILPAINT)){
			return EMBOSS;
		} else if(this.equals(EMBOSS)){
			return DENOISE;
		} else if(this.equals(DENOISE)){
			return SKETCH;
		} else if(this.equals(SKETCH)){
			return SOLARISE;
		} else if(this.equals(SOLARISE)){
			return NEGATIVE;
		} else if(this.equals(NEGATIVE)){
			return NONE;	
		} else if(this.equals(NONE)){
			return CARTOON;
		} else {
			return null;
		}
	}
	
}
