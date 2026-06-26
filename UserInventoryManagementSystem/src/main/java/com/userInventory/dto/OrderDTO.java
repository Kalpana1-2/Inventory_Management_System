package com.userInventory.dto;

import java.time.LocalDate;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
	private int orderId;
	private int userId;
	private String userName;
	private int quantity;
	private LocalDate orderDate;
	private String orderStatus;

}
