package org.cloudbus.cloudsim.network;

import org.cloudbus.cloudsim.cloudlets.network.CloudletReceiveTask;
import org.cloudbus.cloudsim.cloudlets.network.CloudletSendTask;
import org.cloudbus.cloudsim.vms.network.NetworkVm;

public class TaskPacket extends VmPacket {
    
    private final CloudletSendTask senderTask;
    private final CloudletReceiveTask receiverTask;


    /**
     * Creates a packet to be sent to a VM inside the
     * Host of the sender VM.
     * @param sourceVm id of the VM sending the packet
     * @param destinationVm id of the VM that has to receive the packet
     * @param size data length of the packet in bytes
     * @param senderCloudlet cloudlet sending the packet
     * @param receiverCloudlet cloudlet that has to receive the packet
     */
    public TaskPacket(
        final long size,
        final CloudletSendTask senderTask,
        final CloudletReceiveTask receiverTask)
    {
        super(senderTask.getCloudlet().getVm(), receiverTask.getCloudlet().getVm(), size, senderTask.getCloudlet(), receiverTask.getCloudlet());
        
        this.senderTask = senderTask;
        this.receiverTask = receiverTask;
    }
    
    public CloudletReceiveTask getReceiverTask() {
    	return receiverTask;
    }
    
    public CloudletSendTask getSenderTask() {
    	return senderTask;
    }
}

