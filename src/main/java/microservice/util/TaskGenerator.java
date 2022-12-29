package microservice.util;

import java.util.List;

import org.cloudbus.cloudsim.cloudlets.network.CloudletExecutionTask;
import org.cloudbus.cloudsim.cloudlets.network.CloudletReceiveTask;
import org.cloudbus.cloudsim.cloudlets.network.CloudletSendTask;
import org.cloudbus.cloudsim.cloudlets.network.CloudletTask;
import org.cloudbus.cloudsim.cloudlets.network.MicroserviceNetworkCloudlet;
import org.cloudbus.cloudsim.network.TaskPacket;


import microservice.Microservice;



public class TaskGenerator {
	
	private static int idCounter;
	
	public static CloudletExecutionTask generateExecutionTask(MicroserviceNetworkCloudlet cloudlet, long length) {
		CloudletExecutionTask executionTask = new CloudletExecutionTask(idCounter++,length);
		executionTask.setCloudlet(cloudlet);
		return executionTask;
	}
	
	public static CloudletReceiveTask generateTriggerTask(MicroserviceNetworkCloudlet cloudlet,CloudletSendTask taskToExpectFrom) {
		CloudletReceiveTask triggerTask = new CloudletReceiveTask(idCounter++,taskToExpectFrom.getCloudlet().getVm());
		triggerTask.setCloudlet(cloudlet);
		triggerTask.setExpectedPacketsToReceive(1);
		return triggerTask;
	}
	
	public static List<CloudletTask> generateCallOut(MicroserviceNetworkCloudlet cloudlet, Microservice targetService,String requestType,int bytes){
		
		// Create the task that triggers the target
		CloudletSendTask activateTargetTask = new CloudletSendTask(idCounter++);
		activateTargetTask.setCloudlet(cloudlet);
		
		// Create the task that awaits a response from the target
		CloudletReceiveTask awaitTargetTask = new CloudletReceiveTask(idCounter++, null);
		
		// Trigger the target cloudlet to create its processing tasks
		CloudletReceiveTask taskToTriggerOnTarget = targetService.handleNewRequest(requestType,activateTargetTask, awaitTargetTask); //targetCloudlet.prepareNewRequestHandling(awaitDbTask, masterCloudletTable,requestType);
		
		// Set remaining info on await task
		awaitTargetTask.setSourceVm(taskToTriggerOnTarget.getCloudlet().getVm());
		awaitTargetTask.setCloudlet(cloudlet);
		
		// Add packets to trigger task
		TaskPacket activateTargetPacket = new TaskPacket(bytes,activateTargetTask,taskToTriggerOnTarget);
		activateTargetTask.addPacket(activateTargetPacket);
		
		return List.of(activateTargetTask,awaitTargetTask);
		
	}
	
	public static CloudletSendTask generateReportBackTask(MicroserviceNetworkCloudlet cloudlet, CloudletReceiveTask taskToReportBackTo,int bytes) {
		// Prepare the reporting task that gets run at the end of the execution
		CloudletSendTask reportBackTask = new CloudletSendTask(idCounter++);
		reportBackTask.setCloudlet(cloudlet);
		
		
		// Add packet to reportBackTask
		TaskPacket reportBackPacket = new TaskPacket(bytes,reportBackTask,taskToReportBackTo);
		reportBackTask.addPacket(reportBackPacket);
		
		return reportBackTask;
	}
}
