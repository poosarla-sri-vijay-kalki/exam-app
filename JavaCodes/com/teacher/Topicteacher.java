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
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.add.sq.DaoRegister;


@WebServlet("/Topicteacher")
public class Topicteacher extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public Topicteacher() {
        super();

    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	response.setHeader("Access-Control-Allow-Origin", "*");
    	DaoRegister dao=new DaoRegister();
    	PrintWriter out= response.getWriter(); 
		String loginId=(String)request.getParameter("loginId");
		System.out.println("uname="+loginId);
		try {
			out.println(dao.TeacherTopic(Integer.parseInt(loginId)));
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
