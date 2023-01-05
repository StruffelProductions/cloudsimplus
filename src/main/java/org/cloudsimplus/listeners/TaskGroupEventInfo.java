package org.cloudsimplus.listeners;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.network.CloudletTask;
import org.cloudbus.cloudsim.cloudlets.network.CloudletTaskGroup;
import org.cloudbus.cloudsim.hosts.Host;

public interface TaskGroupEventInfo extends EventInfo {
	CloudletTaskGroup getTaskGroup();
	
	static TaskGroupEventInfo of(final EventListener<? extends EventInfo> listener, final CloudletTaskGroup taskGroup){
        //return of(listener, taskGroup);
		return new TaskGroupEventInfo() {
            @Override public CloudletTaskGroup getTaskGroup() { return taskGroup; }
            @Override public double getTime() { return taskGroup.getCloudlet().getSimulation().clock(); }
            @Override public EventListener<? extends EventInfo> getListener() { return listener; }
        };
    }
	
}
