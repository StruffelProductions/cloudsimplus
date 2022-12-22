package org.cloudbus.cloudsim.cloudlets.network;

public class CloudletWaitTask extends CloudletTask {

	private double waitUntil;
	
	public CloudletWaitTask(int id, double timeToWaitUntil) {
		super(id);
		this.waitUntil = timeToWaitUntil;
	}
	
	public void setTimeToWaitUntil(double timeToWaitUntil) {
		this.waitUntil = timeToWaitUntil;
	}
	
	public double getTimeToWaitUntil() {
		return waitUntil;
	}

	public void finishIfPauseIsOver() {
		if(!this.isFinished() && this.getCloudlet().getSimulation().clock() >= this.waitUntil) {
			this.setFinished(true);
		}
	}

}
