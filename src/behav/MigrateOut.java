package behav;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;;

public class MigrateOut extends OneShotBehaviour {
	
	private String ip;
	private final String port_num;
	String vm;
	
	public MigrateOut (Agent a, String VMname, String IP_Address) {
		super(a);
		this.ip = IP_Address;
		this.vm = VMname;
		this.port_num = "6000";
	}
	
	public void action() {	
		//!!!! VM must be already running
		
		ACLMessage dest_msg = myAgent.receive();
		
		if (dest_msg!= null & dest_msg.getContent().equals("Teleporting on destination started")) {
			ProcessBuilder pb = new ProcessBuilder("VBoxManage", "controlvm", vm, "teleport", "--host", ip, "--port", port_num);
			try {
				Process process = pb.start();
				System.out.println("teleport in progress from source");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			block();
		}
	}
}
