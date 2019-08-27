package info.cheremisin.rest.api.db.exceptions;

public class NotEnoughMoneyException extends RuntimeException {
    private static final String MESSAGE = "Not enough money on the account with id = ";

    public NotEnoughMoneyException() {
    }

    public NotEnoughMoneyException(Integer id) {
        super(MESSAGE + id);
    }

    public NotEnoughMoneyException(Integer id, Throwable cause) {
        super(MESSAGE + id, cause);
    }
}
