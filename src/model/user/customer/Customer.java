package model.user.customer;

import model.user.User;
import model.user.cart.Cart;

public class Customer extends User{
	private String address;
	private final Cart cart;
	public Customer(String name, String email, String phone, String username, String password,
			String address) {
		super(name, email, phone, username, password);
		this.address = address;
		cart = new Cart();
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
