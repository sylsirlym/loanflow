package org.skills.loanflow.entity.loan;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 23/03/2025
 * Time: 12:31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "repayment_schedules")
@SQLRestriction("active='1'")
public class RepaymentScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long repaymentScheduleId;

    @ManyToOne
    @JoinColumn(name = "loan_id", nullable = false)
    private LoanEntity loan;

    private LocalDate dueDate;
    private BigDecimal installmentAmount;
    private boolean paid;
    private Integer active = 1;
    @CreationTimestamp
    private LocalDate dateCreated;
    private int createdBy;
    @UpdateTimestamp
    private LocalDate dateModified;
    private int modifiedBy;
}
