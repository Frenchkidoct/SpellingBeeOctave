import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    private String mletters;
    private String oldLetters;

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        // YOUR CODE HERE — Call your recursive method!
        makeWords("", "");

    }
    public void makeWords(String mletters, String oldLetters) {
        if (mletters.isEmpty()) {
            return;
        }
        for (int i = 0; i < mletters.length(); i++)
        {
            words.add(oldLetters);
            makeWords(mletters.substring(0, i) + mletters.substring(i + 1), oldLetters
                        + mletters.substring(i, i + 1));
        }
    }


        // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        // YOUR CODE HERE
        words = mergesort(words, 0, words.size() -1);
    }

    public ArrayList<String> mergesort(ArrayList<String> merge, int min, int max)
    {
        if((min - max) == 0)
        {
            ArrayList<String> a = new ArrayList<>();
            a.add(merge.get(min));
            return a;
        }
        int medium = (max + min) / 2;
        ArrayList<String> a1 = mergesort(merge, min, medium);
        ArrayList<String> a2 = mergesort(merge, medium +1, max);
        return merging(a1, a2);
    }
    public ArrayList<String> merging(ArrayList<String> arr1, ArrayList<String> arr2)
    {
        ArrayList<String> m = new ArrayList<>();
        int i = 0;
        int j = 0;
        while(i < arr1.size() && j < arr2.size())
        {
            if(arr1.get(i).compareTo(arr2.get(j)) < 0)
            {
                m.add(arr2.get(j));
                i++;
            }
            else
            {
                m.add(arr2.get(j));
                j++;
            }
        }
        while(j < arr2.size())
        {
            m.add(arr2.get(j));
        }
        while(j < arr1.size())
        {
            m.add(arr1.get(i));
        }
        return m;
    }



    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        // YOUR CODE HERE
        for(int i = 0; i < words.size(); i++)
        {
            if(searching(words.get(i)))
            {
                words.remove(i);
                i--;
            }
        }
    }
    public boolean searching(String t)
    {
        return wordSearch(t, 0, DICTIONARY_SIZE);
    }

    public boolean wordSearch(String t, int min, int max)
    {
        int medium = (min + max) / 2;

        if(DICTIONARY[medium].equals(t))
        {
            return true;
        }
        if(max <= min)
        {
            return false;
        }
        else if(DICTIONARY[medium].compareTo(t) > 0)
        {
            return wordSearch(t, min, medium -1);
        }
        else
        {
            return wordSearch(t, medium + 1, max);
        }
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
