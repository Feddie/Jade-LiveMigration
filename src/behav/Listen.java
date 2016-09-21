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
		
		if (msg != null && !msg.getSender().equals(this.myAgent.getAID())) {
			
			if (msg.getPerformative() == ACLMessage.REQUEST & msg.getContent().equals("migrate")) {
			AID sender = msg.getSender();
		
			System.out.println(this.myAgent.getName() + " received a message (" + msg.getContent() + ") from " + sender.getName());
			
			//ACLMessage reply = msg.createReply();
	
			////////insert control here [whether the migration on the host is accepted or not]
			/*
			reply.setPerformative(ACLMessage.AGREE);
			reply.setContent("Teleport accepted.");
			this.myAgent.send(reply);
			*/
			System.out.println("Teleport from " + sender.getName() + " to " + this.myAgent.getName() + " accepted.");
			
			this.myAgent.addBehaviour(new MigrateIn(this.myAgent, "Ubu", sender));
		
			}
			
			else if (msg.getPerformative() == ACLMessage.AGREE) {
				String IpTarget = msg.getContent();
				this.myAgent.addBehaviour(new MigrateOut(this.myAgent, "Ubu32", IpTarget));
			}
		}	
		else {
			block();
		}
		
		}
}
