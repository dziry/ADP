package fr.upmc.algav.errors;

@SuppressWarnings("serial")
public class HybridTrieError extends Error {
	
	public HybridTrieError(String message) {  
		super(message); 
	}  

	public HybridTrieError(Throwable cause) {  
		super(cause); 
	}  

	public HybridTrieError(String message, Throwable cause) {  
		super(message, cause); 
	}
}
