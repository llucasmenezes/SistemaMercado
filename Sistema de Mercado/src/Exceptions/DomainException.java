package Exceptions;

import java.io.Serial;
import java.util.InputMismatchException;

public class DomainException extends InputMismatchException {

    @Serial
    private static final long serialVersionUID = 1L;


    public DomainException(String message) {
        super(message);
    }
}
