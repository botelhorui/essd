package pt.ulisboa.tecnico.sdis.store.ws.cli;

import pt.ulisboa.tecnico.sdis.store.ws.*; // classes generated from WSDL


/**
 *  Example program that calls remote operations.
 */
public class SDStoreClientMain {

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Argument(s) missing!");
            System.err.printf("Usage: java %s uddiURL wsName%n", SDStoreClientMain.class.getName());
            return;
        }
        SDStoreClient client = new SDStoreClient(args[0],args[1]);

        // TODO what should we put in main?
        System.out.printf("listDocs('test')=%d\n",client.listDocs("test"));
    }

}
