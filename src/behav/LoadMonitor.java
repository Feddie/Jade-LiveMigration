package behav;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import agents.Teleporter;

import org.virtualbox_5_0.*;
import java.lang.management.ManagementFactory;
//import java.lang.management.OperatingSystemMXBean;
import com.sun.management.OperatingSystemMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;


public class LoadMonitor extends TickerBehaviour {
	double cpuload;
	OperatingSystemMXBean OsB = (com.sun.management.OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
	DFAgentDescription df;
	//OperatingSystemMXBean OsB = ManagementFactory.OperatingSystemMXBean;
	String hostname;
	
	public LoadMonitor(Agent a, long period, DFAgentDescription dfad) {
		super(a,period);
		this.df = dfad;
		ServiceDescription sd = new ServiceDescription();
		sd.setType("teleporting_agent");
		sd.setName("teleport");
		df.addServices(sd);
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
			if (this.cpuload >= 0.6) {
				boolean registered = false;
				DFAgentDescription template = new DFAgentDescription();
		  		ServiceDescription templateSd = new ServiceDescription();
		  		templateSd.setType("teleporting_agent");
		  		template.addServices(templateSd);
		  		try {
		  			DFAgentDescription[] dfd = DFService.search(this.myAgent, template);
		  			for (DFAgentDescription df : dfd) {
		  				if (df.getName() == this.myAgent.getAID()) {
		  					registered = true;
		  				}		
		  			}
		  			if (registered) {
			  			try {
							DFService.deregister(this.myAgent, this.df);
							System.out.println(this.myAgent.getName() + " has unregistered from DF");
						}
						catch (FIPAException fe) {
							fe.printStackTrace();
						}
		  			}
		  		}
		  		catch (FIPAException fe) {
					fe.printStackTrace();
				}
				
				if (this.cpuload > 0.8) {	
					List <IMachine> vms = ((Teleporter) myAgent).getRunVMs();
					String first_vm = vms.get(0).getId();
					this.myAgent.addBehaviour(new SearchNewHome(this.myAgent, first_vm));
				}
			}
			else {
				try {
					
					DFService.register(this.myAgent, this.df);
					System.out.println(this.myAgent.getName() + " has registered on DF");
				}
				catch (FIPAException fe) {
					fe.printStackTrace();
				}
			}
		}
		
	}
}
