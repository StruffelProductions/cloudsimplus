package microservice.usercountgenerator;

public class ConcurrentUserCountGeneratorDynamic implements ConcurrentUserCountGenerator {

	int users;
	
	public ConcurrentUserCountGeneratorDynamic() {
		this.users = 1;
	}
	@Override
	public int generateUserCount(double time) {
		return users; 
	}
	
	public void changeUserCount(int difference) {
		users = Math.max(1, users + difference);
	}

}
