import java.io.*;
import java.util.*;
import java.util.stream.*;
import java.math.*;

public class TestOrderList {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<Order> orderList = new ArrayList<>();
		CustomTest customer1 = new CustomTest("Balaji");
		double amt1 = 10000.00;
		Order order1 = new Order(customer1, "created", amt1);
		orderList.add(order1);
		double amt2 = 12000.00;
		CustomTest customer2 = new CustomTest("Jayma");
		Order order2 = new Order(customer2, "created", amt2);
		orderList.add(order2);
		double amt3 = 15000.00;
		CustomTest customer3 = new CustomTest("Deepi");
		Order order3 = new Order(customer3, "Submitted", amt3);
		orderList.add(order3);	
		
		Map<String, Double> map = 
		orderList.stream().collect(Collectors.groupingBy(Order::getStatus, 
				Collectors.summingDouble(Order::getTotalAmount)));
		
		System.out.println(map);
		
	}

}

class Order implements Serializable
{
	private static final long serialVersionUID = -6358657732114529570L;
	
	long id;
	
	CustomTest customer;
	
	String status;
	
	double totalAmount;
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public CustomTest getCustomer() {
		return customer;
	}

	public void setCustomer(CustomTest customer) {
		this.customer = customer;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Order(CustomTest customer, String status, double totalAmount) {
		super();
		this.customer = customer;
		this.status = status;
		this.totalAmount = totalAmount;
	}

	@Override
	public String toString() {
		return "Order [customer=" + customer + ", status=" + status + ", totalAmount=" + totalAmount + "]";
	}	

}

class CustomTest {
	
	long Id;
	String name;
	
	public CustomTest(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Customer [name=" + name + "]";
	}

}
