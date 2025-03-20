package org.skills.loanflow.entity.product;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLRestriction;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 19/03/2025
 * Time: 23:00
 */
@Data
@Entity
@Table(name="tenure_duration_types")
@SQLRestriction("active='1'")
public class TenureDurationTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tenureDurationTypeId;
    private String tenureDurationType;
}
