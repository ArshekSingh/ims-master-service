package com.sas.ims.enums;

public enum Vendor {
    ACTIVE("A"), INACTIVE("I"), PENDING("P");

    public final String key;

    private Vendor(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public static String findByName(String name) {
        for (Vendor appStatus : values()) {
            if (appStatus.name().equals(name)) {
                return appStatus.getKey();
            }
        }
        return "";
    }
}
