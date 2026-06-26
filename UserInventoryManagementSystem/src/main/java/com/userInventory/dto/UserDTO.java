package com.userInventory.dto;

import com.userInventory.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	private int userId;
	private String userName;
	private String emailId;
	private String phoneNo;
	private Role role;
	

}
