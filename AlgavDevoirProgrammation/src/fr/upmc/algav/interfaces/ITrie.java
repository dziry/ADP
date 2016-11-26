package fr.upmc.algav.interfaces;

import java.util.ArrayList;
import java.util.Collection;

public interface ITrie {
	
	// Basic primitives
	boolean isEmpty();
	void insert(String word);
	void insert(Collection<String> words);
	void removeAll();
	
	// Advanced functions
	boolean search(String word);
	int getWordCount();
	ArrayList<String> getStoredWords();
	int getNullPointerCount();
	int getHeight();
	double getAverageDepthOfLeaves();
	int getPrefixCount(String word);
	boolean remove(String word);
	
	// Create file representing the trie
	void print(String fileName);
}
