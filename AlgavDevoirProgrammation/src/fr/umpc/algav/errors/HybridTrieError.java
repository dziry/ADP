package fr.umpc.algav.errors;

public class HybridTrieError extends Error {

	private static final long serialVersionUID = 1L;
	
	public HybridTrieError(String message) {  
		super("HybridTrieError : " + message); 
	}  

	public HybridTrieError(Throwable cause) {  
		super(cause); 
	}  

	public HybridTrieError(String message, Throwable cause) {  
		super("HybridTrieError : " + message, cause); 
	}

}
