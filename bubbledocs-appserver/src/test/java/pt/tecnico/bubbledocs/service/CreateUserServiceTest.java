package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import pt.tecnico.bubbledocs.integration.CreateUserIntegrator;
import pt.tecnico.bubbledocs.BubbleDocsServiceTest;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.DuplicateUsernameException;
import pt.tecnico.bubbledocs.exception.DuplicateEmailException;
import pt.tecnico.bubbledocs.exception.InvalidEmailException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.exception.EmptyUsernameException;
import pt.tecnico.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.exception.CharacterLimitException;
import pt.tecnico.bubbledocs.exception.InvalidUsernameException;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;

import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class CreateUserServiceTest extends BubbleDocsServiceTest {

	// the tokens
	private String rootToken;
	private String arsToken;

	private static final String USERNAME = "ars";
	private static final String ORIGINAL_EMAIL = "ars@ist.utl.pt";
	private static final String ROOT_USERNAME = "root";
	private static final String USERNAME_DOES_NOT_EXIST = "no-one";
	private static final String USERNAME_DOES_NOT_EXIST2 = "no-one2";
	private static final String EMAIL = "no.one@yahoo.com";
	private static final String INVALID_EMAIL = "superinvalid.com";
	private static final String INVALID_USERNAME = "supercalifragilisticexpialidocious";
	private static final String REMOTE_INVALID_USERNAME = "NoLikeU";

	
	@Override
	public void populate4Test() {

		createUser(USERNAME,"António Rito Silva",ORIGINAL_EMAIL);
		rootToken = addUserToSession(ROOT_USERNAME);
		arsToken = addUserToSession(USERNAME);
	}

	
	// eventual service tests will be added here
	
	
}
