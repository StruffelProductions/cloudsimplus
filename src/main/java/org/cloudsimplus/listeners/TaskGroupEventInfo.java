package org.cloudsimplus.listeners;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.network.CloudletTask;
import org.cloudbus.cloudsim.cloudlets.network.CloudletTaskThread;
import org.cloudbus.cloudsim.hosts.Host;

public interface TaskGroupEventInfo extends EventInfo {
	CloudletTaskThread getTaskGroup();
	
	static TaskGroupEventInfo of(final EventListener<? extends EventInfo> listener, final CloudletTaskThread taskGroup){
        //return of(listener, taskGroup);
		return new TaskGroupEventInfo() {
            @Override public CloudletTaskThread getTaskGroup() { return taskGroup; }
            @Override public double getTime() { return taskGroup.getCloudlet().getSimulation().clock(); }
            @Override public EventListener<? extends EventInfo> getListener() { return listener; }
        };
    }
	
}
