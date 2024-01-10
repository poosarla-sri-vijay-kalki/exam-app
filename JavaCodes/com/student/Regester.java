package com.student;

import java.io.*;
import java.io.IOException;
import java.sql.SQLException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.add.sq.DaoRegister;

@SuppressWarnings("unused")
@WebServlet("/Regester")
public class Regester extends HttpServlet {
	DaoRegister d = new DaoRegister();

	private static final long serialVersionUID = 1L;

	public Regester() {
		super();
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JSONObject obj = new JSONObject();
		JSONObject obj1 = new JSONObject();
		response.setHeader("Access-Control-Allow-Origin", "*");
		System.out.print(obj.toString());
		String uname = (String)request.getParameter("uname");
		String pass = (String) request.getParameter("pass");
		String rpass = (String)request.getParameter("rpass");
		String profile = (String)request.getParameter("profile");
		System.out.println("regestration details:"+uname+pass);
		PrintWriter out = response.getWriter();
		try {
			if (d.checkuname(uname, profile)) {
				if (pass.equals(rpass)) {
					System.out.println("profile"+profile);
					if (profile.equals("Student")) {
					System.out.println("studnt regesterd");
						d.store(uname, pass, false);
					} else {
						System.out.println("teacher regesterd");
						d.store(uname, pass, true);
					}

					obj1.put("status",1);
				} else {
					obj1.put("status", 0);
				}
			} else {
				obj1.put("status", 2);
			}
			out.println(obj1);
			out.flush();

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
