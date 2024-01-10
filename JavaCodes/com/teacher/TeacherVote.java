package com.teacher;

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

import com.add.sq.DaoRegister;

/**
 * Servlet implementation class TeacherVote
 */
@WebServlet("/TeacherVote")
public class TeacherVote extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		DaoRegister dao = new DaoRegister();
		PrintWriter out = response.getWriter();
		String teacher = (String) request.getParameter("Teacher");
		System.out.println(teacher);
		System.out.println((String) request.getParameter("loginId"));
		int studentId = Integer.parseInt((String) request.getParameter("loginId"));
		JSONObject resp=new JSONObject();
		try {
			if (teacher.equals("")) {
				resp=dao.getVoteStatus(studentId);
				resp.put("status","recieved");
				out.print(resp);
			} else {
				
				dao.addVotes(teacher, studentId);
				resp.put("teachers",teacher);
				resp.put("status","updated");
//				teach.add("updated");
				out.print(resp);
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
