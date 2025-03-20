package org.skills.loanflow.repository.customer;

import org.skills.loanflow.entity.customer.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 20/03/2025
 * Time: 23:48
 */
@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity,Long> {
}
