package com.willoacademy.core.security;

public enum Role {
    STUDENT("ROLE_STUDENT"),
    ADMIN("ROLE_ADMIN");

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }

    public static Role fromString(String value) {
        for (Role r : values()) {
            if (r.name().equalsIgnoreCase(value)) return r;
        }
        return STUDENT;
    }
}
