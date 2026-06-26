package com.userInventory.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.userInventory.dto.OrderPaymentDTO;
import com.userInventory.service.OrderPaymentService;

@RestController
@RequestMapping("/orderPayment")
public class OrderPaymentController {
	@Autowired
	private OrderPaymentService paymentService;
	
	//ADD PAYMENT 
	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping("/add")
	public ResponseEntity<Object> addPayment(@RequestBody OrderPaymentDTO dto){
		try {
			 OrderPaymentDTO payment=

	                    paymentService

	                    .savePayment(dto);

	            return ResponseEntity

	                    .status(201)

	                    .body(payment);
		}catch (RuntimeException e) {
			return ResponseEntity

                    .status(400)

                    .body(e.getMessage());
		}
		
	}
	// VIEW ALL

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
	@GetMapping

    public ResponseEntity
    <List<OrderPaymentDTO>>

    viewPayments(){

        return ResponseEntity.ok(

                paymentService

                .viewPayments());

    }


    // SEARCH PAYMENT

    @GetMapping("/{id}")

    public ResponseEntity<Object>

    searchPayment(

            @PathVariable int id){

        Optional<OrderPaymentDTO>

        payment=

        paymentService

        .searchPayment(id);

        if(payment.isPresent()){

            return ResponseEntity

                    .ok(payment.get());

        }

        return ResponseEntity

                .status(404)

                .body("Payment Not Found");

    }


    // GET BY ORDER

    @GetMapping("/order/{orderId}")

    public ResponseEntity
    <List<OrderPaymentDTO>>

    getPaymentByOrder(

            @PathVariable int orderId){

        return ResponseEntity.ok(

                paymentService

                .getPaymentByOrder(orderId));

    }


    // DELETE

    @DeleteMapping("/{id}")

    public ResponseEntity<Object>

    deletePayment(

            @PathVariable int id){

        try{

            paymentService

            .deletePayment(id);

            return ResponseEntity

                    .ok(

                    "Payment Deleted Successfully");

        }

        catch(RuntimeException e){

            return ResponseEntity

                    .status(404)

                    .body(e.getMessage());

        }

    }

}
