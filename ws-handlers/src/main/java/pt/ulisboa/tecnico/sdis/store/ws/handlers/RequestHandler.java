package pt.ulisboa.tecnico.sdis.store.ws.handlers;

import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
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
public class RequestHandler implements SOAPHandler<SOAPMessageContext> {

    public static final String REQUEST_PROPERTY = "my.request.property";
    public static final String RESPONSE_PROPERTY = "my.response.property";
    
    public static final String REQUEST_HEADER = "WebRequest";
    public static final String REQUEST_NS = "urn:question";
    public static final String REQUEST_PREFIX = "q";
    
    public static final String RESPONSE_HEADER = "WebResponse";
    public static final String RESPONSE_NS = "urn:answer";
    public static final String RESPONSE_PREFIX = "a";
    
    public static final String CLASS_NAME = RequestHandler.class.getSimpleName();

    //
    // Handler interface methods
    //
    public Set<QName> getHeaders() {
        return null;
    }
    
    public void println(String s){
		System.out.println(MiniLogger.decorate(RequestHandler.class.getSimpleName(),s));
	}
	
	public void printf(String s, Object... args){
		System.out.printf(MiniLogger.decorate(RequestHandler.class.getSimpleName(),s), args);
	}
    
    /*
     * Checks if the SOAPBody Element matches the given request name.
     */

    public boolean handleMessage(SOAPMessageContext smc) {
        println("RequestHandler: Handling message.");

        Boolean outboundElement = (Boolean) smc
                .get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        try {
            if (outboundElement.booleanValue()) {
                println("Writing header in outbound SOAP message...");
                
                String propertyValue = (String) smc.get(REQUEST_PROPERTY);
                printf("%s received '%s'%n", CLASS_NAME, propertyValue);

        		if(propertyValue==null){
					println("Message didn't get request property");
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

                // add header element (name, namespace prefix, namespace)
                Name name = se.createName(REQUEST_HEADER, REQUEST_PREFIX, REQUEST_NS);
                SOAPHeaderElement element = sh.addHeaderElement(name);

                // add header element value
              
                element.addTextNode(propertyValue);
                
                printf("%s put token '%s' on request message header%n", CLASS_NAME, propertyValue);

            } else {
                println("Reading header in inbound SOAP message...");
               
                // get SOAP envelope header
                SOAPMessage msg = smc.getMessage();
                SOAPPart sp = msg.getSOAPPart();
                SOAPEnvelope se = sp.getEnvelope();
                SOAPHeader sh = se.getHeader();

                // check header
                if (sh == null) {
                    println("Header not found.");
                    return true;
                }

                // get first header element
                Name name = se.createName(RESPONSE_HEADER, RESPONSE_PREFIX, RESPONSE_NS);
                Iterator it = sh.getChildElements(name);
                // check header element
                if (!it.hasNext()) {
                    println("Header element not found.");
                    return true;
                }
                SOAPElement element = (SOAPElement) it.next();

                // get header element value
                String valueString = element.getValue();

                // print received header
                println("Header value is " + valueString);

                // put header in a property context
                smc.put(RESPONSE_PROPERTY, valueString);
                // set property scope to application client/server class can access it
                smc.setScope(RESPONSE_PROPERTY, Scope.APPLICATION);
			
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