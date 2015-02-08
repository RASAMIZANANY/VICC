package fr.unice.vicc;
import java.util.Comparator;
import java.util.Map;
import org.cloudbus.cloudsim.Host;

class ValueComparator implements Comparator<Host> {
	Map<Host, Double> base;

	public ValueComparator(Map<Host, Double> base) {
		this.base = base;
	}

	public int compare(Host a, Host b) {
		if (base.get(a) >= base.get(b)) {
			return -1;
		} else {
			return 1;
		}
	}
}
