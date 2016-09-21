package behav;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class MigrateIn extends OneShotBehaviour {
	//private String ip;
	private final String port_num;
	String vm;
	String myIP;
	AID requester;
	
	public MigrateIn (Agent a, String VMname, AID req) {
		super(a);
		//this.ip = IP_Address;
		this.vm = VMname;
		this.port_num = "6000";
		this.requester = req;
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
		try {
			this.myIP = InetAddress.getLocalHost().getHostAddress().toString();
		} catch (UnknownHostException uh) {
			uh.printStackTrace();
			
		}
		//Inform source host that teleporting is ready here on destination
		ACLMessage inform_ready = new ACLMessage(ACLMessage.AGREE);
		inform_ready.addReceiver(requester);
		inform_ready.setContent(this.myIP);
		
		this.myAgent.send(inform_ready);
	}

	
}
