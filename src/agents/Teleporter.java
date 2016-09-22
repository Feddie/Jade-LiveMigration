package agents;

import java.util.Iterator;
//import org.virtualbox_5_0.*;

import behav.*;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.JADEAgentManagement.QueryPlatformLocationsAction;

public class Teleporter extends Agent {
	
	public AID[] migr_sites;
	
	protected void setup() {
		System.out.println(this.getLocalName() + ": initialization...");

		// DF Registration
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("teleporting_agent");
		sd.setName("teleport");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		
		System.out.println(this.getLocalName() + ": registered.");
		
		
		this.addBehaviour(new Listen());
		///// TODO: LoadMonitor should periodically checks CPU load. When too high, it adds a SearchHome behav
		this.addBehaviour(new LoadMonitor(this, 5000));
		this.addBehaviour(new SearchNewHome(this));
		
		Object[] args = this.getArguments();
		if (args != null & args.length > 0 ) { 
			if (args[0].toString().equals("out")) {	
				this.addBehaviour(new SearchNewHome(this));
				
			}
			else if (args[0].toString().equals("in")) {	
			
			//this.addBehaviour(new MigrateIn(this, "Pepper2", "169.254.244.235"));
			}
		}
		
	}
	
	protected void takeDown() {
		
		// Deregister from DF
		try {
			DFService.deregister(this);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
			System.out.println(getAID().getName()+"terminating");
		}
}