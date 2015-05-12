package pt.ulisboa.tecnico.essd.xml;

import org.w3c.dom.Document;

public class ReservedXML {
	
	private Document _xml;
	
	private String _service-name;
	private int _nounce;
	
	public ReservedXML (String service-name, int nounce){
		
		_service-name = service-name;
		_nounce = nounce;
		
	}
	
	public void generateXML(){
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
		Document doc = docBuilder.newDocument();
		Element root = doc.createElement("reserved");
		doc.appendChild(root);
		
		Element sn = doc.createElement("service-name");
		sn.appendChild(doc.createTextNode(_service-name));
		root.appendChild(sn);
		
		Element nounce = doc.createElement("nounce");
		nounce.appendChild(doc.createTextNode(Integer.toString(_nounce)));
		root.appendChild(nounce);
		
		_xml = doc;
		
	}
	
	public void toOutput(){
		
		
	}
	
	
}