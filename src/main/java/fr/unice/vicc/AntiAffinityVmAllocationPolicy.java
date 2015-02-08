package fr.unice.vicc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;

public class AntiAffinityVmAllocationPolicy extends VmAllocationPolicy {
	// To track the Host for each Vm. The string is the unique Vm identifier,
	// composed by its id and its userId
	private Map<String, Host> vmTable;

	public AntiAffinityVmAllocationPolicy(List<? extends Host> list) {
		super(list);
		vmTable = new HashMap<>();
	}

	public Host getHost(Vm vm) {
		// We must recover the Host which hosting Vm
		return this.vmTable.get(vm.getUid());
	}

	public Host getHost(int vmId, int userId) {
		// We must recover the Host which hosting Vm
		return this.vmTable.get(Vm.getUid(userId, vmId));
	}

	public boolean allocateHostForVm(Vm vm, Host host) {
		int intervalle=vm.getId()/100;
		//System.out.println("Intervalle=>"+ intervalle);
		boolean res=true;
		
		for(Vm v1 : host.getVmList())
		{
			if (inTheIntervall(v1)==intervalle)
			{
				res=false;
				break;
			}
				
		}
		if(res==true)
		{	
				if (host.vmCreate(vm)) {
					// the host is appropriate, we track it
					vmTable.put(vm.getUid(), host);
					return true;
				}	
		}
		return false;
	}

	public boolean allocateHostForVm(Vm vm) {
		// First fit algorithm, run on the first suitable node
		int intervalle=vm.getId()/100;
		//System.out.println("Intervalle=>"+ intervalle);
		boolean res;
		for (Host h : getHostList()) {
			res=true;
			for(Vm v1 : h.getVmList())
			{
				if (inTheIntervall(v1)==intervalle)
				{
					res=false;
					break;
				}
			}
			if(res==true)
			{	
					if (h.vmCreate(vm)) {
						// the host is appropriate, we track it
						vmTable.put(vm.getUid(), h);
						return true;
					}	
			}
		}
		return false;
		
	}

	public void deallocateHostForVm(Vm vm, Host host) {
		vmTable.remove(vm.getUid());
		host.vmDestroy(vm);
	}

	@Override
	public void deallocateHostForVm(Vm v) {
		// get the host and remove the vm
		vmTable.get(v.getUid()).vmDestroy(v);
	}

	public static Object optimizeAllocation() {
		return null;
	}

	@Override
	public List<Map<String, Object>> optimizeAllocation(List<? extends Vm> arg0) {
		// Static scheduling, no migration, return null;
		return null;
	}
	public int inTheIntervall(Vm vm)
	{
		return vm.getId()/100;
	}

}
