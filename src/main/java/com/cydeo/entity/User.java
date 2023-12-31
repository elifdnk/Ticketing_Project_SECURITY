package com.cydeo.entity;

import com.cydeo.enums.Gender;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "users")
//@Where(clause = "is_deleted=false")
public class User extends BaseEntity {

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String userName;

    private String passWord;
    private boolean enabled;
    private String phone;

    @ManyToOne
    private Role role;

    @Enumerated(EnumType.STRING)
    private Gender gender;



}
