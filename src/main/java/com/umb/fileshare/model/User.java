package com.umb.fileshare.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="first_name", nullable=false, unique=true)
    private String firstName;

    @Column(name="last_name", nullable=false, unique=true)
    private String lastName;

    @Column(nullable=false, unique=true, length=320)
    private String email;

    @Column(nullable=false, unique=true, length=50)
    private String username;

    @Column(nullable=false)
    @JsonIgnore
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(
        name="users_roles",
        joinColumns = {@JoinColumn(name="USER_ID", referencedColumnName="ID")},
        inverseJoinColumns = {@JoinColumn(name="ROLE_ID", referencedColumnName = "ID")}
    )

    private Set<Role> roles = new HashSet<>();

    @Column(nullable=true)
    private String details;

    public User(String firstName, String lastName, String email, String username, String password, Set<Role> roles, String details) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.details = details;
    }
}
