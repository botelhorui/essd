package pt.ulisboa.tecnico.essd.crypto;

public class Credentials {
	
	private String username;
	private byte[] ticketEncrypted;
	private byte[] sessionKey;
	private byte[] encryptionKey;
	
	public Credentials(String username, byte[] ticketEncrypted, byte[] sessionKey, byte[] encryptionKey) {
		this.username = username;
		this.ticketEncrypted = ticketEncrypted;
		this.sessionKey = sessionKey;
		this.encryptionKey = encryptionKey;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public byte[] getTicketEncrypted() {
		return ticketEncrypted;
	}

	public void setTicketEncrypted(byte[] ticketEncrypted) {
		this.ticketEncrypted = ticketEncrypted;
	}

	public byte[] getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(byte[] sessionKey) {
		this.sessionKey = sessionKey;
	}

	public byte[] getEncryptionKey() {
		return encryptionKey;
	}

	public void setEncryptionKey(byte[] encryptionKey) {
		this.encryptionKey = encryptionKey;
	}

}
