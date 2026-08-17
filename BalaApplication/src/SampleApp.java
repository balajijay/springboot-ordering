
import java.math.*;
import java.util.*;
import java.util.stream.*;

public class SampleApp {

	public static void main(String[] args) {
		List<Employee> employeeList = new ArrayList<>();
		BigDecimal salary = new BigDecimal(25000);
		for (long i=1; i<=10; i++) {
			StringBuilder sb = new StringBuilder("Department");
			if (i % 2 == 0) {
				sb.append("-IT");
			}
			else sb.append("-Finance");
			salary = salary.add(new BigDecimal(10000));
			Employee emp = new Employee(i,salary, sb.toString());
			employeeList.add(emp);
		}
		//printList(employeeList);
		
		// Highest Salary
		OptionalDouble maxSalary = employeeList.stream().mapToDouble(e-> e.getSalary().doubleValue()).max();
		System.out.println("Max salary = "+maxSalary.getAsDouble());
		// Second Highest Salary
		BigDecimal secondMaxSalary = employeeList.stream()
				.sorted(new EmployeeComparator())
				.skip(1)
				.findFirst().get().getSalary();
		System.out.println("Second Higest Salary is " + secondMaxSalary);
		// Average Salary
		OptionalDouble averageSalary = employeeList.stream().mapToDouble(e-> e.getSalary().doubleValue()).average();
		System.out.println("Average salary = "+ averageSalary.getAsDouble());
		

		// Count employees in each department
		Map<String, Long> employeeCountByDepartment = employeeList.stream().
				collect(Collectors.groupingBy(Employee::getDepartment, Collectors.counting()));
		System.out.println("employeeCountByDepartment " + employeeCountByDepartment);
		
		// Employees grouped by Department
        Map<String, List<Employee>> employeesByDept = employeeList.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment));
            System.out.println(employeesByDept);

	}
	
	public static void printList(List<Employee> list) {
		list.stream()
		.forEach(item -> {
			System.out.println("Employee " + item.getId() + 
					" Department = " + item.getDepartment() + " Salary = " + item.getSalary());
		});
	}
	
	
}

class EmployeeComparator implements Comparator<Employee> {
	@Override
	public int compare(Employee e1, Employee e2) {
		return e2.getSalary().compareTo(e1.getSalary());
	}
	
}

class Employee {

	public Employee(Long id, BigDecimal salary, String department) {
		this.id = id;
		this.salary = salary;
		this.department = department;
	}

	Long id;
	BigDecimal salary;
	String department;
	
	public Long getId() {
		return id;
	}
	public BigDecimal getSalary() {
		return salary;
	}

	public String getDepartment() {
		return this.department;
	}
	@Override
	public String toString() {
		return "Employee [id=" + id + ", salary=" + salary + ", department=" + department + "]";
	}
	
	

}