package org.skills.loanflow.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLRestriction;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 20/03/2025
 * Time: 09:42
 */
@Data
@Entity
@Table(name = "fee_types")
@SQLRestriction("active='1'")
public class FeeTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer feeTypeId;
    private String feeTypeName;
}
