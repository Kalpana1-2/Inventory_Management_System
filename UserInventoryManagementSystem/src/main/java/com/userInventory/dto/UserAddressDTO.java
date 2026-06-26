package com.userInventory.dto;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAddressDTO {
	private int userId;
	private String userName;
	private String houseNo;
	private String street;
	private String city;
	private String distt;
	private String state;
	private int pinCode;

}
