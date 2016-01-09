package kcg.knightsequences;

public enum Key {

	A(true), B(false), C(false), D(false), E(true), F(false), G(false), H(false), I(true), J(false), K(false), L(false),
	M(false), N(false), O(true), ONE(false), TWO(false), THREE(false);
	
	final boolean isVowel;
	
	Key(boolean isVowel) {
		this.isVowel = isVowel;
	}
	
	boolean isVowel() {
		return isVowel;
	}
	
}
