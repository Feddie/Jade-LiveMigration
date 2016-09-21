package behav;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;


public class LoadMonitor extends TickerBehaviour {
	double cpuload;
	OperatingSystemMXBean OsB = ManagementFactory.getOperatingSystemMXBean();;
	
	public LoadMonitor(Agent a, long period ) {
		super(a,period);
		
	}
	
	public void onTick() {
		this.cpuload = this.OsB.getSystemLoadAverage();
		System.out.println(this.cpuload);
	}
}
