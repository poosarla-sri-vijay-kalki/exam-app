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
 * Servlet implementation class teachersAllPass
 */
@WebServlet("/teachersAllPass")
public class metric1 extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public metric1() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		DaoRegister dao=new DaoRegister();
		PrintWriter out=response.getWriter();
		try {
			out.print(dao.getallteacherpass());
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
