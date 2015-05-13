package pt.tecnico.bubbledocs.domain;

import java.lang.String;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.JDOMException;
import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.CharacterLimitException;
import pt.tecnico.bubbledocs.exception.DuplicateUsernameException;
import pt.tecnico.bubbledocs.exception.DuplicateEmailException;
import pt.tecnico.bubbledocs.exception.InvalidEmailException;
import pt.tecnico.bubbledocs.exception.UserIsNotOwnerException;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.SpreadSheetIdUnknown;

public class BubbleDocs extends BubbleDocs_Base {
	private static final Logger logger = LoggerFactory.getLogger(FenixFramework.class);
	private static final int LEASE_HOURS = 2;

	private BubbleDocs() {        
		FenixFramework.getDomainRoot().setBubbleDocs(this);            
	}

	public static BubbleDocs getInstance(){
		BubbleDocs bd = FenixFramework.getDomainRoot().getBubbleDocs();
		if(bd==null){
			bd = new BubbleDocs();
			bd.getUserRoot();
		}		
		return bd;
	}

	private User getUserRoot() {		
		User root = null;
		if(!hasUser("root")){
			root = createUser("root", "Super User", null);
			root.setPassword("root");
		}else{
			root = getUserByUsername("root");
		}
		return null;
	}

	public int generateId(){
		int genId = getGenId();
		setGenId(genId+1);
		return genId;
	}

	public User getUserByUsername(String username){

		for(User u: getUserSet()){

			if(u.getUsername().equals(username)){
				return u;
			}
		}

		return null;
	}

	public User getUserByEmail(String email){

		for(User u: getUserSet()){

			if(u.getEmail().equals(email)){
				return u;
			}
		}

		return null;
	}

	public boolean hasUser(String username){

		for(User u: getUserSet()){
			if(u.getUsername().equals(username)){
				return true;			
			}
		}
		return false;
	}

	public void removeUser(String username) {
		User u = getUserByUsername(username);
		super.removeUser(u);

	}


	public User createUser(String username,String name,String email){

		User u = new User(username, name, email);
		addUser(u);
		return u;
	}

	public SpreadSheet importSheet(Document doc, String username){

		/*
		 * Checking for potential errors.
		 */
		Element root = doc.getRootElement();
		Element owner = root.getChild("Owner");

		String docUsername = owner.getAttributeValue("username");

		//if(!docUsername.equals(username)){
		//throw new UserIsNotOwnerException();
		//}

		if(!hasUser(username)){
			throw new LoginBubbleDocsException();
		}

		/*
		 * Populating data.
		 */

		User creator = getUserByUsername(username);
		SpreadSheet ss = creator.createSheet(root.getAttributeValue("name"), Integer.parseInt(root.getAttributeValue("lines")), Integer.parseInt(root.getAttributeValue("columns")));

		DateTimeFormatter dtf = ISODateTimeFormat.dateTime();
		ss.setCreationDate(dtf.parseDateTime(root.getAttributeValue("creation-date")));

		Element cells = root.getChild("Cells");

		for(Element cellElement : cells.getChildren()){
			Cell cell = ss.getCell(Integer.parseInt(cellElement.getAttributeValue("line")), Integer.parseInt(cellElement.getAttributeValue("column")));
			cell.importXML(cellElement);
		}
		return ss;
	}

	public boolean isUserInSession(String token){
		User u = getUserByToken(token);
		if(u == null)
			return false;
		if(u.getSession() == null)
			return false;
		Session s = u.getSession();
		DateTime now = new DateTime();		
		int dif = Hours.hoursBetween(now, s.getLastAccess()).getHours();
		if(Math.abs(dif)>=LEASE_HOURS){			
			s.delete();
			return false;
		}
		return true;
	}

	public String getUsernameFromToken(String token){
		String username = null;

		User u = this.getUserByToken(token);

		if(u != null)
			username = u.getUsername(); 

		return username;
	}

	public User getUserByToken(String token){
		for(User u : getUserSet()){
			Session s = u.getSession();
			if(s != null && s.getToken().equals(token))
				return u;
		}
		return null;
	}

	public void renewSessionDuration(User u) {
		if(u.getSession()==null)
			u.setSession(new Session());
		u.getSession().renewLassAccess();	
	}

	public void renewSessionDuration(String token){
		getUserByToken(token).getSession().renewLassAccess();
	}


	public void renewToken(User u) {
		u.getSession().renewToken();
	}


	public void cleanInvalidSessions(){		
		for(User u: getUserSet()){
			Session s = u.getSession();
			if(s==null)
				continue;			
			DateTime now = new DateTime();
			int dif = Hours.hoursBetween(now, s.getLastAccess()).getHours();
			if(dif>=LEASE_HOURS){
				s.delete();
			}
		}		
	}

	public void validateUser(String token) throws BubbleDocsException {

		if (!isUserInSession(token)){
			throw new UserNotInSessionException();
		}

		renewSessionDuration(token);

	}

	public void checkIfRoot(String token) throws BubbleDocsException {

		User u = getUserByToken(token);
		if(!(u.getUsername().equals("root"))){
			throw new UnauthorizedOperationException();
		}

	}

	public void checkEmail(String email) throws BubbleDocsException {

		User u = getUserByEmail(email);

		if(u != null)

			throw new DuplicateEmailException();

		String[] strings = email.split("@", -1);

		if( strings.length != 2 ) 

			throw new InvalidEmailException();

	}


	// BUSINESS RULE #2
	public void validateUsername(String username) throws CharacterLimitException {

		if((username.length() < 3) || (username.length() > 8)){

			throw new CharacterLimitException();
		}

	}


	public SpreadSheet getSpreadsheetById(int id){

		SpreadSheet sheet = null;
		for(SpreadSheet spread: getSpreadSheetSet()){
			if(spread.getId() == id){
				sheet = spread;
				break;
			}
		}

		if(sheet == null){
			throw new SpreadSheetIdUnknown();
		}

		return sheet;

	}

	
	public void delete() {		

		for(User u: getUserSet()){		
			u.delete();		
		}		

		this.setRoot(null);

		deleteDomainObject();		
	}

}