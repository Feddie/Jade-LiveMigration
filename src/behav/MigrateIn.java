package behav;

import java.io.IOException;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class MigrateIn extends OneShotBehaviour {
	private String ip;
	private final String port_num;
	String vm;
	
	public MigrateIn (Agent a, String VMname) {
		super(a);
		//this.ip = IP_Address;
		this.vm = VMname;
		this.port_num = "6000";
	}
	
	public void action() {	
		ProcessBuilder pb = new ProcessBuilder("VBoxManage", "modifyvm", vm, "--teleporter", "on", "--teleporterport", port_num);
		try {
			Process process_init = pb.start();
			System.out.println("teleport initiated");
		} catch (IOException e) {
			e.printStackTrace();
		}
		ProcessBuilder svm = new ProcessBuilder("VBoxManage", "startvm", vm);
		try {
			Process process_start  = svm.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ACLMessage inform_telinit = new ACLMessage(ACLMessage.INFORM);
		inform_telinit.setContent("Teleporting on destination started");
		
	}

	
}
