package fr.unice.vicc;

import java.util.List;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.power.PowerHost;

public class BalanceObserver extends SimEntity {

	/** The custom event id, must be unique. */
    public static final int OBSERVE = 801596;

    private List<PowerHost> hosts;
    private double hostsUsed;
    private float delay;

    public static final float DEFAULT_DELAY = 1;

    public BalanceObserver(List<PowerHost> hosts) {
        this(hosts, DEFAULT_DELAY);
    }

    public BalanceObserver(List<PowerHost> hosts, float delay) {
        super("BalanceObserver");
        this.hosts = hosts;
        this.delay = delay;
    }


    /**
     * Get the number of hosts used
     * @return 
     */
    private double getNumberOfHostsUsed() {
        double s = 0;
        for (Host h : hosts) {
        	if(h.getAvailableMips()!=h.getTotalMips())
        		s += 1;
        }
        return s;
    }

    /*
    * This is the central method to implement.
    * CloudSim is event-based.
    * This method is called when there is an event to deal in that object.
    * In practice: create a custom event (here it is called OBSERVE) with a unique int value and deal with it.
     */
    @Override
    public void processEvent(SimEvent ev) {
        //I received an event
        switch(ev.getTag()) {
            case OBSERVE: //It is my custom event
                //I must observe the datacenter
                double cur = getNumberOfHostsUsed();
                if(cur >hostsUsed)
                {
                	hostsUsed=cur;
                	System.out.println("Number of hosts used=>"+hostsUsed);
                }
                
                
                //Observation loop, re-observe in `delay` seconds
                send(this.getId(), delay, OBSERVE, null);
               
        }
    }

    
    /**
     * Get the hostsUsed power consumption.
     * @return a number of Watts
     */
    public double gethostsUsed() {
        return hostsUsed;
    }
    @Override
    public void shutdownEntity() {
        Log.printLine(getName() + " is shutting down...");
    }

    @Override
    public void startEntity() {
        Log.printLine(getName() + " is starting...");
        //I send to myself an event that will be processed in `delay` second by the method
        //`processEvent`
        send(this.getId(), delay, OBSERVE, null);
    }

}
