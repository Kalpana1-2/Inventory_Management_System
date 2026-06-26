package com.userInventory.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.userInventory.repository.entity.Product;
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	long count(); 
	// ✅ Pagination support
    Page<Product> findAll(Pageable pageable);

    // Search by name
    List<Product> findByProductNameContainingIgnoreCase(
        String name);

    // Filter by category
    List<Product> findByCategory_CategoryId(int categoryId);

    }
