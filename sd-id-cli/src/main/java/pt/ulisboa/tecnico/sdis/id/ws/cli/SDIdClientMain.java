package pt.ulisboa.tecnico.sdis.id.ws.cli;

import pt.ulisboa.tecnico.sdis.id.ws.*; // classes generated from WSDL
import java.util.Arrays;

public class SDIdClientMain {

	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			System.err.println("Argument(s) missing!");
			System.err.printf("Usage: java %s uddiURL wsName%n", SDIdClientMain.class.getName());
			return;
		}
		SDIdClient client = new SDIdClient(args[0],args[1]);

		// Main: examples of SDId methods
		
		//createUser example
		client.createUser("johnny", "joao@joao");
		System.out.println("Created User 'johnny'!");
        
        //renewPassword example
        client.renewPassword("johnny");
        System.out.println("Renewed Password of User 'johnny'!");
        
        //removeUser example
        client.removeUser("johnny");
        System.out.println("Removed User 'johnny'!");
        
        //requestAuthentication example
        System.out.print("Request Authentication for 'alice'... ");
        String password = "Aaa1";
        byte[] bytes = client.requestAuthentication("alice", password.getBytes());
        byte[] b = new byte[1];
		b[0] = (byte) 1;
        if(Arrays.equals(bytes, b) == true)
        	System.out.println("OK!");
        
	}

}