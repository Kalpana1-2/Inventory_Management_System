package com.userInventory.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import com.userInventory.repository.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer>{

}
