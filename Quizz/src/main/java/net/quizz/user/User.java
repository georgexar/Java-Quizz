package net.quizz.user;

import java.util.*;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import net.quizz.utils.*;
import net.quizz.utils.Dictionary;
import net.quizz.Main;
import net.quizz.questions.*;
import net.quizz.questions.childs.*;

public class User {
	
	private final UUID id;
	private List<Question> questions;
	private List<Question> loadedQuestions;
	private QuestionLoader questionLoader = Main.getQuestionLoader();
	private Map<Question, Answer> responded = new HashMap<>();
	private long timer;
	private long quizzTimer;
	private String progressionBar;
	private Question currentQuestion;

	
	public User() {
		this.id = UUID.randomUUID();
		this.questions = new ArrayList<>();
		this.loadedQuestions = new ArrayList<>();
		loadQuestions(6);
	}

	private void loadQuestions(int size) {
		
		for(Question question : questionLoader.getQuestions()) {
			loadedQuestions.add(question);
		}
		
		for(int i = 0; i < size;i++) {

			/*getAllSpecific(QuestionType.III).forEach(question-> {
				questions.add(question);
			});
			
			loadedQuestions.clear();
			*/
			Question question = loadedQuestions.get(ThreadLocalRandom.current().nextInt(loadedQuestions.size()));
				questions.add(question);
				loadedQuestions.remove(question);
		}
		
		questions.forEach(question->this.quizzTimer+=TimeUnit.SECONDS.toMillis(question.getInterval()));
		this.quizzTimer+=System.currentTimeMillis();
	}
	
	public Question nextQuestion() {
		timer = System.currentTimeMillis();
		int index;
		if(currentQuestion==null) index = 0; 
		index = questions.indexOf(currentQuestion);
		currentQuestion = questions.get(index+1);
		return currentQuestion;
		
	}
	
	public List<Question> getAllSpecific(QuestionType questionType) {
		List<Question> list = new ArrayList<>();
		for(Question loaded : this.loadedQuestions) {
			if(loaded.getQuestionType() == questionType) {
				list.add(loaded);
			}
		}
		return list;
	}
	
	public Question getSpecific(QuestionType questionType) {
		
		for(Question question : questionLoader.getQuestions()) {
			if(question.getQuestionType().equals(questionType)) {
				return question;
			}
		}
		return null;
	}
	
	public int parseAnswer(String answer) {
		for(Map.Entry<Integer, Option> entry : currentQuestion.getOptions().entrySet()) {
			if(entry.getValue().getOption().equalsIgnoreCase(answer)) {
				return entry.getKey();
			}
		}
		return 0;
	}
	
	public String printResults() {
		float correct = 0;
		for(Map.Entry<Question, Answer> entry : responded.entrySet()) {
			Question question = entry.getKey();
			Answer answer = entry.getValue();
			float answer_points = answer.getPoints();
			
			
			switch(question.getQuestionType()) {
			
			default:
			for(int valid : answer.getResponse()) {
				
				if(question.getOptions().get(valid)!=null) {
				if(question.getOptions().get(valid).isAnswer()) {
					//if(πατηθει κουμπι) {correct = correct + 0.5 ;}
					//else {
					correct+=answer_points;
					
					//}
					}
				}
			}
			break;
			case III:
				if(answer.getResponse().equals(getCorrectAnswers(question))) {
				//	if(πατηθει κουμπι) {correct = correct + 0.5 ;}
				//	else {
					correct+=answer_points;
				//	}
				}
				break;
			case IV:
				if(new StringUtils(answer.getWordAnswer()).parse().getAnwer().equalsIgnoreCase(getCorrectAnswer(question))
						|| new Dictionary(getCorrectAnswer(question)).getOutput().contains(new StringUtils(answer.getWordAnswer()).parse().getAnwer())) {
					correct++;
				}
				break;
				
			case V:
				if(this.checkMatching(answer.getWordAnswer())) {
					correct++;
				}
				break;
				
			case VI:
				if(matchOrder(answer.getOrder(), question)) {
					correct++;
				}
				break;
		}
		}

		return "<!DOCTYPE html>\r\n"
				+ "<html>\r\n"
				+ "<head>\r\n"
				+ "<meta charset=\"UTF-8\">\r\n"
				+ "<title>Quiz</title>\r\n"
				+ "<link rel=\"stylesheet\" href=\"quizz/ResultsStyle.css\" >\r\n"
				+ "</head>\r\n"
				+ "<body>\r\n"
				+ "\r\n"
				+ "<div class=\"result-box\">\r\n"
				+ "    <h1>Quiz Result</h1>\r\n"
				+ "    <table>\r\n"
				+ "        <tr>\r\n"
				+ "            <td>Total Questions</td>\r\n"
				+ "            <td><span class=\"total-question\">"+getRespondedTo().size()+"</td>\r\n"
				+ "        </tr>    \r\n"
				+ "        <tr>\r\n"
				+ "            <td>Correct</td>\r\n"
				+ "            <td><span class=\"total-correct\">"+correct+"</td>\r\n"
				+ "        </tr>\r\n"
				+ "        <tr>\r\n"
				+ "            <td>Wrong</td>\r\n"
				+ "            <td><span class=\"total-wrong\">"+(getRespondedTo().size()-correct)+"</td>\r\n"
				+ "        </tr>\r\n"
				+ "        <tr>\r\n"
				+ "            <td>Percentage</td>\r\n"
				+ "            <td><span class=\"percentage\">"+String.format("%.2f", ((float)correct/(float) getRespondedTo().size())*100f)+"%</td>\r\n"
				+ "        </tr>\r\n"
				+ "    </table>\r\n"
				+ "    <form method=\"post\" action=\"QuestionServlet\"><input type=\"submit\" name=\"retry\" id=\"retry\" class=\"btn\" value=\"Try Again\">\r\n"
				+ "    <input type=\"submit\" name=\"showAnswers\" id=\"showAnswers\" class=\"btn\" value=\"Check my Answers\"></form>\r\n"
				+ "</div>\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "</body>\r\n"
				+ "</html>";
	}
	
	public boolean checkMatching(String answer) {
		String[] splitter = answer.split(":");
		if(splitter.length != 3) return false;
		for(String x : splitter) {
			String[] matrix = x.split("_");
			if(!matrix[0].equalsIgnoreCase(matrix[1])) {
				return false;
			}
		}
		
		return true;
	}
	
	public String getCorrectAnswer(Question question) {
		return new StringUtils(question.getOptions().get(1).getOption()).parse().getAnwer();
	}
	
	public boolean matchOrder(int[] toMatch, Question question) {

	    boolean passed = true;
	    
	    for(int[] orders : question.getOrder()) {
	    	passed=true;
	    	for(int i = 0; i < toMatch.length;i++) {
	    		if(toMatch[i]!=orders[i]) {
	    			passed=false;
	    		}
	    	}
	    }
	    
	    return passed;
	}
	
	
	
	public List<Integer> getCorrectAnswers(Question question) {

		List<Integer> list = new ArrayList<>();
		for(Option options : question.getOptions().values()) {
			if(options.isAnswer()) {
				list.add(options.getId());
			}
		}
		return list;
	}
	
	
	public String printAnswers() {
		
		String text1 = questions.get(0) != null ? questions.get(0).getText() : "Invalid";
		String text2 = questions.get(1) != null ? questions.get(1).getText() : "Invalid";
		String text3 = questions.get(2) != null ? questions.get(2).getText() : "Invalid";
		String text4 = questions.get(3) != null ? questions.get(3).getText() : "Invalid";
		String text5 = questions.get(4) != null ? questions.get(4).getText() : "Invalid";
		String text6 = questions.get(5) != null ? questions.get(5).getText() : "Invalid";
		
		
		return "<!DOCTYPE html>\r\n"
				+ "<html lang=\"en\">\r\n"
				+ "\r\n"
				+ "    <head>\r\n"
				+ "        <meta charset=\"UTF-8\">\r\n"
				+ "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\r\n"
				+ "        <link rel=\"stylesheet\" href=\"quizz/printAnswers.css\">\r\n"
				+ "        <title>See Results</title>\r\n"
				+ "    </head>\r\n"
				+ "\r\n"
				+ "    <body>\r\n"
				+ "<button id=\"previous-page\" onclick=\"redirect()\">Go back..</button>"
				+ "        <div class=\"container\">\r\n"
				+ "            <div class=\"box\" id=\"1\"><h2 class=\"question\">"+ text1+ "</h2><p class=\"answer\">"+getGivenAnswer(questions.get(0))+"</p><p class=\"correct\">"+getRightAnswer(questions.get(0))+"</p></div>\r\n"
				+ "            <div class=\"box\" id=\"2\"><h2 class=\"question\">"+ text2+"</h2><p class=\"answer\">"+getGivenAnswer(questions.get(1))+"</p><p class=\"correct\">"+getRightAnswer(questions.get(1))+"</p></div>\r\n"
				+ "            <div class=\"box\" id=\"3\"><h2 class=\"question\">"+ text3+"</h2><p class=\"answer\">"+getGivenAnswer(questions.get(2))+"</p><p class=\"correct\">"+getRightAnswer(questions.get(2))+"</p></div>\r\n"
				+ "            <div class=\"box\" id=\"4\"><h2 class=\"question\">"+ text4+"</h2><p class=\"answer\">"+getGivenAnswer(questions.get(3))+"</p><p class=\"correct\">"+getRightAnswer(questions.get(3))+"</p></div>\r\n"
				+ "            <div class=\"box\" id=\"5\"><h2 class=\"question\">"+ text5+"</h2><p class=\"answer\">"+getGivenAnswer(questions.get(4))+"</p><p class=\"correct\">"+getRightAnswer(questions.get(4))+"</p></div>\r\n"
				+ "            <div class=\"box\" id=\"6\"><h2 class=\"question\">"+ text6+"</h2><p class=\"answer\">"+getGivenAnswer(questions.get(5))+"</p><p class=\"correct\">"+getRightAnswer(questions.get(5))+"</p></div>\r\n"
				+ "        </div>\r\n"
				+ "    </body>\r\n"
				+ "<script>"
				+ "function redirect() {"
				+ "window.location.href=\"QuestionServlet\";"
				+ ""
				+ ""
				+ ""
				+ ""
				+ "}"
				+ ""
				+ "</script>"
				+ ""
				+ "\r\n"
				+ "</html>";
	}

	
	private String getRightAnswer(Question question) {
		StringBuilder str = new StringBuilder();
		switch(question.getQuestionType()) {
		
		default:
			
			return "Invalid";
		
		case I:
		case II:
			str = new StringBuilder();
			str.append("Correct answer: ");

			for(Option options : question.getOptions().values()) {
				if(options.isAnswer())  {
					str.append(options.getOption());
				}
			}
			
			
			return str.toString();
		case III:
			str = new StringBuilder();
			str.append("Correct answer: <br>");
			for(Option options : question.getOptions().values()) 
			{
				if(options.isAnswer())  
				{
					String text_option = options.getOption();
					str.append("Option: ").append(text_option).append("<br>");
					
				}
			}
			return str.toString();
		case IV:

			return "Correct answer: " + new StringUtils(question.getOptions().get(1).getOption()).parse().getAnwer();
		case V:
			
			str.append("Correct answer: <br>");
			for(Option option : question.getOptions().values()) {
				str.append(option.getOption() + " -> <img src=\"" + option.getImagePath()+ "\" width=\"85px\" height=\"85px\">");
			}
			
			return str.toString();
			
		case VI: 
			str = new StringBuilder();
			str.append("Correct answer: <br>1. ");
			
			int count = 0;
			for(int[] order : question.getOrder()) {
				
				count++;
				if(count==2) str.append("<br> 2.");
			for(int j : order) {
				// # Array is already sorted!
				str.append(" > ").append(question.getOptions().get(j).getOption());

			}
			}
			return str.toString();
		}
	}
	
	
	
	
	
	
	
	
	private String getGivenAnswer(Question question) {
		Answer answer = getRespondedTo().get(question);
		StringBuilder str = new StringBuilder();
		switch(question.getQuestionType()) {

		default:
			return "Invalid";
		
		case I:
		case II:
			str = new StringBuilder();
			str.append("Your answer: ");
			for(int i : answer.getResponse()) {

				str.append(question.getOptions().get(i).getOption());
			}
			
			return str.toString();
		case III:
			str = new StringBuilder();
			str.append("Your answer: <br>");
			for(int i : answer.getResponse()) {
				String text_option = question.getOptions().get(i).getOption();
				str.append("Option: " + (i) + ". ").append(text_option).append("<br>");
			}
			return str.toString();
		case IV:
			return "Your answer: " + new StringUtils(answer.getWordAnswer()).parse().getAnwer();
		case V:
			
			String string = answer.getWordAnswer();
			String[] matrix = string.split(":");
			str = new StringBuilder();
			str.append("Your answer: ").append("<br>");
			for(String s : matrix) {
				String[] split = s.split("_");
				if(split.length > 1) str.append("" + split[0] + " -> <img src=\"" + getOptionFromText(question, split[1]).getImagePath()+"\" width=\"85px\" height=\"85px\">");
			}
			return str.toString();
			
		case VI: 
			str = new StringBuilder();
			str.append("Your answer: ");
			for(int i : answer.getOrder()) {
				if(i==0) {
					str.append("> X ");
					continue;
				}
				str.append(" > ").append(question.getOptions().get(i).getOption());
				
			}
			return str.toString();
		}
	}
	
	private Option getOptionFromText(Question question, String text) {
		for(Option options : question.getOptions().values()) {
			if(options.getOption().equalsIgnoreCase(text)) {
				return options;
			}
		}
		return null;
	}
	
	private String ArrayToString(int[] array) {
		StringBuilder str = new StringBuilder();
		for(int i : array) {
			str.append(i).append(" ");
		}
		return str.toString();
	}
	
	private String ListToString(List<Integer> list) {
		StringBuilder str = new StringBuilder();
		for(int i : list) {
			str.append(String.valueOf(i)).append(" ");
		}
		return str.toString();
	}
	
	public long getTimer() { return timer; }
	
	public long getQuizzTimer() {
		return quizzTimer;
	}
	public String getProgressionBar() { return progressionBar; }
	public Question getCurrentQuestion() { return currentQuestion; }
	public Map<Question, Answer> getRespondedTo() { return responded; }

	
	
	public void setTimer(long timer) {
		this.timer = timer;
		
	}



	
	
	
}
