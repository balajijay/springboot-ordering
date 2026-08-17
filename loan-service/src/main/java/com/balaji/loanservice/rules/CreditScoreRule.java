package com.balaji.loanservice.rules;

import com.balaji.loanservice.model.LoanApplication;

public class CreditScoreRule implements EligibilityRule {

	private final int minimumCreditScore = 620;
	private boolean passed = false;
	
	@Override
	public int getPriority() {
		return 1;
	}

	@Override
	public boolean isPassed() {
		return passed;
	}

	@Override
	public void evaluate(LoanApplication application) {
			this.passed = (application.getCreditScore() < minimumCreditScore);
	}

}
