package org.skills.loanflow.entity.loan;

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
 * Date: 21/03/2025
 * Time: 16:04
 */
@Data
@Entity
@Table(name = "disbursements")
@SQLRestriction("active='1'")
public class DisbursementEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long disbursementId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id", nullable = false)
    private LoanEntity loanEntity;
    private BigDecimal amount;
    private Integer active=1;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    private int createdBy;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModified;
    private int modifiedBy;
}
