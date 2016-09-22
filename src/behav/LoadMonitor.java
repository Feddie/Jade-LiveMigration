package behav;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import java.lang.management.ManagementFactory;
//import java.lang.management.OperatingSystemMXBean;
import com.sun.management.OperatingSystemMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class LoadMonitor extends TickerBehaviour {
	double cpuload;
	OperatingSystemMXBean OsB = (com.sun.management.OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();

	//OperatingSystemMXBean OsB = ManagementFactory.OperatingSystemMXBean;
	String hostname;
	
	public LoadMonitor(Agent a, long period ) {
		super(a,period);
		try {
			this.hostname = InetAddress.getLocalHost().getHostName().toString();
		} catch (UnknownHostException unknownhost) {
			unknownhost.printStackTrace();
		}
	}
	
	public void onTick() {
		this.cpuload = this.OsB.getSystemCpuLoad();
		if (cpuload == -1.0) {
			System.out.println("CPU Load unavailable on " + hostname);
		}
		else {
			System.out.println("Current CPU load on " + hostname +": " + this.cpuload);
			if (this.cpuload >= 0.8) {
				this.myAgent.addBehaviour(new SearchNewHome(this.myAgent));
			}
			else if (this.cpuload >= 0.6) {
				//TODO: de-register as available migration site
			}
			else {
				//TODO: register
			}
		}
		
	}
}
