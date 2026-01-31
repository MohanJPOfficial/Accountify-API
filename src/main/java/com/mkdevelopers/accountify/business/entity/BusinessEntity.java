package com.mkdevelopers.accountify.business.entity;

import com.mkdevelopers.accountify.bill.entity.BillEntity;
import com.mkdevelopers.accountify.journal.entity.JournalEntity;
import com.mkdevelopers.accountify.ledger.entity.LedgerEntity;
import com.mkdevelopers.accountify.user.entity.UserEntity;
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
@Table(name = "business")
public class BusinessEntity {
    @Id
    @Column(name = "business_id")
    private String businessId;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "business_name")
    private String businessName;

    @Column(name = "gst_no")
    private String gstNo;

    @Column(name = "location")
    private String location;

    @Column(name = "timestamp")
    private Long timestamp;

    @OneToMany(mappedBy = "business")
    private Set<BillEntity> bills = new LinkedHashSet<>();

    @OneToMany(mappedBy = "business")
    private Set<JournalEntity> journals = new LinkedHashSet<>();

    @OneToMany(mappedBy = "business")
    private Set<LedgerEntity> ledgers = new LinkedHashSet<>();
}
