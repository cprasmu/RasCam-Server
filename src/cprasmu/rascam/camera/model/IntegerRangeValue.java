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

public abstract class IntegerRangeValue {
	
	protected Integer value = 0;
	protected int VALUE_LIMIT_LOW = 0;
	protected int VALUE_LIMIT_HIGH = 0;
	protected int step=1;
	protected boolean enabled = true;
	
	private IntegerRangeValue(Integer value){
		
		
		if(value == null){
			throw new IllegalArgumentException("Value is null" );
		}
		
		if ((value >= VALUE_LIMIT_LOW) && ((value <= VALUE_LIMIT_HIGH))) {
			this.value = value;
		} else {
			throw new IllegalArgumentException("Value must be between range " + VALUE_LIMIT_LOW + " and " + VALUE_LIMIT_HIGH );
		}
		
	}
	public IntegerRangeValue(int min,int max,Integer value){
		VALUE_LIMIT_LOW = min;
		VALUE_LIMIT_HIGH = max;

		if(value == null){
			throw new IllegalArgumentException("Value is null" );
		}
		
		if ((value >= VALUE_LIMIT_LOW) && ((value <= VALUE_LIMIT_HIGH))) {
			this.value = value;
		} else {
			throw new IllegalArgumentException("Value must be between range " + VALUE_LIMIT_LOW + " and " + VALUE_LIMIT_HIGH );
		}
	}
	public Integer getValue(){
		return value;
	}
	
	public String toString(){
		return value.toString();
	}
	
	public void increment(){
		if(value < VALUE_LIMIT_HIGH){
			value+=step;
		}
	}
	
	public void decrement(){	
		if(value > VALUE_LIMIT_LOW){
			value-=step;
		}
	}
	
	public boolean isEnabled(){
		return enabled;
	}
	public void setEnabled(boolean enabled){
		this.enabled = enabled;
	}
}
