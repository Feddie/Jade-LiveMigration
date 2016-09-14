package agents;

import behav.MigrateOut;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class Teleporter extends Agent {
	
	protected void setup() {
		System.out.println(this.getLocalName() + ": inizialitation...");
		
		// DF Registration
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("teleporting_agent");
		sd.setName("");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		System.out.println(this.getLocalName() + ": registered...");
		
		Object[] args = this.getArguments();
		if (args != null & args.length > 0 ) { 
			if (args[0].toString().equals("out")) {	
			
				this.addBehaviour(new MigrateOut(this, "Pepper2", "169.254.244.235"));
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
			System.out.println("Seller-agent"+getAID().getName()+"terminating");
		}
}