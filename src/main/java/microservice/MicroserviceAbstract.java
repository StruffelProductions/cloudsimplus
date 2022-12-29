package microservice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cloudbus.cloudsim.cloudlets.network.CloudletReceiveTask;
import org.cloudbus.cloudsim.cloudlets.network.CloudletSendTask;
import org.cloudbus.cloudsim.cloudlets.network.MicroserviceNetworkCloudlet;
import org.cloudbus.cloudsim.core.Identifiable;

import microservice.loadbalancer.MicroserviceLoadBalancer;
import microservice.loadbalancer.MicroserviceLoadBalancerRoundRobin;

public abstract class MicroserviceAbstract implements Identifiable, Microservice {

	long id;
	String name;
	
	private MicroserviceManager manager;
	
	private List<MicroserviceNetworkCloudlet> memberCloudlets;
	private MicroserviceLoadBalancer loadBalancer;
	
	public MicroserviceAbstract(MicroserviceManager manager) {
		this.memberCloudlets = new ArrayList<MicroserviceNetworkCloudlet>();
		this.loadBalancer = new MicroserviceLoadBalancerRoundRobin();
		this.manager = manager;
	}
	
	public MicroserviceAbstract(MicroserviceManager manager, List<MicroserviceNetworkCloudlet> members) {
		this(manager);
		this.memberCloudlets.addAll(members);
	}
	
	public abstract CloudletReceiveTask handleNewRequest(String requestType,CloudletSendTask taskToExpectFrom, CloudletReceiveTask taskToReportBackTo);
	
	public long getActiveThreadCount() {
		long threadCount = 0;
		
		for(MicroserviceNetworkCloudlet c : getCloudlets()) {
			threadCount += c.getAllCurrentTasks().size();
		}
		
		return threadCount;
	}
	
	public MicroserviceManager getManager() {
		return manager;
	}
	
	public void setLoadBalancer(MicroserviceLoadBalancer loadBalancer) {
		this.loadBalancer = loadBalancer;
	}
	
	public MicroserviceLoadBalancer getLoadBalancer() {
		return this.loadBalancer;
	}
	
	public MicroserviceNetworkCloudlet selectCloudletUsingLB() {
		return this.loadBalancer.selectCloudlet(memberCloudlets);
	}
	
	@Override
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public List<MicroserviceNetworkCloudlet> getCloudlets() {
		return Collections.unmodifiableList(memberCloudlets);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void addMember(MicroserviceNetworkCloudlet cloudlet) {
		this.memberCloudlets.add(cloudlet);
	}
	
}
