package pt.ulisboa.tecnico.sdis.store.ws.handlers;

public class Tag {
	private int clientId;
	private int seqn;
	
	public Tag(int clientId2, int seqn2) {
		clientId=clientId2;
		seqn=seqn2;
	}
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	public int getSeqn() {
		return seqn;
	}
	public void setSeqn(int seqn) {
		this.seqn = seqn;
	}
	
	public boolean greaterThan(Tag t){
		if(this.getSeqn()>t.getSeqn()){
			return true;
		}else if(this.getSeqn()==t.getSeqn()){
			return this.getClientId() > t.getClientId();
		}else{
			return false;
		}
	}
	
	@Override
	public String toString() {
		return String.format("<clientId=%d,seqn=%d>",clientId,seqn);
	}
}
