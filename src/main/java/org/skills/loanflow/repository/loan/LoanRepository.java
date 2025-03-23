package org.skills.loanflow.repository.loan;

import org.skills.loanflow.entity.loan.LoanEntity;
import org.skills.loanflow.enums.LoanState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 21/03/2025
 * Time: 16:20
 */
@Repository
public interface LoanRepository extends JpaRepository<LoanEntity, Long>{
    List<LoanEntity> findByLoanOffer_Profile_Msisdn(String msisdn);
    List<LoanEntity> findByLoanIdIn(List<Long> loanIds);
    List<LoanEntity> findByLoanState(LoanState state);
}