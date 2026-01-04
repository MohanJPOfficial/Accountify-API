package com.mkdevelopers.accountify.journal.entity;

import com.mkdevelopers.accountify.business.entity.BusinessEntity;
import com.mkdevelopers.accountify.entry.entity.EntryEntity;
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
@Table(name = "journal")
public class JournalEntity {
    @Id
    @Column(name = "journal_id")
    private String journalId;

    @Column(name = "user_id")
    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "business_id")
    private BusinessEntity business;

    @Column(name = "timestamp")
    private String timestamp;

    @OneToMany(mappedBy = "journal")
    private Set<EntryEntity> entries = new LinkedHashSet<>();
}
