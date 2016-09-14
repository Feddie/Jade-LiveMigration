package behav;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Listen extends CyclicBehaviour{
	
	public void action() {
		MessageTemplate template = MessageTemplate.MatchConversationId("migrate");
			
		ACLMessage msg = this.myAgent.receive(template);
		/*
		if (msg != null) {
			this.myAgent.addBehaviour(new Migration(this, IP_address, port));
		}
		else {
			block();
		}
		*/
		}
}
