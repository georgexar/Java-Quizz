package net.quizz.questions;

import net.quizz.questions.childs.Option;
import net.quizz.questions.childs.QuestionType;

import java.util.*;

public class QuestionLoader {

	private Set<Question> questions = new HashSet<>();
	
	public QuestionLoader() {
		
		/*
		 * True/False
		 */
		questions.add(new Question("In Java, the \"==\" operator can be used to compare the content of two strings for equality."
				, QuestionType.I, 31).addOptions(new Option(1, "True", false), new Option(2, "False", true)));
		questions.add(new Question("The \"break\" statement in Java is used to terminate the entire program."
				, QuestionType.I, 31).addOptions(new Option(1, "True", false), new Option(2, "False", true)));
		questions.add(new Question("Java supports multiple inheritance through the use of the \"extends\" keyword."
				, QuestionType.I, 31).addOptions(new Option(1, "True", false), new Option(2, "False", true)));
		
		/*
		 * Multiple Choices (1 choice)
		 */
		

		
		questions.add(new Question("Which keyword is used to declare a method in Java?"
				, QuestionType.II, 31)
				.addOptions(
				  new Option(1, "define", false)
				, new Option(2, "function", false)
				, new Option(3, "method", false)
				, new Option(4, "void", true)));
		
		questions.add(new Question("What is the default value of an integer variable in Java if it is not explicitly initialized?"
				, QuestionType.II, 31)
				.addOptions(
				  new Option(1, "0", true)
				, new Option(2, "1", false)
				, new Option(3, "-1", false)
				, new Option(4, "null", false)));
		
		questions.add(new Question("Which of the following is used to read input from the user in Java?"
				, QuestionType.II, 31)
				.addOptions(
				  new Option(1, "cin", false)
				, new Option(2, "Scanner", true)
				, new Option(3, "input()", false)
				, new Option(4, "read()", false)));
		
		/*
		 * Multiple Choices (with more than 1 answer)
		 */
		
		questions.add(new Question("Which of the following are valid access modifiers in Java? (Select all that apply)"
				, QuestionType.III, 61)
				.addOptions(
						
						new Option(1, "private", true),
						new Option(2, "protected", true),
						new Option(3, "internal", false),
						new Option(4, "public", true)
						
						));
		
		questions.add(new Question("Which data types are considered primitive in Java? (Select all that apply)"
				, QuestionType.III, 61)
				.addOptions(
						
						new Option(1, "int", true),
						new Option(2, "float", true),
						new Option(3, "String", false),
						new Option(4, "char", true)
						
						));
		
		questions.add(new Question("What are the components of a Java class? (Select all that apply)"
				, QuestionType.III, 61)
				.addOptions(
						
						new Option(1, "Fields", true),
						new Option(2, "Methods", true),
						new Option(3, "Constructors", true),
						new Option(4, "Events", false)
						
						));
		
		/*
		 * Filling Gap
		 */
		
		questions.add(new Question("In Java, the process of allocating memory to an object is known as ________________."
				, QuestionType.IV, 61)
				.addOptions(
						
						new Option(1, "instantiation", true)
						
						));
		
		questions.add(new Question("To execute a block of code repeatedly as long as a specified condition is true, Java developers often use the ________________ statement."
				, QuestionType.IV, 61)
				.addOptions(
						
						new Option(1, "while", true)
						
						));
		
		questions.add(new Question("In which programming language are we able to print a message using the function System.out.println(\"\"); ?"
				, QuestionType.IV, 61)
				.addOptions(
						
						new Option(1, "Java", true)
						
						));
		
		/*
		 * Match quizz
		 */
		questions.add(new Question("Matching Quiz: Match the logo with the text."
				, QuestionType.V, 61)
				.addOptions(
						
						new Option(1, "Java", false).setImagePath("resources/Java.png").setMatch("Java_"),
						new Option(2, "C#", false).setImagePath("resources/Csharp.png").setMatch("C#_"),
						new Option(3, "Python", false).setImagePath("resources/Python.png").setMatch("Python_")
						
						));
		
		questions.add(new Question("Matching Quiz: Match the logo with the text."
				, QuestionType.V, 61)
				.addOptions(
						
						new Option(1, "Ruby", false).setImagePath("resources/ruby.jpg").setMatch("Ruby_"),
						new Option(2, "HTML5", false).setImagePath("resources/html5.png").setMatch("HTML5_"),
						new Option(3, "JavaScript", false).setImagePath("resources/javascript.png").setMatch("JavaScript_")
						
						));
		
		
		questions.add(new Question("Matching Quiz: Match the logo with the text."
				, QuestionType.V, 61)
				.addOptions(
						
						new Option(1, "Rust", false).setImagePath("resources/Rust.png").setMatch("Rust_"),
						new Option(2, "CSS", false).setImagePath("resources/css.png").setMatch("CSS_"),
						new Option(3, "Pascal", false).setImagePath("resources/Pascal.png").setMatch("Pascal_")
						
						));
		
		/*
		 * Order By
		 */
		questions.add(new Question("Order the Code Execution: Arrange the Java Code Blocks in Correct Order."
				, QuestionType.VI, 91)
				.addOptions(
						
						new Option(1, "Declare variables", false),
						new Option(2, "Perform calculations", false),
						new Option(3, "Display results", false),
						new Option(4, "Accept user input", false)
						
						).addOrder(new int[] {4, 1, 2, 3})
						.addOrder(new int[] {3,2,1,4}));
		
		questions.add(new Question("Order the Object-Oriented Concepts: Arrange the Concepts in Inheritance Hierarchy."
				, QuestionType.VI, 91)
				.addOptions(
						
						new Option(1, "Interface", false),
						new Option(2, "Class", false),
						new Option(3, "Object", false),
						new Option(4, "Abstract", false)
						
						).addOrder(new int[] {3, 2, 4, 1})
						 .addOrder(new int[] {1,4,2,3}));
		
		questions.add(new Question("Order the Steps in Exception Handling: Arrange the Exception Handling Steps in Java."
				, QuestionType.VI, 91)
				.addOptions(
						
						new Option(1, "Catch the exception", false),
						new Option(2, "Perform normal program flow", false),
						new Option(3, "Monitor for exceptions", false),
						new Option(4, "Specify exception type in try block", false)
						
						).addOrder(new int[] {4, 3, 2, 1})
				    	 .addOrder(new int[] {1,2,3,4}));

		
	}
	

	
	public Set<Question> getQuestions() { return questions; }
	
	
}
