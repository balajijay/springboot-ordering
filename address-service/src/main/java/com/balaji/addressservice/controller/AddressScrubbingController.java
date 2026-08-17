package com.balaji.addressservice.controller;

import com.balaji.addressservice.service.AddressScrubbingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.balaji.addressservice.dto.AddressRequestDto;
import com.balaji.addressservice.dto.AddressResponseDto;

@RestController
@RequestMapping("/api/v1/addresses")
public class AddressScrubbingController {

    private final AddressScrubbingService addressScrubbingService;
    
    public AddressScrubbingController(AddressScrubbingService addressScrubbingService) {
    	this.addressScrubbingService = addressScrubbingService;
    }

    // 🟢 1. SANITY CHECK: Open http://localhost:8084/api/v1/addresses/health in your browser
    @GetMapping("/health")
    public ResponseEntity<String> checkHealth() {
        return ResponseEntity.ok("🟢 Address Service is UP, running, and listening on Port 8084!");
    }

    // 🧼 2. VALIDATION CHECK: Receives a POST payload and calls your single service unit
    @PostMapping("/validate")
    public ResponseEntity<AddressResponseDto> validateAddress(@RequestBody AddressRequestDto request) {
        AddressResponseDto results = addressScrubbingService.scrubAndValidate(request);
        return ResponseEntity.ok(results);
    }
}
