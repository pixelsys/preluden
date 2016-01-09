package bw.exercise;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Concordance {
	
	final InputStream is;
	final TreeMap<String, List<Integer>> results;	
	
	public Concordance(InputStream is) {
		this.is = is;
		this.results = new TreeMap<String, List<Integer>>();
	}
	
	public TreeMap<String, List<Integer>> generate() throws IOException {
		results.clear();
		int ci, prev = 0;
		int sentence = 1;
		StringBuilder word = new StringBuilder();
		while(-1 != (ci = is.read())) {
			if(ci == 10 || ci == 32 || ci == 9) { // whitespace: \n space \t
				saveWord(word.toString(), sentence);
				word = new StringBuilder();
			} else if (ci > 32) {
				int toAppend = ci;
				if(ci >= 65 && ci <= 90) { // uppercase
					toAppend = ci + 32; // convert to lowercase
					if (prev == 33 || prev == 46 || prev  == 63)  // previous character was end of sentence
						++sentence;					
				}
				if(ci == 39 || ci == 45 || ci == 46 || (toAppend >= 97 && toAppend <= 122)) // ' - . a..z
					word.append((char)toAppend);
				prev = ci;
			}
		}
		if(prev == 46) // trim trailing .
			word.deleteCharAt(word.length() - 1);
		saveWord(word.toString(), sentence);
		return results;
	}

	void saveWord(String word, int sentenceNo) {
		if(0 == word.length())
			return;
		List<Integer> sntOcc = results.get(word);
		if(null == sntOcc) {
			sntOcc = new ArrayList<Integer>();
			results.put(word, sntOcc);
		}
		sntOcc.add(sentenceNo);		
	}

	public static void print(PrintStream ps, TreeMap<String, List<Integer>> concordance) {
		for(Map.Entry<String, List<Integer>> entry : concordance.entrySet()) {
			List<Integer> sntOcc = entry.getValue();
			ps.printf("'%s' {%d:%s}\n", entry.getKey(), sntOcc.size(), sntOcc);
		}
	}
	
	public static void main(String[] args) {
		if(0 == args.length) {
			System.err.println("Usage: $0 input-file");
			System.exit(-1);
		}
		try {
			FileInputStream fis = new FileInputStream(args[0]);
			Concordance concordance = new Concordance(fis);
			Concordance.print(System.out, concordance.generate());
		} catch (Exception e) {
			System.err.println("Error while generating concordance: " + e.getMessage());
			e.printStackTrace(System.err);
			System.exit(-1);
		}
	}
	
}
