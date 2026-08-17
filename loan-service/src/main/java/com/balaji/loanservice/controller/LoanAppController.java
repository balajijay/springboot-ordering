package com.balaji.loanservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.balaji.loanservice.model.DecisionResult;
import com.balaji.loanservice.model.LoanApplication;
import com.balaji.loanservice.service.LoanService;

@RestController
@RequestMapping("/api/loan")
public class LoanAppController {
	
	private final LoanService loanService;
	
	public LoanAppController (LoanService loanService) {
		this.loanService = loanService;
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable("id") long id) {
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@PostMapping
	public ResponseEntity<?> processLoanApplication(@RequestBody LoanApplication appliction) {
		DecisionResult result = loanService.processLoanApplication(appliction);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

}
