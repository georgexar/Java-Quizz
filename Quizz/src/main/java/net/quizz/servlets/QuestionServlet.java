package net.quizz.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.quizz.Main;
import net.quizz.questions.Question;
import net.quizz.questions.childs.Answer;
import net.quizz.user.User;

import java.util.*;

@WebServlet("/QuestionServlet")

public class QuestionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public QuestionServlet() {
        super();
        
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        doPost(req, resp);
    }
    
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		if(!(session.getAttribute("user") instanceof User)) {
			return;
		}
		User user = (User) session.getAttribute("user");
		PrintWriter pw = response.getWriter();
		response.setContentType("text/html");
		
		String retry = request.getParameter("retry");
		String showAnswers = request.getParameter("showAnswers");
		
		
		if(retry != null) {
			response.sendRedirect("index.html");
			Main.getUsers().remove(user);
			session.removeAttribute("user");
			return;
		}
		
		if(showAnswers != null) {
			pw.println(user.printAnswers());
			return;
		}
		
		Question current = user.getCurrentQuestion();
		Answer answer = new Answer();
		List<Integer> list = new ArrayList<>();
		switch(current.getQuestionType()) {
		
		case I:
			String answer_str = request.getParameter("answer");
			int answer_int = user.parseAnswer(answer_str);
			list.add(answer_int);
			answer.setResponse(list);
			answer.setPoints(1.0f);
			break;
	
		case II:
			String HelpButtonPressed = request.getParameter("helpbutton");
			answer_str = request.getParameter("answer");
			answer_int = user.parseAnswer(answer_str);
			list.add(answer_int);
			answer.setResponse(list);
			if(HelpButtonPressed!=null && !HelpButtonPressed.isEmpty()) {
				
				answer.setPoints(0.5f);
			} else {
				answer.setPoints(1.0f);
			}
			break;
			
		case III:
			HelpButtonPressed = request.getParameter("helpbutton");
			String option1 = request.getParameter("radio1");
			String option2 = request.getParameter("radio2");
			String option3 = request.getParameter("radio3");
			String option4 = request.getParameter("radio4");

			if(option1!=null) list.add(1);
			if(option2!=null) list.add(2);
			if(option3!=null) list.add(3);
			if(option4!=null) list.add(4);
			answer.setResponse(list);
			
			if(HelpButtonPressed!=null && !HelpButtonPressed.isEmpty()) {
				
				answer.setPoints(0.5f);
			} else {
				answer.setPoints(1.0f);
			}
			
			break;
		case IV:
			answer_str = request.getParameter("answer");
			answer.setWordAnswer(answer_str);
			answer.setPoints(1.0f);
			break;
		
		case V:
			answer_str = request.getParameter("order");
			answer.setWordAnswer(answer_str);
			answer.setPoints(1.0f);
			break;
			
		case VI:
			answer_str = request.getParameter("order");
			int[] order = new int[4];
			int i = 0;
			assert answer_str!=null;
			for(char c : answer_str.toCharArray()) {
				
				order[i]=(Character.getNumericValue(c));
				i++;
			}
			answer.setPoints(1.0f);
			answer.setOrder(order);
			break;
		}	
		

		
		
		user.getRespondedTo().putIfAbsent(current, answer);
		if(user.getRespondedTo().values().size() >= 6 || (user.getQuizzTimer() - System.currentTimeMillis() <= 0)) {
			pw.println(user.printResults());

			return;
		}
		
		pw.println(user.nextQuestion().show(user));
	
		
	}

}
