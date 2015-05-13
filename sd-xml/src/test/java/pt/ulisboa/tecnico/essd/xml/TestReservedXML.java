package pt.ulisboa.tecnico.essd.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;

import org.junit.Test;

public class TestReservedXML{
	
	@Test
	public void outputting(){
		/*
		 * Is being used solely as a 'main' for testing. Is still not a full-fledged test.
		 */
		
		ReservedXML test = new ReservedXML("SD-Testing", 7);
		test.generateXML();
		ReservedXML test2 = null;
		
		try{
			test2 = ReservedXML.parse(test.encode());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		if (test.getServiceName().equals(test2.getServiceName())){
			System.out.println("WOOPIE!");
		}
		if (test.getNounce() == test2.getNounce()){
			System.out.println("YIPPIE!");
		}
		
	}

	@Test
	public void doingShit(){

		try{
			RequestAuthenticationResponse rar = new RequestAuthenticationResponse("ola".getBytes(),"adeus".getBytes());
		
			System.out.println(new String(rar.encode()));
			RequestAuthenticationResponse rol = RequestAuthenticationResponse.parse(rar.encode());
			if(Arrays.equals(rar.getEncryptedTicket(),rol.getEncryptedTicket())){
				System.out.println("YAY!");
			}
			if(Arrays.equals(rar.getEncryptedUserCredentials(), rol.getEncryptedUserCredentials())){
				System.out.println("WOOHOO!");
			}
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void ShitAllOver(){

		try{
			UserCredentials rar = new UserCredentials("ola".getBytes(),"adeus".getBytes(), 741);
		
			System.out.println(new String(rar.encode()));
			UserCredentials rol = UserCredentials.parse(rar.encode());
			if(Arrays.equals(rar.getEncryptionKey(),rol.getEncryptionKey())){
				System.out.println("Y!");
			}
			if(Arrays.equals(rar.getSessionKey(), rol.getSessionKey())){
				System.out.println("A!");
			}
			if(rar.getNounce() == rol.getNounce()){
				System.out.println("Y!");
			}
			
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
	}
}