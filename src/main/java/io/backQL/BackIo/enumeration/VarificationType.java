package io.backQL.BackIo.enumeration;

public enum VarificationType {
    PASSWORD("PASSWORD"),
    ACCOUNT("ACCOUNT");

    private final String type;

    VarificationType(String type) {this.type = type;}

    public String getType(){
        return this.type.toLowerCase();
    }
}
