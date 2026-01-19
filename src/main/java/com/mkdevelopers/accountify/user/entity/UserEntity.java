package com.mkdevelopers.accountify.user.entity;

import com.mkdevelopers.accountify.business.entity.BusinessEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class UserEntity {
    @Id
    @Column(name = "user_id")
    private String userId;

    @NotNull
    @Column(name = "profile_name")
    private String profileName;

    @NotNull
    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "user")
    private Set<BusinessEntity> businesses = new LinkedHashSet<>();
}
