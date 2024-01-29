package net.quizz.questions.childs;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import java.util.*;


public class Option {

	private final int id;
	private final String option;
	private String imagePath;
	private final boolean answer;
	private String pair;
	
	public Option(int id, String option, boolean answer) {
		this.id = id;
		this.option = option;
		this.answer = answer;
	
	}
	
	public Option setMatch(String text) {
		pair=text;
		return this;
	}
	
	public Option setImagePath(String path) {
		this.imagePath = path;
		return this;
	}
	
	public int getId() { return id; }
	public String getOption() { return option; }
	public String getImagePath() { return imagePath; }
	public boolean isAnswer() { return answer; }
	public String getPair() { return pair; } 
	
	
}
