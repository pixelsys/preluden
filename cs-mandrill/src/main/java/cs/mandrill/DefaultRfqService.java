package cs.mandrill;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import cs.mandrill.model.Direction;
import cs.mandrill.model.Order;
import cs.mandrill.model.Quote;

public class DefaultRfqService implements RfqService {

    private static final Comparator<Order> BY_PRICE = Comparator.comparing(Order::getPrice);	
    private static final double COMISSION = 0.02; 
    private final LiveOrderBoard liveOrderBoard; 
    
    public DefaultRfqService(LiveOrderBoard liveOrderBoard) {
        if(null == liveOrderBoard)
        	throw new IllegalArgumentException("LiveOrderBoard can't be null");
        this.liveOrderBoard = liveOrderBoard;
    }

    public Optional<Quote> quoteFor(String currency, int amount) {
    	if(null == currency)
    		throw new IllegalArgumentException("Invalid currency");
    	if(0 >= amount) 
    		throw new IllegalArgumentException("Amount must be positive");
        List<Order> orders = liveOrderBoard.ordersFor(currency);
        if(null != orders && !orders.isEmpty()) {
        	Optional<Order> sell = getLowestSellOrder(orders, currency, amount);
        	Optional<Order> buy = getHighestBuyOrder(orders, currency, amount);
        	if (buy.isPresent() && sell.isPresent()) {
        		double bid = buy.get().price - COMISSION;
        		double ask = sell.get().price + COMISSION;
        		return Optional.of(new Quote(bid, ask));
        	}
        }
        return Optional.empty();
    }
    
    private static Stream<Order> filterOrders(List<Order> orders, String currency, Direction direction, int quantity) {
    	return orders.stream()
    				.filter(o -> direction == o.direction)
                    .filter(o -> currency.equals(o.currency))                    
                    .filter(o -> quantity == o.amount);
    }

	private static Optional<Order> getHighestBuyOrder(List<Order> orders, String currency, int quantity) {
	    return filterOrders(orders, currency, Direction.BUY, quantity).max(BY_PRICE);
	}
	
	private static Optional<Order> getLowestSellOrder(List<Order> orders, String currency, int quantity) {
	    return filterOrders(orders, currency, Direction.SELL, quantity).min(BY_PRICE);
	}    
    
}