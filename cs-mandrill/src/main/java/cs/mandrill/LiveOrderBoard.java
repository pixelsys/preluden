package cs.mandrill;

import java.util.List;

import cs.mandrill.model.Order;

public interface LiveOrderBoard {
    List<Order> ordersFor(String currency);
}
