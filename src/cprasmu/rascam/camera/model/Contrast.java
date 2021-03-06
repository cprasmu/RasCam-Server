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

public class Contrast extends IntegerRangeValue {

	protected static  int VALUE_LIMIT_LOW = -100;
	protected static  int VALUE_LIMIT_HIGH = 100;
	
	public Contrast(Integer value){
		 
		super(VALUE_LIMIT_LOW,VALUE_LIMIT_HIGH,value);
		step=10;
	}

	public static void main(String [] args){
		Contrast c= new Contrast(10);
		System.out.println(c);
		c.increment();
		System.out.println(c);
	}
}
