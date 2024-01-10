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

@WebServlet("/showFailTopics")
public class showFailTopics extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public showFailTopics() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		DaoRegister dao=new DaoRegister();
		String loginId= (String)request.getParameter("loginId");
		PrintWriter out= response.getWriter(); 
		try {
			System.out.println(dao.showFailTopics(Integer.parseInt(loginId)));
			out.println(dao.showFailTopics(Integer.parseInt(loginId)));
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
