package fr.upmc.algav.tools;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class GraphReader {

	private final static String SPACE = " ";
	private Scanner scanner;
	private ArrayList<String> wordsList;

	public GraphReader(String file) {
		try {
			FileReader fileReader = new FileReader(file);
			this.scanner = new Scanner(fileReader);
			this.wordsList = new ArrayList<>();
		}
		catch (FileNotFoundException e) {
			System.out.print("Couldn't read file \"" + file + "\" because of:");
			e.printStackTrace();
		}
	}

	private enum Content {
		FULL_TEXT, WORD_BY_LINE, UNKNOWN
	}
	
	public ArrayList<String> read() {
		if (fileContent() == Content.FULL_TEXT) {			
			while (scanner.hasNextLine()) {
				addWordsToList();
			}
		} else if (fileContent() == Content.WORD_BY_LINE) {
			while (scanner.hasNext()) {
				addWordToList();
			}
		} else if (fileContent() == Content.UNKNOWN) {			
			//throw new Error("Unknown file content.");
		}

		return wordsList;
	}
	
	private Content fileContent() {
		String[] tokens = scanner.hasNextLine() ? scanner.nextLine().split(SPACE) : new String[0];
		int wordsCounter = tokens.length;

        if (wordsCounter > 1) {
			addWordsToList(tokens);
			return Content.FULL_TEXT;
		} else if (wordsCounter == 1) {
			addWordToList(tokens[0]);
			return Content.WORD_BY_LINE;
		} else {
			return Content.UNKNOWN;
		}
	}
	
	private void addWordToList() {
		String word = null;
		word = scanner.next();

        wordsList.add(word);
	}
	
	private void addWordToList(String word) {
		wordsList.add(word);
	}
	
	private void addWordsToList() {
		String[] tokens = null;
		tokens = scanner.nextLine().split(SPACE);

        for (String word : tokens) {
			wordsList.add(word);
		}
	}	
	
	private void addWordsToList(String[] tokens) {
		for (String word : tokens) {
			wordsList.add(word);
		}
	}
}
