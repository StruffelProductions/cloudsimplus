package org.cloudbus.cloudsim.allocationpolicies;

import java.util.Optional;

import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.vms.Vm;

public class VmAllocationPolicyFixed extends VmAllocationPolicyAbstract {

	@Override
	protected Optional<Host> defaultFindHostForVm(Vm vm) {
		// TODO Auto-generated method stub
		return Optional.of(vm.getHost());
		//return Optional.empty();
	}

}
