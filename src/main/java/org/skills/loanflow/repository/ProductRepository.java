package org.skills.loanflow.repository;

import org.skills.loanflow.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 19/03/2025
 * Time: 22:08
 */
@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
