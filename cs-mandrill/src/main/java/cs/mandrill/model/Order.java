package cs.mandrill.model;

public class Order {
	public final Direction direction;
	public final double price;
	public final String currency;
	public final int amount;

	public Order(Direction direction, double price, String currency, int amount) {
		this.direction = direction;
		this.price = price;
		this.currency = currency;
		this.amount = amount;
	}
	
	public double getPrice() {
		return price;
	}
	
}
