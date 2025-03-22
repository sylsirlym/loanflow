package org.skills.loanflow.entity.customer;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 20/03/2025
 * Time: 21:49
 */
@DynamicUpdate
@Data
@Entity
@Table(name = "profiles")
@SQLRestriction("active='1'")
public class ProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;
    private String msisdn;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id" ,nullable = false)
    private CustomerEntity customer;
    private String pinHash;
    private int pinStatus;
    private BigDecimal creditScore;
    private Integer active = 1;

    @CreationTimestamp
    private LocalDate dateCreated;

    private int createdBy;

    @UpdateTimestamp
    private LocalDate dateModified;

    private int modifiedBy;

    @OneToMany(mappedBy = "profile",fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LoanOfferEntity> loanOffers = new ArrayList<>();
}
