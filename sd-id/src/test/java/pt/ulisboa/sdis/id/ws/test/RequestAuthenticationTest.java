package pt.ulisboa.tecnico.sdis.id.ws.test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import pt.ulisboa.tecnico.sdis.id.ws.AuthReqFailed_Exception;

import pt.ulisboa.tecnico.sdis.id.ws.CreateUser;

import pt.ulisboa.tecnico.sdis.id.ws.SDId;
import pt.ulisboa.tecnico.sdis.id.ws.impl.SDIdImpl;
import pt.ulisboa.tecnico.sdis.id.ws.impl.UserAccount;

public class RequestAuthenticationTest extends SDIdImplTest {

	private static final String USERNAME = "no-one";
	private static final String USERNAME_2 = "no-one2";
	private static final String INEXISTENT_USERNAME = "idontexist";

	private static final String EMAIL = "no.one@yahoo.com";
	private static final String EMAIL_2 = "no.one2@yahoo.com";


	// Case 1: 
	//		the username exists and is valid
	//		the password is valid and matches the stored password

	@Test
	public void success() throws Exception {

		SDIdImpl id = new SDIdImpl();

		id.createUser( USERNAME , EMAIL);

		UserAccount user = getUserAccountByUsername( id , USERNAME );

		String password = user.getPassword();
		byte[] password_bytes = password.getBytes();

		byte[] confirmation = id.requestAuthentication( USERNAME , password_bytes );

		byte[] bytes = new byte[1];
		Arrays.fill( bytes, (byte) 1 );

		assertNotNull( "User does not exist." , user );
		assertTrue( "Authentication done incorrectly." , Arrays.equals( confirmation , bytes ) );

	}

	// Case 2: 
	//		the username doesn't exist
	@Test(expected=AuthReqFailed_Exception.class)
	public void authenticationFailedUserInexistent() throws Exception {

		SDIdImpl id = new SDIdImpl();

		id.createUser( USERNAME , EMAIL);
		UserAccount user = getUserAccountByUsername( id , USERNAME );
		String password = user.getPassword();
		byte[] password_bytes = password.getBytes();

		id.requestAuthentication( INEXISTENT_USERNAME, password_bytes);

	}

	// Case 3: 
	//		the username exists 
	//		the password doesn't match the stored
	@Test(expected=AuthReqFailed_Exception.class)
	public void authenticationPasswordDoesntMatch() throws Exception {

		SDIdImpl id = new SDIdImpl();

		id.createUser( USERNAME , EMAIL);
		id.createUser( USERNAME_2 , EMAIL_2);
		
		UserAccount user = getUserAccountByUsername( id , USERNAME );
		
		String password = user.getPassword();
		byte[] password_bytes = password.getBytes();
		
		id.requestAuthentication( USERNAME_2 , password_bytes );

	}
}
