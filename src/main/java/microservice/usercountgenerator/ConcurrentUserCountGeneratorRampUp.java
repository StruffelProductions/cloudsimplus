package microservice.usercountgenerator;

public class ConcurrentUserCountGeneratorRampUp implements ConcurrentUserCountGenerator {

	int users;
	double startDuration;
	
	public ConcurrentUserCountGeneratorRampUp(int users, double startDuration) {
		this.users = users;
		this.startDuration = startDuration;
	}
	@Override
	public int generateUserCount(double time) {
		return Math.max(  (int) (Math.min(time / startDuration, 1.0)  * users)  ,  1); 
	}

}
