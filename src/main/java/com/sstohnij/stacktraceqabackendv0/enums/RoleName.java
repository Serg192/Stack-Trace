package com.sstohnij.stacktraceqabackendv0.enums;

public enum RoleName {
    ROLE_USER("type_user"),
    ROLE_ADMIN("type_admin");

    private final String str;

    RoleName(String str) {
        this.str = str;
    }

    public String str() {
        return str;
    }
}
