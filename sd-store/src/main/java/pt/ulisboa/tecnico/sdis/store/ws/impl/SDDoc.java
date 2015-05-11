package pt.ulisboa.tecnico.sdis.store.ws.impl;

import pt.ulisboa.tecnico.sdis.store.ws.handlers.Tag;

public class SDDoc {
	private byte[] data;
	private Tag version;
	
	public SDDoc(){
		data = new byte[0];
		version = new Tag(0,0);
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public Tag getVersion() {
		return version;
	}
	public void setVersion(int Tag) {
		this.version = version;
	}
	
}
