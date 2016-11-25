package fr.upmc.algav.tools;

import java.util.HashMap;

import fr.upmc.algav.hybridtries.HybridTrieNode;
import fr.upmc.algav.patriciatries.PatriciaTrieNode;

public class GraphPrinter {

	private GraphWriter fileObject;
	private HashMap<Color, String> colorToGraphValue;
	private HashMap<Style, String> styleToGraphValue;
	
	public GraphPrinter(String fileName) {
		fileObject = new GraphWriter(fileName);
		colorToGraphValue = new HashMap<>();
		styleToGraphValue = new HashMap<>();
		
		colorToGraphValue.put(Color.BLUE, "blue");
		colorToGraphValue.put(Color.RED, "red");
		colorToGraphValue.put(Color.GREEN, "green");
		colorToGraphValue.put(Color.BLACK, "black");
		colorToGraphValue.put(Color.DEFAULT, "default");
		
		styleToGraphValue.put(Style.SOLID, "solid");
		styleToGraphValue.put(Style.DASHED, "dashed");
		styleToGraphValue.put(Style.DOTTED, "dotted");
		styleToGraphValue.put(Style.BOLD, "bold");
		styleToGraphValue.put(Style.ROUNDED, "rounded");
		styleToGraphValue.put(Style.DIAGONALS, "diagonals");
		styleToGraphValue.put(Style.FILLED, "filled");
		styleToGraphValue.put(Style.DEFAULT, "default");
	}

	public void beginDirected() {
		fileObject.write("digraph G {\n");
	}

	public void beginUndirected() {
		fileObject.write("graph G {\n");
	}
	
	public void end() {
		fileObject.write("}");
		fileObject.close();
	}
	
	public void printNode(HybridTrieNode node) {
		fileObject.write("	\"" + node.getId() + "\";\n");
	}
	
	public void printNode(HybridTrieNode node, Color color, Color fontColor, Style style) {
		fileObject.write("	\"" + node.getId() + 
				         "\" [color=" + colorToGraphValue.get(color) + ", " +
			             "fontcolor=" + colorToGraphValue.get(fontColor) + ", " +
				             "style=" + styleToGraphValue.get(style) + "];\n");
	}
	
	public void printNodeLabel(HybridTrieNode node) {
		fileObject.write("	\"" + node.getId() + "\" [label=\""  + node.getId().charAt(0) + "\"];\n");
	}
	
	public void printEdge(HybridTrieNode startNode, HybridTrieNode arriveNode, Color color) {
		fileObject.write("	\"" + startNode.getId() + 
				     "\" -> \"" + arriveNode.getId() + 
				   "\" [color=" + colorToGraphValue.get(color) + "];\n");
	}

	public void printNode(PatriciaTrieNode node) {
		fileObject.write(" \"" + Integer.toString(node.getNodeId()) +
				"\" [shape=ellipse style=filled fillcolor=black];\n");
	}

	public void printEdge(String edgeValue, PatriciaTrieNode parentNode, PatriciaTrieNode childNode) {
		fileObject.write(
				" \"" + Integer.toString(parentNode.getNodeId()) +
				"\" [shape=ellipse style=filled fillcolor=black];\n" +
				" \"" + Integer.toString(childNode.getNodeId()) +
				"\" [shape=ellipse style=filled fillcolor=black];\n");

		fileObject.write("     \"" + Integer.toString(parentNode.getNodeId()) +
				"\" -- \"" + Integer.toString(childNode.getNodeId()) +
				"\" [label=\"" + edgeValue + "\"];\n");
	}
}
