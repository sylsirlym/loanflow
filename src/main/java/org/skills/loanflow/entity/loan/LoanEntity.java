package org.skills.loanflow.entity.loan;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;
import org.skills.loanflow.entity.customer.LoanOfferEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 21/03/2025
 * Time: 13:26
 */

@Data
@Entity
@Table(name = "loans")
@SQLRestriction("active='1'")
public class LoanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId;

    @OneToOne
    @JoinColumn(name = "loan_offer_id", nullable = false)
    private LoanOfferEntity loanOffer;
    private BigDecimal principal;
    private BigDecimal netDisbursedAmount;
    private Date disbursementDate;
    private int gracePeriodInDays;
    private Date dueDate;
    private String loanState;
    private Integer active = 1;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    private int createdBy;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModified;
    private int modifiedBy;
}
