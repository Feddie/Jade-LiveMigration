package behav;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;;

public class MigrateOut extends OneShotBehaviour {
	
	String vm;
	private String TargetIp;
	private final String port_num;
	
	
	public MigrateOut (Agent a, String VMname, String Ip) {
		super(a);
		this.vm = VMname;
		this.TargetIp = Ip;
		this.port_num = "6000";
	}
	
	public void action() {	
		
			//start VM on source //!!!! OR IT SHOULD BE ALREADY RUNNING
			ProcessBuilder svm = new ProcessBuilder("VBoxManage", "startvm", vm);
			try {
				Process process_start  = svm.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
			ProcessBuilder pb = new ProcessBuilder("VBoxManage", "controlvm", vm, "teleport", "--host", this.TargetIp, "--port", port_num);
			try {
				Process process = pb.start();
				System.out.println("teleport in progress from source to host " + this.TargetIp);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
}

