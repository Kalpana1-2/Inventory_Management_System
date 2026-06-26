package com.userInventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
	private String message;
	private String token;
	private String role;
	private int userId;
	private String userName;
	private String emailId;

}
