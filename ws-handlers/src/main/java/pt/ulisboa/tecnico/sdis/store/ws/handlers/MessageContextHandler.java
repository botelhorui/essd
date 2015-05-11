package pt.ulisboa.tecnico.sdis.store.ws.handlers;

import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import pt.ulisboa.tecnico.sdis.store.ws.util.MiniLogger;

/**
 * This SOAPHandler outputs the contents of the message context for inbound and
 * outbound messages.
 */
public class MessageContextHandler implements SOAPHandler<SOAPMessageContext> {

	public Set<QName> getHeaders() {
		return null;
	}

	public boolean handleMessage(SOAPMessageContext smc) {
		printMessageContext(smc);
		return true;
	}

	public boolean handleFault(SOAPMessageContext smc) {
		printMessageContext(smc);
		return true;
	}

	// nothing to clean up
	public void close(MessageContext messageContext) {
	}

	public void println(String s){
		System.out.println(MiniLogger.decorate(MessageContextHandler.class.getSimpleName(),s));
	}

	public void printf(String s, Object... args){
		System.out.printf(MiniLogger.decorate(MessageContextHandler.class.getSimpleName(),s), args);
	}

	private void printMessageContext(MessageContext map) {
		String out = "Message context: (scope,key,value)%n";
		try {
			java.util.Iterator it = map.keySet().iterator();
			while (it.hasNext()) {
				try{
					Object key = it.next();
					Object value = map.get(key);

					String keyString;
					if (key == null)
						keyString = "null";
					else
						keyString = key.toString();

					String valueString;
					if (value == null)
						valueString = "null";
					else
						valueString = value.toString();

					Object scope = map.getScope(keyString);
					String scopeString;
					if (scope == null)
						scopeString = "null";
					else
						scopeString = scope.toString();
					scopeString = scopeString.toLowerCase();

					out+="(" + scopeString + "," + keyString + ","
							+ valueString + ")%n";
				}catch(Exception e){
					out+="("+e.toString()+")";
				}
			}
		} catch (Exception e) {
			printf("Exception while printing message context: %s%n",e);
		}
		printf(out);
	}

}
