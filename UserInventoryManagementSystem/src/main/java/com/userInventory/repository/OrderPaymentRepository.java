package com.userInventory.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.userInventory.repository.entity.OrderPayment;

@Repository
public interface OrderPaymentRepository extends JpaRepository<OrderPayment, Integer>{
	List<OrderPayment>
    findByOrder_OrderId(int orderId);
	long countByPaymentStatus(String paymentStatus);

    @Query("""
            SELECT COALESCE(
            SUM(op.amountPaid),0)
            FROM OrderPayment op
            WHERE op.paymentStatus='SUCCESS'
            """)
    BigDecimal getTotalRevenue();

}
