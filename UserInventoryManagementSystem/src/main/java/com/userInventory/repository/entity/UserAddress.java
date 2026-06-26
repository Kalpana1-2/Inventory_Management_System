package com.userInventory.repository.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name="user_address")
public class UserAddress {
	@Id
	@Column(name="user_id")
	private int userID;
		
	@OneToOne
	@MapsId
	@JoinColumn(name="user_id")
	@JsonIgnore
	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
	private Users user;
	
	@Column(name="house_no")
	private String houseNo;

	@Column(name="street")
	private String street;

	@Column(name="city")
	private String city;
	
	@Column(name="distt")
	private String distt;
	
	@Column(name="state")
	private String state;
	
	@Column(name="pincode")
	private int pincode;

}
