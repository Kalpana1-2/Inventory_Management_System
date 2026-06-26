package com.userInventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import com.userInventory.repository.entity.Orders;
@Repository
public interface OrderRepository extends JpaRepository<Orders,Integer>
{long count();

    // Get orders by status
    List<Orders> findByOrderStatus(String status);

    // Get orders by user
    List<Orders> findByUser_UserId(int userId);

    // Count orders by status
    long countByOrderStatus(String status);
}