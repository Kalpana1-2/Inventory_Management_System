package com.userInventory.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.userInventory.service.DashboardService;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping
    public ResponseEntity<Map<String,Object>>
    getDashboard(){

        return ResponseEntity.ok(
                dashboardService.getDashboard());
    }
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/revenue")
    public ResponseEntity<Map<String,Object>>
    getRevenueReport(){

        return ResponseEntity.ok(
                dashboardService
                .getRevenueReport());
    }
}