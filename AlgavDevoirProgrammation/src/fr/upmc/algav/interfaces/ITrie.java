package fr.upmc.algav.interfaces;

import java.util.ArrayList;

public interface ITrie {
	
	// Basic primitives
	boolean isEmpty();
	void insert(String word);
	void insert(ArrayList<String> words);
	
	// Advanced functions
	boolean search(String word);
	int countWords();
	ArrayList<String> listWords();
	int countNull();
	int height();
	double averageDepth();
	int prefix(String word);
	boolean remove(String word);
	
	// Show visual preview
	void print(String fileName);
}
