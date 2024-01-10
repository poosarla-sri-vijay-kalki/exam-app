package com.teacher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.add.sq.DaoRegister;

@WebServlet("/Question")
public class Question extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
// used for adding questions
		response.setHeader("Access-Control-Allow-Origin", "*");

		DaoRegister dao=new DaoRegister();
		List<JSONObject> questions = new ArrayList<JSONObject>();
		Map<String, String[]> quest = request.getParameterMap();
		JSONParser parser = new JSONParser();

		for (Object i : quest.keySet().toArray()) {
			String q = quest.get(i)[0];
			System.out.println(q);
			try {
				JSONObject obj = (JSONObject) parser.parse(q);
				System.out.println(obj.get("Topic"));
				questions.add(obj);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		try {
			System.out.println(questions);
			System.out.println(questions.size());
			dao.allquestions(questions);
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
