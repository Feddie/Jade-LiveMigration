package behav;

import behav.MigrateOut;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.util.leap.ArrayList;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;

public class SearchNewHome extends OneShotBehaviour {
	
	public SearchNewHome (Agent a) {
		super(a);
	}
	
	public void action() {
		//create a template to be searched in the DF in order to find an agent available for teleporting
		DFAgentDescription template = new DFAgentDescription();
  		ServiceDescription templateSd = new ServiceDescription();
  		templateSd.setType("teleporting_agent");
  		template.addServices(templateSd);
		try {
				DFAgentDescription[] result = DFService.search(myAgent, template);
				
				AID[] migr_sites = new AID[result.length]; //agents available for teleporting
				System.out.println(result.length + " migration sites found by " + myAgent.getName());
				for (int i = 0; i < result.length; ++i) {
						migr_sites[i] = result[i].getName();
						//System.out.println(migr_sites[i].toString());
				}
				
				if (migr_sites.length != 0) { 
					ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
					request.setContent("migrate");
					//System.out.println("req " + request);
					for (int i = 0; i < migr_sites.length; i++) {
							System.out.println(this.myAgent.getLocalName() + ": migration service offered by " + migr_sites[i].getName());
							request.addReceiver(migr_sites[i]);
					}
					this.myAgent.send(request);
				}
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		
		//The first agent (host) that agrees to the teleport request becomes the migration target
		ACLMessage answer = myAgent.receive();
		//AID sender = answer.getSender();
		if (answer != null && answer.getPerformative() == ACLMessage.AGREE ) {
			this.myAgent.addBehaviour(new MigrateOut(this.myAgent, "Ubu", "169.254.76.216"));
			
		}
	}
	
}
