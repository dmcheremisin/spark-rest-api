package info.cheremisin.rest.api.db.exceptions;

public class TransactionNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Transaction not found with id = ";

    public TransactionNotFoundException() {
    }

    public TransactionNotFoundException(Integer id) {
        super(MESSAGE + id);
    }

    public TransactionNotFoundException(Integer id, Throwable cause) {
        super(MESSAGE + id, cause);
    }
}
