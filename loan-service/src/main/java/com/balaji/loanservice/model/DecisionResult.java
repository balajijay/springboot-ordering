package com.balaji.loanservice.model;

import com.balaji.loanservice.enums.LoanStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class DecisionResult {
	
	private long applicationId;
    @Enumerated(EnumType.STRING)
	private LoanStatus status;
	private String reason;
	
	public DecisionResult() {}
	
	public DecisionResult(long applicationId, LoanStatus status, String reason) {
		super();
		this.applicationId = applicationId;
		this.status = status;
		this.reason = reason;
	}
	public long getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(long applicationId) {
		this.applicationId = applicationId;
	}
	public LoanStatus getStatus() {
		return status;
	}
	public void setStatus(LoanStatus status) {
		this.status = status;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	

}
