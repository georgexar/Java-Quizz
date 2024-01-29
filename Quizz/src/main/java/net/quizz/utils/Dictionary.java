package net.quizz.utils;

import java.util.Map;
import java.util.*;

public class Dictionary {

	private final String input;
	private final Map<Character, Character> map;
	
	public Dictionary(String input) {
		this.input = input;
		this.map = new HashMap<>();
		
		
		// Converts s -> c, and c-> s
		map.put('s', 'c');
		map.put('c', 's');
		
		
		// Converts y -> u, and u -> y
		map.put('y', 'u');
		map.put('u', 'y');
		
		
		// Converts j -> g, and g-> j
		map.put('j', 'g');
		map.put('g', 'j');
		
	}
	
	public List<String> getOutput() {
		
		List<String> words = new ArrayList<String>();
		StringBuilder str;
		int[] count = new int[20];
		for(int i = 0; i < input.length(); i++) 
		{
			str = new StringBuilder();
			char ch = input.charAt(i);
				
			if(map.containsKey(ch))
			{
				ch = map.get(ch);
				count[i] = i;
			} 
			str.append(ch);
			
			if(i == input.length()-1) {
				words.add(str.toString());
			}
			
		}
		
		for(int counter : count) {
			str = new StringBuilder();
			for(int i = 0; i < input.length(); i++) {
				if(i==counter && i <input.length()-1) {
					if(map.containsKey(input.charAt(counter))) {
					 str.append(Character.toString(map.get(input.charAt(counter))));
					} else {
						str.append(Character.toString(input.charAt(i)));
					}
				} else {
					str.append(Character.toString(input.charAt(i)));
				}
				
				if(i == input.length()-1) {
					words.add(str.toString());
				}
			}
		}
			
	
		return words;
	}
	public String getInput() {return input;}
	
	public int getIndex(int[] matrix, int index) {
		for(int i = 0; i < matrix.length; i++) {
			if(matrix[i] == index) {
				return i;
			}
		}
		return 0;
	}
	
	
}
