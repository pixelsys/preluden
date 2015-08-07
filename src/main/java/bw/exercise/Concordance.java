package bw.exercise;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.TreeMap;

public class Concordance {
	
	final InputStream is;
	
	public Concordance(InputStream is) {
		this.is = is;
	}
	
	public TreeMap<String, List<Integer>> generate() throws IOException {
		int nextChar = 0;
		return null;
	}


	public static void print(PrintStream ps, TreeMap<String, List<Integer>> concordance) {
		System.out.println("hmm");
	}
	
}
