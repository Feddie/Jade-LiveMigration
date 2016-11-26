package behav;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import utils.VBoxInterface;
import agents.Teleporter;

import org.virtualbox_5_0.*;
import java.lang.management.ManagementFactory;
//import java.lang.management.OperatingSystemMXBean;
import com.sun.management.OperatingSystemMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


public class LoadMonitor extends TickerBehaviour {
	public static final double REG_THR = 0.6; //CPUload threshold for DF registration
	public static final double MIGR_THR = 0.9; //CPUload threshold for agent migration
	double cpuload;
	List <IMachine> runvms = new ArrayList<IMachine>();
	public static boolean registered;
	DFAgentDescription ag_desc;
	ServiceDescription serv_desc;
	OperatingSystemMXBean OsB = (com.sun.management.OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
	String hostname;
	
	
	public LoadMonitor(Agent a, long period) {
		super(a,period);
		this.ag_desc = new DFAgentDescription();
		this.serv_desc = new ServiceDescription();
		this.serv_desc.setType("teleporting_agent");
		this.serv_desc.setName("teleport");
		ag_desc.addServices(serv_desc);
		LoadMonitor.registered = true;
		try {
			//this.hostname = InetAddress.getLocalHost().getHostName().toString();
			this.hostname = InetAddress.getLocalHost().toString();
		} catch (UnknownHostException unknownhost) {
			unknownhost.printStackTrace();
		}
	}
	
	public void onTick() {
		//Prints VMs running on this host ///NOTE: could also use a different TickerBehav with a larger period
		runvms = VBoxInterface.getInstance().getRunningMachines();
		if (runvms.size() != 0) {
			System.out.print("VMs running on " + hostname + " : ");
			for (IMachine m : runvms){
				System.out.print(" " + m.getName() + " ");
				((Teleporter) myAgent).runvms.add(m);
			}
			System.out.println();
		}
		
		
		this.cpuload = this.OsB.getSystemCpuLoad();
		if (cpuload == -1.0) { //error in reading CPU Load
			System.out.println("CPU Load unavailable on " + hostname);
		}
		else {
			System.out.println("Current CPU load on " + hostname +": " + this.cpuload);
			
			if (this.cpuload >= LoadMonitor.REG_THR) { //Deregister from DF as a migration site	
		  		checkRegistration();	
				if (registered) {
			  			try {
							DFService.deregister(this.myAgent, this.ag_desc);
							LoadMonitor.registered = false;
							System.out.println(this.myAgent.getName() + " has unregistered from DF");
						}
						catch (FIPAException fe) {
							fe.printStackTrace();
						}
		  			}
				if (this.cpuload > LoadMonitor.MIGR_THR) { //with a high CPU load, try to perform a migration	
					if (!runvms.isEmpty()) { 
						System.out.println(runvms.get(0));
						String first_vm = runvms.get(0).getHardwareUUID();
						this.myAgent.addBehaviour(new SearchNewHome(this.myAgent, first_vm));
					}
				}
			}
			else { //if CPU load is lower than registration threshold, register as a migration agent
				try {
					checkRegistration();
					if (!registered) {
						DFService.register(this.myAgent, this.ag_desc);
						LoadMonitor.registered = true;
						System.out.println(this.myAgent.getName() + " has registered on DF");
					}
				}
				catch (FIPAException fe) {
					fe.printStackTrace();
				}
			}
		}
		
	}
	
	/*Asks the DF if the current agent is already registered and updates "registered" flag */
	public void checkRegistration() {
		LoadMonitor.registered = false;
		DFAgentDescription template = new DFAgentDescription();
  		ServiceDescription templateSd = new ServiceDescription();
  		templateSd.setType("teleporting_agent");
  		template.addServices(templateSd);
  		try {
  			DFAgentDescription[] dfd = DFService.search(this.myAgent, template);
  			for (DFAgentDescription df : dfd) { 		
  				if (df.getName().equals( this.myAgent.getAID())) {
  					LoadMonitor.registered = true;
  				}
  			}
  		}
	  	catch (FIPAException fe) {
				fe.printStackTrace();
			}
  		}
}
