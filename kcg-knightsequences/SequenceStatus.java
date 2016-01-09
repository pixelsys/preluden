package kcg.knightsequences;

public class SequenceStatus {

	final Key key;
	final int length;
	final int vowels;
	
	public SequenceStatus(Key key, int length, int vowels) {
		this.key = key;
		this.length = length;
		this.vowels = vowels;
	}

	public Key getKey() {
		return key;
	}

	public int getLength() {
		return length;
	}

	public int getVowels() {
		return vowels;
	}

	@Override
	public int hashCode() {
		int result = 1;
		result = 31 * result + ((key == null) ? 0 : key.hashCode());
		result = 17 * result + length;
		result = 29 * result + vowels;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SequenceStatus other = (SequenceStatus) obj;
		if (key != other.key)
			return false;
		if (length != other.length)
			return false;
		if (vowels != other.vowels)
			return false;
		return true;
	}
	
}
