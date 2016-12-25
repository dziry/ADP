package fr.upmc.algav.errors;

@SuppressWarnings("serial")
public class PatriciaTrieError extends Error {

	public PatriciaTrieError(String message) {  
		super(message); 
	}  

	public PatriciaTrieError(Throwable cause) {  
		super(cause); 
	}  

	public PatriciaTrieError(String message, Throwable cause) {  
		super(message, cause); 
	}
}