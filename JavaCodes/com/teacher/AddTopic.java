package com.teacher;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.SQLException;
import org.json.simple.JSONObject;
import com.add.sq.DaoRegister;

@WebServlet("/AddTopic")
public class AddTopic extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public AddTopic() {
        super();
    }

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		response.setHeader("Access-Control-Allow-Origin", "*");
		JSONObject obj1=new JSONObject();
		DaoRegister dao=new DaoRegister();
		PrintWriter out= response.getWriter(); 
		String topic=(String)request.getParameter("topic");
		String des=(String)request.getParameter("des");
		String teacher=(String)request.getParameter("teacher");
		String loginId=(String)request.getParameter("loginId");
		System.out.println("done1"+teacher+topic);
		//System.out.println(dao.checkTest(Topic, Des));
		try {
			if(dao.checkTest(topic, des,Integer.parseInt(loginId)))
				{
					dao.NewTest(topic,des,loginId);
					obj1.put("status",1);
					int topicId=dao.getTopicId(topic,loginId);
					obj1.put("topicId", topicId);
					//dao.addcoloumn(topicId+"");
					
				}
				else
				{
					obj1.put("status",0);
					System.out.println("already test exists");
				}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			out.println(obj1);
			out.flush();
		
	}

}
