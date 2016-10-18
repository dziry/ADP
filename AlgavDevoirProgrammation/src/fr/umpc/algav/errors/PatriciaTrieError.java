package fr.umpc.algav.errors;

public class PatriciaTrieError extends Error {

	private static final long serialVersionUID = 1L;
	
	public PatriciaTrieError(String message) {  
		super("PatriciaTrieError : " + message); 
	}  

	public PatriciaTrieError(Throwable cause) {  
		super(cause); 
	}  

	public PatriciaTrieError(String message, Throwable cause) {  
		super("PatriciaTrieError : " + message, cause); 
	}

}