package pt.ulisboa.tecnico.sdis.store.ws.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MiniLogger {	
	public static String decorate(String className, String s){	
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
		Date date = new Date();
		return String.format("[%s][%s][%s]", format.format(date),className,Thread.currentThread().getName())+s;
		
	}

}
