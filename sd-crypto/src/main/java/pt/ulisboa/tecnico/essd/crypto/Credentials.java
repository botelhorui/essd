package pt.ulisboa.tecnico.essd.crypto;

public class Credentials {
	
	private String username;
	private byte[] ticketKerberos, clientKey, encryptionKey;
	
	
	
	public Credentials(String username, byte[] ticketKerberos,
			byte[] clientKey, byte[] encryptionKey) {
		super();
		this.username = username;
		this.ticketKerberos = ticketKerberos;
		this.clientKey = clientKey;
		this.encryptionKey = encryptionKey;
	}



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public byte[] getTicketKerberos() {
		return ticketKerberos;
	}



	public void setTicketKerberos(byte[] ticketKerberos) {
		this.ticketKerberos = ticketKerberos;
	}



	public byte[] getClientKey() {
		return clientKey;
	}



	public void setClientKey(byte[] clientKey) {
		this.clientKey = clientKey;
	}



	public byte[] getEncryptionKey() {
		return encryptionKey;
	}



	public void setEncryptionKey(byte[] encryptionKey) {
		this.encryptionKey = encryptionKey;
	}
	
	
	
	

}
