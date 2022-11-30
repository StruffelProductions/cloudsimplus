package org.cloudbus.cloudsim.schedulers.cloudlet.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.network.CloudletExecutionTask;
import org.cloudbus.cloudsim.cloudlets.network.CloudletReceiveTask;
import org.cloudbus.cloudsim.cloudlets.network.CloudletSendTask;
import org.cloudbus.cloudsim.cloudlets.network.CloudletTask;
import org.cloudbus.cloudsim.cloudlets.network.NetworkCloudlet;
import org.cloudbus.cloudsim.cloudlets.network.NetworkCloudletMT;
import org.cloudbus.cloudsim.core.CloudSimTag;
import org.cloudbus.cloudsim.network.VmPacket;
import org.cloudbus.cloudsim.vms.Vm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CloudletTaskSchedulerMT implements CloudletTaskScheduler {

	private static final Logger LOGGER = LoggerFactory.getLogger(CloudletTaskSchedulerSimple.class.getSimpleName());
	
	/** @see #getVm() */
    private Vm vm;

    /** @see #getVmPacketsToSend() */
    private final List<VmPacket> vmPacketsToSend;

    /**
     * A map of {@link VmPacket}'s received, where each key is the
     * sender VM and each value is the list of packets sent by that VM
     * targeting the VM of this scheduler.
     */
    private final Map<Vm, List<VmPacket>> vmPacketsReceivedMap;
	
	public CloudletTaskSchedulerMT() {
		 vmPacketsToSend = new ArrayList<>();
	     vmPacketsReceivedMap = new HashMap<>();
	}

	@Override
	public Vm getVm() {
		return this.vm;
	}

	@Override
	public void setVm(Vm vm) {
		this.vm = vm;

	}

	@Override
	public void clearVmPacketsToSend() {
		vmPacketsToSend.clear();
	}

	@Override
	public List<VmPacket> getVmPacketsToSend() {
		return Collections.unmodifiableList(vmPacketsToSend);
	}

	@Override
	public boolean addPacketToListOfPacketsSentFromVm(VmPacket pkt) {
		final Vm vm = pkt.getSource();
        return vmPacketsReceivedMap.compute(vm, (k, v) -> v == null ? new ArrayList<>() : v).add(pkt);
	}

	@Override
	public void processCloudletTasks(Cloudlet cloudlet, long partialFinishedMI) {
		// TODO Auto-generated method stub
		
		// split the MI accross all exec tasks
		
		
		// then run network tasks
		
		

	}
	
	private void updateExecutionTasks(final NetworkCloudletMT cloudlet, final long partialFinishedMI) {
        /*
         * @TODO autor: manoelcampos It has to be checked if the task execution
         *       is considering only one cloudlet PE or all PEs.
         *       Each execution task is supposed to use just one PE.
         */
		
        final Optional<List<CloudletTask>> optional = cloudlet.getCurrentTasks();
        optional.ifPresent(taskList -> {
        	
        	int numberOfExecTasks = 0;
        	
        	for(CloudletTask t : taskList) {
        		
        		if(t.isExecutionTask()) {
        			numberOfExecTasks++;
        		}
        		
        	}
        	
        	if(numberOfExecTasks > 0) {
        	
	        	for(CloudletTask t : taskList) {
	        		
	        		if(t.isExecutionTask()) {
	        			CloudletExecutionTask et = (CloudletExecutionTask) t;
	        			et.process(partialFinishedMI / numberOfExecTasks);
	        		}
	        		
	        	}
        	}
        	

            scheduleNextTaskIfCurrentIsFinished(cloudlet);
        });
    }

    private void updateNetworkTasks(final NetworkCloudletMT cloudlet) {
        //TODO Needs to use polymorphism to avoid these ifs
        cloudlet.getCurrentTask().ifPresent(task -> {
            if (task instanceof CloudletSendTask sendTask)
               addPacketsToBeSentFromVm(cloudlet, sendTask);
            else if (task instanceof CloudletReceiveTask receiveTask)
                receivePackets(cloudlet, receiveTask);
        });
    }
    
    /**
     * Schedules the execution of the next task of a given cloudlet.
     */
    private void scheduleNextTaskIfCurrentIsFinished(final NetworkCloudletMT cloudlet) {
        if(!cloudlet.startNextTaskIfCurrentIsFinished(cloudlet.getSimulation().clock())){
            return;
        }

        final var dc = getVm().getHost().getDatacenter();
        dc.schedule(dc, dc.getSimulation().getMinTimeBetweenEvents(), CloudSimTag.VM_UPDATE_CLOUDLET_PROCESSING);
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
	
	private boolean isNotNetworkCloudlet(final Cloudlet cloudlet) {
        return !(cloudlet instanceof NetworkCloudlet);
    }

}
