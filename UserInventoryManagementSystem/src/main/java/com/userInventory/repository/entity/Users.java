package com.userInventory.repository.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.userInventory.enums.Role;

import jakarta.persistence.*;
import lombok.*;
@Setter
@Getter

@Entity

@Table(name="users")

public class Users {
	
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name="user_id")
private int userId;

@Column(name="user_name",length=25)
private String userName;

@Column(name="email_id",nullable=false,unique=true)
private String emailId;

@Column(name="password",nullable=false)
private String password;

@Column(name="phone_no",length=10)
private String phoneNo;

@Enumerated(EnumType.STRING)
@Column(name="role")
private Role role;

@OneToOne(mappedBy = "user",cascade = CascadeType.ALL,fetch=FetchType.LAZY)
@JsonIgnore
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
private UserAddress address;

@OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch=FetchType.LAZY)
@JsonIgnore
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
private List<Orders> orders;


}
