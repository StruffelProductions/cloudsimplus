package microservice.loadbalancer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.cloudbus.cloudsim.cloudlets.network.MicroserviceNetworkCloudlet;

public abstract class MicroserviceLoadBalancerAbstract implements MicroserviceLoadBalancer {
	
	public abstract MicroserviceNetworkCloudlet selectCloudlet(List<MicroserviceNetworkCloudlet> cloudletList);
	
}
