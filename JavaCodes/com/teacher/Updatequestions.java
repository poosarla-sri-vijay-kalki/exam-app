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

@WebServlet("/Updatequestions")
public class Updatequestions extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public Updatequestions() {
        super();

    }

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		
		
//		this file is saved for further use used for updating the questions
		response.setHeader("Access-Control-Allow-Origin", "*");
		JSONObject obj=new JSONObject();
		JSONObject obj1=new JSONObject();
		DaoRegister dao=new DaoRegister();
		PrintWriter out= response.getWriter(); 
		obj.put( "Question_id",(String)request.getParameter("questionId"));
		obj.put("Question",(String)request.getParameter("question"));
		obj.put( "op1",(String)request.getParameter("op1"));
		obj.put( "op2",(String)request.getParameter("op2"));
		obj.put( "op3",(String)request.getParameter("op3"));
		obj.put( "op4",(String)request.getParameter("op4"));
		obj.put("ca",(String)request.getParameter("ca"));
		//System.out.println(Question+Question_id);
		
		try {
			if(dao.updatequestions(obj)==1)
			{
				obj1.put("status", 1);
			}
			else
			{
				obj1.put("status", 0);
			}
			out.println(obj1);
			out.flush();
		
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
