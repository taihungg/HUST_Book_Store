package model.user.manager;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import model.user.User;
import model.user.interfaces.Manager;

public class Employee extends User implements Manager{
	private DoubleProperty salary;

	public Employee(String name, String email, String phone, String username, String password, double salary) {
		super(name, email, phone, username, password);
		this.salary = new SimpleDoubleProperty(salary);
	}

	public final DoubleProperty salaryProperty() {
		return this.salary;
	}
	
	public final double getSalary() {
		return this.salaryProperty().get();
	}

	public final void setSalary(final double salary) {
		this.salaryProperty().set(salary);
	}
}
