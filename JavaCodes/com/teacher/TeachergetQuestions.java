package com.teacher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.add.sq.DaoRegister;

/**
 * Servlet implementation class TeachergetQuestions
 */
@WebServlet("/TeachergetQuestions")
public class TeachergetQuestions extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public TeachergetQuestions() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");

		DaoRegister dao=new DaoRegister();
		PrintWriter out= response.getWriter(); 
		try {
			String TopicId=(String)request.getParameter("topicId");
			String loginId=(String)request.getParameter("loginId");
			out.println(dao.getquestionsteacher(Integer.parseInt(TopicId), Integer.parseInt(loginId)));
			out.flush();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
