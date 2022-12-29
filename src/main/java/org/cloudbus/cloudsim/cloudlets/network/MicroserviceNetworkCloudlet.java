package org.cloudbus.cloudsim.cloudlets.network;

import static java.util.Objects.requireNonNull;


import microservice.Microservice;



public class MicroserviceNetworkCloudlet extends NetworkCloudlet {
    
    Microservice microservice;
	
    public MicroserviceNetworkCloudlet() {
    	this(-1,1);
    }
	
	public MicroserviceNetworkCloudlet(int length, int pesNumber) {
		super(length, pesNumber);
	}
    
    public Microservice getMicroservice() {
    	return microservice;
    }
    
    public void setMicroservice(Microservice microservice) {
    	this.microservice = microservice;
    }
    
    
    @Override
    public boolean isFinished() {
        return false;
    }
	
}
