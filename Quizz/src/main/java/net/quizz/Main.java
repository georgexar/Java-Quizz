package net.quizz;

import java.util.HashSet;
import java.util.Set;

import net.quizz.questions.QuestionLoader;
import net.quizz.user.User;

public class Main {

	private static QuestionLoader questionLoader = new QuestionLoader();
	private static Set<User> users = new HashSet<>();
	
	
	public static void main(String[] args) {

		
	}

	
	public static Set<User> getUsers() { return users; }
	
	public static QuestionLoader getQuestionLoader() { return questionLoader; }
	public static void setQuestionLoader(QuestionLoader valid) {
		questionLoader = valid;
		
	}
	
}
