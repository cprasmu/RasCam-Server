package cprasmu.rascam.pi4j;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

public class TestGPIO {
	
	
	public static void switchPin(int pinNum,boolean on){
		
		 final GpioController gpio = GpioFactory.getInstance();
	        
	        // provision gpio pin #01 & #03 as an output pins and blink
	      //  final GpioPinDigitalOutput led = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05);
	        
	        Pin pin = null;
	        PinState ps =null;
	        
	        if(on){
	        	ps=PinState.HIGH;
	        	
	        } else {
	        	ps=PinState.LOW;
	        }
	        
	        switch (pinNum){
	        case 0:
	        	pin=RaspiPin.GPIO_00;
	        	break;
	        case 1:
	        	pin=RaspiPin.GPIO_01;
	        	break;
	        case 2:
	        	pin=RaspiPin.GPIO_02;
	        	break;
	        case 3:
	        	pin=RaspiPin.GPIO_03;
	        	break;
	        case 4:
	        	pin=RaspiPin.GPIO_04;
	        	break;
	        case 5:
	        	pin=RaspiPin.GPIO_05;
	        	break;
	        case 6:
	        	pin=RaspiPin.GPIO_06;
	        	break;
	        case 7:
	        	pin=RaspiPin.GPIO_07;
	        	break;
	        case 8:
	        	pin=RaspiPin.GPIO_08;
	        	break;
	        case 9:
	        	pin=RaspiPin.GPIO_09;
	        	break;
	        case 10:
	        	pin=RaspiPin.GPIO_10;
	        	break;
	        case 11:
	        	pin=RaspiPin.GPIO_11;
	        	break;
	        case 12:
	        	pin=RaspiPin.GPIO_12;
	        	break;
	        case 13:
	        	pin=RaspiPin.GPIO_13;
	        	break;
	        case 14:
	        	pin=RaspiPin.GPIO_14;
	        	break;
	        case 15:
	        	pin=RaspiPin.GPIO_15;
	        	break;
	        case 16:
	        	pin=RaspiPin.GPIO_16;
	        	break;
	        case 17:
	        	pin=RaspiPin.GPIO_17;
	        	break;
	        case 18:
	        	pin=RaspiPin.GPIO_18;
	        	break;
	        case 19:
	        	pin=RaspiPin.GPIO_19;
	        	break;
	        case 20:
	        	pin=RaspiPin.GPIO_20;
	        	break;
	        }
	        
	         GpioPinDigitalOutput pizn = gpio.provisionDigitalOutputPin(pin, "MyLED", ps);
	        
	        
	}
	
	public static void blinkAll()throws InterruptedException{
		
		final GpioController gpio = GpioFactory.getInstance();
		 
		final GpioPinDigitalOutput led1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01);
        final GpioPinDigitalOutput led2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02);
        final GpioPinDigitalOutput led3 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03);
        final GpioPinDigitalOutput led4 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04);
        final GpioPinDigitalOutput led5 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05);
        final GpioPinDigitalOutput led6 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06);
        final GpioPinDigitalOutput led7 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07);
        final GpioPinDigitalOutput led8 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_08);
        final GpioPinDigitalOutput led9 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_09);
        final GpioPinDigitalOutput led10 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_10);
        final GpioPinDigitalOutput led11 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_11);
        final GpioPinDigitalOutput led12 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_12);
        final GpioPinDigitalOutput led13 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_13);
        final GpioPinDigitalOutput led14 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_14);
        
        final GpioPinDigitalOutput led15 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_15);
        final GpioPinDigitalOutput led16 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_16);
        final GpioPinDigitalOutput led17 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_17);
        final GpioPinDigitalOutput led18 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_18);
        final GpioPinDigitalOutput led19 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_19);
        final GpioPinDigitalOutput led20 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_20);
        
        GpioPin p = (GpioPin) RaspiPin.GPIO_05;
        
        gpio.setPullResistance(PinPullResistance.PULL_DOWN, p);
        
        led1.blink(500);
        led2.blink(500);
        led3.blink(500);
        led4.blink(500);
        led5.blink(500);
        led6.blink(500);
        led7.blink(500);
        led8.blink(500);
        led9.blink(500);
        led10.blink(500);
        led11.blink(500);
        led12.blink(500);
        led13.blink(500);
        led14.blink(500);
        led15.blink(500);
        led16.blink(500);
        led17.blink(500);
        led18.blink(500);
        led19.blink(500);
        led20.blink(500);
        System.out.println("sleeping");
        
        Thread.sleep(2000);
	}
	public static void main(String[] args) throws InterruptedException {
		
		
		blinkAll();
		
		// create gpio controller
        //final GpioController gpio = GpioFactory.getInstance();
        
        // provision gpio pin #01 & #03 as an output pins and blink
        //final GpioPinDigitalOutput led = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05);
        
        //final GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "MyLED", PinState.HIGH);
        
       // led.blink(1000);
        //Thread.sleep(2000);
        
        // turn off gpio pin #01
       // pin.low();
        
      //  System.out.println("--> GPIO state should be: OFF");
        
       // System.out.println("flashing LED");
       // Thread.sleep(2000);
	}
	
	
	
	
}
