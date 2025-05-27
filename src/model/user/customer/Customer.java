package model.user.customer;

import model.user.User;
import model.user.cart.Cart;

public class Customer extends User{
	private String address;
	private Cart cart;
	public Customer(String name, String email, String phone, String address, String username, String password,
			String address2) {
		super(name, email, phone, address, username, password);
		this.address = address2;
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
	public void setCart(Cart cart) {
		this.cart = cart;
	}
}
