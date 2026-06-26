package com.userInventory.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.userInventory.repository.entity.UserAddress;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Integer>{
	 Optional<UserAddress>

	    findByUser_UserId(int userId);

}
