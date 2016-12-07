package behav;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import utils.VBoxInterface;

import java.io.IOException;

import org.virtualbox_5_0.IMachine;;

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
			IMachine machine = VBoxInterface.getInstance().getMachinebyUUID(this.vm);
			
			VBoxInterface.getInstance().teleport(machine, this.TargetIp);
			this.myAgent.doWait(5000);
			//may want to insert a check for Teleport success
			System.out.println(this.myAgent.getName() +" Teleported " + this.vm + " to " + this.TargetIp);
		}
}

