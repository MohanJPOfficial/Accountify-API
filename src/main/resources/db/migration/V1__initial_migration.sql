create table user
(
    user_id      varchar(255) not null
        primary key,
    profile_name varchar(255) not null,
    email        varchar(255) not null
);

create table business
(
    business_id   varchar(255) not null
        primary key,
    user_id       varchar(255) not null,
    business_name varchar(255) not null,
    gst_no        varchar(255) null,
    location      varchar(255) not null,
    timestamp     long         not null
);

create table journal
(
    journal_id  varchar(255) not null
        primary key,
    user_id     varchar(255) not null,
    business_id varchar(255) not null,
    timestamp   long         not null
);

create table ledger
(
    ledger_id     varchar(255) not null
        primary key,
    user_id       varchar(255) not null,
    business_id   varchar(255) not null,
    ledger_type   varchar(255) not null,
    business_name varchar(255) not null,
    gst_no        varchar(255) null,
    location      varchar(255) not null,
    timestamp     long         not null
);

create table bill
(
    bill_id       varchar(255) not null
        primary key,
    user_id       varchar(255) not null,
    business_id   varchar(255) null,
    ledger_id     varchar(255) null,
    bill_number   varchar(255) not null,
    bill_name     varchar(255) not null,
    date          varchar(255) not null,
    gst_no        varchar(255) null,
    location      varchar(255) not null,
    tax_rate      double       null,
    tax_type      varchar(255) null,
    state_code    varchar(255) null,
    timestamp     long         not null
);

create table entry
(
    entry_id           varchar(255) not null
        primary key,
    user_id            varchar(255) not null,
    journal_id         varchar(255) null,
    ledger_id          varchar(255) null,
    date               varchar(255) not null,
    particular         varchar(255) not null,
    particular_type    varchar(255) not null,
    transaction_value  long         not null,
    timestamp          long         not null
);

create table bill_entry
(
    bill_entry_id    varchar(255) not null
        primary key,
    user_id          varchar(255) not null,
    bill_id          varchar(255) null,
    particular       varchar(255) not null,
    amount           long         not null,
    quantity         int          not null,
    entry_type       varchar(255) not null,
    return_date      varchar(255) not null,
    timestamp        long         not null
);