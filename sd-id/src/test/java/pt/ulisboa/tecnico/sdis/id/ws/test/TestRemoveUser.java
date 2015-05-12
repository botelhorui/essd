package pt.ulisboa.tecnico.sdis.id.ws.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist_Exception;

import pt.ulisboa.tecnico.sdis.id.ws.CreateUser;

import pt.ulisboa.tecnico.sdis.id.ws.SDId;
import pt.ulisboa.tecnico.sdis.id.ws.impl.SDIdImpl;
import pt.ulisboa.tecnico.sdis.id.ws.impl.UserAccount;

public class TestRemoveUser extends TestSDIdImpl {

	private static final String USERNAME = "no-one";
	private static final String INEXISTENT_USERNAME = "idontexist";

	private static final String EMAIL = "no.one@yahoo.com";


	// Case 1: 
	//		the user exists
	@Test
	public void success() throws Exception {

		SDIdImpl id = new SDIdImpl();

		id.createUser( USERNAME , EMAIL);
		id.removeUser( USERNAME );

		UserAccount user = getUserAccountByUsername( id , USERNAME );

		assertNull( "User was not deleted correctly." , user );

	}

	// Case 2: 
	//		the new user doesn't exist
	@Test(expected=UserDoesNotExist_Exception.class)
	public void userDoesNotExist() throws Exception {

		SDIdImpl id = new SDIdImpl();

		id.removeUser( INEXISTENT_USERNAME );

	}
	

	// Case 3: 
	//		the user was already removed
	@Test(expected=UserDoesNotExist_Exception.class)
	public void userTwiceRemoved() throws Exception {

		SDIdImpl id = new SDIdImpl();

		id.createUser( USERNAME , EMAIL);
		id.removeUser( USERNAME );
		id.removeUser( USERNAME );

	}

}
