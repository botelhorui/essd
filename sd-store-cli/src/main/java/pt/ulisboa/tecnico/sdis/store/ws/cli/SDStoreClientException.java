package pt.ulisboa.tecnico.sdis.store.ws.cli;


public class SDStoreClientException extends Exception {

    public SDStoreClientException() {
    }

    public SDStoreClientException(String message) {
        super(message);
    }

    public SDStoreClientException(Throwable cause) {
        super(cause);
    }

    public SDStoreClientException(String message, Throwable cause) {
        super(message, cause);
    }

}
