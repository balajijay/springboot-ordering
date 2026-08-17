
import java.util.*;
import java.time.*;
import java.math.*;

public class CustomSort {

	private List<Customer> customerList = new ArrayList<>();

	public static void main(String[] args) {
		CustomSort customer = new CustomSort();
		customer.buildCustomerList();
		customer.printList(customer.getList());
		customer.customerList.sort(new CustomerComparator());
		customer.printList(customer.getList());
	}

	public List<Customer> getList(){
		return customerList;
	}

	public void printList(List<Customer> list) {
		list.stream()
		.filter(item -> item.isActive)
		.forEach(item -> {
			System.out.println("Customer " + item.getName() + 
					" Credit Limit = " + item.getCreditLimit() + " created date = " + item.getCreatedDate());
		});
	}

	public LocalDate buildLocalDate (int year, int month, int day) {
		return LocalDate.of(year, month, day);
	}

	public void buildCustomerList() {
		Customer customer = new Customer (1, "Balaji", buildLocalDate(2024, 04, 11), new BigDecimal(500), true);
		customerList.add(customer);
		customer = new Customer (2, "Jayma", buildLocalDate(2023, 05, 18), new BigDecimal(300), true);
		customerList.add(customer);
		customer = new Customer (3, "Deeptha", buildLocalDate(2022, 11, 21), new BigDecimal(300), true);
		customerList.add(customer);
		customer = new Customer (4, "Mayura", buildLocalDate(2021, 11, 04), new BigDecimal(400), true);
		customerList.add(customer);
	}
}

class CustomerComparator implements Comparator<Customer> {

	@Override
	public int compare(Customer customer1, Customer customer2) {
		int compareCredLimit = customer1.getCreditLimit().compareTo(customer2.getCreditLimit());
		int compareCreatedDate = customer1.getCreatedDate().compareTo(customer2.getCreatedDate());
		if (compareCredLimit != 0) {
			return compareCredLimit;
		} 
		else if (compareCreatedDate != 0) {
			return compareCreatedDate;
		}
		return 0;
	}
}


class Customer {

	public long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public LocalDate getCreatedDate() {
		return createdDate;
	}
	public BigDecimal getCreditLimit() {
		return creditLimit;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public Customer(long id, String name, LocalDate createdDate, 
			BigDecimal creditLimit, Boolean isActive) {
		super();
		this.id = id;
		this.name = name;
		this.createdDate = createdDate;
		this.creditLimit = creditLimit;
		this.isActive = isActive;
	}

	long id;
	String name;
	LocalDate createdDate;
	BigDecimal creditLimit;
	Boolean isActive;
}
