package pt.ulisboa.tecnico.sdis.id.ws.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import pt.ulisboa.tecnico.sdis.id.ws.InvalidUser_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.UserAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.EmailAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidEmail_Exception;

import pt.ulisboa.tecnico.sdis.id.ws.SDId;
import pt.ulisboa.tecnico.sdis.id.ws.impl.SDIdImpl;
import pt.ulisboa.tecnico.sdis.id.ws.impl.UserAccount;

public class TestCreateUser extends TestSDIdImpl {

	private static final String USERNAME = "no-one";
	private static final String USERNAME_2 = "no-one2";
	private static final String INVALID_USERNAME = "";
	private static final String NULL_USERNAME = null;
	
	private static final String EMAIL = "no.one@yahoo.com";
	private static final String EMAIL_2 = "no.one2@yahoo.com";
	private static final String INVALID_EMAIL = "nooneyahoocom";
	

	// Case 1: 
	//		the new username doesn't exist and is valid
	//		the new email doesn't exist
	@Test
	public void success() throws Exception {

		SDIdImpl id = new SDIdImpl();

		id.createUser( USERNAME , EMAIL);

		UserAccount user = getUserAccountByUsername( id , USERNAME );

		assertNotNull( "User was not created correctly." , user );
		assertEquals( "Created user's Username is not correct." , user.getUserId() , USERNAME );
		assertEquals( "Created user's Email is not correct." , user.getEmail() , EMAIL );

	}

	// Case 2: 
	//		the new username already exists and is valid
	//		the new email doesn't exist
	@Test(expected=UserAlreadyExists_Exception.class)
	public void userAlreadyExists() throws Exception {

		SDIdImpl id = new SDIdImpl();

		id.createUser( USERNAME , EMAIL);
		id.createUser( USERNAME , EMAIL_2);

	}

	// Case 3: 
	//		the new username doesn't exist and is valid
	//		the new email already exists
	@Test(expected = EmailAlreadyExists_Exception.class)
	public void emailAlreadyExists() throws Exception {

		SDIdImpl id = new SDIdImpl();

		id.createUser( USERNAME , EMAIL);
		id.createUser( USERNAME_2 , EMAIL);

	}

	// Case 4: 
	//		the new username doesn't exist and is invalid
	//		the new email doesn't exist
	@Test(expected = InvalidUser_Exception.class)
	public void invalidUsername() throws Exception {

		SDIdImpl id = new SDIdImpl();

		id.createUser( INVALID_USERNAME , EMAIL);

	}

	// Case 5: 
	//		the new username doesn't exist and is null
	//		the new email doesn't exist
	@Test(expected = InvalidUser_Exception.class)
	public void nullUsername() throws Exception {

		SDIdImpl id = new SDIdImpl();

		id.createUser( NULL_USERNAME , EMAIL);

	}

	// Case 6: 
	//		the new username doesn't exist and is valid
	//		the new email is invalid
	@Test(expected = InvalidEmail_Exception.class)
	public void invalidEmail() throws Exception {

		SDIdImpl id = new SDIdImpl();

		id.createUser( USERNAME , INVALID_EMAIL);

	}
}
