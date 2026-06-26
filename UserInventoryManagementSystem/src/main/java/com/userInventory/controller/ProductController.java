package com.userInventory.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.userInventory.dto.ProductDTO;
import com.userInventory.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
@Tag(name="Product API",
description="APIs for managing products") 
@RestController
@RequestMapping("/products")
public class ProductController {
	@Autowired
	ProductService product_service;
	
	//GET PRODUCT BY PAGE 
	@GetMapping("/pages")
	@Operation(summary = "View Product Page",description = "List of Each 10 Product")
	@ApiResponses(value = {@ApiResponse( responseCode = "200",description = "Product Found"),
			@ApiResponse(responseCode = "404",description = "Product Not Found!")})
	public ResponseEntity<Map<String,Object>> getAllPages(@RequestParam(defaultValue = "0")  int page,
	        @RequestParam(defaultValue = "10") int size) {
		return ResponseEntity.ok(product_service.getAllPages(page,size));
	}
	
	//SEARCH BY NAME
	@Operation(summary = "Search Product By Name",description = "Returns Product Detail By ProductName")
	@ApiResponses(value = {@ApiResponse( responseCode = "200",description = "Product Found"),
			@ApiResponse(responseCode = "404",description = "Prodcut Not Found!")})
	@GetMapping("/searchByName")
	public ResponseEntity<List<ProductDTO>> searchByName(@RequestParam String name){
		return ResponseEntity.ok(product_service.searchByName(name));
	}
	
	//GET BY CATEGORY
	@Operation(summary = "Search Prodcut By CategoryID",description = "Returns List According to categoryId")
	@ApiResponses(value = {@ApiResponse( responseCode = "200",description = "Product Found"),
			@ApiResponse(responseCode = "404",description = "Product Not Found!")})
	@GetMapping("/category/{categoryId}")
	public ResponseEntity<List<ProductDTO>> getByCategory(@PathVariable int categoryId){
		return ResponseEntity.ok(product_service.getByCategory(categoryId));
	}
	
	//VIEW ALL PRODUCTS
	@Operation(summary = "View All Products",description = "Returns list of all products")
	@ApiResponses(value = {@ApiResponse(responseCode = "200",description = "Product Found" ),
	@ApiResponse(responseCode = "500",description = "Internal Server Error!")})
	@GetMapping
	public ResponseEntity<List<ProductDTO>> viewProducts(){
		return ResponseEntity.ok(product_service.viewProducts());
	}
	
	//CREATE NEW PRODUCT
	@Operation(summary = "Create New Product",description = "Creating New Product")
	@ApiResponses(value = {@ApiResponse( responseCode = "201",description = "Successfully Created The Product"),
			@ApiResponse(responseCode = "400",description = "Invalid User Input!")
	})
	@PreAuthorize("hasAnyRole('ADMIN','VENDOR','MANAGER')")
	@PostMapping
	public ResponseEntity<Object> createproduct(@Valid @RequestBody ProductDTO productDTO){
		return ResponseEntity.status(201).body(product_service.createProduct(productDTO));
	}
	
	//SEARCH PRODUCT VIEW ID	
	@Operation(summary = "Search Product",description = "Searching Product By ID")
	@ApiResponses(value= {@ApiResponse( responseCode = "200",description = "Product Found"),
			@ApiResponse(responseCode = "404",description = "Product Not Found!")
	})
	@GetMapping("/{id}")
	public ResponseEntity<Object> searchProduct(@PathVariable int id){
		Optional<ProductDTO>product=product_service.searchProduct(id);
		if(product.isPresent()) {
			return ResponseEntity.ok(product.get());		}
		return ResponseEntity.status(404).body("Product Not Found!");
	}

	//UPDATE PRODUCT BY GETTING ID 
	@Operation(summary = "Update Product",description = "Updating Product By ID")
	@ApiResponses(value = {@ApiResponse(responseCode =  "200",description = "Product Updated Successfully"),
			@ApiResponse(responseCode = "404",description = "Product Not Found!")})
	@PreAuthorize("hasAnyRole('ADMIN','MANAGER','VENDOR')")
	@PutMapping("/{id}")
	public ResponseEntity<Object> updateProduct(@PathVariable int id,@Valid @RequestBody ProductDTO productDTO){
		Optional<ProductDTO>update=product_service.updateProduct(id, productDTO);
		if(update.isPresent()) {
			return ResponseEntity.ok(update.get());		}
		return ResponseEntity.status(404).body("Product Not Found With ID:"+id);
	}
	
	//DELETE PRODUCT  BY ID
	@Operation(summary = "Delete Product",description = "Deleting Product By ID")
	@ApiResponses(value = {@ApiResponse( responseCode = "200",description = "Product Deleted Successfully"),
	@ApiResponse(responseCode = "404",description = "Product Not Found!")})
	@PreAuthorize("hasAnyRole('ADMIN','MANAGER','VENDOR')")
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable int id){
		String result=product_service.deleteProduct(id);
		if(result.equals("Product Deleted Successfully!")) {
			return ResponseEntity.ok(result);
		}
		return ResponseEntity.status(404).body(result);
	}

}
