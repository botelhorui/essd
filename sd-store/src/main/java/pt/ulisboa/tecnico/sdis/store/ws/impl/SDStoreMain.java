package pt.ulisboa.tecnico.sdis.store.ws.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.ws.Endpoint;

import example.ws.uddi.UDDINaming;


public class SDStoreMain {
	
	public static String decorate(String s){
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return String.format("[%s][SDStoreMain]", format.format(date))+s;
	}
	
	public static void println(String s){
		System.out.println(decorate(s));
	}
	
	public static void printf(String s, Object... args){
		System.out.printf(decorate(s), args);
	}

    public static void main(String[] args) {
		if(args.length < 3){
			println("Argument(s) missing!");
			printf("Usage: java %s uddiUrl wsName wsUrl%n", SDStoreMain.class.getName());
		}
		String uddiUrl = args[0];
		String wsName = args[1];
		String url = args[2];
		Endpoint endpoint = null;
		UDDINaming uddiNaming = null;
		Executor executor = null;
		try {
			endpoint = Endpoint.create(new SDStoreImpl());
			// public endpoint
			printf("Starting %s%n",url);
			// multi-threading support.
			executor = Executors.newFixedThreadPool(5);
			endpoint.setExecutor(executor);
			endpoint.publish(url);
			
			
			// publish to UDDI
			printf("Publishing '%s' to UDDI at %s%n",wsName,uddiUrl);
			uddiNaming = new UDDINaming(uddiUrl);
			uddiNaming.addServiceBinding(wsName, url);

			// wait
			println("Awaiting connections");
			println("Press enter to shutdown");
			System.in.read();
		} catch (Exception e) {
			printf("Caught exception: %s%n",e);
			e.printStackTrace();
		} finally {
			if(executor != null){				
				ExecutorService es = (ExecutorService)executor;
				es.shutdown();
				println("Stopped thread pool");
			}
			try {
				if(endpoint != null){
					endpoint.stop();
					
					printf("Stopped %s%n",url);
				}
			} catch (Exception e) {
				printf("Caught exception when stopping %s%n",e);
				e.printStackTrace();
			}
			try{
				if(uddiNaming!=null){
					//Delete from UDDI
					uddiNaming.unbind(wsName);
					printf("Deleted '%s' from UDDI%n",wsName);
				}
			}catch(Exception e){
				printf("Caught exception when deleting: %s%n", e);
				e.printStackTrace();
			}
		}

    }

}
