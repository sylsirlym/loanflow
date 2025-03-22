package org.skills.loanflow.entity.customer;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 20/03/2025
 * Time: 21:42
 */
@Data
@Entity
@Table(name = "customers")
@SQLRestriction("active='1'")
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private Integer active = 1;

    @CreationTimestamp
    private LocalDate dateCreated;

    private int createdBy;

    @UpdateTimestamp
    private LocalDate dateModified;

    private int modifiedBy;
}
