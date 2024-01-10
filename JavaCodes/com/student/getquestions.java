package com.student;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

//import com.add.sq.Dao;
import com.add.sq.DaoRegister;

@WebServlet("/getquestions")
public class getquestions extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setHeader("Access-Control-Allow-Origin", "*");
		PrintWriter out= response.getWriter(); 	
		List <JSONObject> allQuestions =new ArrayList<JSONObject>();
		int id=Integer.parseInt((String)request.getParameter("id"));
		DaoRegister dao = new DaoRegister();
		try {
			allQuestions=dao.getquestions(id);
			System.out.println(allQuestions);
			out.println(allQuestions);
			out.flush();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
