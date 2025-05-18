package maliszkiew.dev.twitter_clone.exception;


public class UserValidationException extends RuntimeException {
    public UserValidationException(String message) {
        super(message);
    }
}
