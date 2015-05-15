package pt.ulisboa.tecnico.sdis.store.ws.impl;

import javax.jws.*;

@WebService
public interface StoreReset {

	@WebMethod void reset();
}
