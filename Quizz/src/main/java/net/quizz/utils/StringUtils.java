package net.quizz.utils;


public class StringUtils {
	
	private final String string;
	private String answer;
	public StringUtils(String string) {
		this.string = string;

	}
	
	public StringUtils parse() {

		answer = string.replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("\s+", "")
                .replaceAll("[^A-Za-z]", "")
                .toLowerCase()
                .trim();
		
		return this;
	}
	
	
	public String getAnwer() { return answer; }
}
