package com.userInventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.userInventory.repository.entity.OrderItems;
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItems, Integer>{
	List<OrderItems> findByOrder_OrderId(int orderId);
}
