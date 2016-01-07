
class ColorPeg {
	private int color;
	private boolean counted;
	private boolean fixed;
	
	public ColorPeg() {
		color = 0;
		counted = false;
		fixed = false;
	}
	
	public ColorPeg(int c) {
		color = c;
		counted = false;
		fixed = false;
	}
	
	public int getColor() {
		return color;
	}
	
	public boolean getCounted() {
		return counted;
	}
	
	public boolean getfixed() {
		return fixed;
	}
	
	public void setColor(int c) {
		color = c;
	}
	
	public void setCounted() {
		counted = true;
	}
	
	public void setUncounted() {
		counted = false;
	}
	
	public void setfixed() {
		fixed = true;
	}

	public boolean equals(ColorPeg o) {
		if ((color == o.color) && (counted == false) && (o.counted == false))
			return true;
		else
			return false;
	}
}
