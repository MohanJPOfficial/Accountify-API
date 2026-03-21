package com.mkdevelopers.accountify.ledger.entity;

import com.mkdevelopers.accountify.bill.entity.BillEntity;
import com.mkdevelopers.accountify.business.entity.BusinessEntity;
import com.mkdevelopers.accountify.entry.entity.EntryEntity;
import com.mkdevelopers.accountify.ledger.enums.LedgerType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "ledger")
public class LedgerEntity {
    @Id
    @Column(name = "ledger_id")
    private String ledgerId;

    @Column(name = "user_id")
    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "business_id")
    private BusinessEntity business;

    @Enumerated(EnumType.STRING)
    @Column(name = "ledger_type")
    private LedgerType ledgerType;

    @Column(name = "business_name")
    private String businessName;

    @Column(name = "gst_no")
    private String gstNo;

    @Column(name = "location")
    private String location;

    @Column(name = "timestamp")
    private Long timestamp;

    @OneToMany(mappedBy = "ledger")
    private Set<BillEntity> bills = new LinkedHashSet<>();

    @OneToMany(mappedBy = "ledger")
    private Set<EntryEntity> entries = new LinkedHashSet<>();
}
