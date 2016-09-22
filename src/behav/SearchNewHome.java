package behav;

import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import behav.MigrateOut;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.util.leap.ArrayList;
import jade.util.leap.List;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;

public class SearchNewHome extends OneShotBehaviour {
	CopyOnWriteArrayList migr_sites;
	Iterator<AID> ms;
	
	public SearchNewHome (Agent a) {
		super(a);
		this.migr_sites = new CopyOnWriteArrayList();
		//this.migr_sites = new ArrayList();
		
	}
	
	public void action() {
		
		
		//create a template to be searched in the DF in order to find an agent available for teleporting
		DFAgentDescription template = new DFAgentDescription();
  		ServiceDescription templateSd = new ServiceDescription();
  		templateSd.setType("teleporting_agent");
  		template.addServices(templateSd);
		try {
				DFAgentDescription[] result = DFService.search(myAgent, template);
				//AID[] migr_sites = new AID[result.length]; //agents available for teleporting
				
				for (int i = 0; i < result.length; ++i) {
					if (!result[i].getName().equals(this.myAgent.getAID())) {
						this.migr_sites.add(result[i].getName());
					}
				}
				
				if (!this.migr_sites.isEmpty()) { 
					System.out.println(this.migr_sites.size() + " migration site(s) found by " + myAgent.getName());
					ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
					request.setContent("migrate");
					this.ms = migr_sites.iterator();
					while (ms.hasNext()) {
							AID site = ms.next();
							System.out.println(this.myAgent.getLocalName() + ": migration service offered by " + site.toString());
							request.addReceiver(site);
					}
					this.myAgent.send(request);
				}
				else {
					System.out.println("No migration sites found by " + myAgent.getName());
				}
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		
	}
	
}
