package com.userInventory.repository.entity;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
public class PurchaseOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long poId;
	private LocalDate orderDate;
	private String status;
	private Double totalAmount;
	

}
