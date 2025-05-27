package model.user.manager;

import model.user.User;
import model.user.interfaces.Manager;

public class Admin extends User implements Manager{
	public Admin(String name, String email, String phone, String username, String password) {
		super(name, email, phone, username, password);
	}
}
