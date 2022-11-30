package org.cloudbus.cloudsim.schedulers.cloudlet.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.network.CloudletExecutionTask;
import org.cloudbus.cloudsim.cloudlets.network.CloudletReceiveTask;
import org.cloudbus.cloudsim.cloudlets.network.CloudletSendTask;
import org.cloudbus.cloudsim.cloudlets.network.CloudletTask;
import org.cloudbus.cloudsim.cloudlets.network.CloudletTaskGroup;
import org.cloudbus.cloudsim.cloudlets.network.NetworkCloudlet;
import org.cloudbus.cloudsim.cloudlets.network.NetworkCloudletMT;
import org.cloudbus.cloudsim.core.CloudSimTag;
import org.cloudbus.cloudsim.network.VmPacket;
import org.cloudbus.cloudsim.vms.Vm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CloudletTaskSchedulerMT extends CloudletTaskSchedulerSimple implements CloudletTaskScheduler {

	private static final Logger LOGGER = LoggerFactory.getLogger(CloudletTaskSchedulerSimple.class.getSimpleName());
	

	public CloudletTaskSchedulerMT() {

	}

	@Override
	public void processCloudletTasks(Cloudlet cloudlet, long partialFinishedMI) {
		NetworkCloudletMT netCloudletMT = (NetworkCloudletMT)cloudlet;
		
		if (cloudlet.isFinished() || isNotNetworkCloudlet(cloudlet)) {
            return;
        }
		
		
		if (!netCloudletMT.isTasksStarted()) {

				scheduleNextTaskIfCurrentIsFinishedForAllGroups(netCloudletMT);
       
				return;
        }
		
		// split the MI across all exec tasks
		
		if(isTimeToUpdateCloudletProcessing(netCloudletMT)) {
			updateExecutionTasks(netCloudletMT,partialFinishedMI);
		}
		
		
		// then run network tasks
		
		super.updateNetworkTasks((NetworkCloudlet) cloudlet);

	}
	
	private void updateExecutionTasks(NetworkCloudletMT netCloudlet, long partialMI) {
		long pes = netCloudlet.getNumberOfPes();
		
		List<CloudletTask> currentExecTasks = netCloudlet.getCurrentTasks().get().stream().filter(t -> t.isExecutionTask()).collect(Collectors.toList());
		
		Collections.shuffle(currentExecTasks);
		
		var limit = Math.max(pes, currentExecTasks.size()-1);
		
		for(int i = 0 ; i < limit ; i++) {
			( (CloudletExecutionTask)currentExecTasks.get(i) ).process(partialMI / limit);
		}
		
		
	}

	
	/**
     * Schedules the execution of the next task of a given cloudlet.
     */
     private void scheduleNextTaskIfCurrentIsFinished(final CloudletTaskGroup taskGroup) {
        if(!taskGroup.startNextTaskIfCurrentIsFinished(taskGroup.getCloudlet().getSimulation().clock())){
            return;
        }

        final var dc = taskGroup.getCloudlet().getVm().getHost().getDatacenter();
        dc.schedule(dc, dc.getSimulation().getMinTimeBetweenEvents(), CloudSimTag.VM_UPDATE_CLOUDLET_PROCESSING);
    }
	
	private void scheduleNextTaskIfCurrentIsFinishedForAllGroups(NetworkCloudletMT cloudlet) {
		for(CloudletTaskGroup g : cloudlet.getTaskGroups()) {
			scheduleNextTaskIfCurrentIsFinished(g);
		}
	}

	@Override
	public boolean isTimeToUpdateCloudletProcessing(Cloudlet cloudlet) {
		
		// In a MT context it IS time to update if any one of the currently running tasks is an ExecTask
		
		Objects.requireNonNull(cloudlet);
        if(cloudlet.isFinished()){
            return false;
        }

        if(isNotNetworkCloudlet(cloudlet)) {
            return true;
        }

        Optional<List<CloudletTask>> currentTasks = ((NetworkCloudletMT)cloudlet).getCurrentTasks();//.filter(CloudletTask::isExecutionTask).isPresent();
		
        boolean execTaskPresent = false;
        
        for(CloudletTask t : currentTasks.get()) {
        	execTaskPresent = t.isExecutionTask() || execTaskPresent;
        }
        
        return execTaskPresent;
	}
	

}
