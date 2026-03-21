package com.mkdevelopers.accountify.billentry.entity;

import com.mkdevelopers.accountify.bill.entity.BillEntity;
import com.mkdevelopers.accountify.billentry.constant.EntryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "bill_entry")
public class BillEntryEntity {
    @Id
    @Column(name = "bill_entry_id")
    private String billEntryId;

    @Column(name = "user_id")
    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "bill_id")
    private BillEntity bill;

    @Column(name = "particular")
    private String particular;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "quantity")
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "entry_type")
    private EntryType entryType;

    @Column(name = "return_date")
    private String returnDate;

    @Column(name = "timestamp")
    private Long timestamp;
}
