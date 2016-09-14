package behav;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
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
		ProcessBuilder pb = new ProcessBuilder("VBoxManage", "modifyvm", vm, "--teleporter", "on", "--teleporterport", port_num);
		try {
			Process process = pb.start();
			System.out.println("teleport initiated");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
