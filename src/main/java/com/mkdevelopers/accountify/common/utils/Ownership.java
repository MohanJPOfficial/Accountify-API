package com.mkdevelopers.accountify.common.utils;

public final class Ownership {

    private Ownership() {
    }

    public static boolean denied(String ownerUserId) {
        return ownerUserId == null || !ownerUserId.equals(SecurityUtils.getCurrentUserId());
    }
}
