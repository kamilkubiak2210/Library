package pl.javastart.library.Exceptions;

public class UserArleadyExistsException extends RuntimeException{
    public UserArleadyExistsException(String message) {
        super(message);
    }
}
