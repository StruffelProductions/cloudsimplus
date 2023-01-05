package microservice.usercountgenerator;

public class ConcurrentUserCountGeneratorConstant implements ConcurrentUserCountGenerator {

	int users;
	
	public ConcurrentUserCountGeneratorConstant(int users) {
		this.users = users;
	}
	@Override
	public int generateUserCount(double time) {

		return users;
	}

}
