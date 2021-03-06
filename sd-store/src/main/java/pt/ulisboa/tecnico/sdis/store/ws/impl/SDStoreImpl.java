package pt.ulisboa.tecnico.sdis.store.ws.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Calendar;

import javax.annotation.Resource;
import javax.jws.*;
import javax.xml.bind.DatatypeConverter;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import pt.ulisboa.tecnico.sdis.store.ws.handlers.ResponseHandler;

import pt.ulisboa.tecnico.essd.xml.WebServiceRequest;
import pt.ulisboa.tecnico.essd.xml.WebServiceResponse;
import pt.ulisboa.tecnico.essd.xml.Ticket;
import pt.ulisboa.tecnico.essd.xml.Authenticator;
import pt.ulisboa.tecnico.essd.crypto.AESCipher;

import pt.ulisboa.tecnico.sdis.store.ws.*; // classes generated from WSDL
import pt.ulisboa.tecnico.sdis.store.ws.handlers.Tag;
import pt.ulisboa.tecnico.sdis.store.ws.handlers.VersionHandler;

@WebService(
    endpointInterface="pt.ulisboa.tecnico.sdis.store.ws.SDStore", 
    wsdlLocation="SD-STORE.1_1.wsdl",
    name = "SDStore",
    portName="SDStoreImplPort",
    targetNamespace="urn:pt:ulisboa:tecnico:sdis:store:ws",
    serviceName="SDStore"
)
@HandlerChain(file="/handler-chain.xml")
public class SDStoreImpl implements SDStore {
	
	Map<String, UserRepo> userRepos = new HashMap<String, UserRepo>();
	
	private String _sStoreKey = "7o4+TVh/u+0je+qKK9UkPg==";
	
	@Resource
	private WebServiceContext webServiceContext;

	public SDStoreImpl(){
		populateDomain();
	}
	
	public String decorate(String s){
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return String.format("[%s][SDStoreImpl]", format.format(date))+s;
	}
	
	public void println(String s){
		System.out.println(decorate(s));
	}
	
	public void printf(String s, Object... args){
		System.out.printf(decorate(s), args);
	}
	
	private void populateDomain(){
		try{
			UserRepo ur = new UserRepo("alice");
			userRepos.put("alice", ur);
			ur.createDoc("a1");		
			ur.storeDoc("a1", "AAAAAAAAAA".getBytes());
			ur.createDoc("a2");
			ur.storeDoc("a2", "aaaaaaaaaa".getBytes());
			
			ur = new UserRepo("bruno");
			userRepos.put("bruno", ur);
			ur.createDoc("b1");
			ur.storeDoc("b1", "BBBBBBBBBBBBBBBBBBBB".getBytes());
			
			ur = new UserRepo("carla");
			userRepos.put("carla", ur);
			
			ur = new UserRepo("duarte");
			userRepos.put("duarte", ur);
			
			ur = new UserRepo("eduardo");
			userRepos.put("eduardo", ur);
		}catch(Exception e){
			
		}
	}
	
	
	
	@Override
	public void createDoc(DocUserPair docUserPair)
			throws DocAlreadyExists_Exception {		
		if( docUserPair.getDocumentId().equals("reset") ){			
			userRepos.clear();
			populateDomain();
			println("Resetting");
			return;
		}		
		println("Creating Doc");
		if ( userRepos.containsKey( docUserPair.getUserId() )){
			userRepos.get(docUserPair.getUserId()).createDoc(docUserPair.getDocumentId());
		} else {			
			UserRepo ur = new UserRepo(docUserPair.getUserId());
			ur.createDoc(docUserPair.getDocumentId());
			userRepos.put(docUserPair.getUserId(), ur);			
		}		
	}

	@Override
	public List<String> listDocs(String userId)
			throws UserDoesNotExist_Exception {		
		println("Listing docs");
		if(userRepos.containsKey(userId)){
			return userRepos.get(userId).listDocs();
		}else{
			throw new UserDoesNotExist_Exception("The user '"+userId+"' does not exist", new UserDoesNotExist());
		}
	}
	
	

	@Override
	public void store(DocUserPair docUserPair, byte[] contents)
			throws CapacityExceeded_Exception, DocDoesNotExist_Exception,
			UserDoesNotExist_Exception {		
		println("Storing doc:"+docUserPair.getUserId()+","+docUserPair.getDocumentId());
		println("Contents:"+new String(contents));
		byte[] eRep = receiveFromHandler();
		if(userRepos.containsKey(docUserPair.getUserId())){			
			userRepos.get(docUserPair.getUserId()).storeDoc(docUserPair.getDocumentId(), contents);
		} else {			
			throw new UserDoesNotExist_Exception("The user '"+docUserPair.getUserId()+"' does not exist", new UserDoesNotExist());
		}		
		sendToHandler(eRep);
	}
	
	

	@Override
	public byte[] load(DocUserPair docUserPair)
			throws DocDoesNotExist_Exception, UserDoesNotExist_Exception {		
		println("Loading docs");
		byte[] eRep = receiveFromHandler();
		if(userRepos.containsKey(docUserPair.getUserId())){
			UserRepo r = userRepos.get(docUserPair.getUserId());
			MessageContext messageContext = webServiceContext.getMessageContext();			
			Tag version = r.getDocVersion(docUserPair.getDocumentId());
			messageContext.put(VersionHandler.VERSION_PROPERTY, version);
			String version_query = (String)messageContext.get(VersionHandler.VERSION_QUERY_PROPERTY);
			sendToHandler(eRep);
			if(version_query==null){
				return r.loadDoc(docUserPair.getDocumentId());
			}else{
				return new byte[0];
			}			
		}else{
			throw new UserDoesNotExist_Exception("The user '"+docUserPair.getUserId()+"' does not exist", null);
		}
	}
	
	private byte[] receiveFromHandler(){
		
		MessageContext messageContext = webServiceContext.getMessageContext();
		AESCipher aes = null;
		byte[] storeKey = DatatypeConverter.parseBase64Binary(_sStoreKey);
		
		String propertyValue = (String) messageContext.get(ResponseHandler.REQUEST_PROPERTY);
		byte[] bReq = DatatypeConverter.parseBase64Binary(propertyValue);
		
		WebServiceRequest req = null;
		
		try{
			req = WebServiceRequest.parse(bReq);
		} catch (Exception e) { 
			throw new SDStoreException("Error: parsing WebServiceRequest!", e);
		}
		
		byte[] eTicket = req.getEncryptedTicket();
		
		byte[] bTicket = null;
		Ticket t = null;
		
		try{
			aes = new AESCipher();
			bTicket = aes.decrypt(eTicket, storeKey);
			t = Ticket.parse(bTicket);
		} catch (Exception e) {
			throw new SDStoreException("Error: decrypting or parsing Ticket!", e);
		}

		if(!(t.getServicename().equals("SD-STORE"))){
			throw new SDStoreException("Error: Invalid Service Name!");
		}
		
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		
		Date currentTime = cal.getTime();
		
		String sFinalTime = t.getEndTime();
		Date finalTime = null;
		try{
			finalTime = df.parse(sFinalTime);
		} catch (Exception e) {
			throw new SDStoreException("Error: parsing Final Timestamp!", e);
		}
		
		if(finalTime.compareTo(currentTime) < 0){
			throw new SDStoreException("Error: Ticket Expired!");
		}
		
		byte[] sessionKey = t.getSessionKey();
		
		byte[] eAuth = req.getEncryptedAuthenticator();
		byte[] bAuth = null;
		Authenticator auth = null;
		
		try{
			bAuth = aes.decrypt(eAuth, sessionKey);
			auth = Authenticator.parse(bAuth);
		} catch (Exception e){
			throw new SDStoreException("Error: decrypting or parsing Authenticator!", e);
		}
		
		String req_time = auth.getRequestTime();
		Date reqTime = null;
		try{
			reqTime = df.parse(req_time);
		} catch (Exception e) {
			throw new SDStoreException("Error: parsing Request Timestamp!", e);
		}
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(reqTime);
		cal2.add(Calendar.SECOND, 4); //<-- Mudar se necessario
		Date compareTime = cal2.getTime();
		
		if(compareTime.compareTo(currentTime) < 0){
			throw new SDStoreException("Error: Request as too far away in the past!");
		}
		
		WebServiceResponse rep = new WebServiceResponse(req_time);
		byte[] bRep = rep.encode();
		
		byte[] eRep = null;
		try{
			eRep = aes.encrypt(bRep, sessionKey);
		} catch (Exception e) {
			throw new SDStoreException("Error: encrypting WebServiceReply!", e);
		}
		
		return eRep;
	}
	
	private void sendToHandler(byte[] eRep){
		
		MessageContext messageContext = webServiceContext.getMessageContext();		
		String sRep = DatatypeConverter.printBase64Binary(eRep);
		messageContext.put(ResponseHandler.RESPONSE_PROPERTY, sRep);
		
	}
	
	
}
