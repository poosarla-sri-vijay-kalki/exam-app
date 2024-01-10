package com.teacher;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.add.sq.DaoRegister;

/**
 * Servlet implementation class bestTeachers
 */
@WebServlet("/bestTeachers")
public class bestTeachers extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public bestTeachers() {
        super();
        // TODO Auto-generated constructor stub
    }

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		DaoRegister dao= new DaoRegister();
		JSONObject bestTeacher=new JSONObject();
		PrintWriter out=response.getWriter();
		try {
			bestTeacher=dao.bestTeacher();
			if((int)bestTeacher.get("loginId")!=-1)
			{String uname= dao.getUname((int)bestTeacher.get("loginId"));
				bestTeacher.put("uname", uname);}
			else {
				bestTeacher.put("uname", "no Teacher");
			}
			out.print(bestTeacher);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
