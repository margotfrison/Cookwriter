package me.margotfrison.cookwriter.exceptions;

/**
 * Root {@link Exception} for all exceptions thrown by this app
 */
public class CookwriterException extends Exception {
    public CookwriterException(String message) {
        super(message);
    }
    public CookwriterException(String message, Throwable cause) {
        super(message, cause);
    }
}
