package com.student;

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
 * Servlet implementation class getHistory
 */
@WebServlet("/getHistory")
public class getHistory extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
 
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		DaoRegister  dao=new DaoRegister();
		PrintWriter out= response.getWriter();
		int loginId=Integer.parseInt((String)request.getParameter("loginId"));
		System.out.println(loginId);
		try {
			out.print(dao.getHistory(loginId));
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
