package fr.unice.vicc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;

public class BalanceVmAllocationPolicy extends VmAllocationPolicy {
	// To track the Host for each Vm. The string is the unique Vm identifier,
	// composed by its id and its userId
	private Map<String, Host> vmTable;

	public BalanceVmAllocationPolicy(List<? extends Host> list) {
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
		// System.out.println("MIPS=>"+host.getTotalMips());
		if (host.vmCreate(vm)) {
			// the host is appropriate, we track it
			vmTable.put(vm.getUid(), host);
			return true;
		}

		return false;
	}

	public boolean allocateHostForVm(Vm vm) {
		// First fit algorithm, run on the first suitable node
		Map<Host, Double> mapMips = new HashMap<Host, Double>();
		ValueComparator comparateur = new ValueComparator(mapMips);
		TreeMap<Host, Double> mapTriee = new TreeMap<Host, Double>(comparateur);

		for (Host h : getHostList()) {
			mapMips.put(h, h.getAvailableMips());
		}
		mapTriee.putAll(mapMips);
		
		
		Host h=mapTriee.firstKey();
		if (h.vmCreate(vm)) {
			// track the host
			vmTable.put(vm.getUid(), h);
			
			return true;
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
}
