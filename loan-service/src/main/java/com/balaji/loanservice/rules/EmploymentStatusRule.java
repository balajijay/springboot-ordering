package com.balaji.loanservice.rules;

import com.balaji.loanservice.model.LoanApplication;

public class EmploymentStatusRule implements EligibilityRule {

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public boolean isPassed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void evaluate(LoanApplication application) {
		// TODO Auto-generated method stub
		
	}

}
