package org.cloudbus.cloudsim.cloudlets.network;

import java.util.ArrayList;
import java.util.List;

public class CloudletTaskGroup {
	
	private List<CloudletTask> memberTaskList;
	
	public CloudletTaskGroup() {
		memberTaskList = new ArrayList<CloudletTask>();
	}
	
	public CloudletTaskGroup(List<CloudletTask> taskList) {
		this();
		memberTaskList.addAll(taskList);
	}
	
	public void addTask(CloudletTask task) {
		task.setTaskGroup(this);
		memberTaskList.add(task);
	}
	
	public void removeTask(CloudletTask task) {
		task.removeTaskGroup();
		memberTaskList.remove(task);
	}
	
}
