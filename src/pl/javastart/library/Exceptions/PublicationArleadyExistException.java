package pl.javastart.library.Exceptions;

public class PublicationArleadyExistException extends RuntimeException{
    public PublicationArleadyExistException(String message) {
        super(message);
    }
}
