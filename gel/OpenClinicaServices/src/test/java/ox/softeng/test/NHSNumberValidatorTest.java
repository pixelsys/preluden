package ox.softeng.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ox.softeng.nhsnumservice.NHSNumberValidator;

public class NHSNumberValidatorTest {

	@Test
	public void validNHSNum_validates_the() {
		
		assertEquals(NHSNumberValidator.validNHSNum("1111111111"),true);
		assertEquals(NHSNumberValidator.validNHSNum("123"),false);
		assertEquals(NHSNumberValidator.validNHSNum("aaaaaaaaaaa"),false);
	}
	
}
