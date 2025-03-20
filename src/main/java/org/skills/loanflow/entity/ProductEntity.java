package org.skills.loanflow.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;


import java.math.BigDecimal;
import java.util.Date;

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
    private BigDecimal serviceFee;
    private BigDecimal dailyFee;
    private BigDecimal lateFee;
    private int daysAfterDueForFeeApplication;
    private int active=1;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    private int createdBy;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModified;
    private int modifiedBy;
}

