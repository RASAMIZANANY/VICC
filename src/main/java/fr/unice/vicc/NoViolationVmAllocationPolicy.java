package fr.unice.vicc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.HostStateHistoryEntry;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.VmStateHistoryEntry;

public class NoViolationVmAllocationPolicy extends VmAllocationPolicy {
	// To track the Host for each Vm. The string is the unique Vm identifier,
	// composed by its id and its userId
	private Map<String, Host> vmTable;

	public NoViolationVmAllocationPolicy(List<? extends Host> list) {
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
		if (host.isSuitableForVm(vm)) {

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
		for (Host h : getHostList()) {

			if (h.isSuitableForVm(vm)) {
				if (h.vmCreate(vm)) { // track the host
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

	public double availability(Vm v) {
		double totalMissing = 0;
		double prev = 0;
		// Browse the Vm history
		for (VmStateHistoryEntry e : v.getStateHistory()) {
			// the time elapsed since the last event
			double diff = e.getTime() - prev;
			prev = e.getTime();
			// Get the number of missing mips for that period
			totalMissing += missingMips(e, diff);
		}
		// The total number of Mips the VMs should have in theory
		double totalAllocated = v.getMips() * Constants.SIMULATION_LIMIT;
		// The % of time it eventually had enough MIPS
		double availabilityPct = (totalAllocated - totalMissing)
				/ totalAllocated * 100;

		return availabilityPct;
	}

	private double missingMips(VmStateHistoryEntry e, double d) {
		double want = e.getRequestedMips() * d;
		double got = e.getAllocatedMips() * d;
		if (got < want) {
			return want - got;
		}
		return 0;
	}

}
