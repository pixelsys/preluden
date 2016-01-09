package bw.exercise;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.TreeMap;

import org.junit.Test;

public class ConcordanceTest {

	@Test
	public void testConcordanceGeneration() throws Exception {
		final String text = "Given an arbitrary text document written in English, write a program that will generate a" + System.lineSeparator()
					      + "concordance, i.e. an alphabetical list of all word occurrences, labeled with word frequencies." + System.lineSeparator()
					      + "Bonus: label each word with the sentence numbers in which each occurrence appeared.";
		Concordance c = new Concordance(new ByteArrayInputStream(text.getBytes()));
		TreeMap<String, List<Integer>> concordance = c.generate();
		assert(null != c);
		assert(34 == concordance.size());
		Concordance.print(System.out, concordance);
	}
	
}
