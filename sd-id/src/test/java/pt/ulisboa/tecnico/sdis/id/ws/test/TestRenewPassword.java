package pt.ulisboa.tecnico.sdis.id.ws.test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist_Exception;

import pt.ulisboa.tecnico.sdis.id.ws.SDId;
import pt.ulisboa.tecnico.sdis.id.ws.impl.SDIdImpl;
import pt.ulisboa.tecnico.sdis.id.ws.impl.UserAccount;

public class TestRenewPassword extends TestSDIdImpl{

	private static final String USERNAME = "no-one";
	private static final String INEXISTENT_USERNAME = "idontexist";

	private static final String EMAIL = "no.one@yahoo.com";


	// Case 1: 
	//		the user exists
	@Test
	public void success() throws Exception {

		SDIdImpl id = new SDIdImpl();
		id.createUser( USERNAME , EMAIL );

		UserAccount user = getUserAccountByUsername( id , USERNAME );

		String password1 = user.getPassword();

		id.renewPassword( USERNAME );

		String password2 = user.getPassword();

		assertNotNull( "User does not have a password." , password1);
		assertNotNull( "Password change produces null." , password2);
		assertNotEquals( "Password didn't change on renew." , password1 , password2 );
	}

	// Case 2: 
	//		the user does not exist
	@Test(expected=UserDoesNotExist_Exception.class)
	public void userDoesNotExist() throws Exception {

		SDIdImpl id = new SDIdImpl();

		id.createUser( INEXISTENT_USERNAME , EMAIL);
		id.removeUser( INEXISTENT_USERNAME );
		
		id.renewPassword( INEXISTENT_USERNAME );

	}



}
