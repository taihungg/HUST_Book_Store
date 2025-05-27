package model.user.customer;

import model.user.User;
import model.user.cart.Cart;

public class Customer extends User{
	private static int index = 0;
	private final String customerId;
	private String address;
	private final Cart cart;
	public Customer(String name, String email, String phone, String username, String password,
			String address) {
		super(name, email, phone, username, password);
		this.address = address;
		this.customerId = generateCustomerId();
		cart = new Cart();
	}
	private static String generateCustomerId() {
		String tempId = new String();
		if(index == 0 && index < 10) tempId = "C00" + (++index);
		else if(index >= 10 && index < 100) tempId = "C0" + (++index);
		else tempId = "C" + (++index);
		return tempId;
	}
	public String getCustomerId() {
		return customerId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Cart getCart() {
		return cart;
	}
}
