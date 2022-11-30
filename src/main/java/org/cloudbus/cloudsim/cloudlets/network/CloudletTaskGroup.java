package org.cloudbus.cloudsim.cloudlets.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.cloudbus.cloudsim.core.CloudSimTag;

public class CloudletTaskGroup {
	
	private List<CloudletTask> tasks;
	
	/**
     * The index of the active running task or -1 if no task has started yet.
     */
	private int currentTaskNum;
	
	public CloudletTaskGroup() {
		tasks = new ArrayList<CloudletTask>();
		currentTaskNum = -1;
	}
	
	public CloudletTaskGroup(List<CloudletTask> taskList) {
		this();
		tasks.addAll(taskList);
	}
	
	public void addTask(CloudletTask task) {
		task.setTaskGroup(this);
		tasks.add(task);
	}
	
	public void removeTask(CloudletTask task) {
		task.removeTaskGroup();
		tasks.remove(task);
	}
	
	public List<CloudletTask> getTasks(){
		return Collections.unmodifiableList(tasks);
	}
	
	public CloudletTask getCurrentTask() {
		return tasks.get(currentTaskNum);
	}
	
	public int getCurrentTaskNum(){
		return currentTaskNum;
	}
	
	public boolean isActive() {
		return currentTaskNum > -1;
	}
	
	public boolean isFinished() {
		boolean isFinished = true;
		for(CloudletTask t : this.tasks) {
			isFinished = t.isFinished() && isFinished;
		}
		return isFinished;
	}
	

	
	/**
     * Gets an {@link Optional} containing the next task in the list if the current task is finished.
     *
     * @return the next task if the current one is finished;
     *         otherwise an {@link Optional#empty()} if the current task is already the last one,
     *         or it is not finished yet.
     */
    Optional<CloudletTask> getNextTaskIfCurrentIsFinished(){
    	
        if(getCurrentTask().isActive()) {
            return Optional.empty();
        }

        if(this.currentTaskNum <= tasks.size()-1) {
            this.currentTaskNum++;
        }

        return Optional.of(getCurrentTask());
    }
    
    public long getLength() {
        return getTasks().stream()
                .filter(CloudletTask::isExecutionTask)
                .map(task -> (CloudletExecutionTask)task)
                .mapToLong(CloudletExecutionTask::getLength)
                .sum();
    }
	
}
