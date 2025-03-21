package org.skills.loanflow.repository.loan;

import org.skills.loanflow.entity.loan.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 21/03/2025
 * Time: 16:20
 */

public interface LoanRepository extends JpaRepository<LoanEntity, Long>{
}
