package in.maitra.treats.invento.exception;

import lombok.Getter;

@Getter
public class InventoException extends Exception {
    private final Object[] args;

    public InventoException(String message) {
        super(message);
        this.args =  new Object[0];
    }

    public InventoException(Throwable cause) {
        super(cause);
        this.args =  new Object[0];
    }

    public InventoException(String message, Throwable cause) {
        super(message, cause);
        this.args =  new Object[0];
    }

    public InventoException(String message, Object... args) {
        super(message);
        this.args = args;
    }

    public InventoException(String message, Throwable cause, Object... args) {
        super(message, cause);
        this.args = args;
    }
}
