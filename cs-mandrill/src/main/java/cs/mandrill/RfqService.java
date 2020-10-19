package cs.mandrill;

import java.util.Optional;

import cs.mandrill.model.Quote;

public interface RfqService {
	Optional<Quote> quoteFor(String currency, int amount);
}
