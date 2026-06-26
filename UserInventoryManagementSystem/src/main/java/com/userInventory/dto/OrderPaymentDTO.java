package com.userInventory.dto;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderPaymentDTO {
	private int paymentId;
	private int order_id;
	private String paymentMethod;
	private String paymentStatus;
	private String transaction_id;
	private BigDecimal amountPaid;
	private LocalDateTime paymentDate;

}
