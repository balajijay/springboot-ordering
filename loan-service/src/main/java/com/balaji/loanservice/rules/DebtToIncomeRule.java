package com.balaji.loanservice.rules;

import com.balaji.loanservice.model.LoanApplication;
import java.math.*;

public class DebtToIncomeRule implements EligibilityRule {
	
	boolean passed = false;
	BigDecimal debtRatio = new BigDecimal(0.40);
	
	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public boolean isPassed() {
		return this.passed;
	}

	@Override
	public void evaluate(LoanApplication application) {
		BigDecimal customerDebtRatio = new BigDecimal(application.getTotalDebt() / application.getTotalIncome());
		this.passed =  (customerDebtRatio.compareTo(debtRatio)) <= 1;
	}
}
