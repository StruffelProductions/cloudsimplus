package microservice.loadbalancer;

import java.util.List;

import org.cloudbus.cloudsim.cloudlets.network.MicroserviceNetworkCloudlet;

public interface MicroserviceLoadBalancer {
	abstract MicroserviceNetworkCloudlet selectCloudlet( List<MicroserviceNetworkCloudlet> cloudletList );
}
