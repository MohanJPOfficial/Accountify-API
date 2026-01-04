package com.mkdevelopers.accountify.bill.entity;

import com.mkdevelopers.accountify.business.entity.BusinessEntity;
import com.mkdevelopers.accountify.ledger.entity.LedgerEntity;
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
@Table(name = "bill")
public class BillEntity {
    @Id
    @Column(name = "bill_id")
    private String billId;

    @Column(name = "user_id")
    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "business_id")
    private BusinessEntity business;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "ledger_id")
    private LedgerEntity ledger;

    @Column(name = "bill_number")
    private String billNumber;

    @Column(name = "bill_name")
    private String billName;

    @Column(name = "date")
    private String date;

    @Column(name = "gst_no")
    private String gstNo;

    @Column(name = "location")
    private String location;

    @Column(name = "tax_rate")
    private Double taxRate;

    @Column(name = "tax_type")
    private String taxType;

    @Column(name = "state_code")
    private String stateCode;

    @Column(name = "timestamp")
    private String timestamp;

    @OneToMany(mappedBy = "bill")
    private Set<BillEntryEntity> billEntries = new LinkedHashSet<>();
}
