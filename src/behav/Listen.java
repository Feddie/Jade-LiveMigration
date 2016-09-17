package behav;

import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Listen extends CyclicBehaviour{
	
	public void action() {
		//MessageTemplate template = MessageTemplate.MatchConversationId("migrate");
			
		//ACLMessage msg = this.myAgent.receive(template);
		
		ACLMessage msg = myAgent.receive();
		
		if (msg != null  ) {
			AID sender = msg.getSender();
		
			System.out.println(this.myAgent.getName() + " received a message (" + msg.getContent() + ") from " + sender.getName());
			if (msg.getContent().equals("migrate") & !sender.equals(this.myAgent.getAID())) {
				ACLMessage reply = msg.createReply();
		
				////////insert control here
				reply.setPerformative(ACLMessage.AGREE);
				System.out.println(this.myAgent.getLocalName() + " agreed.");
			}
		
		}	
		else {
			block();
		}
		
		}
}
