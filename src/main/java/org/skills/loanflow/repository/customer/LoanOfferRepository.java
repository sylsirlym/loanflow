package org.skills.loanflow.repository.customer;

import org.skills.loanflow.entity.customer.LoanOfferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 20/03/2025
 * Time: 23:41
 */
@Repository
public interface LoanOfferRepository extends JpaRepository<LoanOfferEntity, Long> {
}
