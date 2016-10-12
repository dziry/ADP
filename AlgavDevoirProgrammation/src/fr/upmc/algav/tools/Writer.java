package fr.upmc.algav.tools;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Writer {

	private final String DRAWABLES_DIRECTORY = System.getProperty("user.dir") + "/drawables/";
	private String fileName;
	private String filePath;
	BufferedWriter outPut;
	
	public Writer(String fileName) {
		this.fileName = fileName;
		this.filePath = DRAWABLES_DIRECTORY + fileName;		
		try {
			FileWriter fileWriter = new FileWriter(filePath, false);
			this.outPut = new BufferedWriter(fileWriter);
			System.out.print("Creating the file..\n");
		}
		catch(IOException ioe) {
			System.out.print("Writer() error : ");
			ioe.printStackTrace();
		}
	}
	
	public void write(String text) {
		try {						
			outPut.write(text);			
			outPut.flush();
			System.out.print("Writing in the file..\n");			
		}
		catch(IOException ioe) {
			System.out.print("write() error : ");
			ioe.printStackTrace();
		}
	}
	
	public void close() {
		try {									
			outPut.close();
			System.out.print("Closing the file..\n");
			System.out.println("It can be found in : " + filePath);
			System.out.println("Use this command line to open it : xdot " + fileName);
			System.out.println("If xdot is is not installed, tape first : sudo apt install xdot");
		}
		catch(IOException ioe) {
			System.out.print("Close() error : ");
			ioe.printStackTrace();
		}
	}
}
