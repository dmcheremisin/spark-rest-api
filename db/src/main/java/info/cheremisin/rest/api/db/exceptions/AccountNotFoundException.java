package info.cheremisin.rest.api.db.exceptions;

public class AccountNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Account not found with id = ";

    public AccountNotFoundException() {
    }

    public AccountNotFoundException(Integer id) {
        super(MESSAGE + id);
    }

    public AccountNotFoundException(Integer id, Throwable cause) {
        super(MESSAGE + id, cause);
    }
}
