package org.skills.loanflow.repository;

import org.skills.loanflow.entity.TenureDurationTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 19/03/2025
 * Time: 23:53
 */
@Repository
public interface TenureDurationTypesRepository extends JpaRepository<TenureDurationTypeEntity, Integer> {
}
