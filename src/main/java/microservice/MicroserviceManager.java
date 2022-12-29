package microservice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.cloudbus.cloudsim.cloudlets.network.MicroserviceNetworkCloudlet;

public class MicroserviceManager {
	List<Microservice> services = new ArrayList<Microservice>();
	Microservice clientService;
	
	public void registerService(Microservice service) {
		services.add(service);
	}
	
	public void setClientService(Microservice clientService) {
		this.clientService = clientService;
	}
	
	public List<Microservice> getServices() {
		return Collections.unmodifiableList(services);
	}
	
	public Microservice getClientService() {
		return this.clientService;
	}
	
	public Optional<Microservice> getMicroservice(long id) {
		return services.stream().filter(s -> s.getId() == id ).findFirst();
	}
	
	public Optional<Microservice> getMicroservice(String name) {
		return services.stream().filter(s -> s.getName() == name ).findFirst();
	}
	
	public List<MicroserviceNetworkCloudlet> getAllManagedCloudlets() {
		
		ArrayList<MicroserviceNetworkCloudlet> cloudletList = new ArrayList<MicroserviceNetworkCloudlet>();
		
		//Services
		for(Microservice s : services) {
			cloudletList.addAll(s.getCloudlets());
		}
		
		cloudletList.addAll(clientService.getCloudlets());
		
		return cloudletList;
		
	}
	
	public void updateClientRequests(int targetClientThreadNumber) {		
		while(clientService.getActiveThreadCount() < targetClientThreadNumber) {
			clientService.handleNewRequest("", null, null);
			LOGGER.debug("Triggered new request of type {} ({}) on cloudlet {} because it only had {}/{} requests active",requestType, requestRoundRobin,currentClientCloudlet.getId(),numberOfActiveThreads,targetNumberOfActiveThreads);
		}
	}
	
}
