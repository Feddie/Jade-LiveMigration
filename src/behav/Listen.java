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
			
			if (msg.getPerformative() == ACLMessage.REQUEST & msg.getContent().contains("migrate")) {
			AID sender = msg.getSender();
		
			System.out.println(this.myAgent.getName() + " received a message (" + msg.getContent() + ") from " + sender.getName());

			System.out.println("Teleport from " + sender.getName() + " to " + this.myAgent.getName() + " accepted.");
			
			String vm_name = msg.getContent().split(":")[1];
			System.out.println("Migrating " + vm_name);
			this.myAgent.addBehaviour(new MigrateIn(this.myAgent, vm_name, sender));
		
			}
			
			else if (msg.getPerformative() == ACLMessage.AGREE) {
				
				String IpTarget = msg.getContent().split(":")[0];
				String vmname = msg.getContent().split(":")[1];
				this.myAgent.addBehaviour(new MigrateOut(this.myAgent, vmname, IpTarget));
			}
		}	
		else {
			block();
		}
		
		}
}
