package com.userInventory.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.userInventory.repository.entity.Users;
@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
	long count();
	Optional<Users> findByEmailId(String emailId);
    boolean existsByEmailId(String emailId);
}
