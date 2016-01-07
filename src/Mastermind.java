
import java.util.Arrays;
import java.util.Scanner;

public class Mastermind {
	public static void main(String arg[]) {
		int pegs;
		int colors;

		System.out.println(
				"\n***************************************************************************\n" +
				"*                             GAME MASTERMIND                             *\n" +
				"*- You can specify arbitrary number of pegs and colors.                   *\n" +
				"*- You can choose to randomly generate a secret code or specify one.      *\n" +
				"*- There are 4 modes to play the game:                                    *\n" +
				"*        1. Manual mode:        user to play as the decoder;              *\n" +
				"*        2. Exhaustive Search:  algorithm to exhaustively search all      *\n" +
				"*                               possible color combinations;              *\n" +
				"*        3. Greedy Heuristic:   algorithm to guess the colors one peg     *\n" +
				"*                               at a time using heuristic;                *\n" +
				"*        4. Local Search:       guided search algorithm to guess the      *\n" +
				"*                               colors using the feedback as heuristic    *\n" +
				"***************************************************************************\n");
		
		Scanner sc = new Scanner(System.in);		
		System.out.println("Enter number of pegs:");
		pegs = sc.nextInt();
		System.out.println("\nEnter number of colors:");
		colors = sc.nextInt();
		
		CodePegs secretCode = new CodePegs(pegs, colors);
		boolean validChoice = false;

		while(!validChoice) {
			System.out.println("\nHow do you want to set secret code?"
					+ "\n1 - Randomly\n2 - Manually");
			int choice = sc.nextInt();
			
			if (choice == 1) {
				secretCode.setRandomColors();
				validChoice = true;
			}
			
			else if (choice == 2) {
				int[] secretColors = new int[pegs];
				System.out.println("\nEnter " + pegs + " colors in the range of 1 to " + colors + 
						", seperated by space. Only the first " + pegs + " numbers will be taken.");
				for (int i = 0; i < pegs; i++) {
					secretColors[i] = sc.nextInt(); 
				}
				for (int i = 0; i < pegs; i++) {
					if ((secretColors[i] < 1) || (secretColors[i] > colors)) {
						System.out.println("Invalid color. Color must be in the range of 1 to " + 
								colors + "." );
						break;						
					}
					else {
						secretCode.setCustomizedColors(secretColors);
						validChoice = true;
					}
				}
			}
			else
				System.out.println("Invalid input.");
		}
		
		CodePegs guess = new CodePegs(pegs, colors);
		int[] colorsguess = new int[pegs];
		for (int i = 0; i < pegs; i++)
			colorsguess[i] = 1;
		guess.setCustomizedColors(colorsguess);
		KeyPegs feedback = secretCode.compareColors(guess);
		

		int algorithmChoice = -1;
		while(!((algorithmChoice == 0) || (algorithmChoice == 1) ||
				(algorithmChoice == 2) || (algorithmChoice == 3))) {
			System.out.println("\nWhich game mode do you want to play?\n"
					+ "0 - Manual Mode\n"
					+ "1 - Exhaustive Search (slow)\n"
					+ "2 - Greedy Heuristic Search (faster)\n"
					+ "3 - Guided Local Search(fastest)");
			algorithmChoice = sc.nextInt();
		}
		
		switch (algorithmChoice) {
		case 0:
			do {
				guess = guess.manualColorUpdate(guess);
				feedback = secretCode.compareColors(guess);
				System.out.println("Guess #" + (guess.getNumberOfGuesses()) + ":");
				System.out.println(Arrays.toString(guess.getColors()));
				feedback.displayKey();
			} while(!feedback.allBlack());
			break;
		
		case 1:
			do {
				guess = guess.exhaustiveUpdate(guess);
				feedback = secretCode.compareColors(guess);
				feedback.displayKey();
			} while(!feedback.allBlack());
			break;
		
		case 2:
			do {
				guess = guess.greedyHeuristicUpdate(secretCode, guess, feedback);
				feedback = secretCode.compareColors(guess);
				feedback.displayKey();
			} while(!feedback.allBlack());
			break;
		
		case 3:
			guess.findColorSet(secretCode, guess);		
			feedback = secretCode.compareColors(guess);
			if (feedback.allBlack())
				break;

			for (int i = 0; i < pegs; i++)
				for (int j = i + 1; j < pegs; j++) {
					if (!guess.equals(guess.localSearch(secretCode, guess, i, j))){
						feedback = secretCode.compareColors(guess);
						feedback.displayKey();
						if (feedback.allBlack())
							break;
					}					
				}

		default:
			break;
		}
				
		System.out.println("\nYou did it, the secret code is:");

		System.out.println(Arrays.toString(secretCode.getColors()));
		System.out.println("Total number of guesses: " + guess.getNumberOfGuesses());
		System.out.println("End of Game.\n");
		
		sc.close();
	}
}
