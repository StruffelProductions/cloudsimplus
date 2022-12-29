package microservice.loadbalancer;

import java.util.List;

import org.cloudbus.cloudsim.cloudlets.network.MicroserviceNetworkCloudlet;

public class MicroserviceLoadBalancerRoundRobin extends MicroserviceLoadBalancerAbstract {

	int roundRobinIndex = 0;

	@Override
	public MicroserviceNetworkCloudlet selectCloudlet(List<MicroserviceNetworkCloudlet> cloudletList) {
		roundRobinIndex = ( roundRobinIndex + 1 ) % cloudletList.size();
		return cloudletList.get(roundRobinIndex);
	}

}
