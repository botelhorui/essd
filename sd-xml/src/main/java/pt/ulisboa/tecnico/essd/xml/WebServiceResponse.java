package pt.ulisboa.tecnico.essd.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

public class WebServiceResponse {
	private static String FIELD_1 = "requestTime";

	private String requestTime;

	public WebServiceResponse(String requestTime) {
		this.requestTime = requestTime;
	}

	public static void main(String[] args) throws JDOMException, IOException {
		WebServiceResponse rar = new WebServiceResponse("08-08-2008 20:40:08");
		byte[] b = rar.encode();
		System.out.println(new String(rar.encode()));
		rar = parse(b);
		System.out.println(new String(rar.encode()));
	}
	
	public byte[] encode(){
		Document doc = new Document();
		Element root = new Element(this.getClass().getSimpleName());
		doc.setRootElement(root);
		
		Element f1 = new Element(FIELD_1);
		f1.setText(requestTime);
		root.addContent(f1);
		
		XMLOutputter xml = new XMLOutputter();		
		
		return xml.outputString(doc).getBytes();
	}
	
	public static WebServiceResponse parse(byte[] bXML) throws JDOMException, IOException{
		SAXBuilder builder = new SAXBuilder();
		InputStream stream = new ByteArrayInputStream(bXML);
		Document doc = builder.build(stream);
		
		Element root = doc.getRootElement();
		return new WebServiceResponse(
				root.getChildText(FIELD_1));
	}
	
	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

}
