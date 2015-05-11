package pt.ulisboa.tecnico.sdis.store.ws.cli;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import pt.ulisboa.tecnico.sdis.store.ws.*; // classes generated from WSDL


/**
 *  Example program that calls remote operations.
 */
public class SDStoreClientMain {

	public static void main(String[] args) throws Exception {
		if (args.length < 5) {
			System.err.println("Argument(s) missing!");
			System.err.printf("Usage: java %s uddiURL wsName clientId N RT WT%n", SDStoreClientMain.class.getName());
			return;
		}
		SDStoreClient client = new SDStoreClient(
				args[0],
				args[1],
				Integer.parseInt(args[2]),
				Integer.parseInt(args[3]),
				Integer.parseInt(args[4]),
				Integer.parseInt(args[5]));

		
		// TODO what should we put in main?
		DocUserPair up = new DocUserPair();
		up.setUserId("alice");
		up.setDocumentId("a1");
		byte[] val = client.load(up);
		/*
		client.store(up, "Adeus".getBytes());
		byte[] val = client.load(up);
		String expected = "Adeus";
		if(Arrays.equals(val, expected.getBytes())){
			System.out.println("Got excepted value");
		}else{
			System.out.println("Got different value:'"+new String(val)+"' expected:'"+expected+"'");
		}	
		*/	

	}

}
