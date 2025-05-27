package model.user.manager;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import model.user.User;
import model.user.interfaces.Manager;

public class Employee extends User implements Manager{
	private DoubleProperty salary;

	public Employee(String name, String email, String phone, String address, String username, String password,
			double salary) {
		super(name, email, phone, address, username, password);
		this.salary = new SimpleDoubleProperty(salary);
	}
	
}
