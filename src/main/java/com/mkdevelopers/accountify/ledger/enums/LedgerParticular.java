package com.mkdevelopers.accountify.ledger.enums;

import lombok.Getter;

import java.util.Map;
import java.util.Set;

@Getter
public enum LedgerParticular {

    BY_PURCHASE("By Purchase"),
    BY_CASH("By Cash"),
    TO_CASH("To Cash"),
    TO_RETURN("To Return"),
    BY_RETURN("By Return"),
    TO_SALES("To Sales");

    private final String displayName;

    LedgerParticular(String displayName) {
        this.displayName = displayName;
    }

    private static final Set<String> PURCHASE_PARTICULARS = Set.of(
            BY_PURCHASE.displayName,
            BY_CASH.displayName,
            TO_CASH.displayName,
            TO_RETURN.displayName);

    private static final Set<String> SALES_PARTICULARS = Set.of(
            BY_CASH.displayName,
            BY_RETURN.displayName,
            TO_SALES.displayName,
            TO_CASH.displayName);

    private static final Map<String, Set<String>> VALID_PARTICULARS = Map.of(
            "PURCHASE", PURCHASE_PARTICULARS,
            "SALES", SALES_PARTICULARS);

    public static Set<String> getValidParticulars(String ledgerType) {
        return VALID_PARTICULARS.getOrDefault(ledgerType, Set.of());
    }

    public static boolean isInvalidParticular(String ledgerType, String particular) {
        return !getValidParticulars(ledgerType).contains(particular);
    }
}
