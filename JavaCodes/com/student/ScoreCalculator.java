package com.student;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.add.sq.DaoRegister;

/**
 * Servlet implementation class ScoreCalculator
 */
@WebServlet("/ScoreCalculator")
public class ScoreCalculator extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ScoreCalculator() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// doGet(request, response);
		response.setHeader("Access-Control-Allow-Origin", "*");
		PrintWriter out = response.getWriter();

		DaoRegister dao = new DaoRegister();
		List<JSONObject> answersJson = new ArrayList<JSONObject>();
		Map<String, String[]> quest = request.getParameterMap();
		JSONObject meta=new JSONObject();
		JSONParser parser = new JSONParser();
//		System.out.println("parametermap=" + quest);
//		System.out.println("parametermap class=" + quest.getClass());
//		System.out.println("parametermap=" + quest.keySet());
//		System.out.println("parametermap=" + quest.keySet().toArray());
		for (Object i : quest.keySet().toArray()) {
			String q = quest.get(i)[0];
//			System.out.println(q + "kfdjhg");
			try {

				JSONObject obj = (JSONObject) parser.parse(q);
				if (i.equals("metaData")) {
					 meta = obj;
				} else

				{
					answersJson.add(obj);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		System.out.println("input=" + answersJson);
		try {
			String topicId = (String) meta.get("topicId");
			String loginId = (String) meta.get("loginId");
			System.out.println("duration"+meta.get("duration").getClass());
			String duration=(String) meta.get("duration");
			int score = dao.checkanswers(answersJson,Integer.parseInt(topicId));
			System.out.println("Score=" + score);
			out.println(score);
			dao.updatemarks(score, Integer.parseInt(topicId), Integer.parseInt(loginId),Integer.parseInt(duration));
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
