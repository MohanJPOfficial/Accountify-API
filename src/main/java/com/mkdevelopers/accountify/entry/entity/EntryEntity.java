package com.mkdevelopers.accountify.entry.entity;

import com.mkdevelopers.accountify.journal.entity.JournalEntity;
import com.mkdevelopers.accountify.ledger.entity.LedgerEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "entry")
public class EntryEntity {
    @Id
    @Column(name = "entry_id")
    private String entryId;

    @Column(name = "user_id")
    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "journal_id")
    private JournalEntity journal;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "ledger_id")
    private LedgerEntity ledger;

    @Column(name = "date")
    private String date;

    @Column(name = "particular")
    private String particular;

    @Column(name = "particular_type")
    private String particularType;

    @Column(name = "transaction_value")
    private String transactionValue;

    @Column(name = "timestamp")
    private String timestamp;
}
