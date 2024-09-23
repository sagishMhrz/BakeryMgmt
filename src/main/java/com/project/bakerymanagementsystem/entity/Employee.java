package com.project.bakerymanagementsystem.entity;


import com.project.bakerymanagementsystem.dto.PhoneNumber;
import com.project.bakerymanagementsystem.data.UserRole;
import com.project.bakerymanagementsystem.dto.Address;
import com.project.bakerymanagementsystem.dto.Name;
import lombok.Data;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "employees")
@Data
public class Employee implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EMPLOYEE_ID")
    private long employeeId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "firstName", column = @Column(name = "FIRST_NAME")),
            @AttributeOverride( name = "lastName", column = @Column(name = "LAST_NAME"))
    })
    private Name name;

    @NotNull
    @Column(name = "EMAIL")
    private String email;

    @ElementCollection
    private List<PhoneNumber> phoneNumbers = new ArrayList<>();

    @ElementCollection
    private List<Address> addressList = new ArrayList<>();

    @Column(name = "ROLE")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "SALARY")
    private double salary;

    @Column(name = "JOIN_DATE")
    private Date joinDate;

    @Column(name = "LAST_LOGIN")
    private Date lastLoginDate;

    @Column(name = "LAST_LOGOUT")
    private Date lastLogoutDate;

    @Column(name = "PASSWORD")
    private String password;
    private boolean isEnabled = false;

}
