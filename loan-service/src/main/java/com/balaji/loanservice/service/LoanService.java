package com.balaji.loanservice.service;

import com.balaji.loanservice.enums.LoanStatus;
import com.balaji.loanservice.model.DecisionResult;
import com.balaji.loanservice.model.LoanApplication;
import com.balaji.loanservice.rules.EligibilityRule;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class LoanService {
	
	private final List<EligibilityRule> rules;
	
	public LoanService (List<EligibilityRule> rules) {
		this.rules = rules.stream()
				.sorted(Comparator.comparingInt(EligibilityRule::getPriority))
				.collect(Collectors.toList());
	}
	
	public DecisionResult processLoanApplication(LoanApplication application ) {
		
		Optional<EligibilityRule> failedRule = rules.stream()
				.peek(rule -> rule.evaluate(application))
				.filter(rule -> !rule.isPassed()).findFirst();
		
		if (failedRule.isPresent()) {
			return new DecisionResult(application.getId(), LoanStatus.REJECTED, "");
		}
		
		return new DecisionResult(application.getId(), LoanStatus.APPROVED, "");
	}
}
