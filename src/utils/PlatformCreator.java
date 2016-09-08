package utils;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class PlatformCreator {
	public static void main(String[] args) {

		Runtime rt = Runtime.instance();
		
		Profile p = new ProfileImpl();
		ContainerController cc = rt.createMainContainer(p);
		
		try {
			AgentController ag = cc.createNewAgent("Alpha", "agents.Agent0", null);
			ag.start();
		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
