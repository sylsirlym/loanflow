package org.skills.loanflow.repository;

import org.skills.loanflow.entity.FeeTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 20/03/2025
 * Time: 10:49
 */
@Repository
public interface FeeTypeRepository extends JpaRepository<FeeTypeEntity, Integer> {
}
