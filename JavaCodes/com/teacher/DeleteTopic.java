package com.teacher;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.add.sq.DaoRegister;

/**
 * Servlet implementation class DeleteTopic
 */
@WebServlet("/DeleteTopic")
public class DeleteTopic extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		DaoRegister dao=new DaoRegister();
		PrintWriter out=response.getWriter();
		String topicId= (String)request.getParameter("topicId");
		//String loginId= (String)request.getParameter("loginId");
		try {
			dao.removeTopic(Integer.parseInt(topicId));
		} catch (NumberFormatException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
