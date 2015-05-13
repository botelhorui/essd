package pt.tecnico.bubbledocs.service.remote;

import java.io.IOException;
import java.util.Properties;

import pt.tecnico.bubbledocs.exception.DuplicateEmailException;
import pt.tecnico.bubbledocs.exception.DuplicateUsernameException;
import pt.tecnico.bubbledocs.exception.InvalidEmailException;
import pt.tecnico.bubbledocs.exception.InvalidUsernameException;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.ulisboa.tecnico.sdis.id.ws.*;
import pt.ulisboa.tecnico.sdis.id.ws.cli.*;

import javax.xml.registry.JAXRException;
import javax.xml.bind.DatatypeConverter;

public class IDRemoteServices {
	private static final String PROP_FILE = "remote-services.properties";

	private SDIdClient idClient; 

	public IDRemoteServices() throws RemoteInvocationException{
		Properties props = new Properties();
		try {
			props.load(IDRemoteServices.class.getClassLoader().getResourceAsStream(PROP_FILE));
		} catch (IOException e1) {
			throw new RemoteInvocationException(e1);
		}
		
		try{
			String uddiUrl = props.getProperty("uddi.url");
			String idName = props.getProperty("id.name");
			System.out.printf("uddi.url:%s id.name:%s%n",uddiUrl,idName);
			//idClient = new SDIdClient("http://localhost:8081", "SD-ID");
			idClient = new SDIdClient(uddiUrl,idName);
		}catch(SDIdClientException e){
			throw new RemoteInvocationException(e);
		}catch(JAXRException e){
			throw new RemoteInvocationException(e);
		}
	}
	
	public static void main(String[] args) {
		new IDRemoteServices();
	}

	public void createUser(String username, String name, String email)
			throws InvalidUsernameException, DuplicateUsernameException,
			DuplicateEmailException, InvalidEmailException,
			RemoteInvocationException {
		try{
			idClient.createUser(username, email);
		}catch(EmailAlreadyExists_Exception e){
			throw new DuplicateEmailException();
		}catch(InvalidEmail_Exception e){
			throw new InvalidEmailException();
		}catch(InvalidUser_Exception e){
			throw new InvalidUsernameException();
		}catch(UserAlreadyExists_Exception e){
			throw new DuplicateUsernameException();
		}

	}

	public void loginUser(String username, String password)
			throws LoginBubbleDocsException, RemoteInvocationException {
		try{
			byte[] p = DatatypeConverter.parseBase64Binary(password);
			idClient.requestAuthentication(username, p);
		}catch(AuthReqFailed_Exception e){
			throw new LoginBubbleDocsException();
		}
	}

	public void removeUser(String username)
			throws LoginBubbleDocsException, RemoteInvocationException {
		try{
			idClient.removeUser(username);
		}catch(UserDoesNotExist_Exception e){
			throw new LoginBubbleDocsException();
		}
	}

	public void renewPassword(String username)
			throws LoginBubbleDocsException, RemoteInvocationException {
		try{
			idClient.renewPassword(username);
		}catch(UserDoesNotExist_Exception e){
			throw new LoginBubbleDocsException();
		}
	}

}
