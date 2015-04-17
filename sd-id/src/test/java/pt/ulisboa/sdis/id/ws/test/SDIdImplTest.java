package pt.ulisboa.tecnico.sdis.id.ws.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pt.ulisboa.tecnico.sdis.id.ws.InvalidUser_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.UserAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.EmailAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidEmail_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.AuthReqFailed_Exception;

import pt.ulisboa.tecnico.sdis.id.ws.SDId;
import pt.ulisboa.tecnico.sdis.id.ws.impl.SDIdImpl;
import pt.ulisboa.tecnico.sdis.id.ws.impl.UserAccount;

public class SDIdImplTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	

	public UserAccount getUserAccountByUsername( SDIdImpl service, String username ) {
		
		for (UserAccount user : service.getUsers()){
			if(user.getUserId().equals(username))
				return user;
		}
		
		return null;
	}
	
	public UserAccount getUserAccountByEmail( SDIdImpl service , String email ) {
		
		for (UserAccount user : service.getUsers()){
			if(user.getEmail().equals(email))
				return user;
		}
		
		return null;
		
	}
	
}
