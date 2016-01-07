
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

class CodePegs {
	private int numberOfPegs;
	private int numberOfColors;
	private ArrayList<ColorPeg> colorPegs = new ArrayList<>();
	private int numberOfGuess = 0;
	
	//constructor
	public CodePegs() {
		numberOfPegs = 4;
		numberOfColors = 6;
		for (int i = 0; i < numberOfPegs; i++)
			//0 means no color assigned
			colorPegs.add(new ColorPeg(0));
		colorPegs.trimToSize();
	}
	
	public CodePegs(int np, int nc) {
		numberOfPegs = np;
		numberOfColors = nc;
		for (int i = 0; i < numberOfPegs; i++)
			//0 means no color assigned
			colorPegs.add(new ColorPeg(0));
		colorPegs.trimToSize();
	}
	
	//getter method
	public int getNumberOfPegs() {
		return numberOfPegs;
	}
	
	public int getNumberOfColors() {
		return numberOfColors;
	}
	
	public int[] getColors() {
		int[] c = new int[numberOfPegs];
		for (int i = 0; i < numberOfPegs; i++){
			c[i] = this.colorPegs.get(i).getColor();
		}
		return c;
	}
	
	public int getNumberOfGuesses() {
		return numberOfGuess;
	}
	
	//method to set colors
	public void setRandomColors() {
		for (int i = 0; i < numberOfPegs; i++) {
			colorPegs.get(i).setColor(randInt(numberOfColors));
		}
	}
	
	public void setCustomizedColors(int[] c) {
		for (int i = 0; i < numberOfPegs; i++) {
			colorPegs.get(i).setColor(c[i]);
		}
	}
	
	//
	public KeyPegs compareColors(CodePegs guess) {
		//implement compare function
		if ((numberOfPegs != guess.numberOfPegs)
				||(numberOfColors != guess.numberOfColors)) {
			System.out.println("Inconsistent Game Configuration.");
			return null;
		}
		
		KeyPegs feedback = new KeyPegs(numberOfPegs);
		for (int i = 0; i < numberOfPegs; i++) {
			if (colorPegs.get(i).equals(guess.colorPegs.get(i))) {
				feedback.increaseBlack();
				colorPegs.get(i).setCounted();
				guess.colorPegs.get(i).setCounted();
			}
		}
		
		for (int i = 0; i < numberOfPegs; i++) {
			for (int j = 0; j < numberOfPegs; j++) {
				if (colorPegs.get(i).equals(guess.colorPegs.get(j))) {
					feedback.increaseWhite();
					colorPegs.get(i).setCounted();
					guess.colorPegs.get(j).setCounted();
					break;
				}
			}
		}
		
		for (int i = 0; i < numberOfPegs; i++) {
			colorPegs.get(i).setUncounted();
			guess.colorPegs.get(i).setUncounted();
		}
		
		return feedback;
	}
	
	//manual guess
	public CodePegs manualColorUpdate(CodePegs guess) {
		int[] colors = guess.getColors();		
		System.out.println("Take a guess.");
		
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		for (int i = 0; i < guess.getNumberOfPegs(); i++)
			colors[i] = sc.nextInt();
		
		guess.setCustomizedColors(colors);
		guess.numberOfGuess++;
		
		return guess;
	}
	
	//exhaustively update colors using lexicographic order
	public CodePegs exhaustiveUpdate(CodePegs guess) {
		int[] c = guess.getColors();
		int carry = 1;
		for (int i = numberOfPegs - 1; i >=0; i--) {
			c[i] = c[i] + carry;
			carry = (c[i] - 1) / numberOfColors;
			if (c[i] > numberOfColors)
				c[i] = c[i] % numberOfColors;
		}
		guess.setCustomizedColors(c);
		guess.numberOfGuess++;
		System.out.println("Guess #" + (guess.getNumberOfGuesses()) + ":");
		System.out.println(Arrays.toString(guess.getColors()));
		return guess;
	}
	
	//update color peg by peg
	public CodePegs greedyHeuristicUpdate(CodePegs secretCode, CodePegs guess, KeyPegs fb) {
		for (ColorPeg e : guess.colorPegs) {
			if (!e.getfixed()) {
				int newColor = e.getColor() + 1;
				if (newColor > guess.numberOfColors)
					newColor = 1;
				e.setColor(newColor);
				
				KeyPegs newFeedback = secretCode.compareColors(guess);
				if (newFeedback.getBlack() > fb.getBlack())
					e.setfixed();
				
				break;
			}
		}
		guess.numberOfGuess++;
		System.out.println("Guess #" + (guess.getNumberOfGuesses()) + ":");
		System.out.println(Arrays.toString(guess.getColors()));
		return guess;
	}
	
	public void findColorSet (CodePegs secretCode, CodePegs guess) {
		int[] colorSet = new int[numberOfColors + 1];
		for (int i : colorSet)
			colorSet[i] = 0;
		int colorSum = 0;
		
		int[] colors = guess.getColors();
		for (int i = 1; i <= numberOfColors; i++) {
			for (int j = colorSum; j < numberOfPegs; j++) {
				colors[j] = i;
			}
			guess.setCustomizedColors(colors);

			colorSet[i] = secretCode.compareColors(guess).getBlack() + 
					secretCode.compareColors(guess).getWhite() - colorSum;
			colorSum = colorSum + colorSet[i];
			
			guess.numberOfGuess++;
			System.out.println("Guess #" + (guess.getNumberOfGuesses()) + ":");
			System.out.println(Arrays.toString(guess.getColors()));
			
			secretCode.compareColors(guess).displayKey();
			if ((secretCode.compareColors(guess).getBlack() + 
					secretCode.compareColors(guess).getWhite()) == numberOfPegs) {
				break;
			}
		}
		
/*		System.out.println("The color set in the secret code is:");
		for (int i = 1; i <= numberOfColors; i++) {
			System.out.print("Number of Color " + i + "	:");
			System.out.println(colorSet[i]);
		}
		*/
	}
	
	public CodePegs localSearch(CodePegs secretCode, CodePegs guess, int i, int j) {
		
		int [] colors = guess.getColors();
		if (colors[i] == colors[j])
			return guess;
		else {
			int oldHeuristic = secretCode.compareColors(guess).getHeuristicValue();
			swap(colors, i, j);
			guess.setCustomizedColors(colors);
			int newHeuristic = secretCode.compareColors(guess).getHeuristicValue();
			
			if (newHeuristic > oldHeuristic){
				guess.numberOfGuess++;
				System.out.println("Guess #" + (guess.getNumberOfGuesses()) + ":");
				System.out.println(Arrays.toString(guess.getColors()));
				secretCode.compareColors(guess).displayKey();
				return guess;
				
			}
			else {
				swap(colors, i, j);
				guess.setCustomizedColors(colors);
				return guess;
			}
			
		}
		
	}
	
	//utility method
	private int randInt(int nc) {
		Random rand = new Random();
		//actual color code start from 1 to number of colors
		return rand.nextInt(nc) + 1;
	}
	
	private void swap(int [] colors, int i, int j) {
		int temp = colors[i];
		colors[i] = colors[j];
		colors[j] = temp;
	}
}
