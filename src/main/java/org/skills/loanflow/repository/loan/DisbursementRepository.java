package org.skills.loanflow.repository.loan;

import org.skills.loanflow.entity.loan.DisbursementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 21/03/2025
 * Time: 16:23
 */
@Repository
public interface DisbursementRepository extends JpaRepository<DisbursementEntity, Long> {
    List<DisbursementEntity> findByScheduledDateAndIsDisbursedFalse(LocalDate date);
}
