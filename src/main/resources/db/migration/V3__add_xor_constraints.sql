# entry must reference either journal or ledger, but not both
alter table entry
    add constraint entry_parent_xor
        check ((journal_id IS NOT NULL) <> (ledger_id IS NOT NULL));

# bill must reference either ledger or business, but not both
alter table bill
    add constraint bill_parent_xor
        check ((ledger_id IS NOT NULL) <> (business_id IS NOT NULL));