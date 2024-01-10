package com.student;

import java.io.*;
import java.sql.SQLException;
import org.json.simple.JSONObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
//import com.add.sq.Dao;
import com.add.sq.DaoRegister;

@WebServlet("/Login")
public class Login extends HttpServlet {

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/JSON");
		response.setHeader("Access-Control-Allow-Origin", "*");
		JSONObject responseData = new JSONObject();
		PrintWriter out = response.getWriter();

		DaoRegister dao = new DaoRegister();
		System.out.println("Login.java");
		String uname = (String) request.getParameter("uname");
		String pass = (String) request.getParameter("pass");
		String profile = (String) request.getParameter("profile");
//		System.out.println("done1"+s1+s2+s4);

		System.out.println(uname + "login");
		int loginId = 0;
		try {
			if (dao.checkUserCredentials(uname, pass, profile)) {
				loginId = dao.getLoginId(uname, profile);
				responseData.put("status", 1);
				responseData.put("loginId", loginId);
				System.out.println("loging In with Id=" + loginId);
				if (profile.equals("Student")) {

					responseData.put("profile", 0);
				} else {
					responseData.put("profile", 1);
				}
			} else {
				responseData.put("status", 0);
				responseData.put("redirect", "username or password incorrect");
				System.out.println("Notdone");
			}

			out.println(responseData);
			out.flush();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
