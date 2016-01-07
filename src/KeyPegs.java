
class KeyPegs {
	private int numberOfPegs;
	private int grey;
	private int white;
	private int black;
	
	public KeyPegs(int np) {
		this.numberOfPegs = np;
		/*
		 * grey represents color code does not exist
		 * white represents color code exist but in wrong position
		 * black represents color code exist and in right position
		 */
		grey = numberOfPegs;
		white = 0;
		black = 0;
	}
	
	public void increaseBlack() {
		black++;
		grey--;
	}
	
	public void increaseWhite() {
		white++;
		grey--;
	}
	
	public int getGrey() {
		return grey;
	}
	
	public int getWhite() {
		return white;
	}
	
	public int getBlack() {
		return black;
	}
	
	public int getHeuristicValue() {
		return black * 2 + white * 1;
	}
	
	public void displayKey() {
		System.out.println("Black hits:	" + black + "	White hits:	" + white);
	}
	
	public boolean allBlack() {
		if ((black == numberOfPegs) && (grey == 0) && (white == 0))
			return true;
		else
			return false;
	}
}
