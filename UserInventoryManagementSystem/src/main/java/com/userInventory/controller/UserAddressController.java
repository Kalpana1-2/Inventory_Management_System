package com.userInventory.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.userInventory.dto.UserAddressDTO;
import com.userInventory.service.UserAddressService;

@RestController
@RequestMapping("/userAddress")

public class UserAddressController {

    @Autowired
    private UserAddressService service;

    // ADD ADDRESS
    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN','VENDOR','MANAGER')")
    @PostMapping("/add")
    public ResponseEntity<Object> add(@RequestBody UserAddressDTO dto) {

        try {
            return ResponseEntity
                    .status(201)
                    .body(service.addAddress(dto));

        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    // VIEW ALL
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping
    public ResponseEntity<List<UserAddressDTO>> viewAll() {
        return ResponseEntity.ok(service.viewAll());
    }

    // GET BY USER ID
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getByUser(@PathVariable int userId) {

        Optional<UserAddressDTO> addr = service.getByUserId(userId);

        if (addr.isPresent()) {
            return ResponseEntity.ok(addr.get());
        }

        return ResponseEntity.status(404)
                .body("Address not found");
    }

    // UPDATE
    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("/{userId}")
    public ResponseEntity<Object> update(
            @PathVariable int userId,
            @RequestBody UserAddressDTO dto) {

        try {
            return ResponseEntity.ok(
                    service.updateAddress(userId, dto));

        } catch (RuntimeException e) {
            return ResponseEntity.status(404)
                    .body(e.getMessage());
        }
    }

    // DELETE
    @PreAuthorize("hasRole('CUSTOMER')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> delete(@PathVariable int userId) {

        service.deleteAddress(userId);

        return ResponseEntity.ok("Address deleted successfully");
    }
}