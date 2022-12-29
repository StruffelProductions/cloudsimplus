package org.cloudbus.cloudsim.cloudlets.network;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;



public abstract class MicroserviceNetworkCloudlet extends NetworkCloudlet {
	
    protected int requestsReceived;
	
    public MicroserviceNetworkCloudlet() {
    	this(-1,1);
    }
	
	public MicroserviceNetworkCloudlet(int length, int pesNumber) {
		super(length, pesNumber);
	}
    
    
	
    //public abstract List<CloudletTask> generateProcessingTasks(HashMap<String, ArrayList<MicroservicePodCloudlet>> masterCloudletTable,List<VmPacket> packetTrackingList);
    
    /**
     * Sets up the tasks for processing.
     * Returns the task that triggers the rest of the execution, basically the "Trigger" for this cloudlet.
     * 
     * @param taskToReportBackTo
     * @param masterCloudletTable
     */
    public abstract CloudletReceiveTask prepareNewRequestHandling(CloudletReceiveTask taskToReportBackTo, HashMap<String, ArrayList<MicroserviceNetworkCloudlet>> masterCloudletTable, String requestType);

    protected abstract int generateMIWorkload(String requestType, double factor);
    
    public int getRequestsReceivedCounter() {
    	return requestsReceived;
    }
    
    protected void addTasksOnNewThread(List<CloudletTask> taskList) {
		// Add new tasks as a new thread
		CloudletTaskGroup processingTaskGroup = new CloudletTaskGroup();
		//processingTaskGroup.setId(CcspUtil.nextId("TASKGROUP"));
		processingTaskGroup.setCloudlet(this);
		
		for(CloudletTask t : taskList) {
			processingTaskGroup.addTask(t);
		}
		
		this.addTaskGroup(processingTaskGroup);
	}
	
}
