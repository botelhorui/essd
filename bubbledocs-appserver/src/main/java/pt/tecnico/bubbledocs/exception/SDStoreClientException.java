package pt.tecnico.bubbledocs.exception;


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
