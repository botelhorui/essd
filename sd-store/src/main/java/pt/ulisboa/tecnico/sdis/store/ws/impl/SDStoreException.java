package pt.ulisboa.tecnico.sdis.store.ws.impl;


public class SDStoreException extends RuntimeException {

    public SDStoreException() {
    }

    public SDStoreException(String message) {
        super(message);
    }

    public SDStoreException(Throwable cause) {
        super(cause);
    }

    public SDStoreException(String message, Throwable cause) {
        super(message, cause);
    }

}
