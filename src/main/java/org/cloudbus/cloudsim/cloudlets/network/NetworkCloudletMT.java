package org.cloudbus.cloudsim.cloudlets.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.CloudletSimple;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModel;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudbus.cloudsim.vms.network.NetworkVm;

public class NetworkCloudletMT extends CloudletSimple {

    /** @see #getTasks() */
    private final List<CloudletTaskGroup> taskGroups;
    
    private final CloudletTaskGroup defaultTaskGroup;

    /**
     * Creates a NetworkCloudlet with no priority and file size and output size equal to 1.
     *
     * @param length the length or size (in MI) of this cloudlet to be executed in a VM (check out {@link #setLength(long)})
     * @param pesNumber the number of PEs this Cloudlet requires
     */
    public NetworkCloudletMT(final long length, final int pesNumber) {
        this(-1, length, pesNumber);
    }

    /**
     * Creates a NetworkCloudlet with no priority and file size and output size equal to 1.
     *
     * @param id the unique ID of this cloudlet
     * @param length the length or size (in MI) of this cloudlet to be executed in a VM (check out {@link #setLength(long)})
     * @param pesNumber the pes number
     */
    public NetworkCloudletMT(final int id,  final long length, final int pesNumber) {
        super(id, length, pesNumber);
        this.defaultTaskGroup = new CloudletTaskGroup();
        this.taskGroups = new ArrayList<CloudletTaskGroup>();
        this.taskGroups.add(defaultTaskGroup);
    }

    public double getNumberOfTasks() {
        int numberOfTasks = 0;
    	for(CloudletTaskGroup g : this.taskGroups) {
        	numberOfTasks += g.getTasks().size();
        }
    	return numberOfTasks;
    }

    /**
     * @return a read-only list of Cloudlet's tasks.
     */
    public List<CloudletTaskGroup> getTaskGroups() {
        return Collections.unmodifiableList(taskGroups);
    }

    /**
     * Checks if some Cloudlet Task has started yet.
     *
     * @return true if some task has started, false otherwise
     */
    public boolean isTasksStarted() {
        boolean tasksStarted = false;
        
        for(CloudletTaskGroup g : this.taskGroups) {
        	tasksStarted = g.isRunning() || tasksStarted;
        }
        
        return tasksStarted;
    }

    /**
     * Change the current task to the next one in order
     * to start executing it, if the current task is finished.
     *
     * @param nextTaskStartTime the time that the next task will start
     * @return true if the current task finished and the next one was started, false otherwise
     */
    public boolean startNextTaskIfOneOfCurrentIsFinished(final double nextTaskStartTime){
        return
            getNextTaskIfCurrentIfFinished()
                .map(task -> task.setStartTime(nextTaskStartTime))
                .isPresent();
    }

    /**
     * Gets an {@link Optional} containing the current task
     * or an {@link Optional#empty()} if there is no current task yet.
     * @return
     */
    public Optional<List<CloudletTask>> getCurrentTasks() {
        if(!isTasksStarted()) {
        	return Optional.empty();
        }
        
        ArrayList<CloudletTask> currentTasks = new ArrayList<CloudletTask>();
        
        for(CloudletTaskGroup g : this.taskGroups) {
        	currentTasks.add(g.getCurrentTask());
        }

        return Optional.of(currentTasks);
    }

    /**
     * Gets an {@link Optional} containing the next task in the list if the current task is finished.
     *
     * @return the next task if the current one is finished;
     *         otherwise an {@link Optional#empty()} if the current task is already the last one,
     *         or it is not finished yet.
     */
    private Optional<List<CloudletTask>> getUpcomingTasksFromAllGroups(){
    	
    	// Iterate over all task groups and get the next task if the current one is finished
    	
    	List<CloudletTask> newUpcomingTasks = new ArrayList<CloudletTask>();
    	
    	// test if there is at least one task that isn't finished
        if(getCurrentTasks().isPresent()) {
        	
        	for(CloudletTask t : getCurrentTasks().get()) {
        		Optional<CloudletTask> upcomingTask = t.getTaskGroup().getNextTaskIfCurrentIsFinished();
        		
        		upcomingTask.ifPresent(task -> newUpcomingTasks.add(task));
        		
        	}	
            
        }else {
        	return Optional.empty();
        }


        return Optional.of(newUpcomingTasks);
    }

    @Override
    public boolean isFinished() {
        final boolean allTasksFinished = true;
        
        for(CloudletTaskGroup g : this.taskGroups) {
        	g.isFinished();
        }
        
        return super.isFinished() && allTasksFinished;
    }

    /**
     * {@inheritDoc}
     * <p>The length of a NetworkCloudlet is the
     * length sum of all its {@link CloudletExecutionTask}'s.</p>
     * @return the length sum of all {@link CloudletExecutionTask}'s
     */
    @Override
    public long getLength() {
        return this.taskGroups.stream()
                .mapToLong(CloudletTaskGroup::getLength)
                .sum();
    }

    /**
     * Adds a task to the {@link #getTasks() task list}
     * and links the task to the NetworkCloudlet.
     *
     * @param task Task to be added
     * @return the NetworkCloudlet instance
     */
    public NetworkCloudletMT addTask(final CloudletTask task) {
        Objects.requireNonNull(task);
        //task.setCloudlet(this);
        defaultTaskGroup.addTask(task);
        return this;
    }

    @Override
    public NetworkVm getVm() {
        return (NetworkVm)super.getVm();
    }

    @Override
    public Cloudlet setVm(final Vm vm) {
        if(vm == Vm.NULL)
            return super.setVm(NetworkVm.NULL);

        if(vm instanceof NetworkVm)
            return super.setVm(vm);

        throw new IllegalArgumentException("NetworkCloudlet can just be executed by a NetworkVm");
    }
	
	

}
