package org.cloudbus.cloudsim.cloudlets.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CloudletTaskGroup {
	
	private List<CloudletTask> memberTaskList;
	
	/**
     * The index of the active running task or -1 if no task has started yet.
     */
	private int currentTaskNum;
	
	public CloudletTaskGroup() {
		memberTaskList = new ArrayList<CloudletTask>();
		currentTaskNum = -1;
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
	
	public List<CloudletTask> getTasks(){
		return Collections.unmodifiableList(memberTaskList);
	}
	
	public CloudletTask getCurrentTask() {
		return memberTaskList.get(currentTaskNum);
	}
	
	public int getCurrentTaskNum(){
		return currentTaskNum;
	}
	
}
