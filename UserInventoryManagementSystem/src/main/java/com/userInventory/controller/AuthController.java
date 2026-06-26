package com.userInventory.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.userInventory.dto.AuthResponseDTO;
import com.userInventory.dto.LoginDTO;
import com.userInventory.repository.entity.Users;
import com.userInventory.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@Tag(name = "User LogIn/Registration",description = "API for Register or LogIn User")
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins="*")
public class AuthController {
	
	private final AuthService authService;
	
	//LOGIN
	@Operation(summary = "User LogIn",description = "LogIn For Existing Users")
	@ApiResponses(value = {@ApiResponse( responseCode = "200",description = "LogIn Successfully"),
			@ApiResponse(responseCode = "404",description = "User Does Not Exist!")})
	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestBody LoginDTO loginDTO){
		try {
			AuthResponseDTO response=authService.login(loginDTO);
			return ResponseEntity.ok(response);
		}catch (RuntimeException e) {
			return ResponseEntity.status(401).body(e.getMessage());
		}	
	}
	
	//REGISTER
	@Operation(summary = "User Registration",description = "Registration For New User")
	@ApiResponses(value = {@ApiResponse( responseCode = "201",description = "User Register Successfully"),
			@ApiResponse(responseCode = "409",description = "User Already Exist!")})
	@PostMapping("/register")
	public ResponseEntity<Object> register(@RequestBody Users user){
		try {
			AuthResponseDTO response=authService.register(user);
			return ResponseEntity.status(201).body(response);
		}catch (RuntimeException e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}

	}

}
