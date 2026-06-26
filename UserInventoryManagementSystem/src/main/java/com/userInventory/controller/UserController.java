package com.userInventory.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.userInventory.dto.UserDTO;
import com.userInventory.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
@Tag(name = "User API",description = "API for viewing users")
@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	UserService user_service;
	
	@Operation(summary = "View All Users",description = "Return List of All Users")
	@ApiResponses(value = {@ApiResponse(responseCode = "200",description = "Users Found"),
	@ApiResponse(responseCode = "500",description = "Internal Server Error!")})
	//VIEW ALL USERS
	@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
	@GetMapping
	public ResponseEntity<List<UserDTO>> viewUsers(){
	return ResponseEntity.ok(user_service.viewUsers());
	}
	
	@Operation(summary ="Search User",description = "Searching User By ID")
	@ApiResponses(value = {@ApiResponse(responseCode = "200",description = "User Found"),
			@ApiResponse(responseCode = "404",description = "User Not Found!")})
	//SEARCH USER BY ID
	@GetMapping("/searchUser/{id}")
	public  ResponseEntity<Object> searchUser(@PathVariable int id){
		Optional<UserDTO> user=user_service.searchUser(id);
		if(user.isPresent()) {
			return ResponseEntity.ok(user.get());
		}
		return ResponseEntity.status(404).body("User Not Found!");

	}
}
