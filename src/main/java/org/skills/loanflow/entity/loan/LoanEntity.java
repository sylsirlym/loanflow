package org.skills.loanflow.entity.loan;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;
import org.skills.loanflow.entity.customer.LoanOfferEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
    private int gracePeriodInDays;
    private LocalDate dueDate;
    private String loanState;
    @OneToMany(mappedBy = "loanEntity", cascade = CascadeType.ALL)
    private List<DisbursementEntity> disbursements;
    private boolean isFullyDisbursed;
    private Integer active = 1;
    @CreationTimestamp
    private LocalDate dateCreated;
    private int createdBy;
    @UpdateTimestamp
    private LocalDate dateModified;
    private int modifiedBy;
}
