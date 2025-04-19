package me.margotfrison.cookwriter.exceptions;

import lombok.Getter;
import me.margotfrison.cookwriter.utils.AndroidResources;

@Getter
public class InvalidQuantityFormatException extends CookwriterException {
    private final int errorDescription;
    private final String originalString;

    public InvalidQuantityFormatException(int errorDescription, String originalString, Throwable cause) {
        super(buildMessage(errorDescription, originalString), cause);
        this.errorDescription = errorDescription;
        this.originalString = originalString;
    }
    public InvalidQuantityFormatException(int errorDescription, String originalString) {
        super(buildMessage(errorDescription, originalString));
        this.errorDescription = errorDescription;
        this.originalString = originalString;
    }

    public static String buildMessage(int errorDescription, String originalString) {
        return String.format("Because : %s. String parsed : %s", AndroidResources.getString(errorDescription), originalString);
    }
}
