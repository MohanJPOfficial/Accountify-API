# user -> business
alter table business
    add constraint business_user_user_id_fk
        foreign key (user_id) references user (user_id)
            on delete cascade;

# business -> journal
alter table journal
    add constraint journal_business_business_id_fk
        foreign key (business_id) references business (business_id)
            on delete cascade;

# journal -> entry
alter table entry
    add constraint entry_journal_journal_id_fk
        foreign key (journal_id) references journal (journal_id)
            on delete cascade;

# business -> ledger
alter table ledger
    add constraint ledger_business_business_id_fk
        foreign key (business_id) references business (business_id)
            on delete cascade;

# ledger -> entry
alter table entry
    add constraint entry_ledger_ledger_id_fk
        foreign key (ledger_id) references ledger (ledger_id)
            on delete cascade;

# ledger -> bill
alter table bill
    add constraint bill_ledger_ledger_id_fk
        foreign key (ledger_id) references ledger (ledger_id)
            on delete cascade;

# business -> bill
alter table bill
    add constraint bill_business_business_id_fk
        foreign key (business_id) references business (business_id)
            on delete cascade;

# bill -> bill_entry
alter table bill_entry
    add constraint bill_entry_bill_bill_id_fk
        foreign key (bill_id) references bill (bill_id)
            on delete cascade;