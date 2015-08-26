package kcg.knightsequences;

import java.util.HashMap;
import java.util.Map;

import static kcg.knightsequences.Key.*;

public class Sequences {
		
	@SuppressWarnings("serial")
	static final Map<Key, Key[]> nextMoves = new HashMap<Key, Key[]>(){{
		put(A, new Key[] { H, L });
		put(B, new Key[] { I, K, M });
		put(C, new Key[] { F, J, L, N });
		put(D, new Key[] { G, M, O });
		put(E, new Key[] { H, N });
		put(F, new Key[] { C, M, ONE });
		put(G, new Key[] { D, N, TWO });
		put(H, new Key[] { A, E, K, O, ONE, THREE });
		put(I, new Key[] { B, L, TWO });
		put(J, new Key[] { C, M, THREE });
		put(K, new Key[] { B, H, TWO });
		put(L, new Key[] { A, C, I, THREE });
		put(M, new Key[] { B, D, F, J });
		put(N, new Key[] { C, E, G, ONE });
		put(O, new Key[] { D, H, TWO });
		put(ONE, new Key[] { F, H, N });
		put(TWO, new Key[] { G, I, K, O });
		put(THREE, new Key[] { H, J, L });
	}};
	
	final Map<SequenceStatus, Long> cache;
	final Key[] input;
	final int length;
	final int maxVowels;
	
	public Sequences(Key[] keypad, int length, int maxVowels) {
		cache = new HashMap<SequenceStatus, Long>();
		this.input = keypad;
		this.length = length;
		this.maxVowels = maxVowels;
	}

	public long count() {
		return countSequences(input, length, maxVowels);
	}
	
	long countSequences(Key[] keys, int length, int vowels) {
		long sum = 0;
		for(Key k : keys) {
			SequenceStatus key = new SequenceStatus(k, length, vowels);
			Long count = cache.get(key);
			if(null == count) {
				count = 0l;
				int v = k.isVowel ? vowels - 1 : vowels;
				if (v >= 0) {
					if(length > 0) 
						count += countSequences(nextMoves.get(k), length - 1, v);
					else 
						return 1;					
				}
				cache.put(key, count);				
			}
			sum += count;
		}
		return sum;
	}
	
	public static void main(String[] args) {
		Key[] input = { A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, ONE, TWO, THREE };
		long count10 = new Sequences(input, 10, 2).count();
		System.out.println("Sequences count 10: " + count10);
		long count16 = new Sequences(input, 16, 2).count();
		System.out.println("Sequences count 16: " + count16);
		long count32 = new Sequences(input, 32, 2).count();
		System.out.println("Sequences count 32: " + count32);
	}
	
}
