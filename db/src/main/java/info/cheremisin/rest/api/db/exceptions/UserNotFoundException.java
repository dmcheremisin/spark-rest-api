package info.cheremisin.rest.api.db.exceptions;

public class UserNotFoundException extends RuntimeException {
    private static final String MESSAGE = "User not found with id = ";

    public UserNotFoundException() {
    }

    public UserNotFoundException(Integer id) {
        super(MESSAGE + id);
    }

    public UserNotFoundException(Integer id, Throwable cause) {
        super(MESSAGE + id, cause);
    }
}
