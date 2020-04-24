package me.Stefan923.SuperVotes.Commands.Exceptions;

public class MissingPermissionException extends Exception {
    public MissingPermissionException() {
        super("");
    }

    public MissingPermissionException(final String string) {
        super(string);
    }
}
