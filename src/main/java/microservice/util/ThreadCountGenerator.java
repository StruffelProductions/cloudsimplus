package microservice.util;

public class ThreadCountGenerator {
	
	
	
	public static int rampUp (double riseTime, int targetCount, double time) {
		return Math.max(  (int) (Math.min(time / riseTime, 1.0)  * targetCount)  ,  1); 
	}
}
