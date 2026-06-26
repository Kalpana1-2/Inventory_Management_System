package com.userInventory.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.userInventory.configuration.JwtUtil;
import com.userInventory.dto.AuthResponseDTO;
import com.userInventory.dto.LoginDTO;
import com.userInventory.enums.Role;
import com.userInventory.repository.UserRepository;
import com.userInventory.repository.entity.Users;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Service
public class AuthService {
	private final UserRepository userRepo;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	
	//REGISTER
	public AuthResponseDTO register(Users user) {
		if(userRepo.existsByEmailId(user.getEmailId())) {
			throw new RuntimeException("Email Already Exist!");
		} 
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		if(user.getRole()==null) {
			user.setRole(Role.CUSTOMER);
		}
		if(user.getRole()==Role.ADMIN){

		    throw new RuntimeException(
		            "Admin Registration Not Allowed");

		}
		Users saved=userRepo.save(user);
		
		String token = jwtUtil.generateToken(
	            saved.getEmailId(),
	            saved.getRole(),
	            saved.getUserId()
	        );
		
		return new AuthResponseDTO("Registration Successful",token,saved.getRole().name(),saved.getUserId(),saved.getUserName(),saved.getEmailId());
	}
	
	//LOGIN
	public AuthResponseDTO login(LoginDTO loginDTO) {
		Users user=userRepo
				.findByEmailId(loginDTO.getEmailId())
				.orElseThrow(() -> 
				new RuntimeException("User Not Found!"));
		
		if(!passwordEncoder.matches(loginDTO.getPassword(),user.getPassword())) {
			throw new RuntimeException("Invalid Password!");
		}
		String token = jwtUtil.generateToken(
	            user.getEmailId(),
	            user.getRole(),
	            user.getUserId()
	        );
		
		return new AuthResponseDTO("Login Successfully",token,user.getRole().name(),user.getUserId(),user.getUserName(),user.getEmailId());
	}

}
