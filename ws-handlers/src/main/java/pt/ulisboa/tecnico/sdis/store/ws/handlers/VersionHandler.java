package pt.ulisboa.tecnico.sdis.store.ws.handlers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.MessageContext.Scope;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import pt.ulisboa.tecnico.sdis.store.ws.util.MiniLogger;

/**
 *  This SOAPHandler shows how to set/get values from headers in
 *  inbound/outbound SOAP messages.
 *
 *  A header is created in an outbound message and is read on an
 *  inbound message.
 *
 *  The value that is read from the header
 *  is placed in a SOAP message context property
 *  that can be accessed by other handlers or by the application.
 */
@SuppressWarnings("restriction")
public class VersionHandler implements SOAPHandler<SOAPMessageContext> {

	public static final String VERSION_PROPERTY = "version";
	public static final String VERSION_LOCAL_NAME = "version";
	public static final String VERSION_PREFIX = "v";
	public static final String VERSION_URI = "urn:version";
	
	public static final String VERSION_QUERY_PROPERTY = "version-only";
	public static final String VERSION_QUERY_LOCAL_NAME = "version-only";
	public static final String VERSION_QUERY_PREFIX = "v";
	public static final String VERSION_QUERY_URI = "urn:version";
	
	public static final String VERSION_CLIENT_ID = "clientId";
	public static final String VERSION_SEQ_N = "seqn";
	
	public void println(String s){
		System.out.println(MiniLogger.decorate(VersionHandler.class.getSimpleName(),s));
	}
	
	public void printf(String s, Object... args){
		System.out.printf(MiniLogger.decorate(VersionHandler.class.getSimpleName(),s), args);
	}
	
	//
	// Handler interface methods
	//
	public Set<QName> getHeaders() {
		return null;
	}

	private boolean isOperation(SOAPMessageContext smc,String op) throws SOAPException{
		SOAPMessage msg = smc.getMessage();
		SOAPPart sp = msg.getSOAPPart();
		SOAPEnvelope se = sp.getEnvelope();
		SOAPBody sb = se.getBody();
		Iterator it = sb.getChildElements();
		boolean found = false;
		while(it.hasNext()){
			SOAPElement element = (SOAPElement) it.next();
			if(element.getLocalName().equals(op)){
				found = true;
				break;
			}
		}        
		return found;
	}
	
	public boolean handleMessage(SOAPMessageContext smc) {
		println("Version Handler handling message.");

		Boolean outboundElement = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

		try {
			if (outboundElement.booleanValue()) {
				println("Message is outbound");
				if(isOperation(smc,"load")){
					
					String versionQuery = (String)smc.get(VERSION_QUERY_PROPERTY);
					if(versionQuery==null){
						println("Message is Load but didn't receive version query");
						return true;
					}
					// get SOAP envelope
					SOAPMessage msg = smc.getMessage();
					SOAPPart sp = msg.getSOAPPart();
					SOAPEnvelope se = sp.getEnvelope();
					// add header
					SOAPHeader sh = se.getHeader();
					if (sh == null)
						sh = se.addHeader();
					Name name = se.createName(VERSION_QUERY_LOCAL_NAME, VERSION_QUERY_PREFIX, VERSION_QUERY_URI);
					SOAPHeaderElement element = sh.addHeaderElement(name);

					// add header element value                
					println(String.format("Received version query %s",versionQuery));
					element.addTextNode(versionQuery);					
					
				}else if(isOperation(smc,"store") || isOperation(smc,"loadResponse")){
					Tag version = (Tag)smc.get(VERSION_PROPERTY);
					if(version==null){
						println("Didn't receive version");
						return true;
					}
					printf("Writing version(%s) header in outbound SOAP message...%n",version);
					// get SOAP envelope
					SOAPMessage msg = smc.getMessage();
					SOAPPart sp = msg.getSOAPPart();
					SOAPEnvelope se = sp.getEnvelope();
					// add header
					SOAPHeader sh = se.getHeader();
					if (sh == null)
						sh = se.addHeader();

					// add header element (name, namespace prefix, namespace)
					Name name = se.createName(VERSION_LOCAL_NAME, VERSION_PREFIX, VERSION_URI);
					SOAPHeaderElement element = sh.addHeaderElement(name);

					// add header element value                
					println(String.format("Received version %s",version));					
					element.addAttribute(se.createName(VERSION_CLIENT_ID), Integer.toString(version.getClientId()));
					element.addAttribute(se.createName(VERSION_SEQ_N), Integer.toString(version.getSeqn()));
				}else{
					println("Operation is neither readResponse or write");
					return true;
				}


			} else {
				// Inbound Message, Received Message
				println("Message is inbound");
				
				if(isOperation(smc, "load")){
					// the replica receives load message
					// Check if the load message only asks for version, telling the application that
					// get SOAP envelope header
					SOAPMessage msg = smc.getMessage();
					SOAPPart sp = msg.getSOAPPart();
					SOAPEnvelope se = sp.getEnvelope();
					SOAPHeader sh = se.getHeader();
					// check header
					if (sh == null) {
						println("Version query Header not found.");
						return true;
					}
					// get first header element
					Name name = se.createName(VERSION_QUERY_LOCAL_NAME, VERSION_QUERY_PREFIX, VERSION_QUERY_URI);
					Iterator it = sh.getChildElements(name);
					// check header element
					if (!it.hasNext()) {
						println("Version Header element not found.");
						return true;
					}
					SOAPElement element = (SOAPElement) it.next();

					// get header element value
					String version_query = element.getValue();					

					// print received header
					println("Query Version Header value is " + version_query);

					// put header in a property context
					smc.put(VERSION_QUERY_PROPERTY, version_query);
					// set property scope to application client/server class can access it
					smc.setScope(VERSION_QUERY_PROPERTY, Scope.APPLICATION);
					
				}else if(isOperation(smc,"loadResponse")||isOperation(smc,"store")){
					// the client receives loadResponse message
					// the replica receives store message
					println("Reading header in inbound SOAP message...");

					// get SOAP envelope header
					SOAPMessage msg = smc.getMessage();
					SOAPPart sp = msg.getSOAPPart();
					SOAPEnvelope se = sp.getEnvelope();
					SOAPHeader sh = se.getHeader();

					// check header
					if (sh == null) {
						println("Version Header not found.");
						return true;
					}

					// get first header element
					Name name = se.createName(VERSION_LOCAL_NAME, VERSION_PREFIX, VERSION_URI);
					Iterator it = sh.getChildElements(name);
					// check header element
					if (!it.hasNext()) {
						println("Version Header element not found.");
						return true;
					}
					SOAPElement element = (SOAPElement) it.next();

					int clientId = Integer.parseInt(element.getAttribute(VERSION_CLIENT_ID));
					int seqn = Integer.parseInt(element.getAttribute(VERSION_SEQ_N));
					// get header element value
					Tag version = new Tag(clientId,seqn);

					// print received header
					println("Version Header value is " + version);

					// put header in a property context
					smc.put(VERSION_PROPERTY, version);
					// set property scope to application client/server class can access it
					smc.setScope(VERSION_PROPERTY, Scope.APPLICATION);
				}else{
					println("Operation is neither readResponse or write");
					return true;
				}
			}
		} catch (Exception e) {
			println("Caught exception in handleMessage: ");
			println(e.toString());
			println("Continue normal processing...");
		}

		return true;
	}

	public boolean handleFault(SOAPMessageContext smc) {
		println("Ignoring fault message...");
		return true;
	}

	public void close(MessageContext messageContext) {
	}

}