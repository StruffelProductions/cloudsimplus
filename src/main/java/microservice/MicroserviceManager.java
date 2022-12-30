package microservice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import org.cloudsimplus.util.Log;
import org.cloudbus.cloudsim.cloudlets.network.CloudletTaskGroup;
import org.cloudbus.cloudsim.cloudlets.network.MicroserviceNetworkCloudlet;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

public class MicroserviceManager {
	List<Microservice> services = new ArrayList<Microservice>();
	ClientMicroservice clientService;
	org.slf4j.Logger LOGGER = LoggerFactory.getLogger(this.getClass().getSimpleName());
	
	public void registerService(Microservice service) {
		services.add(service);
	}
	
	public void setClientService(ClientMicroservice clientService) {
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
	
	public LinkedList<Double> getResponseTimes(String filterByTaskGroupName){
		return getResponseTimes(0.0,filterByTaskGroupName);
	}
	
	public LinkedList<Double> getResponseTimes(double start, String filterByTaskGroupName){
		LinkedList<Double> responseTimes = new LinkedList<Double>();
		responseTimes.add(0.0);
		for(MicroserviceNetworkCloudlet c : clientService.getCloudlets()) {
			for(CloudletTaskGroup g : c.getTaskGroups()) {
				if(g.getThreadType() == filterByTaskGroupName && g.getTasks().get(0).getStartTime() >= start && g.measurementFinished()) {
					responseTimes.add(g.getMeasurementTime());
				}
			}
		}
		return responseTimes;
	}
	
	public void updateClientRequests(int targetClientThreadNumber) {	
		LOGGER.debug("{} of {} client threads active",clientService.getUnfinishedTaskGroupCount(),targetClientThreadNumber);
		if(clientService.getUnfinishedTaskGroupCount() < targetClientThreadNumber) {
			clientService.handleNewRequest("", null, null);
		}
	}
	
}
