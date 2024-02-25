package io.getarrays.BackIo.enumeration;

public enum EventType {
    LOGIN_ATTEMPT("You tried to log in"),
    LOGIN_ATTEMPT_FAILURE("You tried to log in and failed"),
    LOGIN_ATTEMPT_SUCCESS("You tried to log in and succeeded"),
    PROFILE_UPDATE("You updated your profile information"),
    PROFILE_PICTURE_UPDATE("You updated your profile picture"),
    ROLE_UPDATE("You updated your role and permission"),
    ACCOUNT_SETTINGS_UPDATE("You updated your account settings"),
    PASSWORD_UPDATE("You updated your password"),
    MFA_UPDATE("You updated your mfa settings");

    private final String description;

    EventType(String description) {
        this.description = description;
    }
    public String getDescription() {
        return this.description;
    }
}
