package cprasmu.rascam.camera.model;

public class ShutterSpeed extends IntegerRangeValue  {

	
	private static final int VALUE_LIMIT_LOW = 1000;
	private static final int VALUE_LIMIT_HIGH = 4000000;
	
	public ShutterSpeed(Integer value){

		super(VALUE_LIMIT_LOW,VALUE_LIMIT_HIGH,value);
		step=10000;
	}
	

}
