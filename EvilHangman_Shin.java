// File: EvilHangman_Shin.java
// EvilHangman_Shin class
// @author Austin Shin

import java.io.IOException;
import java.util.Scanner;

/*
 * Steps on how to run this project
 *  The instructions are outlined while running the project,
 *  but I'll clarify here beforehand.
 *  1. Enter in a length for how long you want your word to be.
 *  2. guess letters. can be uppercase/lowercase
 *  3. if you run out of guesses, you lose. if you figure out the word, you win.
 */

/*
 * This class deals with taking input from the user.
 */
public class EvilHangman_Shin {

	private static int numGuess; //number of guesses the user has
	
	/*
	 * Runs the game.
	 * @throws IOException in case of bad input
	 */
	public static void main(String[] args) throws IOException {
		
		System.out.println("Let's play!");
		System.out.println();
		
		Extra.process();
		Extra.inputLength();
		
		numGuess = Extra.setGuess();
		for (int b = numGuess; b > 0; b--)
        {
        	System.out.println();
			System.out.println("You have " + numGuess + " guesses left.");
        	b = Extra.play(b);
	        numGuess--;
        }
	}
	
	/*
	 * Takes user's guess and returns lowercase version
	 * @return lower case letter
	 */
	public static String guess() {
		System.out.print("Enter guess: ");
        Scanner obj = new Scanner(System.in);
        String letter = obj.nextLine();
        System.out.println();
        return letter.toLowerCase();
	}
}
