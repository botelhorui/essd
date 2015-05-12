package pt.ulisboa.tecnico.essd.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TestReservedXML{
	
	@Test
	public void outputting(){
		/*
		 * Is being used solely as a 'main' for testing. Is still not a full-fledged test.
		 */
		
		ReservedXML test = new ReservedXML("SD-Testing", 7);
		test.generateXML();
		test.toOutput();
		
	}
}