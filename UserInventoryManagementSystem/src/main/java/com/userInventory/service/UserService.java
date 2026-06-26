package com.userInventory.service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.userInventory.dto.UserDTO;
import com.userInventory.repository.UserRepository;
import com.userInventory.repository.entity.Users;
@Service
public class UserService {
	private final PasswordEncoder passwordEncoder;
	@Autowired
	UserRepository user_repo;
	public UserService(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	//ENTITY TO DTO
	private UserDTO convertToDTO(Users user) {
		UserDTO dto=new UserDTO();
		dto.setUserId(user.getUserId());
		dto.setUserName(user.getUserName());
		dto.setEmailId(user.getEmailId());
		dto.setPhoneNo(user.getPhoneNo());
		dto.setRole(user.getRole());
		return dto;
		
	}
	
	public List<UserDTO> viewUsers(){
		return user_repo.findAll().
				stream().map(this::convertToDTO).collect(Collectors.toList());
	}
	
	public Optional<UserDTO> searchUser(int id){
		return user_repo.findById(id).map(this::convertToDTO);
				}
	public Users registerUser(Users user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return user_repo.save(user);
	}

}
