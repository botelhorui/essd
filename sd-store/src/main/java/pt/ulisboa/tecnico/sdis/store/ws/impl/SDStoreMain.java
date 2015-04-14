package pt.ulisboa.tecnico.sdis.store.ws.impl;

import javax.xml.ws.Endpoint;
import uddi.UDDINaming;

public class SDStoreMain {

    public static void main(String[] args) {
		if(args.length < 3){
			System.err.println("Argument(s) missing!");
			System.err.printf("Usage: java %s uddiUrl wsName wsUrl%n", SDStoreMain.class.getName());
		}
		String uddiUrl = args[0];
		String wsName = args[1];
		String url = args[2];
		Endpoint endpoint = null;
		UDDINaming uddiNaming = null;
		try {
			endpoint = Endpoint.create(new SDStoreImpl());
			// public endpoint
			System.out.printf("Starting %s%n",url);
			endpoint.publish(url);
			
			// publish to UDDI
			System.out.printf("Publishing '%s' to UDDI at %s%n",wsName,uddiUrl);
			uddiNaming = new UDDINaming(uddiUrl);
			uddiNaming.rebind(wsName,url);

			// wait
			System.out.println("Awaiting connections");
			System.out.println("Press enter to shutdown");
			System.in.read();
		} catch (Exception e) {
			System.out.printf("Caught exception: %s%n",e);
			e.printStackTrace();
		} finally {
			try {
				if(endpoint != null){
					endpoint.stop();
					System.out.printf("Stopped %s%n",url);
				}
			} catch (Exception e) {
				System.out.printf("Caught exception when stopping %s%n",e);
				e.printStackTrace();
			}
			try{
				if(uddiNaming!=null){
					//Delete from UDDI
					uddiNaming.unbind(wsName);
					System.out.printf("Deleted '%s' from UDDI%n",wsName);
				}
			}catch(Exception e){
				System.out.printf("Caught exception when deleting: %s%n", e);
				e.printStackTrace();
			}
		}

    }

}
