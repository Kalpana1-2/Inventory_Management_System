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
import org.springframework.web.bind.annotation.RestController;

import com.userInventory.dto.OrderDTO;
import com.userInventory.dto.OrderStatusDTO;
import com.userInventory.dto.PlaceOrderDTO;
import com.userInventory.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
@Tag(name="Order API",description = "API for viewing orders")
@RestController
@RequestMapping("/orders")
public class OrderController {
	
	@Autowired
	OrderService order_service;
	
	@Operation(summary = "View All Orders",description = "Return List of All Orders")
	@ApiResponses(value = {@ApiResponse(responseCode = "200",description = "Orders Found"),
	@ApiResponse(responseCode = "500",description = "Internal Server Error!")})
	@PreAuthorize("hasAnyRole('ADMIN','MANAGER','VENDOR')")
	@GetMapping
	//VIEW ALL ORDERS
	public ResponseEntity<List<OrderDTO>> viewOrders() {
	return ResponseEntity.ok(order_service.viewOrders());			
	}
	
	@Operation(summary ="Search Order",description = "Searching Order By ID")
	@ApiResponses(value = {@ApiResponse(responseCode = "200",description = "Order Found"),
			@ApiResponse(responseCode = "404",description = "Order Not Found!")})
	@PreAuthorize("hasAnyRole('ADMIN','MANAGER','VENDOR')")
    @GetMapping("/searchOrder/{id}")
	//SEARCH ORDER BY ID
	public ResponseEntity<Object> searchOrder(@PathVariable int id)
	{
		Optional<OrderDTO>order=order_service.searchOrder(id);
		if(order.isPresent()) {
			return ResponseEntity.ok(order.get());}	
		return ResponseEntity.status(404).body("Order Not Found!");
}
	// Add these methods to existing Order_Controller

	// ── Get by Status ─────────────────────────────
	@PreAuthorize("hasAnyRole('ADMIN','MANAGER','VENDOR')")
	@GetMapping("/status/{status}")
	public ResponseEntity<List<OrderDTO>> getByStatus(
	        @PathVariable String status) {
	    return ResponseEntity.ok(
	           order_service.getOrdersByStatus(status));
	}

	// ── Get by User ───────────────────────────────
	@PreAuthorize("hasAnyRole('CUSTOMER','ADMIN','MANAGER')")
	@GetMapping("/user/{userId}")
	public ResponseEntity<List<OrderDTO>> getByUser(
	        @PathVariable int userId) {
	    return ResponseEntity.ok(
	           order_service.getOrdersByUser(userId));
	}

	// ── Order Report ──────────────────────────────
	@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
	@GetMapping("/report")
	public ResponseEntity<Map<String, Object>> getReport() {
	    return ResponseEntity.ok(
	           order_service.getOrderReport());
	}
	
	@PostMapping("/addOrder")
	public ResponseEntity<Object> addOrder(
	        @RequestBody OrderDTO dto){

	    try {

	        OrderDTO order =
	                order_service.saveOrder(dto);

	        return ResponseEntity
	                .status(201)
	                .body(order);

	    } catch (RuntimeException e) {

	        return ResponseEntity
	                .status(400)
	                .body(e.getMessage());

	    }

	}
	
	@PutMapping("/updateOrder/{id}")
	public ResponseEntity<Object> updateOrder(

	        @PathVariable int id,

	        @RequestBody OrderDTO dto){

	    try {

	        OrderDTO order =
	                order_service
	                .updateOrder(id,dto);

	        return ResponseEntity.ok(order);

	    }

	    catch(RuntimeException e){

	        return ResponseEntity
	                .status(404)
	                .body(e.getMessage());

	    }

	}
	@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
	@DeleteMapping("/deleteOrder/{id}")

	public ResponseEntity<Object>
	deleteOrder(

	@PathVariable int id){

	    try{

	        order_service.deleteOrder(id);

	        return ResponseEntity
	                .ok("Order Deleted Successfully");

	    }

	    catch(RuntimeException e){

	        return ResponseEntity
	                .status(404)
	                .body(e.getMessage());

	    }

	}
	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping("/placeOrder")
	public ResponseEntity<Object>
	placeOrder(
	@RequestBody PlaceOrderDTO dto){

	    try{

	        return ResponseEntity
	                .status(201)
	                .body(
	                order_service
	                .placeOrder(dto));

	    }

	    catch(RuntimeException e){

	        return ResponseEntity
	                .badRequest()
	                .body(
	                e.getMessage());

	    }

	}
	@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
	@PutMapping("/status/{orderId}")
	public ResponseEntity<Object>
	updateOrderStatus(
	        @PathVariable int orderId,
	        @RequestBody OrderStatusDTO dto){

	    try {

	        return ResponseEntity.ok(
	                order_service
	                .updateOrderStatus(
	                orderId,
	                dto));

	    }
	    catch(RuntimeException e){

	        return ResponseEntity
	                .badRequest()
	                .body(e.getMessage());
	    }
	}
	@PreAuthorize("hasAnyRole('ADMIN','CUSTOMER','MANAGER')")
	@PutMapping("/cancel/{orderId}")
	public ResponseEntity<Object>
	cancelOrder(@PathVariable int orderId){

	    try {

	        return ResponseEntity.ok(
	                order_service.cancelOrder(
	                orderId));

	    }
	    catch(RuntimeException e){

	        return ResponseEntity
	                .badRequest()
	                .body(e.getMessage());
	    }
	}
	
	}
