package org.cloudsimplus.listeners;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.network.CloudletTask;
import org.cloudbus.cloudsim.cloudlets.network.CloudletTaskGroup;

public interface TaskEventInfo extends EventInfo {
	CloudletTask getTask();
	
	static TaskEventInfo of(final EventListener<? extends EventInfo> listener, final CloudletTask task){
		return new TaskEventInfo() {
            @Override public CloudletTask getTask() { return task; }
            @Override public double getTime() { return task.getCloudlet().getSimulation().clock(); }
            @Override public EventListener<? extends EventInfo> getListener() { return listener; }
        };
    }
	
}
