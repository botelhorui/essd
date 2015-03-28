package pt.tecnico.bubbledocs.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom2.Document;
import org.jdom2.Element;
import org.joda.time.Hours;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.exception.DifferentUserImportException;
import pt.tecnico.bubbledocs.exception.UnknownBubbleDocsUserException;
import pt.tecnico.bubbledocs.exception.DuplicateUsernameException;
import pt.tecnico.bubbledocs.exception.UserIsNotOwnerException;

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
		}

		return bd;
	}

	public int generateId(){
		int genId = getGenId();
		setGenId(genId+1);
		return genId;
	}

	public User getUserByUsername(String username) throws UnknownBubbleDocsUserException {

		for(User u: getUserSet()){

			if(u.getUsername().equals(username)){
				return u;
			}
		}

		throw new UnknownBubbleDocsUserException();
	}

	public boolean hasUser(String username){

		for(User u: getUserSet()){

			if(u.getUsername().equals(username)){
				return true;
			}
		}

		return false;
	}

	@Override
	public void addUser(pt.tecnico.bubbledocs.domain.User user) throws DuplicateUsernameException {
		// TODO if the user is BubbleDocs already has the user User, should it throw exception?
		if(hasUser(user.getUsername())){
			throw new DuplicateUsernameException();
		}

		super.addUser(user);
	}


	public void removeUser(String username) {

		try {
			User u = getUserByUsername(username);
			super.removeUser(u);
		}
		catch (UnknownBubbleDocsUserException e) {
			System.out.println("User \"" + username + "\" does not exist.");
		}
	}


	public User createUser(String username,String password,String name) throws DuplicateUsernameException{

		if(hasUser(username)){
			throw new DuplicateUsernameException();
		}

		User u = new User(username, password, name);
		addUser(u);
		return u;
	}

	public void importSheet(Document doc, String username){

		/*
		 * Checking for potential errors.
		 */
		Element root = doc.getRootElement();
		Element owner = root.getChild("Owner");

		String docUsername = owner.getAttributeValue("username");

		if(!docUsername.equals(username)){
			throw new UserIsNotOwnerException();
		}

		if(!hasUser(username)){
			throw new UnknownBubbleDocsUserException();
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
	}
	
	public User getUserByToken(String token){
		for(User u:getUserSet()){			
			if(u.getToken() != null && u.getToken().equals(token))
				return u;
		}
		return null;
	}


	public void renewSessionDuration(User u) {
		u.setLastAccess(new LocalTime());		
	}


	public void renewToken(User u) {
		logger.info("renewing token for user:"+u.getUsername()+" token:"+u.getToken());
		String token;
		int rn;
		if(u.getToken()==null){
			rn = (int)(Math.random()*9);
			token = u.getUsername()+"-"+rn;
			u.setToken(token);
			return;
		}
		Pattern p = Pattern.compile("(\\d)$");
		Matcher m = p.matcher(u.getToken());
		m.find();
		int n = Integer.parseInt(m.group());		
		while(true){
			rn = (int)(Math.random()*9);
			if(rn!=n){
				token = u.getUsername()+"-"+rn;
				u.setToken(token);
				return;
			}			
		}
	}


	public void cleanInvalidSessions(){
		LocalTime now = new LocalTime();
		for(User u: getUserSet()){
			int dif = Hours.hoursBetween(now, u.getLastAccess()).getHours();
			if(dif>=LEASE_HOURS){
				u.setLastAccess(null);
				u.setToken(null);
			}
		}		
	}
}
