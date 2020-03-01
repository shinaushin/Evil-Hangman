// File: Extra.java
// Extra class
// @author Austin Shin

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/*
 * Implements the behind-the-scenes functionality to the hangman game
 */
public class Extra {
	
	// list of possible words as an answer
	private static ArrayList<String> list = new ArrayList<String>();
	private static String[] array = {"a", "b", "c", "d", "e", "f", "g", "h",
									 "i", "j", "k", "l", "m", "n", "o", "p",
									 "q", "r", "s", "t", "u", "v", "w", "x",
									 "y", "z"};
	private static ArrayList<String> letters =
		new ArrayList<String>(Arrays.asList(array));
	// stores each letter of the answer
	private static ArrayList<String> answer = new ArrayList<String>();
	// stores all the letters that the user has used
	private static ArrayList<String> used = new ArrayList<String>();
	private static int length; // length of the answer
	
	/*
	 * Naive algorithm to determine how many guesses the user deserves based on
	 * length of word
	 * @return number of guesses
	 */
	public static int setGuess() {
		if(length >= 14) {
			return 14;
		} else {
			return 14 + (14-length)/2;
		}
	}
	
	/*
	 * Reads text on website and stores all words in the arraylist
	 * @throws IOException in case of bad input
	 */
	public static void process() throws IOException {
		URL oracle = 
			new URL("http://nifty.stanford.edu/2011/schwarz-evil-hangman/dictionary.txt");
        BufferedReader in = new BufferedReader(
        new InputStreamReader(oracle.openStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            list.add(inputLine);
        in.close();
	}
	
	/*
	 * Initializes answer arraylist with "_" characters
	 */
	public static void initAnswer(int length) {
		for (int a = 0; a < length; a++) {
			answer.add("_");
		}
	}
	
	/*
	 * Prints all the used letters
	 */
	public static void printUsed() {
		System.out.print("Used letters: ");
		for (int a = 0; a < used.size(); a++) {
			System.out.print(used.get(a) + " ");
		}
		System.out.println();
	}
	
	/*
	 * Prints the answer arraylist
	 */
	public static void printWord() {
		System.out.print("Word: ");
		for (int a = 0; a < answer.size(); a++) {
			System.out.print(answer.get(a) + " ");
		}
		System.out.println();
	}
	
	/*
	 * Executes gameplay after each user guess
	 * @returns number of guesses left
	 */
	public static int play(int c) {
		printUsed();
		printWord();
		
		boolean container = true;
		String letter = "";
		// checks to see if the user already guessed that letter
		while (container) {
			letter = EvilHangman_Shin.guess();
			if (!used.contains(letter)) {
				container = false;
			} else { 
				System.out.println("You already used that letter.");
			}
		}
		
		// checks to see if the letter is actually a part of the alphabet
        if (!letters.contains(letter)) {
        	System.out.println("Don't break the game. Let's make a proper guess.");
        	System.out.println();
        	letter = EvilHangman_Shin.guess();
        }
        
        // adds the guess to the used arraylist
        used.add(letter);
        
		// break apart list of words into different families

		// family of words without the letter in it
		ArrayList<String> famWO = new ArrayList<String>();
		// family of words with the letter in it
		ArrayList<String> famW = new ArrayList<String>();
		// family of words in which all letters except guessed letter is changed to dashes
		ArrayList<String> dash = new ArrayList<String>();
		// family with most words in it to use as next list
        ArrayList<String> finalL = new ArrayList<String>();
        
        // splits words into famWO and famW
        for (int a = 0; a < list.size(); a++) {
        	if (list.get(a).contains(letter)) {
        		famW.add(list.get(a));
        	} else {
				famWO.add(list.get(a));
			}
        }
        
        if (famWO.size() >= famW.size()) {
			// if num of words without letter is greater than num of words with
			// letter, that will automatically become new list
        	list = famWO;
        	System.out.println("Sorry. That letter isn't in the word.");
        	System.out.println();
        } else {
			// if famW is bigger, then it changes all words in list to dashes 
			// (except for guessed letter)
        	for (int a = 0; a < famW.size(); a++) {
        		String word = "";
        		for (int b = 0; b < famW.get(a).length(); b++) {
        			if (!famW.get(a).substring(b,b+1).equals(letter)) {
        				word += "-";
        			} else {
						word += letter;
					}
        		}
        		dash.add(word);
        	}
        	
        	String tempR = "";
        	String ref = "";
        	int count = 0;
        	
        	/*
        	 * The dash list now displays all different combinations of guessed 
			 * letter in those remaining words. Have to find out with
			 * combination appears most often. Then that becomes new list for
			 * next round.
        	 */
        	for (int a = 0; a < dash.size(); a++) {
        		tempR = dash.get(a);
        		int temp = 0;
        		
        		for (int b = a; b < dash.size(); b++) {
        			if (tempR.equals(dash.get(b))) {
        				temp++;
        				dash.remove(b);
        				b--;
        			}
        		}
        		
        		if (temp > count) {
        			count = temp;
        			ref = tempR;
        		}
        	}
			
			// after loop, mainCount stores num of times guessed letter appears
			// in combination that appears most often
        	int mainCount = 0;
        	for (int a = 0; a < ref.length(); a++) {
        		if (!ref.substring(a,a+1).equals("-")) {
        			mainCount++;
        		}
        	}
        	
			// puts all words that represent most often-appearing combination
			// into finalL
        	for (int a = 0; a < famW.size(); a++) {
        		int countL = 0;
        		for (int b = 0; b < famW.get(a).length(); b++) {
        			if (famW.get(a).substring(b,b+1).equals(letter)) {
        				if (!ref.substring(b,b+1).equals(letter)) {
        					b = famW.get(a).length();
        				} else {
							countL++;
						}
        			} 
        			if (b == famW.get(a).length()-1 && countL == mainCount) {
    					finalL.add(famW.get(a));
    				}
        		}
        	}
        	
			// Verifies if finalL is greater than num of words that don't
			// contain guessed letter.
			// if finalL is greater, then it becomes new list
			// Else, famWO becomes new list.
        	if (finalL.size() > famWO.size()) {
        		list = finalL;
        		ArrayList<Integer> indices = new ArrayList<Integer>();
        		
        		for (int a = 0; a < finalL.get(0).length(); a++) {
        			if (finalL.get(0).substring(a,a+1).equals(letter)) {
        				indices.add(a+1);
        			}
        		}
        		
        		String display = "The letter you guessed is at space ";
        		
        		for (int a = 0; a < indices.size(); a++) {
        			display += indices.get(a) + ", ";
        			answer.set(indices.get(a)-1, letter);
        		}
        		
        		System.out.println(display.substring(0,display.length()-2) + ".");
        		System.out.println();
        	} else {
        		list = famWO;
        		System.out.println("Sorry. That letter isn't in the word.");
        		System.out.println();
        	}
        }
       
        // checks if answer arraylist is filled
        if (isDone()) {
        	System.out.print("Word: ");
        	
    		for (int a = 0; a < answer.size(); a++) {
    			System.out.print(answer.get(a) + " ");
    		}
    		
    		System.out.println();
        	System.out.println("Congrats! You won!");
        	return 0;
        }
        
        // if answer arraylist is not done and last guess is used, user loses
        if (!isDone() && c == 1) {
        	System.out.print("Word: ");
    		for (int a = 0; a < answer.size(); a++) {
    			System.out.print(answer.get(a) + " ");
    		}
    		System.out.println();
    		
        	System.out.println("You ran out of guesses. Better luck next time.");
        	System.out.println("The word was " + list.get(0) + ".");
        }
        
        return c;
	}
	
	/*
	 * Verifies if answer arraylist is filled
	 * @returns if word has been completely guessed
	 */
	public static boolean isDone() {
		if (answer.contains("_")) {
			return false;
		} else { return true; }
	}
	
	/*
	 * Verifies that length from method inputLength is a number, an integer, and
	 * that there are words of that length.
	 */
	public static void useLength(String size) {
		try {
            double d= Double.valueOf(size);
            
            if (d < 2 || d > 29) {
            	System.out.println("There is no such word with that length.");
            	inputLength();
            } else if (d==(int)d) {
            	length = Integer.parseInt(size);
            	for (int a = 0; a < list.size(); a++) {
        			if (list.get(a).length() == length) {
        				a = list.size();
        				initAnswer(length);
        				System.out.println("This may take a while...");
        			} else if (a == list.size()-1) {
        				System.out.println("Sorry. There are no words of that length.");
        				System.out.println();
        				
        				inputLength();
        			}
        		}
            } else {
            	System.out.println("That is not an integer.");
            	System.out.println();
            	inputLength();
            }
        } catch(Exception e) {
            System.out.println("That is not a number. Please enter a number.");
            System.out.println();
            inputLength();
        }
		
		//takes all the words that are not of that length out of the list
        for (int a = 0; a < list.size(); a++) {
        	if (list.get(a).length() != length) {
        		list.remove(a);
        		a = -1;
        	}
        }
	}
	
	/*
	 * Receives length that the user puts in
	 */
	public static void inputLength()
	{
		System.out.println("How long do you want your word to be?");
		System.out.println("The shortest word is 2 letters. And the longest is 29 letters.");
        Scanner obj = new Scanner(System.in);
        String size = obj.next();
        System.out.println();
        useLength(size);
	}
}
