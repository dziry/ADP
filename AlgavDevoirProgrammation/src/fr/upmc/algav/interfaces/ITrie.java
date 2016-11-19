package fr.upmc.algav.interfaces;

import java.util.ArrayList;

public interface ITrie {
	
	// Basic primitives
	boolean isEmpty();
	void insert(String word);
	void insert(ArrayList<String> words);
	
	// Advanced functions
	boolean search(String word);
	int getWordCount();
	ArrayList<String> getStoredWords();
	int getNullPointerCount();
	int getHeight();
	double getAverageDepthOfLeaves();
	int getPrefixCount(String word);
	boolean remove(String word);
	
	// Show visual preview
	void print(String fileName);
}
