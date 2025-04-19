package me.margotfrison.cookwriter.exceptions;

public class CookwriterException extends Exception {
    public CookwriterException(String message) {
        super(message);
    }
    public CookwriterException(String message, Throwable cause) {
        super(message, cause);
    }
}
