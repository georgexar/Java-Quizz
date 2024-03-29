package net.quizz.questions;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.quizz.questions.childs.Option;
import net.quizz.questions.childs.QuestionType;
import net.quizz.user.User;
import net.quizz.utils.DateUtil;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

public class Question implements Cloneable {

	
	private final String question;
	private final QuestionType questionType;
	private final long maxInterval;
	private Map<Integer, Option> options;
	private List<int[]> orders;
	private Map<Integer, Integer> pairs;
	
	
	
	public Question(String question, QuestionType questionType, long maxInterval) {
		this.question = question;
		this.questionType = questionType;
		this.maxInterval = maxInterval;
		this.options = new HashMap<>();
		this.pairs = new HashMap<>();
		this.orders = new ArrayList<>();
	}
	
	public Question addPair(int key, int value) {this.pairs.put(key, value);return this;}
	
	public Question addOptions(Option... options) {
	
		for(Option option : options) {
			getOptions().put(option.getId(), option);
		}
		return this;
	}
	
	public Question setOrder(List<int[]> orders) { this.orders = orders; return this; }
	public Question addOrder(int[]... order) {
		Arrays.stream(order).forEach(o -> orders.add(o));
		return this;
	}
	
	public int getRandomWrong() {
		List<Integer> list = new ArrayList<>();
		
		for(Option option : options.values()) {
			if(!option.isAnswer()) {
				list.add(option.getId());
			}
		}
		
		Collections.shuffle(list);
		return list.get(0);
		
	}
	
	
	public String show(User user) throws UnknownHostException {
		String response = "";
		user.setTimer(System.currentTimeMillis()+TimeUnit.SECONDS.toMillis(maxInterval));
		switch(questionType) {
		case I:
			response="<!DOCTYPE html>\r\n"
					+ "<html>\r\n"
					+ "<head>\r\n"
					+ "<meta charset=\"UTF-8\">\r\n"
					+ "<title>True False Quiz!</title>\r\n"
					+ "<link rel=\"stylesheet\" href=\"quizz/TrueFalseStyle.css\"> \r\n"
					+ "</head>\r\n"
					+ "<body>\r\n"
					+ "<div class=\"app\">\r\n"
					+ "	<h1>Test your knowledge in Java</h1>\r\n"
					+ "	<div class=\"quiz\">\r\n"
					+ "		<h2 id=\"Question\">" +question+ "</h2>\r\n"
					+ "<p>Total Time left: <span id=\"spanTime2\" class=\"time2\">" + DateUtil.displayDuration(TimeUnit.MILLISECONDS.toSeconds(user.getQuizzTimer()-System.currentTimeMillis()-1000))+ "s</span></p>\r\n"
+ "        <p>Question Time left: <span id=\"spanTime\" class=\"time\">" + DateUtil.displayDuration(TimeUnit.MILLISECONDS.toSeconds(user.getTimer()-System.currentTimeMillis()-1000))+ "s</span></p>\r\n"					+ "        <div class=\"progress\"><div id=\"progressbar\" class=\"progress-inner\"></div></div>"
					+ "</div>\r\n"  
					+ "		<div id=\"answer buttons\">\r\n"
					+ "			<form method=\"post\" action=\"QuestionServlet\">\r\n"
					+ "			<input type=\"submit\" id=\"answer\" name=\"answer\" class=\"btn\" value=\"" + getOptions().get(1).getOption()+ "\"><br>\r\n"
					+ "			<input type=\"submit\" id=\"answer\" name=\"answer\" class=\"btn\" value=\"" + getOptions().get(2).getOption()+ "\"><br>\r\n"
					+ "			</form>\r\n"
					+ "		</div>\r\n"
					+ "\r\n"
					+ "	</div>\r\n"
					+ "</div>\r\n"

					+ "\r\n"
					+ "</body>\r\n"
					+ "<script>"
					+ "var timer;"
					+ "let changedBar = document.getElementById(\"bar\");"
					+ "let finalTime = " + user.getTimer() + "-Date.now();"
					+ "const progressBar = document.querySelector(\".progress-inner\");"
					+ "timer = setInterval(function(){"
					+ "let time = " + user.getTimer() + "-Date.now();"  
					+ "let variable = new Date(time).toISOString().slice(14,19);"
					+ ""
					+ "let total = " + user.getQuizzTimer() + "-Date.now();"
					+ "let totalTimer = new Date(total).toISOString().slice(14,19);"
					+ "if(time/1000 <= 0) {" 
					+ ""
					+ "progressBar.style.width=\"0%\";"
					+ "document.getElementById(\"spanTime\").innerHTML=\"Time is over!\";"
					+ "document.getElementById(\"spanTime\").value=\'0\';"  
					+ "clearInterval(timer);"
					+ "window.location.assign(\'QuestionServlet\');"
					+ "}else{"
					+ "document.getElementById(\"spanTime2\").innerHTML=totalTimer + \"s\";"
					+ "document.getElementById(\"spanTime\").innerHTML=variable + \"s\";"  
					+ "document.getElementById(\"spanTime\").value=\'time/1000\';"  
					+ "let progressWidth=((time/1000)/(finalTime/1000)) * 100;\r\n" 
					+ "            \r\n" 
					+ "if(time/1000 > 0) {"
					+ "progressBar.style.width = progressWidth + \"%\";"
					+ "checkColors(progressWidth);"
					+ "}}"
					+ "}, 1000);"
							
					+ "    const checkColors =(width) => {\r\n"
					+ "        if(width>60){\r\n"
					+ "            progressBar.style.background = \"green\";\r\n"
					+ "            \r\n"
					+ "        }else if(width>30){\r\n"
					+ "            progressBar.style.background=\"yellow\";\r\n"
					+ "        }else{\r\n"
					+ "            progressBar.style.background=\"red\";\r\n"
					+ "        }\r\n"
					+ "    }"
					+ ""
					+ ""
					+ ""
					+ ""
					+ "</script>"
					+ "</html>";
			break;
		case II:		
			response="<!DOCTYPE html>\r\n"
					+ "<html>\r\n"
					+ "<head>\r\n"
					+ "<meta charset=\"UTF-8\">\r\n"
					+ "<title>Quiz</title>\r\n"
					+ "<link rel=\"stylesheet\" href=\"quizz/MultiChoiceStyle.css\">\r\n"
					+ "</head>\r\n"
					+ "<body>\r\n"
					+ "<div class=\"app\">\r\n"
					+ "    <h1>Simple Quiz</h1>\r\n"
					+ "    <div class=\"quiz\">\r\n"
					+ "        <h2 id=\"question\">" +question+"</h2>\r\n"
					+ "<p>Total Time left: <span id=\"spanTime2\" class=\"time2\">" + DateUtil.displayDuration(TimeUnit.MILLISECONDS.toSeconds(user.getQuizzTimer()-System.currentTimeMillis()-1000))+ "s</span></p>\r\n"
+ "        <p>Question Time left: <span id=\"spanTime\" class=\"time\">" + DateUtil.displayDuration(TimeUnit.MILLISECONDS.toSeconds(user.getTimer()-System.currentTimeMillis()-1000))+ "s</span></p>\r\n"					+ "        <div class=\"progress\"><div id=\"progressbar\" class=\"progress-inner\"></div></div>"
					+ "        <div id=\"answer-buttons\">\r\n"
					+ "<form id=\"quizz\" method=\"post\" action=\"QuestionServlet\">\r\n"
					+ "<input type=\"submit\" selected id=\"answer\" name=\"answer\" class=\"btn 1\" value=\"" + options.get(1).getOption() + "\">\r\n"
					+ "<input type=\"submit\" id=\"answer\" name=\"answer\" class=\"btn 2\" value=\"" + options.get(2).getOption() + "\" onclick=\"select()\">\r\n"
					+ "<input type=\"submit\" id=\"answer\" name=\"answer\" class=\"btn 3\" value=\"" + options.get(3).getOption() + "\">\r\n"
					+ "<input type=\"submit\" id=\"answer\" name=\"answer\" class=\"btn 4\" value=\"" + options.get(4).getOption() + "\">\r\n"
					+ "<br><input type=\"button\" id=\"next-btn\" name=\"next-btn\"  value=\"help\" onclick=\"disableWrongAnswer()\">"
					+ "<input type=\"textarea\" name=\"helpbutton\" id=\"helpbutton\" hidden>\r\n"
					+ "        </div>\r\n"
					+ "    </div></form>\r\n"
					+ "</div>\r\n"	
					+ "\r\n"
					+ "</body>\r\n"
					+ "<script>\r\n"
					+ " function disableWrongAnswer() { \r\n "
					+ "let nodeList = document.querySelectorAll(\".btn\");"
					+ "for (let i = 0; i < nodeList.length; i++) {\r\n"
					+ "let match = nodeList[i];"
					+ "    if(match.value===\"" + options.get(this.getRandomWrong()).getOption() +"\") {\r\n"
					+ "match.remove();\r\n"
					+ "document.getElementById(\"next-btn\").remove();\r\n	"
					+ "nodeList = document.querySelectorAll(\".btn\");"
					+ "document.getElementById(\"helpbutton\").value =\"sjfnadfjkgnfg\";"
					+ "         break;\r\n"
					+ "}}"
					+ ""
					+ "}"
					+ ""
					+ ""
					+ ""
					+ ""
					+ ""
					+ ""
					+ ""
					+ ""
					+ ""
					+ ""
					+ ""
					+ ""
					+ ""
					+ ""
					+ ""
					+ ""
					+ ""
					+ ""
					+ ""
					+ ""
					+ ""
					+ ""
					+ ""
					+ ""
					+ "var timer;"
					+ "let changedBar = document.getElementById(\"bar\");"
					+ "let finalTime = " + user.getTimer() + "-Date.now();"
					+ "const progressBar = document.querySelector(\".progress-inner\");"
					+ "timer = setInterval(function(){"
					+ "let time = " + user.getTimer() + "-Date.now();"  
					+ "let variable = new Date(time).toISOString().slice(14,19);"
					+ ""
					+ "let total = " + user.getQuizzTimer() + "-Date.now();"
					+ "let totalTimer = new Date(total).toISOString().slice(14,19);"
					+ "if(time/1000 <= 0) {" 
					+ ""
					+ "progressBar.style.width=\"0%\";"
					+ "document.getElementById(\"spanTime\").innerHTML=\"Time is over!\";"
					+ "document.getElementById(\"spanTime\").value=\'0\';"  
					+ "clearInterval(timer);"
					+ "window.location.assign(\'QuestionServlet\');"
					+ "}else{"
					+ "document.getElementById(\"spanTime2\").innerHTML=totalTimer + \"s\";"
					+ "document.getElementById(\"spanTime\").innerHTML=variable + \"s\";"  
					+ "document.getElementById(\"spanTime\").value=\'time/1000\';"  
					+ "let progressWidth=((time/1000)/(finalTime/1000)) * 100;\r\n" 
					+ "            \r\n" 
					+ "if(time/1000 > 0) {"
					+ "progressBar.style.width = progressWidth + \"%\";"
					+ "checkColors(progressWidth);"
					+ "}}"
					+ "}, 1000);"
							
					+ "    const checkColors =(width) => {\r\n"
					+ "        if(width>60){\r\n"
					+ "            progressBar.style.background = \"green\";\r\n"
					+ "            \r\n"
					+ "        }else if(width>30){\r\n"
					+ "            progressBar.style.background=\"yellow\";\r\n"
					+ "        }else{\r\n"
					+ "            progressBar.style.background=\"red\";\r\n"
					+ "        }\r\n"
					+ "    }"
					+ "</script>"
					
					+ "</html>";
			break;
		case III:
			response="<!DOCTYPE html>\r\n"
					+ "<html>\r\n"
					+ "<head>\r\n"
					+ "<meta charset=\"UTF-8\">\r\n"
					+ "<title>Quiz</title>\r\n"
					+ "<link rel=\"stylesheet\" href=\"quizz/MultipleChoise2Style.css\" >\r\n"
					+ "</head>\r\n"
					+ "<body>\r\n"
					+ "<div class=\"app\">\r\n"
					+ "    <h1>Simple Quiz</h1>\r\n"
					+ "    <div class=\"quiz\">\r\n"
					+ "        <h2 id=\"question\">"+question+"</h2>\r\n"
												+ "<p>Total Time left: <span id=\"spanTime2\" class=\"time2\">" + DateUtil.displayDuration(TimeUnit.MILLISECONDS.toSeconds(user.getQuizzTimer()-System.currentTimeMillis()-1000))+ "s</span></p>\r\n"
					+ "        <p>Question Time left: <span id=\"spanTime\" class=\"time\">" + DateUtil.displayDuration(TimeUnit.MILLISECONDS.toSeconds(user.getTimer()-System.currentTimeMillis()-1000))+ "s</span></p>\r\n"
					+ "        <div class=\"progress\"><div id=\"progressbar\" class=\"progress-inner\"></div></div>"
					+"<form method=\"post\" action=\"QuestionServlet\">"
					+ "        <div class=\"row\">\r\n"
					+ "            <p>"+getOptions().get(1).getOption()+"</p>\r\n"
					+ "            <label>\r\n"
					+ "            <input type=\"checkbox\" id=\"radio1\" class=\"test 1\" name=\"radio1\" value=\""+getOptions().get(1).getOption()+"\">\r\n"
					+ "            </label>\r\n"
					+ "        </div>\r\n"
					+ "        <div class=\"row\">\r\n"
					+ "            <p>"+getOptions().get(2).getOption()+"</p>\r\n"
					+ "            <label>\r\n"
					+ "            <input type=\"checkbox\" id=\"radio2\" class=\"test 2\" name=\"radio2\" value=\""+getOptions().get(2).getOption()+"\">\r\n"
					+ "            </label>\r\n"
					+ "        </div>\r\n"
					+ "        <div class=\"row\">\r\n"
					+ "            <p>"+getOptions().get(3).getOption()+"</p>\r\n"
					+ "            <label>\r\n"
					+ "            <input type=\"checkbox\" id=\"radio3\" class=\"test 3\" name=\"radio3\" value=\""+getOptions().get(3).getOption()+"\">\r\n"
					+ "            </label>\r\n"
					+ "        </div>\r\n"
					+ "        <div class=\"row\">\r\n"
					+ "            <p>"+getOptions().get(4).getOption()+"</p>\r\n"
					+ "            <label>\r\n"
					+ "            <input type=\"checkbox\" id=\"radio4\" class=\"test 4\" name=\"radio4\" value=\""+getOptions().get(4).getOption()+"\">\r\n"
					+ "            </label>\r\n"
					+ "        </div>\r\n"
					+ "        <input type=\"submit\" id=\"next-btn\" value=\"Next\">\r\n"
					+ "<br><input type=\"button\" id=\"help-btn\" name=\"help-btn\"  value=\"help\" onclick=\"disableWrongAnswer()\">"
					+ "<input type=\"textarea\" name=\"helpbutton\" id=\"helpbutton\" hidden>\r\n"
					+ "</form>"
					+ "    </div>\r\n"
					+ "</div>\r\n"
					+ "</body>\r\n"
					+ "\r\n"
					+ "<script>"
					+ "var timer;"
					+ "let changedBar = document.getElementById(\"bar\");"
					+ "let finalTime = " + user.getTimer() + "-Date.now();"
					+ "const progressBar = document.querySelector(\".progress-inner\");"
					+ "timer = setInterval(function(){"
					+ "let time = " + user.getTimer() + "-Date.now();"  
					+ "let variable = new Date(time).toISOString().slice(14,19);"
					+ "let total = " + user.getQuizzTimer() + "-Date.now();"
					+ "let totalTimer = new Date(total).toISOString().slice(14,19);"
					+ ""
					+ "if(time/1000 <= 0) {" 
					+ ""
					+ "progressBar.style.width=\"0%\";"
					+ "document.getElementById(\"spanTime\").innerHTML=\"Time is over!\";"
					+ "document.getElementById(\"spanTime\").value=\'0\';"  
					+ "clearInterval(timer);"
					+ "window.location.assign(\'QuestionServlet\');"
					+ "}else{"
					+ "document.getElementById(\"spanTime2\").innerHTML=totalTimer + \"s\";"
					+ "document.getElementById(\"spanTime\").innerHTML=variable + \"s\";"  
					+ "document.getElementById(\"spanTime\").value=\'time/1000\';"  
					+ "let progressWidth=((time/1000)/(finalTime/1000)) * 100;\r\n" 
					+ "            \r\n" 
					+ "if(time/1000 > 0) {"
					+ "progressBar.style.width = progressWidth + \"%\";"
					+ "checkColors(progressWidth);"
					+ "}}"
					+ "}, 1000);"
							
					+ "    const checkColors =(width) => {\r\n"
					+ "        if(width>60){\r\n"
					+ "            progressBar.style.background = \"green\";\r\n"
					+ "            \r\n"
					+ "        }else if(width>30){\r\n"
					+ "            progressBar.style.background=\"yellow\";\r\n"
					+ "        }else{\r\n"
					+ "            progressBar.style.background=\"red\";\r\n"
					+ "        }\r\n"
					+ "    }"
					
					+ "\r\n"
					+ "function disableWrongAnswer() { \r\n "
					+ "let nodeList = document.querySelectorAll(\".test\");"
					+ "for (let i = 0; i < nodeList.length; i++) {\r\n"
					+ "let match = nodeList[i];"
					+ "    if(match.value===\"" + options.get(this.getRandomWrong()).getOption() +"\") {\r\n"
					+ "match.remove();\r\n"
					+ "document.getElementById(\"help-btn\").remove();\r\n	"
					+ "document.getElementById(\"helpbutton\").value =\"sjfnadfjkgnfg\";"
					+ "         break;\r\n"
					+ "}}"
					+ ""
					+ "}"
					+ "</script>"
					+ "\r\n"
					+ "</html>";
			break;
			
		case IV:
			response="<!DOCTYPE html>\r\n"
					+ "<html>\r\n"
					+ "<head>\r\n"
					+ "<meta charset=\"UTF-8\">\r\n"
					+ "<title>Quiz</title>\r\n"
					+ "<link rel=\"stylesheet\" href=\"quizz/MultiChoiceStyle.css\" >\r\n"
					+ "</head>\r\n"
					+ "<body>\r\n"
					+ "<div class=\"app\">\r\n"
					+ "    <h1>Simple Quiz</h1>\r\n"
					+ "    <div class=\"quiz\">\r\n"
					+ "    <form method=\"post\" action=\"QuestionServlet\">\r\n"
					+ "        <h2 id=\"question\">" + question +"</h2>\r\n"
					+ "<p>Total Time left: <span id=\"spanTime2\" class=\"time2\">" + DateUtil.displayDuration(TimeUnit.MILLISECONDS.toSeconds(user.getQuizzTimer()-System.currentTimeMillis()-1000))+ "s</span></p>\r\n"
+ "        <p>Question Time left: <span id=\"spanTime\" class=\"time\">" + DateUtil.displayDuration(TimeUnit.MILLISECONDS.toSeconds(user.getTimer()-System.currentTimeMillis()-1000))+ "s</span></p>\r\n"					+ "        <div class=\"progress\"><div id=\"progressbar\" class=\"progress-inner\"></div></div>"
					+ "<br><input type=\"text\" id=\"answer\" name=\"answer\" style=\"width:180px;\">\r\n"
					+ "        <input type=\"submit\" id=\"next-btn\" value=\"Next\">\r\n"
					+ "    </form>\r\n"
					+ "    </div>\r\n"
					+ "</div>\r\n"
					+ "\r\n"
					+ "</body>\r\n"
					+ "<script>"
					+ "var timer;"
					+ "let changedBar = document.getElementById(\"bar\");"
					+ "let finalTime = " + user.getTimer() + "-Date.now();"
					+ "const progressBar = document.querySelector(\".progress-inner\");"
					+ "timer = setInterval(function(){"
					+ "let time = " + user.getTimer() + "-Date.now();"  
					+ "let variable = new Date(time).toISOString().slice(14,19);"
					+ ""
					+ "let total = " + user.getQuizzTimer() + "-Date.now();"
					+ "let totalTimer = new Date(total).toISOString().slice(14,19);"
					+ "if(time/1000 <= 0) {" 
					+ ""
					+ "progressBar.style.width=\"0%\";"
					+ "document.getElementById(\"spanTime\").innerHTML=\"Time is over!\";"
					+ "document.getElementById(\"spanTime\").value=\'0\';"  
					+ "clearInterval(timer);"
					+ "window.location.assign(\'QuestionServlet\');"
					+ "}else{"
					+ "document.getElementById(\"spanTime2\").innerHTML=totalTimer + \"s\";"
					+ "document.getElementById(\"spanTime\").innerHTML=variable + \"s\";"  
					+ "document.getElementById(\"spanTime\").value=\'time/1000\';"  
					+ "let progressWidth=((time/1000)/(finalTime/1000)) * 100;\r\n" 
					+ "            \r\n" 
					+ "if(time/1000 > 0) {"
					+ "progressBar.style.width = progressWidth + \"%\";"
					+ "checkColors(progressWidth);"
					+ "}}"
					+ "}, 1000);"
							
					+ "    const checkColors =(width) => {\r\n"
					+ "        if(width>60){\r\n"
					+ "            progressBar.style.background = \"green\";\r\n"
					+ "            \r\n"
					+ "        }else if(width>30){\r\n"
					+ "            progressBar.style.background=\"yellow\";\r\n"
					+ "        }else{\r\n"
					+ "            progressBar.style.background=\"red\";\r\n"
					+ "        }\r\n"
					+ "    }"
					+ "</script>"
					+ "</html>";
			break;
		case V:
			List<Integer> shuffleList = new ArrayList<>();
			List<Integer> shuffleList2 = new ArrayList<>();
			
			shuffleList.addAll(getOptions().keySet());
			shuffleList2.addAll(getOptions().keySet());
			Collections.shuffle(shuffleList);
			Collections.shuffle(shuffleList2);
			
			response="<!DOCTYPE html>\r\n"
					+ "<html>\r\n"
					+ "<head>\r\n"
					+ "<meta charset=\"UTF-8\">\r\n"
					+ " <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
					+ "<title>Quiz</title>\r\n"
					+ "<link rel=\"stylesheet\" href=\"quizz/MatchingStyle.css\" >\r\n"
					+ "</head>\r\n"
					+ "<body>\r\n"
					+ "<div class=\"app\">\r\n"
					+ "	<h1>Matching Quiz</h1>\r\n"
					+ "	<h2 id=\"question\">Match the logo with the text!</h2>\r\n"
					+ "<p>Total Time left: <span id=\"spanTime2\" class=\"time2\">" + DateUtil.displayDuration(TimeUnit.MILLISECONDS.toSeconds(user.getQuizzTimer()-System.currentTimeMillis()-1000))+ "s</span></p>\r\n"
+ "        <p>Question Time left: <span id=\"spanTime\" class=\"time\">" + DateUtil.displayDuration(TimeUnit.MILLISECONDS.toSeconds(user.getTimer()-System.currentTimeMillis()-1000))+ "s</span></p>\r\n"					+ "        <div class=\"progress\"><div id=\"progressbar\" class=\"progress-inner\"></div></div>"
					+ "	 <div id=\"draggableContainer\">\r\n"
					+ "        <img id=\"" + getOptions().get(shuffleList.get(0)).getOption() + "\" class=\"draggable\" src=\""+getOptions().get(shuffleList.get(0)).getImagePath()+"\" alt=\"Image 1\">\r\n"
					+ "        <img id=\"" + getOptions().get(shuffleList.get(1)).getOption() + "\"class=\"draggable\" src=\""+getOptions().get(shuffleList.get(1)).getImagePath()+"\" alt=\"Image 2\">\r\n"
					+ "        <img id=\"" + getOptions().get(shuffleList.get(2)).getOption() + "\" class=\"draggable\" src=\""+getOptions().get(shuffleList.get(2)).getImagePath()+"\" alt=\"Image 3\">\r\n"
					+ "    	<img id=\"empty\" hidden>\r\n"
					+ "    	</div>\r\n"
					+ "         <div class=\"dropContainer\">\r\n"
					+ "			 <label for=\""+getOptions().get(shuffleList2.get(0)).getPair()+"\">"+getOptions().get(shuffleList2.get(0)).getOption()+"<div id=\""+getOptions().get(shuffleList2.get(0)).getPair()+"\" class=\"drop-box\" ondrop=\"drop(event)\" ondragover=\"allowDrop(event)\"></div></label>\r\n"
					+ "            <label for=\""+getOptions().get(shuffleList2.get(1)).getPair()+"\">"+getOptions().get(shuffleList2.get(1)).getOption()+"<div id=\""+getOptions().get(shuffleList2.get(1)).getPair()+ "\" class=\"drop-box\" ondrop=\"drop(event)\" ondragover=\"allowDrop(event)\"></div></label>\r\n"
					+ "            <label for=\""+getOptions().get(shuffleList2.get(2)).getPair()+"\">"+getOptions().get(shuffleList2.get(2)).getOption()+"<div id=\""+getOptions().get(shuffleList2.get(2)).getPair() +"\" class=\"drop-box\" ondrop=\"drop(event)\" ondragover=\"allowDrop(event)\"></div></div></label>\r\n"
					+ "<br>    <div class=\"form-class\">"
					+ "        <button id=\"reset-btn\" onclick=\"reset()\">Reset</button>\r\n<br><br>"
					+ "<form method=\"post\" action=\"QuestionServlet\"><input type=\"textarea\" name=\"order\" id=\"order\" hidden><input type=\"submit\" id=\"next-btn\" value=\"Next\"></form>\r\n"
					+ "</div>\r\n"
					+ "</div>\r\n"
					+ "\r\n"
					+ "\r\n"
					+ " <script>\r\n"
					+ "var timer;"
					+ "let changedBar = document.getElementById(\"bar\");"
					+ "let finalTime = " + user.getTimer() + "-Date.now();"
					+ "const progressBar = document.querySelector(\".progress-inner\");"
					+ "timer = setInterval(function(){"
					+ "let time = " + user.getTimer() + "-Date.now();"  
					+ "let variable = new Date(time).toISOString().slice(14,19);"
					+ "let total = " + user.getQuizzTimer() + "-Date.now();"
					+ "let totalTimer = new Date(total).toISOString().slice(14,19);"
					+ ""
					+ "if(time/1000 <= 0) {" 
					+ ""
					+ "progressBar.style.width=\"0%\";"
					+ "document.getElementById(\"spanTime\").innerHTML=\"Time is over!\";"
					+ "document.getElementById(\"spanTime\").value=\'0\';"  
					+ "clearInterval(timer);"
					+ "window.location.assign(\'QuestionServlet\');"
					+ "}else{"
					+ "document.getElementById(\"spanTime2\").innerHTML=totalTimer + \"s\";"
					+ "document.getElementById(\"spanTime\").innerHTML=variable + \"s\";"  
					+ "document.getElementById(\"spanTime\").value=\'time/1000\';"  
					+ "let progressWidth=((time/1000)/(finalTime/1000)) * 100;\r\n" 
					+ "            \r\n" 
					+ "if(time/1000 > 0) {"
					+ "progressBar.style.width = progressWidth + \"%\";"
					+ "checkColors(progressWidth);"
					+ "}}"
					+ "}, 1000);"
							
					+ "    const checkColors =(width) => {\r\n"
					+ "        if(width>60){\r\n"
					+ "            progressBar.style.background = \"green\";\r\n"
					+ "            \r\n"
					+ "        }else if(width>30){\r\n"
					+ "            progressBar.style.background=\"yellow\";\r\n"
					+ "        }else{\r\n"
					+ "            progressBar.style.background=\"red\";\r\n"
					+ "        }\r\n"
					+ "    }"
					+ "	 \r\n"
					+ "	 function reset() {\r\n"
					+ "		 \r\n"
					+ "		 let draggableContainer = document.getElementById(\"draggableContainer\");\r\n"
					+ "		 let container1 = document.getElementById(\""+getOptions().get(1).getPair()+"\");\r\n"
					+ "		 let container2 = document.getElementById(\"" +getOptions().get(2).getPair()+"\");\r\n"
					+ "		 let container3 = document.getElementById(\""+getOptions().get(3).getPair()+"\");\r\n"
					+ "      document.getElementById(\'order\').value=\'\';"
					+ "		 for(elements of container1.children) {\r\n"
					+ "			 container1.removeChild(elements);\r\n"
					+ "			draggableContainer.appendChild(elements);\r\n"
					+ "	 }\r\n"
					+ "	 		 for(elements of container2.children) {\r\n"
					+ "			 container2.removeChild(elements);\r\n"
					+ "			 draggableContainer.appendChild(elements);\r\n"
					+ "	 }\r\n"
					+ "	 		 for(elements of container3.children) {\r\n"
					+ "			 container3.removeChild(elements);\r\n"
					+ "	 		draggableContainer.appendChild(elements);\r\n"
					+ "	 }\r\n"
					+ "	 }\r\n"
					+ "	 \r\n"
					+ "        // Function to make elements draggable\r\n"
					+ "        document.addEventListener(\"DOMContentLoaded\", function() {\r\n"
					+ "            var draggables = document.querySelectorAll('.draggable');\r\n"
					+ "            draggables.forEach(function(draggable) {\r\n"
					+ "                draggable.draggable = true;\r\n"
					+ "                draggable.addEventListener('dragstart', function (e) {\r\n"
					+ "                    e.dataTransfer.setData('text/plain', draggable.id);\r\n"
					+ "                });\r\n"
					+ "            });\r\n"
					+ "        });\r\n"
					+ "\r\n"
					+ "        // Function to allow elements to be dropped\r\n"
					+ "        function allowDrop(event) {\r\n"
					+ "            event.preventDefault();\r\n"
					+ "        }\r\n"
					+ "\r\n"
					+ "        // Function to handle dropping of elements into the boxes\r\n"
					+ "        function drop(event) {\r\n"
					+ "            event.preventDefault();\r\n"
					+ "            var data = event.dataTransfer.getData('text/plain');\r\n"
					+ "            var draggableElement = document.getElementById(data);\r\n"
					+ "            var dropZone = event.target;\r\n"
					+ "\r\n"
					+ "            // Check if the dropped element is an image and the drop zone is a box\r\n"
					+ "            if (draggableElement.classList.contains('draggable') && dropZone.classList.contains('drop-box')) {\r\n"
					+ "                dropZone.innerHTML ='';\r\n"
					+ "                dropZone.appendChild(draggableElement);\r\n"
					+ "                document.getElementById(\'order\').value+=dropZone.id+ \'\' + draggableElement.id + \':\';\r\n"
					+ "            }\r\n"
					+ "        }\r\n"
					+ "       /* function drop(event) {\r\n"
					+ "    event.preventDefault();\r\n"
					+ "    var data = event.dataTransfer.getData('text/plain');\r\n"
					+ "    var draggableElement = document.getElementById(data);\r\n"
					+ "    var dropZone = event.target;\r\n"
					+ "\r\n"
					+ "    // Check if the dropped element is an image and the drop zone is a box\r\n"
					+ "    if (draggableElement.classList.contains('draggable') && dropZone.classList.contains('drop-box')) {\r\n"
					+ "        // Create a placeholder element\r\n"
					+ "        var placeholder = document.createElement('div');\r\n"
					+ "        placeholder.style.width = '100px'; // Set the width to match the dragged element\r\n"
					+ "        placeholder.style.height = '100px'; // Set the height to match the dragged element\r\n"
					+ "\r\n"
					+ "        // Insert the placeholder before the drop zone content\r\n"
					+ "        dropZone.insertBefore(placeholder, dropZone.firstChild);\r\n"
					+ "\r\n"
					+ "        // Remove any existing content in the drop zone\r\n"
					+ "        dropZone.innerHTML = '';\r\n"
					+ "\r\n"
					+ "        // Append the dragged element to the drop zone\r\n"
					+ "        dropZone.appendChild(draggableElement);\r\n"

					+ "    }\r\n"
					+ "}*/\r\n"
					+ "        \r\n"
					+ "        \r\n"
					+ "    </script>\r\n"
					+ "\r\n"
					+ "\r\n"
					+ "\r\n"
					+ "</body>\r\n"
					+ "</html>";
			break;
		case VI:
			response="<!DOCTYPE html>\r\n"
					+ "<html>\r\n"
					+ "    <head>\r\n"
					+ "        <meta charset=\"UTF-8\" name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
					+ "        <link rel=\"stylesheet\" href=\"quizz/DragDropStyle.css\">\r\n"
					+ "    </head>\r\n"
					+ "    <body>\r\n"
					+ "\r\n"
					+ "        <div class=\"app\">\r\n"
					+ "            <h1>Simple Quiz</h1>\r\n"
					+ "\r\n"
					+ "            <div class=\"question\"><h2>"+question+"</h2></div>\r\n"
					+ "<p>Total Time left: <span id=\"spanTime2\" class=\"time2\">" + DateUtil.displayDuration(TimeUnit.MILLISECONDS.toSeconds(user.getQuizzTimer()-System.currentTimeMillis()-1000))+ "s</span></p>\r\n"
+ "        <p>Question Time left: <span id=\"spanTime\" class=\"time\">" + DateUtil.displayDuration(TimeUnit.MILLISECONDS.toSeconds(user.getTimer()-System.currentTimeMillis()-1000))+ "s</span></p>\r\n"					+ "        <div class=\"progress\"><div id=\"progressbar\" class=\"progress-inner\"></div></div>"
					+ "            <div class=\"container\">\r\n"
					+ "                <div id=\"left\" draggable=\"true\">\r\n"
					+ "                    <div id=\"option1\" class=\"list\" draggable=\"true\">"+getOptions().get(1).getOption()+"</div>\r\n"
					+ "                    <div id=\"option2\" class=\"list\" draggable=\"true\">"+getOptions().get(2).getOption()+"</div>\r\n"
					+ "                    <div id=\"option3\" class=\"list\" draggable=\"true\">"+getOptions().get(3).getOption()+"</div>\r\n"
					+ "                    <div id=\"option4\" class=\"list\" draggable=\"true\">"+getOptions().get(4).getOption()+"</div>\r\n"
					+ "                </div>\r\n"

					+ "                <div id=\"right\"></div>\r\n"
					+ "            </div>\r\n"
					+ "             <button id=\"reset-btn\" onclick=\"reset()\">Reset</button>\r\n"
					+ "				<form action=\"QuestionServlet\" method=\"post\"><input type=\"textarea\" name=\"order\" id=\"order\" hidden>\r\n"
					+ "            <input type=\"submit\" id=\"next-btn\" value=\"Next\"></form>\r\n"
					+ "        </div>\r\n"
					+ "        </body>\r\n"
					+ "        <script>\r\n"
					+ "    	let list = [1,2,3,4];\r\n"
					+ "        let rightBox = document.getElementById(\"right\");\r\n"
					+ "        let leftBox = document.getElementById(\"left\");\r\n"
					+ "        let order = document.getElementById(\"order\");\r\n"
					+ "        \r\n"
					+ "        function reset() {\r\n"
					+ "        	order.value=\"\";\r\n"
					+ "\r\n"
					+ "        	for(id of list) {\r\n"
					+ "            	let element = document.getElementById(\"option\"+id);\r\n"
					+ "            	var hasChild = rightBox.querySelector(\"#\" + element.id) != null;\r\n"
					+ "            	if(hasChild){\r\n"
					+ "        		rightBox.removeChild(element);\r\n"
					+ "        		leftBox.appendChild(element);\r\n"
					+ "        		continue;\r\n"
					+ "            	}\r\n"
					+ "        	}\r\n"
					+ "        }\r\n"
					+ "        \r\n"
					+ "\r\n"
					+ "            for(numbers of list){\r\n"
					+ "				let element = document.getElementById(\"option\"+numbers);\r\n"
					+ "                element.addEventListener(\"dragstart\", function(e){\r\n"
					+ "                    let selected = e.target;                    \r\n"
					+ "\r\n"
					+ "                    rightBox.addEventListener(\"dragover\", function(e){\r\n"
					+ "                        e.preventDefault();\r\n"
					+ "                    });\r\n"
					+ "                    \r\n"
					+ "                    rightBox.addEventListener(\"drop\", function(e){\r\n"
					+ "                     	order.value=\"\";\r\n"
					+ "                        for(child of rightBox.children) {\r\n"
					+ "                        	order.value+=child.id.replace(\"option\", \"\");\r\n"
					+ "                        }\r\n"
					+ "                     	if(!order.value.includes(selected.id.replace(\"option\", \"\"))){\r\n"
					+ "                        rightBox.appendChild(selected);\r\n"
					+ "                        order.value+=selected.id.replace(\"option\", \"\");\r\n"
					+ "						}\r\n"
					+ "                       	selected=null;\r\n"
					+ "                    });\r\n"
					+ "                    \r\n"
					+ "                	})\r\n"
					+ "            }\r\n"
					+ "var timer;"
					+ "let changedBar = document.getElementById(\"bar\");"
					+ "let finalTime = " + user.getTimer() + "-Date.now();"
					+ "const progressBar = document.querySelector(\".progress-inner\");"
					+ "timer = setInterval(function(){"
					+ "let time = " + user.getTimer() + "-Date.now();"  
					+ "let variable = new Date(time).toISOString().slice(14,19);"
					+ ""
					+ "let total = " + user.getQuizzTimer() + "-Date.now();"
					+ "let totalTimer = new Date(total).toISOString().slice(14,19);"
					+ "if(time/1000 <= 0) {" 
					+ ""
					+ "progressBar.style.width=\"0%\";"
					+ "document.getElementById(\"spanTime\").innerHTML=\"Time is over!\";"
					+ "document.getElementById(\"spanTime\").value=\'0\';"  
					+ "clearInterval(timer);"
					+ "window.location.assign(\'QuestionServlet\');"
					+ "}else{"
					+ "document.getElementById(\"spanTime2\").innerHTML=totalTimer + \"s\";"
					+ "document.getElementById(\"spanTime\").innerHTML=variable + \"s\";"  
					+ "document.getElementById(\"spanTime\").value=\'time/1000\';"  
					+ "let progressWidth=((time/1000)/(finalTime/1000)) * 100;\r\n" 
					+ "            \r\n" 
					+ "if(time/1000 > 0) {"
					+ "progressBar.style.width = progressWidth + \"%\";"
					+ "checkColors(progressWidth);"
					+ "}}"
					+ "}, 1000);"
							
					+ "    const checkColors =(width) => {\r\n"
					+ "        if(width>60){\r\n"
					+ "            progressBar.style.background = \"green\";\r\n"
					+ "            \r\n"
					+ "        }else if(width>30){\r\n"
					+ "            progressBar.style.background=\"yellow\";\r\n"
					+ "        }else{\r\n"
					+ "            progressBar.style.background=\"red\";\r\n"
					+ "        }\r\n"
					+ "}"
					+ "        </script>\r\n"
					+ "</html>";
			break;
		
		}
		
		return response;
		
		
	}
	
    @Override
	public Object clone() 
        throws CloneNotSupportedException 
    { 
        return super.clone(); 
    } 
	
	
	
	
	public String getText() { return question; }
	public QuestionType getQuestionType() { return questionType; }
	public long getInterval() { return maxInterval; }
	public Map<Integer, Option> getOptions() { return options;  }
	public List<int[]> getOrder() { return orders; }
	
	
}
