package pt.ulisboa.tecnico.sdis.store.ws.handlers;

import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import pt.ulisboa.tecnico.sdis.store.ws.util.MiniLogger;

/**
 * This SOAPHandler outputs the contents of inbound and outbound messages.
 */
@SuppressWarnings("restriction")
public class LoggingHandler implements SOAPHandler<SOAPMessageContext> {

    public Set<QName> getHeaders() {
        return null;
    }

    public boolean handleMessage(SOAPMessageContext smc) {
        logToSystemOut(smc);
        return true;
    }

    public boolean handleFault(SOAPMessageContext smc) {
        logToSystemOut(smc);
        return true;
    }

    // nothing to clean up
    public void close(MessageContext messageContext) {
    }
    
	public void println(String s){
		System.out.println(MiniLogger.decorate(LoggingHandler.class.getSimpleName(),s));
	}
	
	public void printf(String s, Object... args){
		System.out.printf(MiniLogger.decorate(LoggingHandler.class.getSimpleName(),s), args);
	}

    /**
     * Check the MESSAGE_OUTBOUND_PROPERTY in the context to see if this is an
     * outgoing or incoming message. Write a brief message to the print stream
     * and output the message. The writeTo() method can throw SOAPException or
     * IOException
     */
    private void logToSystemOut(SOAPMessageContext smc) {
        Boolean outbound = (Boolean) smc
                .get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        if (outbound) {
            println("Outbound SOAP message:");
        } else {
            println("Inbound SOAP message:");
        }
        
        try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			StreamResult result = new StreamResult(new StringWriter());
			DOMSource source = new DOMSource(smc.getMessage().getSOAPPart());
			transformer.transform(source, result);
			String xmlString = result.getWriter().toString();
			printf("\n"+xmlString);			
		} catch (TransformerFactoryConfigurationError|TransformerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        /*
        SOAPMessage message = smc.getMessage();
        try {
            message.writeTo(System.out);
            System.out.println(); // just to add a newline to output
        } catch (Exception e) {
            System.out.printf(decorate("Exception in handler: %s%n"), e);
        }
        */
    }

}
