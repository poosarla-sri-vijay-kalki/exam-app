package com.student;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.add.sq.DaoRegister;


@WebServlet("/AllTopics")
public class AllTopics extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setHeader("Access-Control-Allow-Origin", "*");
		DaoRegister dao=new DaoRegister();
		String loginId=(String)request.getParameter("loginId");
		PrintWriter out= response.getWriter(); 	
		try {
			out.println(dao.allTopic(Integer.parseInt(loginId)));
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
}
