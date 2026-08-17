package com.balaji.loanservice.rules;

import com.balaji.loanservice.model.LoanApplication;

public interface EligibilityRule {
	
	public int getPriority();
	
	public boolean isPassed();
	
	 void evaluate(LoanApplication application);
}
