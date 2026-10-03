package com.mkdevelopers.accountify.common.validation;

import com.mkdevelopers.accountify.billentry.constant.EntryType;
import com.mkdevelopers.accountify.billentry.dto.CreateBillEntryRequest;
import com.mkdevelopers.accountify.billentry.dto.UpdateBillEntryRequest;
import com.mkdevelopers.accountify.entry.dto.JournalEntryRequest;
import com.mkdevelopers.accountify.entry.dto.UpdateLedgerEntryRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PositiveAmountValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void createBillEntryRejectsZeroAmountAndNegativeQuantity() {
        var request = new CreateBillEntryRequest();
        request.setBillEntryId("be1");
        request.setBillId("bill1");
        request.setParticular("Rice");
        request.setAmount(0L);
        request.setQuantity(-1);
        request.setEntryType(EntryType.SALES);
        request.setTimestamp(1L);

        assertEquals(
                Set.of("Amount must be greater than zero", "Quantity must be greater than zero"),
                messages(request));
    }

    @Test
    void updateBillEntryAllowsOmittedAmountAndQuantity() {
        assertTrue(validator.validate(new UpdateBillEntryRequest()).isEmpty());
    }

    @Test
    void journalEntryRejectsNegativeTransactionValue() {
        var request = new JournalEntryRequest();
        request.setEntryId("e1");
        request.setJournalId("j1");
        request.setDate("2026-01-01");
        request.setParticular("Cash");
        request.setParticularType("IN");
        request.setTransactionValue(-1L);
        request.setTimestamp(1L);

        assertEquals(Set.of("Transaction value must be greater than zero"), messages(request));
    }

    @Test
    void updateLedgerEntryAllowsOmittedTransactionValue() {
        assertTrue(validator.validate(new UpdateLedgerEntryRequest()).isEmpty());
    }

    private static Set<String> messages(Object request) {
        return validator.validate(request).stream()
                .map(violation -> violation.getMessage())
                .collect(Collectors.toSet());
    }
}
