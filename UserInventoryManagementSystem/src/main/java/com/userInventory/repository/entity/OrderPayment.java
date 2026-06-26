package com.userInventory.repository.entity;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@Entity
@Table(name="order_payment")
public class OrderPayment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="payment_id")
	private int paymentId;
	
	@ManyToOne
	@JoinColumn(name="orderId")
	@JsonIgnore
	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
	private Orders order;
	
	@Column(name="payment_method")
	private String paymentMethod;
	
	@Column(name="payment_status")
	private String paymentStatus;
	
	@Column(name="transaction_id",length=255)
	private String transactionId;
	
	@Column(name="amount_paid",precision = 10,scale = 2)
	private BigDecimal amountPaid;
	
	@Column(name="payment_date")
	private LocalDateTime paymentDate;

}
