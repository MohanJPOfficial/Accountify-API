package com.mkdevelopers.accountify.bill.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Size(max = 255)
    @Column(name = "bill_entry_id", nullable = false)
    private String billEntryId;

    @Size(max = 255)
    @NotNull
    @Column(name = "user_id", nullable = false)
    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "bill_id")
    private BillEntity bill;

    @Size(max = 255)
    @NotNull
    @Column(name = "particular", nullable = false)
    private String particular;

    @NotNull
    @Lob
    @Column(name = "amount", nullable = false)
    private String amount;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Size(max = 255)
    @NotNull
    @Column(name = "entry_type", nullable = false)
    private String entryType;

    @Size(max = 255)
    @NotNull
    @Column(name = "return_date", nullable = false)
    private String returnDate;

    @NotNull
    @Lob
    @Column(name = "timestamp", nullable = false)
    private String timestamp;
}
