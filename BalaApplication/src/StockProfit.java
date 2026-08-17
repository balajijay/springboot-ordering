
public class StockProfit {
	/*
	 Given an array of stock prices by day, find the maximum profit from buying once and selling once later."
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] stockPrices = {700, 1200, 900, 1350, 1500, 600};
		int profit = maximumProfit(stockPrices);
		System.out.println(profit);
	}
	
	public static int maximumProfit(int[] stockPrices) {
		int minPrice = stockPrices[0];
	    int maxProfit = 0;
	    for (int i = 1; i < stockPrices.length; i++) {
	        maxProfit = Math.max(maxProfit, stockPrices[i] - minPrice); 
	        minPrice = Math.min(minPrice, stockPrices[i]);
	    }
	    return maxProfit;
	}

}
