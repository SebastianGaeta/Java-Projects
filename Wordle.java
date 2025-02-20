import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.io.FileWriter;

// A word file of "wordlength" words and file with the associated name "gameWords" must be provided for program to work

public class Wordle{

    // field variables
    public static int wordLength = 5; // change value to N in order to play wordle, but with on N letter solutions and guesses (keep in mind, files that contain the respective N letter words must be provided) 
    public static Boolean useDuplicateLetterWords = true; // toggle true/false to use duplicate letter words
    public static String gameWords = "sgb-words.txt"; // change file to write to and read from (this file will continually remove words until empty)
    public static String GREEN = "\033[0;32m";
    public static String RED = "\033[0;31m";
    public static String YELLOW = "\033[0;33m";
    public static String RESET = "\033[0m";

    // WAI
    public static void writeToFile(ArrayList<String> words, int randomNumberIndex, String filename){
        words.remove(randomNumberIndex);
        try {
            FileWriter fileHandle = new FileWriter(filename);
            for (String word : words){
                fileHandle.write(word + "\n");
            }
            fileHandle.close();
        } catch (IOException e) {
            System.out.println("Error: Failed to write any/all words back to: " + filename);
        }
    }
    
    // WAI
    public static ArrayList<String> readFile(String filename){ // reads in file with option to either read in duplicate letter words or not
        ArrayList<String> words = new ArrayList<String>();

        try {
            File file = new File(filename); 
            Scanner fileHandle = new Scanner(file);
            
            while (fileHandle.hasNextLine()){ // check if text is still available to scan    
                
                String[] tempWords = fileHandle.nextLine().replaceAll("\\p{Punct}"," ").trim().split("\\s+");
        
                if (Wordle.useDuplicateLetterWords){
                    for (String word : tempWords){ 
                        if (word.length() == Wordle.wordLength)
                            words.add(word);
                    } 
                } else {
                    tempWords = removeDuplicateLetterWords(tempWords); // remove words that contain duplicate letters // B: works
                    for (String word : tempWords){
                        if (word != null && word.length() == Wordle.wordLength){ // add words so long as they arent null
                            words.add(word);
                        }
                    } 
                }
            }

            words = removeDuplicateWords(words); // remove potential duplicate words in words
            fileHandle.close();
        } catch (FileNotFoundException e) { // file fails to open
            System.out.println("Error: File failed to open");
        }
        return words;
    }

    // WAI*
    public static ArrayList<String> removeDuplicateWords(ArrayList<String> words){ // remove duplicate words
        ArrayList<String> newWords = new ArrayList<String>();
        
        for (int i = 0; i < words.size(); i++){ // get ith word to compare
            Boolean validWord = true;
            int occurrences = 0;
            for (int j = 0; j < words.size(); j++){ // compare ith word to jth word
                if (words.get(j).equals(words.get(i))){
                    occurrences += 1;
                    if (occurrences > 1){ // in the case we find a duplicate word
                        for (int k = 0; k < newWords.size(); k++){ // check if the duplicate word is already in new words
                            if (newWords.get(k).equals(words.get(j))){ 
                                validWord = false; // we found a duplicate present in new words so dont add
                                break;
                            }
                        }
                    }
                }  
            }
            if (validWord != false){
                newWords.add(words.get(i)); // add word to new words
            } 
        }
        
        return newWords;
    }
    
    // WAI*
    public static String[] formatLetters(ArrayList<String> colorToChange, String guess, String[] formattedLetters){
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"}; // need this because the color strings are making the formatted strings unwritable
        
        for (int i = 0; i < colorToChange.size(); i++){
            
            for (int j = 0; j < letters.length; j++){
                if (letters[j].equals(String.valueOf(guess.charAt(i)))){ //find the color
                    switch (colorToChange.get(i)) {
                        case "Green":                 
                            formattedLetters[j]= Wordle.GREEN + letters[j] + Wordle.RESET; // format to green letter 
                            break;
                        case "Yellow":
                            if (formattedLetters[j].contains(Wordle.GREEN) == false){ // format to yellow letter (make sure not to overwrite green)
                                formattedLetters[j]= Wordle.YELLOW + letters[j] + Wordle.RESET;
                            }                 
                            break;
                        case "Red":
                            if (formattedLetters[j].contains(Wordle.GREEN) == false && formattedLetters[j].contains(Wordle.YELLOW) == false){ // format to red letter (make sure not to overwrite green or yellow)
                                formattedLetters[j]= Wordle.RED + letters[j] + Wordle.RESET;
                            }                 
                            break;
                        default:
                            System.out.println("*Error* keyboard could not be formatted");       
                    }
                }
            }
        }
        return formattedLetters;
    }
    
    // WAI*
    public static String[] removeDuplicateLetterWords(String[] wordsToCheck){ // check if a given word has duplicate letters
        String[] newWords = new String[wordsToCheck.length]; 
        

        for (int i = 0; i < wordsToCheck.length; i++){ // outer loop to check word
            Boolean validWord = true;
            char[] tempCharacters = new char[wordsToCheck[i].length()];
            for (int j = 0; j < wordsToCheck[i].length() && validWord == true; j++){ // add jth character to array
                tempCharacters[j] = wordsToCheck[i].charAt(j);
                int count = 0;
                for (int k = 0; k < tempCharacters.length; k++){ // check if jth character exist already
                    if (tempCharacters[k] == tempCharacters[j]){
                        count += 1;
                    }
                    if (count > 1){
                        validWord = false;
                    }
                }

            }

            if (validWord != false){ // add word if no duplicate letters
                newWords[i] = wordsToCheck[i];
            }

        }
        return newWords;
    }

    // WAI*
    public static int getWord(ArrayList<String> words){ // get random index from words
        Random randomNumber = new Random(); 
        try {
            return randomNumber.nextInt(words.size());
        } catch (Exception e) {
            System.out.println("*Error* The program failed to read in any words. Try and take a look at settings or the word files");
            return -1;
        }
    }
    
    // WAI*
    public static String formatGuess(ArrayList<String> colorToChange, String guess){
        String formattedGuess = "";
        for (int i = 0; i < colorToChange.size(); i++){
            switch (colorToChange.get(i)){
                case "Green":
                    formattedGuess += Wordle.GREEN + guess.charAt(i) + Wordle.RESET;
                    break;
                case "Yellow":
                    formattedGuess += Wordle.YELLOW + guess.charAt(i) + Wordle.RESET;
                    break;
                case "Red":
                    formattedGuess += Wordle.RED + guess.charAt(i) + Wordle.RESET;
                    break;
                default:    
                    System.out.println("*Error* program failed to find matching color");

            }
        }
        return formattedGuess;
    }

    // WAI*
    public static ArrayList<String> checkGuess(String correctWord, String guess){ // what if instead of an index to change, i give a character to change
        ArrayList<String> colorToChange = new ArrayList<String>(); // store the necessary colors to change
        char[] yellowLettersAvailable = correctWord.toCharArray(); // keep track of the actual valid yellow characters that can exist
        for (int i = 0; i < guess.length(); i++){
            if (correctWord.charAt(i) == guess.charAt(i)){ // direct match letter to letter
                colorToChange.add("Green"); 
                yellowLettersAvailable[i] = '*'; // remove a potential yellow character, because we have a direct match
            } else if (correctWord.indexOf(guess.charAt(i)) == -1 ){ // the letter doesnt exist
                colorToChange.add("Red");
            } else if (correctWord.indexOf(guess.charAt(i)) != -1){ // the character exist, but its in the wrong location
                colorToChange.add("Yellow");
            }
                
        }   
        
        
        for (int i = 0; i < colorToChange.size(); i++){ // check through assigned yellow colors, see how many yellow slots can actually exist
            Boolean validLetter = false; // assume the yellow cant exist
            if (colorToChange.get(i).equals("Yellow")){ // check for yellows
                for (int j = 0; j < yellowLettersAvailable.length; j++){ // does the yellow we found, have a corresponding character? 
                    if (guess.charAt(i) == yellowLettersAvailable[j]){ // the yellow we found does have a corresponding character
                        yellowLettersAvailable[j] = '*'; // remove a potential yellow
                        validLetter = true; // dont change the yellow we found
                        break;
                    }
                }
                if (validLetter == false){ // change the yellow we found to red, because it cant exist
                    colorToChange.set(i, "Red"); 
                }
            }
        }

        return colorToChange;
    }

    // WAI*
    public static int countColor(String[] formattedLetters, String color){ // counts the occcurences of a given color in order to format the prefered keyboard
        int count = 0;
        for (int i = 0; i < formattedLetters.length; i++){
            if (formattedLetters[i].contains(color) == true){ // is given letter the provided color
                count += 1;
            }
        }
        return count;
    }
    
    // WAI*
    public static Boolean checkWin(String correctWord, String guess){
        if (guess.equals(correctWord)){
            return true; // Win!
        } else {
            return false; // Game continues...
        }
    }

    // WAI*
    public static Boolean checkUserInput(String guess, int desiredGuessLength){
        if (guess.length() != desiredGuessLength){ // check if our guess is the correct length
            return false; // its not a valid guess
        }
        
        char[] guessCharacters = guess.toCharArray();

        for (char ch : guessCharacters){ // check if it consist of only valid characters
            if (Character.isLetter(ch) == false){
                return false; // its not a valid guess
            }
        }

        return true; // its a valid guess
    }

    public static void main(String[] args){
        
        Scanner scnr = new Scanner(System.in);
        ArrayList<String> words = readFile(Wordle.gameWords); // read file
        int randomWordIndex = getWord(words); // get index of random word
        String randomWord = words.get(randomWordIndex).toUpperCase(); // get random word
        ArrayList<String> guessedWords = new ArrayList<String>(); // initilize a list to hold past guesses
        ArrayList<String> colorToChange; 
        String colorToFormatKeyboard = Wordle.GREEN; 
        String[] keyboard = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"}; // initilize the keyboard
        String guess = ""; // initilize guess
         
        

        for (int i = 0; i < 6; i++){
            
            if (i < 1){ // first trial
                System.out.print("Enter a guess: "); // prompt
                
                guess = scnr.nextLine().replaceAll("\\p{Punct}"," ").trim().toUpperCase();
                
                while (checkUserInput(guess, Wordle.wordLength) == false){ // check input
                    System.out.println("*Error* enter valid input that consist of " + Wordle.wordLength + " alphabetic characters");
                    System.out.print("Enter a guess: ");
                    guess = scnr.nextLine().replaceAll("\\p{Punct}"," ").trim().toUpperCase();
                }

                colorToChange = checkGuess(randomWord, guess);
                guessedWords.add(formatGuess(colorToChange, guess));
                keyboard = formatLetters(colorToChange, guess, keyboard);
                System.out.println(guessedWords.get(0)); // print guess
                
                for (String s : keyboard){ // print the current state of keyboard
                    System.out.print(s);
                }
                
                System.out.println();
                
                if (checkWin(randomWord, guess)){ // check if user won
                    System.out.println("Congratualtions! You Win!");
                    break;
                }
            
            } else { // remaining trials
                System.out.print("Enter a guess: "); // prompt
                guess = scnr.nextLine().replaceAll("\\p{Punct}"," ").trim().toUpperCase();

                while (checkUserInput(guess, Wordle.wordLength) == false){ // check input
                    System.out.print("*Error* enter valid input that consist of " + Wordle.wordLength + " alphabetic characters");
                    
                    System.out.println();
                    for (int j = 0; j < guessedWords.size(); j++){ // output previous guesses
                        System.out.println(guessedWords.get(j));
                    }
                    for (String s : keyboard){ // print the current state of keyboard
                        System.out.print(s);
                    }
                    System.out.println();
                    System.out.print("Enter a guess: ");
                    guess = scnr.nextLine().replaceAll("\\p{Punct}"," ").trim().toUpperCase();
                }

                colorToChange = checkGuess(randomWord, guess);
                guessedWords.add(formatGuess(colorToChange, guess));
                //                  new                                                                      old
                if (countColor(formatLetters(colorToChange, guess, keyboard), colorToFormatKeyboard) > countColor(keyboard, colorToFormatKeyboard)){ // checks if the newest guess reflects a better keyboard
                    keyboard = formatLetters(colorToChange, guess, keyboard); // newest keyboard is better than previous
                }
            
                for (int j = 0; j < guessedWords.size(); j++){ // output previous guesses
                    System.out.println(guessedWords.get(j));
                }
                
                for (String s : keyboard){ // print the current state of keyboard
                    System.out.print(s);
                }

                System.out.println();
                
                if (checkWin(randomWord, guess)){ // check if user won
                    System.out.println("Congratualtions! You Win!");
                    break;
                }
            }
        }

        if (checkWin(randomWord, guess) == false){ // check if user won
            System.out.println("The correct word was: " + randomWord);
            System.out.println("You lost, better luck next time!");
            
        }  

        writeToFile(words, randomWordIndex, Wordle.gameWords); // delete word from game file and remove duplicate words 
        scnr.close();
    }
}