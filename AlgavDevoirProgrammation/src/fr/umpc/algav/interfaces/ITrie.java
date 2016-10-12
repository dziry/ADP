package fr.umpc.algav.interfaces;

import java.util.ArrayList;

public interface ITrie {
	
	// Basic primitives
	boolean isEmpty();
	void insert(String word);
	
	// Advanced functions
	boolean search(String word);
	int countWords();
	ArrayList<String> listWords();
	int countNull();
	int height();
	float averageDepth();
	int prefix(String word);
	ITrie remove(String word);
	
	// Show visual preview
	void print(String fileName);
}
