package org.skills.loanflow.entity.product;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;
import org.skills.loanflow.enums.BillingCycle;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 19/03/2025
 * Time: 22:10
 */
@Data
@Entity
@Table(name="products")
@SQLRestriction("active='1'")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String name;
    private int tenureDuration;
    @OneToOne()
    @JoinColumn(name = "tenure_duration_type_id")
    private TenureDurationTypeEntity tenureDurationTypeEntity;
    private int daysAfterDueForFeeApplication;
    private String disbursementType;
    private int disbursementIntervalInDays;
    @OneToMany(mappedBy = "productEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductFeeEntity> productFees = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private BillingCycle billingCycle;
    private Integer active=1;
    @CreationTimestamp
    private LocalDate dateCreated;
    private int createdBy;
    @UpdateTimestamp
    private LocalDate dateModified;
    private int modifiedBy;
}

