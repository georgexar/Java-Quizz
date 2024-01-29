package net.quizz.questions.childs;

import java.util.List;
import java.util.*;

public class Answer {
	
	private List<Integer> response;
	private int[] order;
	private Map<Integer, Integer> pairs = new HashMap<>();
	private String wordAnswer;
	private float points;
	
	public Answer setResponse(List<Integer> response) { this.response = response; return this;}
	public Answer setOrder(int[] order) { this.order = order; return this;}
	public Answer addPair(int k, int v) {
		pairs.put(k, v);
		return this;
	}
	
	public Answer removePair(int k, int v) {
		pairs.remove(k, v);
		return this;
		
	}
	
	public Answer setWordAnswer(String answer_str) {
		this.wordAnswer = answer_str;
		return this;
	}
	
	public Answer setPoints(float points) { this.points = points; return this; }
	
	public float getPoints() { return points; }
	public List<Integer> getResponse() { return response; }	
	public int[] getOrder() { return order; }
	public Map<Integer, Integer> getPairs() { return pairs; }
	public String getWordAnswer() { return wordAnswer; }
	public void parsePair(String pairs) {
		String[] parsing = pairs.split(":");
		
		for(String x : parsing) {
			String answer = x.replace("box", "").replace("image", "");
			
			addPair(Character.getNumericValue(answer.toCharArray()[0]), Character.getNumericValue(answer.toCharArray()[1]));
		}
	}

}
