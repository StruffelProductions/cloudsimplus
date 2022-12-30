package microservice;

import java.util.List;

import org.cloudbus.cloudsim.cloudlets.network.CloudletReceiveTask;
import org.cloudbus.cloudsim.cloudlets.network.CloudletSendTask;
import org.cloudbus.cloudsim.cloudlets.network.MicroserviceNetworkCloudlet;
import org.cloudbus.cloudsim.core.Identifiable;

public interface Microservice extends Identifiable {
	public String getName();
	public CloudletReceiveTask handleNewRequest(String requestType,CloudletSendTask taskToExpectFrom, CloudletReceiveTask taskToReportBackTo);
	public long getUnfinishedTaskGroupCount(); 
	public long getTaskGroupCount(); 
	public List<MicroserviceNetworkCloudlet> getCloudlets();
}
