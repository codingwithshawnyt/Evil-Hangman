//Shawn Ray
//Period 4
/*
 * This code is for the back-end of a spin-off of the classical game Hangman.
 * It was created to demonstrate mastery over sets and maps.
 * It uses the features of sets and maps to make it as difficult as possible for the user to win,
 * dynamically changing the possible words that the guesses could be until no longer possible.
 */
import java.util.*; // Importing Java's utility package for data structures

public class HangmanManager { // Start of our HangmanManager (back-end) class
    private Set<String> wordSet; // Set to store possible words (will declare type later)
    private Set<Character> guessedLetters; // Set to store guessed letters (will declare type later)
    private String pattern; // String to store the current pattern of letters
    private int guessesLeft; // Integer to store the number of guesses left before a loss for the user is declared and the program is terminated

    public HangmanManager(List<String> dictionary, int length, int max) { // Constructor for HangmanManager class
        if (length < 1 || max < 0) { // Check if length is less than 1 or max is less than 0
            throw new IllegalArgumentException("Bruh whatchu mean you want a word length less than 1 or a max number of wrong attempts less than 0"); // If so, throw an exception
        }

        wordSet = new TreeSet<String>(); // Initialize wordSet as a TreeSet since we need this to be ordered
        for (String word : dictionary) { // For each word in the dictionary
            if (word.length() == length) { // If the word's length equals the specified length (by the user)
                wordSet.add(word); // Add the word to the wordSet
            }
        }

        guessedLetters = new TreeSet<Character>(); // Initialize guessedLetters as a TreeSet; while this isn't completely necessary, it doesn't hurt
        guessesLeft = max; // Set guessesLeft to the user-declared number of maximum guesses

        pattern = ""; // Initialize pattern as an empty string, since we don't know anything yet
        for (int i = 0; i < length; i++) { // For each character in the specified length
            pattern += "-"; // Add a dash to the pattern
        }
    }

    public Set<String> words() { // Method to return the current set of words that we can use
        return wordSet;
    }

    public int guessesLeft() { // Method to return the number of guesses left for the user
        return guessesLeft;
    }

    public Set<Character> guesses() { // Method to return the set of guessed letters by the user so far
        return guessedLetters;
    }

    public String pattern() { // Method to return the current pattern that we have (again we want the least helpful possible pattern)
        if (wordSet.isEmpty()) { // If wordSet is currently empty
            throw new IllegalStateException("Empty word list"); // Throw an exception
        }
        return pattern;
    }

    public int record(char guess) { // Method to record a guess and update the game state accordingly
        if (guessesLeft < 1 || wordSet.isEmpty()) { // If no guesses are left or wordSet is empty
            throw new IllegalStateException("Cannot have less than 1 guess or no words left");
        }
        
        if (guessedLetters.contains(guess)) { // If guess has already been made
            throw new IllegalArgumentException("Guess already made");
        }

        guessedLetters.add(guess); // Add user's guess to our set guessedLetters
        
        Map<String, Set<String>> families = new TreeMap<String, Set<String>>(); // Initialize families as a TreeMap to store word families, which are actually sets for us
        
        for (String word : wordSet) { // For each word in the current set of possible words
            String newPattern = pattern; // Initialize newPattern with the current pattern (testing purposes)
                    
            for (int i = 0; i < word.length(); i++) { // For each character in the word
                if (word.charAt(i) == guess) { // If the character at position i is equal to the guessed character
                    newPattern = newPattern.substring(0, i) + guess + newPattern.substring(i + 1); // Update the pattern with the guessed character at position i
                }
            }
                    
            if (!families.containsKey(newPattern)) { // If the new pattern is not already a key in the families MAP (not set)
                families.put(newPattern, new TreeSet<String>()); // Add the new pattern as a key to the families map with an empty set of words as its value
            }
                    
            families.get(newPattern).add(word); // Add the current word to the set of words associated with the new pattern in the families map
        }
                
        int maxFamilySize = 0; // Initialize maxFamilySize to 0 to keep track of the size of the largest family cause that's what we want to use
                
        for (String familyPattern : families.keySet()) { // For each pattern in the families map
            int familySize = families.get(familyPattern).size(); // Get the size of the family associated with the current pattern
                    
            if (familySize > maxFamilySize) { // If the size of this family is larger than maxFamilySize, generally basic maximum finding algorithm
                maxFamilySize = familySize; // Update maxFamilySize to this family size
                pattern = familyPattern; // Update pattern to this family pattern
                wordSet = families.get(familyPattern); // Update wordSet to this family of words
            }
        }
                
        int count = 0; // Initialize count to 0 to count how many times guess appears in pattern
                
        for (int i = 0; i < pattern.length(); i++) { // For each character in pattern
            if (pattern.charAt(i) == guess) { // If the character at position i is equal to guess
                count++; // Increment count by 1
            }
        }
                
        if (count == 0) { // If guess does not appear in pattern
            guessesLeft--; // Decrement guessesLeft by 1 because a wrong guess was made
        }
                
        return count; // Return count, which is how many times guess appears in pattern
    }
}