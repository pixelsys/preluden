package cs.mandrill;

import static cs.mandrill.model.Direction.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import cs.mandrill.math.util.Precision;
import cs.mandrill.model.Order;
import cs.mandrill.model.Quote;

public class DefaultRfqServiceTest {

	private static final String TEST_CURRENCY = "USD";

	private LiveOrderBoard liveOrderBoard;
	private RfqService rfqService;

	@Before
	public void setUp() {
		liveOrderBoard = mock(LiveOrderBoard.class);
		when(liveOrderBoard.ordersFor(TEST_CURRENCY)).thenReturn(Arrays.asList(new Order[]{
				new Order(BUY,  232.71, TEST_CURRENCY, 200),
				new Order(SELL, 232.74, TEST_CURRENCY, 100),
				new Order(SELL, 232.73, TEST_CURRENCY, 200),
				new Order(BUY,  232.71, TEST_CURRENCY, 500),
				new Order(BUY,  232.70, TEST_CURRENCY, 100),
				new Order(SELL, 232.75, TEST_CURRENCY, 200),
				new Order(BUY,  232.69, TEST_CURRENCY, 500),
				new Order(SELL, 232.76, TEST_CURRENCY, 300),
				new Order(BUY,  232.70, TEST_CURRENCY, 200)
		}));
		rfqService = new DefaultRfqService(liveOrderBoard);
	}

	@Test
	public void testGetResponseWithResult1() {
		Optional<Quote> quoteOption = rfqService.quoteFor(TEST_CURRENCY, 200);
		assertTrue(true == quoteOption.isPresent());
		Quote quote = quoteOption.get();
		assertTrue(Precision.equals(232.69, quote.bid));
		assertTrue(Precision.equals(232.75, quote.ask));		
	}

	@Test
	public void testGetResponseNoResult() {
		Optional<Quote> quoteOption = rfqService.quoteFor(TEST_CURRENCY, 500);
		assertTrue(false == quoteOption.isPresent());
	}
	
	@Test
	public void testGetResponseWithResult2() {
		Optional<Quote> quoteOption = rfqService.quoteFor(TEST_CURRENCY, 100);
		assertTrue(true == quoteOption.isPresent());
		Quote quote = quoteOption.get();
		assertTrue(Precision.equals(232.68, quote.bid));
		assertTrue(Precision.equals(232.76, quote.ask));
	}
	
	@Test(expected=Exception.class)
	public void testInvalidCurrency() {
		rfqService.quoteFor(null, 500);
	}

	@Test(expected=Exception.class)
	public void testInvalidAmount() {
		rfqService.quoteFor(null, -1);
	}	
	
}
