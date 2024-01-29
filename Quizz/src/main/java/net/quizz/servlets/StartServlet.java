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
import net.quizz.questions.QuestionLoader;
import net.quizz.user.User;

@WebServlet("/StartServlet")
public class StartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = new User();
		Main.getUsers().add(user);
		Main.setQuestionLoader(new QuestionLoader());
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		HttpSession session = request.getSession(true);

		session.setMaxInactiveInterval(20 * 900);
		session.setAttribute("user", user);
		Question question = user.nextQuestion();
		pw.println(question.show(user));
	}

}
