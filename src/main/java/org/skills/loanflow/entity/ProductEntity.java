package org.skills.loanflow.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
import java.util.Date;
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
    @OneToMany(mappedBy = "productEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductFeeEntity> productFees = new ArrayList<>();
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

