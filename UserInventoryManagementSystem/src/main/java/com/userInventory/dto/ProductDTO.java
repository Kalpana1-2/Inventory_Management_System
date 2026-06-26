package com.userInventory.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
	private int productId;
	 @NotBlank(message = "Product name is required")
	    @Size(max = 20, message = "Name must be max 20 characters")
	private String productName;
	 @Size(max = 255, message = "Description too long")
	private String description;
	 @NotNull(message = "Price is required")
	    @DecimalMin(value = "0.0",
	                inclusive = false,
	                message = "Price must be greater than 0")
	private BigDecimal price;
	private String imgUrl;
	private int categoryId;
	private String categoryType;
	@Min(value = 0, message = "Stock cannot be negative")

	private int stockQuantity;
	
	private Long vendorId;
	private String vendorName;

}
