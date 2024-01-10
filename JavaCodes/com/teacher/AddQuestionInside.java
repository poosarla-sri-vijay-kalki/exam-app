package com.teacher;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.add.sq.DaoRegister;

/**
 * Servlet implementation class AddQuestionInside
 */
@WebServlet("/AddQuestionInside")
public class AddQuestionInside extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public AddQuestionInside() {
        super();
        // TODO Auto-generated constructor stub
    }
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
//		this file is saved for further use used for adding more questions
		
		response.setHeader("Access-Control-Allow-Origin", "*");
		DaoRegister dao=new DaoRegister();
		PrintWriter out= response.getWriter(); 
		JSONObject obj1=new JSONObject();
		String topicId=(String)request.getParameter("topicId");
		try {
			String topicName=dao.getTopicbyId(topicId);
			obj1.put("topicName", topicName);
			System.out.println("topic name sent:"+topicName);
			out.println(obj1);
			out.flush();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
