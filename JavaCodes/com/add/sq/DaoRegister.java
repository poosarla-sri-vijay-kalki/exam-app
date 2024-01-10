package com.add.sq;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import com.mysql.cj.jdbc.result.ResultSetMetaData;
import com.mysql.cj.xdevapi.JsonArray;

public class DaoRegister {
	String url = "jdbc:mysql://127.0.0.1:3306/examapp";
	String usname = "root";
	String passw = "Smv@12345";

	public boolean checkUserCredentials(String uname, String pass, String profile)
			throws ClassNotFoundException, SQLException {
		String query = "select * from examapp.login where uname=? and pass=? and profile=?";
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, usname, passw);
		PreparedStatement st = con.prepareStatement(query);
		st.setString(1, uname);
		st.setString(2, pass);
		if (profile.equals("Student")) {
			st.setBoolean(3, false);
		} else {
			st.setBoolean(3, true);
		}
		ResultSet rs = st.executeQuery();
		if (rs.next()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean checkuname(String uname, String profile) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, usname, passw);
		String Que1 = "select uname from examapp.login where uname=? and profile=?";
		PreparedStatement st = con.prepareStatement(Que1);
		st.setString(1, uname);
		if (profile.equals("Student")) {
			st.setBoolean(2, false);
		} else {
			st.setBoolean(2, true);
		}
		ResultSet rs = st.executeQuery();
		if (rs.next()) {
			return false;
		} else {
			return true;
		}

	}

	public boolean store(String uname, String pass, boolean profile) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, usname, passw);
		String Que1 = " insert into examapp.login (uname,pass,profile)values(?,?,?)";
		PreparedStatement st = con.prepareStatement(Que1);
		st.setString(1, uname);
		st.setString(2, pass);
		st.setBoolean(3, profile);
		st.execute();
		String Que2 = " select loginId from examapp.login where uname=?";
		PreparedStatement st2 = con.prepareStatement(Que2);
		st2.setString(1, uname);
		ResultSet rs2 = st2.executeQuery();
		int loginId = 0;
		if (rs2.next()) {
			loginId = rs2.getInt("loginId");
		}
		String Que3 = " insert into examapp.marks (loginId)values(?)";
		PreparedStatement st3 = con.prepareStatement(Que3);
		st3.setInt(1, loginId);
		return st3.execute();
	}

	public boolean checkTest(String Topic, String Desc, int loginId) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, usname, passw);
		String Que = "select * from examapp.examdesc where examtopic=? and loginId=?";
		PreparedStatement st = con.prepareStatement(Que);
		System.out.println("checking the topic availability" + st);
		st.setString(1, Topic);
		st.setInt(2, loginId);
		System.out.println("checking the topic availability" + st);
		ResultSet rs = st.executeQuery();
		if (rs.next()) {
			return false;
		} else {
			return true;
		}
	}

	public boolean NewTest(String Topic, String Desc, String loginId) throws ClassNotFoundException, SQLException {

		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, usname, passw);
		String Que = "insert into examapp.examdesc (examtopic,examdesc,loginId) values(?,?,?)";
		PreparedStatement st = con.prepareStatement(Que);
		st.setString(1, Topic);
		st.setString(2, Desc);
		st.setString(3, loginId);
		return st.execute();
	}

	@SuppressWarnings("unchecked")
	public List<JSONObject> TeacherTopic(int loginId) throws ClassNotFoundException, SQLException {
		List<JSONObject> reslist = new ArrayList<JSONObject>();
		JSONObject obj1 = new JSONObject();
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, usname, passw);
		String Que = "select * from examapp.examdesc where loginId=?";
		PreparedStatement st = con.prepareStatement(Que);
		st.setInt(1, loginId);
		ResultSet rs = st.executeQuery();
		while (rs.next()) {
			JSONObject js = new JSONObject();
			js.put("topic_id", rs.getInt(1));
			js.put("Topic", rs.getString(2));
			js.put("Des", rs.getString(3));
			js.put("Teacher", rs.getString(4));
			reslist.add(js);
		}
		System.out.println(reslist);
		return reslist;
	}

	@SuppressWarnings("unchecked")
	public List<JSONObject> allTopic(int loginId) throws ClassNotFoundException, SQLException {
		// List<JSONObject> allTopics = new ArrayList<JSONObject>();
		List<JSONObject> finalTopics = new ArrayList<JSONObject>();
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, usname, passw);
		String query = "select * from examapp.examdesc join examapp.login on examapp.login.loginId= examapp.examdesc.loginId where examapp.examdesc.id  not in ( select topicId from examapp.marks where (loginId=? ));";
		PreparedStatement st = con.prepareStatement(query);
		st.setInt(1, loginId);
		ResultSet rs = st.executeQuery();
		while (rs.next()) {
			JSONObject temp = new JSONObject();
			temp.put("topicId", rs.getInt("id"));
			temp.put("Topic", rs.getString("examtopic"));
			temp.put("Des", rs.getString("examdesc"));
			temp.put("Teacher", rs.getString("uname"));
			finalTopics.add(temp);
		}

		// System.out.println("alltopics=" + allTopics);
		System.out.println("finaltopics=" + finalTopics);
		return finalTopics;
	}

	public void allquestions(List<JSONObject> jar) throws ClassNotFoundException, SQLException {

		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, usname, passw);
		String Que = "insert into examapp.questions (TopicId,Question,Option_1,Option_2,Option_3,Option_4,Correct_ans) values(?,?,?,?,?,?,?)";
		PreparedStatement st = con.prepareStatement(Que);
		for (JSONObject jo : jar) {
			String TopicId = (String) jo.get("TopicId");
			String Question = (String) jo.get("Question");
			String op1 = (String) jo.get("op1");
			String op2 = (String) jo.get("op2");
			String op3 = (String) jo.get("op3");
			String op4 = (String) jo.get("op4");
			String co = (String) jo.get("co");
			// String teacher = (String) jo.get("Teacher");
			st.setString(1, TopicId);
			// st.setString(2,Des);
			st.setString(2, Question);
			st.setString(3, op1);
			st.setString(4, op2);
			st.setString(5, op3);
			st.setString(6, op4);
			st.setString(7, co);
			// st.setString(8, teacher);
			System.out.println(st);
			System.out.println(st.execute());
		}

		String topicId = (String) jar.get(0).get("TopicId");
		System.out.println("Topic Id=" + topicId);
/////////////////////////////////////////////////////////////////insert into examapp.marks (loginId,topicId,marks) values(?,?,?);
		String Que3 = "insert into examapp.marks (loginId,topicId,marks) values(?,?,?);";
		PreparedStatement st3 = con.prepareStatement(Que3);
		st3.setInt(1, this.getLoginId(Integer.parseInt(topicId)));
		st3.setInt(2, Integer.parseInt(topicId));
		st3.setInt(3, jar.size());
		System.out.println("St3=" + st3);
		st3.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<JSONObject> getquestions(int topic_id) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		List<JSONObject> reslist = new ArrayList<JSONObject>();
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, usname, passw);
		String Que = "SELECT * FROM examapp.questions where TopicId =? ;";
		PreparedStatement st = con.prepareStatement(Que);
		st.setInt(1, topic_id);
		ResultSet rs = st.executeQuery();
		while (rs.next()) {
			// Question_Id, TopicId, Question, Option_1, Option_2, Option_3, Option_4,
			// Correct_ans
			JSONObject js = new JSONObject();
			js.put("Question_Id", rs.getInt("Question_Id"));
			js.put("TopicId", rs.getString("TopicId"));
			// js.put("Teacher", rs.getString(3));
			js.put("Question", rs.getString("Question"));
			js.put("Option_1", rs.getString("Option_1"));
			js.put("Option_2", rs.getString("Option_2"));
			js.put("Option_3", rs.getString("Option_3"));
			js.put("Option_4", rs.getString("Option_4"));
			// js.put("Correct_ans", rs.getString(9));
			// System.out.println("jhdfv");
			// System.out.println(js.get("Correct_ans"));
			reslist.add(js);
			// System.out.println(reslist);
		}
		System.out.println(reslist);
		return reslist;
	}

	/**
	 * @param answered
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public int checkanswers(List<JSONObject> answered, int topicId) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		int Topic_id = topicId;// Integer.parseInt((answered.get(0).get("topicId").toString()));
		// System.out.println(answe);
		List<JSONObject> correctAns = new ArrayList<JSONObject>();
		JSONObject obj1 = new JSONObject();
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, usname, passw);
		String Que = "SELECT Question_id,Correct_ans FROM examapp.questions where TopicId=? ;";
		PreparedStatement st = con.prepareStatement(Que);
		st.setInt(1, Topic_id);
		ResultSet rs = st.executeQuery();
		while (rs.next()) {
			JSONObject js = new JSONObject();
			js.put("id", rs.getInt(1));
			js.put("Correct_ans", rs.getString(2));
			correctAns.add(js);
		}
		System.out.println("correct answers" + correctAns);
		int correct = 0;
		System.out.println("Size" + answered.size() + "Size" + correctAns.size());
		for (int i = 0; i < answered.size(); i++) {
			System.out.println(answered.get(i).get("id").toString());
			for (int j = 0; j < correctAns.size(); j++) {
				// System.out.println((i)+(j)+"hello");
				if (answered.get(i).get("id").toString().equals(correctAns.get(j).get("id").toString())) {
					// System.out.println((i)+(j)+"hii");
					System.out.println("given" + answered.get(i).get("ans_choice") + "expected:"
							+ correctAns.get(j).get("Correct_ans").toString());
					if (answered.get(i).get("ans_choice").equals(correctAns.get(j).get("Correct_ans").toString())) {
						correct += 1;
					}
				}
			}
		}
		System.out.println("correct+" + correct);
		return correct;
	}

	@SuppressWarnings("unchecked")
	public List<JSONObject> getquestionsteacher(int topicId, int loginId) throws ClassNotFoundException, SQLException {
		List<JSONObject> reslist = new ArrayList<JSONObject>();
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, usname, passw);
		String Que = "SELECT * FROM examapp.questions where TopicId=?;";
		PreparedStatement st = con.prepareStatement(Que);
		st.setInt(1, topicId);
		ResultSet rs = st.executeQuery();
		while (rs.next()) {
			JSONObject js = new JSONObject();
			js.put("Question_id", rs.getInt("Question_id"));
			// js.put("Topic", rs.getString(2));
			// js.put("Teacher", rs.getString(3));
			js.put("Question", rs.getString("Question"));
			js.put("Option_1", rs.getString("Option_1"));
			js.put("Option_2", rs.getString("Option_2"));
			js.put("Option_3", rs.getString("Option_3"));
			js.put("Option_4", rs.getString("Option_4"));
			js.put("Correct_ans", rs.getString("Correct_ans"));
			// System.out.println("jhdfv");
			// System.out.println(js.get("Correct_ans"));
			reslist.add(js);
			// System.out.println(reslist);
		}
		return reslist;
	}

	public int updatequestions(JSONObject obj) throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, usname, passw);
		String Que = "UPDATE examapp.questions SET Question =?, Option_1 =?, Option_2 =?, Option_3 =?, Option_4 =?, Correct_ans =?  WHERE (Question_Id =?);";
		PreparedStatement st = con.prepareStatement(Que);
		String q = (String) obj.get("Question");
		st.setString(1, q);
		st.setString(2, (String) obj.get("op1"));
		st.setString(3, (String) obj.get("op2"));
		st.setString(4, (String) obj.get("op3"));
		st.setString(5, (String) obj.get("op4"));
		st.setString(6, (String) obj.get("ca"));
		st.setString(7, (String) obj.get("Question_id"));
		System.out.println(st);
		return st.executeUpdate();

	}

	public String getTopicbyId(String s1) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, usname, passw);
		String Que = "select examtopic from examapp.examdesc where id=?";
		PreparedStatement st = con.prepareStatement(Que);
		st.setString(1, s1);
		ResultSet rs = st.executeQuery();
		String topic = null;
		while (rs.next()) {
			topic = rs.getString("examtopic");
			// System.out.println(rs.getString(1));
		}
		return topic;
	}

	public void addcoloumn(String topicId) throws ClassNotFoundException, SQLException {

		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, usname, passw);
		String Que = "ALTER table examapp.marks add " + "marks_" + topicId + " int DEFAULT -1;";
		PreparedStatement st = con.prepareStatement(Que);
		System.out.println(st);
		st.executeUpdate();
	}

	public int getTopicId(String topic, String loginId) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, usname, passw);
		String Que = "select id from examapp.examdesc where examtopic=? and loginId=?";
		PreparedStatement st = con.prepareStatement(Que);
		st.setString(1, topic);
		st.setString(2, loginId);
		ResultSet rs = st.executeQuery();
		int topicId = 0;
		while (rs.next()) {
			topicId = rs.getInt("id");
		}
		System.out.println("Topic Id=" + topicId);
		return topicId;

	}

	public int updatemarks(int score, int topicId, int loginId, int duration)
			throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, usname, passw);
		String Que = "insert into examapp.marks (loginId,topicId,marks,duration) values(?,?,?,?)";
		PreparedStatement st = con.prepareStatement(Que);
		st.setInt(1, loginId);
		st.setInt(2, topicId);
		st.setInt(3, score);
		st.setInt(4, duration);
		System.out.println(st);
		return st.executeUpdate();
	}

	public List<JSONObject> showNotAnsTopics(int loginId) throws ClassNotFoundException, SQLException {
		// List<JSONObject> TopicByTeacher = new ArrayList<JSONObject>();
		List<JSONObject> noStudentTopic = new ArrayList<JSONObject>();
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, usname, passw);
		String Que = "select * from examapp.examdesc where (id not in (select topicId from examapp.marks where loginId!=?) and loginId=?)";
		PreparedStatement st = con.prepareStatement(Que);
		st.setInt(1, loginId);
		st.setInt(2, loginId);
		ResultSet rs = st.executeQuery();
		int topicId = 0;
		while (rs.next()) {
			JSONObject temp = new JSONObject();
			temp.put("topic_id", rs.getInt("id"));
			temp.put("Topic", rs.getString("examtopic"));
			noStudentTopic.add(temp);
		}
//		for (JSONObject topic : TopicByTeacher) {
//			System.out.println(topic);
//			int id=(int) topic.get("topic_id");
//			String Que1 = "select avg(marks_"+id+") as avg from examapp.marks";
//			PreparedStatement st1 = con.prepareStatement(Que1);
//			ResultSet rs1 = st1.executeQuery();
//			int avg=0;
//			while (rs1.next()) {
//					avg=rs1.getInt("avg");
//					System.out.println("avg="+avg+(avg==-1));
//			}
//			if(avg==-1)
//			{
//				noStudentTopic.add(topic);
//				System.out.println(topic+" addwed");
//			}
//		}
		System.out.println(noStudentTopic);
		return noStudentTopic;
	}

	public List<JSONObject> showFailTopics(int loginId) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		List<JSONObject> TopicByTeacher = new ArrayList<JSONObject>();
		List<JSONObject> failTopic = new ArrayList<JSONObject>();
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, usname, passw);
		String Que = "select id,examtopic from examapp.examdesc where loginId=?";
		PreparedStatement st = con.prepareStatement(Que);
		st.setInt(1, loginId);
		ResultSet rs = st.executeQuery();
		int topicId = 0;
		while (rs.next()) {
			JSONObject temp = new JSONObject();
			temp.put("topic_id", rs.getInt(1));
			temp.put("Topic", rs.getString(2));
			TopicByTeacher.add(temp);
		}
		for (JSONObject topic : TopicByTeacher) {
			System.out.println(topic);
			int id = (int) topic.get("topic_id");
			String Que1 = "select count(*) as count,avg(marks) as avg from examapp.marks where topicId=? and loginId!=? ";
			PreparedStatement st1 = con.prepareStatement(Que1);
			st1.setInt(1, id);
			st1.setInt(2, loginId);
			ResultSet rs1 = st1.executeQuery();
			int attemptedCount = 0;
			double avgScore = 0;
			while (rs1.next()) {
				avgScore = rs1.getDouble("avg");
				attemptedCount = rs1.getInt("count");
				System.out.println("avgScore=" + avgScore);
				System.out.println("attemptedCount=" + attemptedCount);
			}
			int maxScore = 0;
			String Que2 = "select marks as maxScore from examapp.marks where (topicId=? and loginId=?);";
			PreparedStatement st2 = con.prepareStatement(Que2);
			st2.setInt(1, id);
			st2.setInt(2, loginId);
			ResultSet rs2 = st2.executeQuery();
			while (rs2.next()) {
				maxScore = rs2.getInt("maxScore");
				System.out.println("maxScore=" + maxScore);
			}
			double passPercentage = ((double) avgScore / maxScore) * 100;
			if (passPercentage < 50 && attemptedCount != 0) {
				failTopic.add(topic);
			}
		}
		return failTopic;
	}

	public int getLoginId(int topicId) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, usname, passw);
		String query = "select loginId from examapp.examdesc where id=?";
		PreparedStatement st = con.prepareStatement(query);
		st.setInt(1, topicId);
		int loginId = 0;
		ResultSet rs = st.executeQuery();
		while (rs.next()) {
			loginId = rs.getInt("loginId");
		}
		return loginId;

	}

	public int getLoginId(String uname, String profile) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, usname, passw);
		String query = "select loginId from examapp.login where uname=? and profile=?";
		PreparedStatement st = con.prepareStatement(query);
		st.setString(1, uname);
		boolean isTeacher = false;
		int loginId = 0;
		if (profile.equals("Student")) {
			isTeacher = false;
		} else {
			isTeacher = true;
		}
		st.setBoolean(2, isTeacher);
		ResultSet rs = st.executeQuery();
		while (rs.next()) {
			loginId = rs.getInt("loginId");
		}
		return loginId;
	}

	@SuppressWarnings("unchecked")
	public JSONObject bestTeacher() throws ClassNotFoundException, SQLException {
		List<Integer> teacherLoginIds = new ArrayList<Integer>();
		// List<JSONObject> teacherpasspercentage = new ArrayList<JSONObject>();
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, usname, passw);
		String Que = "select loginId from examapp.login where profile=1";
		PreparedStatement st = con.prepareStatement(Que);
		ResultSet rs = st.executeQuery();
		int loginId = 0;
		while (rs.next()) {
			loginId = rs.getInt("loginId");
			teacherLoginIds.add(loginId);
		}
		JSONObject bestTeacher = new JSONObject();
		bestTeacher.put("loginId", -1);
		bestTeacher.put("PassPercent", (double) 0.0);
		System.out.println("loginids=" + teacherLoginIds);
		for (Integer lid : teacherLoginIds) {
			List<Integer> topicIds = new ArrayList<Integer>();
			String Que1 = "select * from examapp.examdesc where loginId=?";
			PreparedStatement st1 = con.prepareStatement(Que1);
			st1.setInt(1, (int) lid);
			System.out.println("getting topic IDs=" + st1);
			ResultSet rs1 = st1.executeQuery();
			// System.out.println(rs1.getMetaData());

			int topicId = 0;
			while (rs1.next()) {
				// System.out.println("Table contains "+rs1.getRow()+" rows");
				topicId = rs1.getInt("id");
				// System.out.println(topicId);
				topicIds.add(topicId);
			}
			System.out.println(lid + "topicIds" + topicIds);
			double avgPasspercentage = 0;
			int validtopicsCount = 0;
			for (Integer id : topicIds) {
				String Que2 = "select count(*) as count,avg(marks) as avg from examapp.marks where topicId=? and loginId!=? ";
				PreparedStatement st2 = con.prepareStatement(Que2);
				st2.setInt(1, id);
				st2.setInt(2, lid);
				ResultSet rs2 = st2.executeQuery();
				// System.out.println("getting average score="+st2);
				double avgScore = 0;
				int attemptedCount = 0;
				while (rs2.next()) {
					avgScore = rs2.getDouble("avg");
					attemptedCount = rs2.getInt("count");
					// System.out.println("avgScore="+avgScore);
				}
				int maxScore = 0;
				String Que3 = "select marks as maxScore from examapp.marks where (topicId=? and loginId=?);";
				PreparedStatement st3 = con.prepareStatement(Que3);
				st3.setInt(1, id);
				st3.setInt(2, lid);
				ResultSet rs3 = st3.executeQuery();
				while (rs3.next()) {
					maxScore = rs3.getInt("maxScore");
					// System.out.println("maxScore="+maxScore);
				}
				System.out.println("topicId=" + id + " averageScore=" + avgScore + "attemptedCount=" + attemptedCount
						+ "maxScore" + maxScore);
				if (maxScore > 0 && attemptedCount > 0) {
					validtopicsCount += 1;
					double passPercentage = (((double) avgScore) / maxScore) * 100;
					avgPasspercentage += passPercentage;
				}
			}
			avgPasspercentage = avgPasspercentage / validtopicsCount;
			System.out.println("teacher id=" + lid + "avgpercent=" + avgPasspercentage);
			if ((double) bestTeacher.get("PassPercent") < avgPasspercentage) {
				bestTeacher.replace("loginId", lid);
				bestTeacher.replace("PassPercent", avgPasspercentage);
				System.out.println("best teacher=" + bestTeacher);
			}

		}
		return bestTeacher;
	}

	public String getUname(int loginId) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, usname, passw);
		String Que = "select uname from examapp.login where loginId=?";
		PreparedStatement st = con.prepareStatement(Que);
		st.setInt(1, loginId);
		ResultSet rs = st.executeQuery();
		String uname = "";
		while (rs.next()) {
			uname = rs.getString("uname");
		}
		return uname;
	}

	@SuppressWarnings("unchecked")
	public List<JSONObject> getallteacherpass() throws ClassNotFoundException, SQLException {

		List<Integer> teacherLoginIds = new ArrayList<Integer>();
		List<JSONObject> allteacherpass = new ArrayList<JSONObject>();
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, usname, passw);
		String Que = "select loginId from examapp.login where profile=1";
		PreparedStatement st = con.prepareStatement(Que);
		ResultSet rs = st.executeQuery();
		int loginId = 0;
		while (rs.next()) {
			loginId = rs.getInt("loginId");
			teacherLoginIds.add(loginId);
		}
		System.out.println("teacherLoginIds=" + teacherLoginIds);
		for (Integer lid : teacherLoginIds) {
			List<Integer> topicIds = new ArrayList<Integer>();
			String Que1 = "select * from examapp.examdesc where loginId=?";
			PreparedStatement st1 = con.prepareStatement(Que1);
			st1.setInt(1, (int) lid);
			System.out.println("getting topic IDs=" + st1);
			ResultSet rs1 = st1.executeQuery();
			// System.out.println(rs1.getMetaData());

			int topicId = 0;
			while (rs1.next()) {
				// System.out.println("Table contains "+rs1.getRow()+" rows");
				topicId = rs1.getInt("id");
				System.out.println(topicId);
				topicIds.add(topicId);
			}
			System.out.println("teacherLoginId=" + lid);
			System.out.println("topicIds=" + topicIds);
			int status = 0;
			int validTopicsCount = 0;
			for (Integer id : topicIds) {
				String Que2 = "select count(*) as count,avg(marks) as avg from examapp.marks where topicId=? and loginId!=? ";
				PreparedStatement st2 = con.prepareStatement(Que2);
				st2.setInt(1, id);
				st2.setInt(2, lid);
				ResultSet rs2 = st2.executeQuery();
				// System.out.println("getting average score="+st2);
				double avgScore = 0;
				int attemptedCount = 0;
				while (rs2.next()) {
					avgScore = rs2.getDouble("avg");
					attemptedCount = rs2.getInt("count");
					// System.out.println("avgScore="+avgScore);
				}
				int maxScore = 0;
				String Que3 = "select marks as maxScore from examapp.marks where (topicId=? and loginId=?);";
				PreparedStatement st3 = con.prepareStatement(Que3);
				st3.setInt(1, id);
				st3.setInt(2, lid);
				ResultSet rs3 = st3.executeQuery();
				while (rs3.next()) {
					maxScore = rs3.getInt("maxScore");
					// System.out.println("maxScore="+maxScore);
				}
				System.out.println("topicId=" + id + " averageScore=" + avgScore + "attemptedCount=" + attemptedCount
						+ "maxScore" + maxScore);
				if (maxScore > 0 && attemptedCount > 0) {
					validTopicsCount += 1;
				}

				double passPercentage = (((double) avgScore) / maxScore) * 100;
				if (passPercentage < 50 && attemptedCount != 0) {
					status = -1;
					break;
				}
			}
			if (status != -1 && validTopicsCount != 0) {
				JSONObject Oneofthebest = new JSONObject();
				Oneofthebest.put("loginId", lid);
				Oneofthebest.put("uname", this.getUname(lid));
				allteacherpass.add(Oneofthebest);
			}
		}
		System.out.println("allteacher apss" + allteacherpass);
		return allteacherpass;
	}

	@SuppressWarnings("unchecked")
	public List<JSONObject> teacherswithallpass() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		List<Integer> teacherLoginIds = new ArrayList<Integer>();
		List<Integer> studentLoginIds = new ArrayList<Integer>();
		List<JSONObject> teacherswithallstudentpass = new ArrayList<JSONObject>();
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, usname, passw);
		String Que = "select loginId from examapp.login where profile=1";
		PreparedStatement st = con.prepareStatement(Que);
		ResultSet rs = st.executeQuery();
		int loginId = 0;
		while (rs.next()) {
			loginId = rs.getInt("loginId");
			teacherLoginIds.add(loginId);
		}
		System.out.println("loginids=" + teacherLoginIds);
//		String Que1 = "select loginId from examapp.login where profile=0";
//		PreparedStatement st1 = con.prepareStatement(Que1);
//		ResultSet rs1 = st1.executeQuery();
//		loginId=0;
//		while(rs1.next())
//		{
//			loginId=rs1.getInt("loginId");
//			studentLoginIds.add(loginId);
//		}
//		System.out.println("student login Ids="+studentLoginIds);
		int status = 0, therequestions = -1;
		for (Integer tid : teacherLoginIds) {
			status = 0;
			therequestions = -1;
			List<Integer> teachertopicIds = new ArrayList<Integer>();
			String Que2 = "select id,examtopic from examapp.examdesc where loginId=?";
			PreparedStatement st2 = con.prepareStatement(Que2);
			st2.setInt(1, tid);
			ResultSet rs2 = st2.executeQuery();
			int topicId = 0;
			while (rs2.next()) {
				topicId = rs2.getInt("id");
				teachertopicIds.add(topicId);
			}
			int validTopicCount = 0;
			for (Integer topicid : teachertopicIds) {
//				for(Integer sid:studentLoginIds)
//				{	System.out.println("HI"+sid);
//					String Que3 = "select marks_"+topicid+""+" as marks from examapp.marks where (marks_"+topicid+">=0 and loginId=?);";
//					PreparedStatement st3 = con.prepareStatement(Que3);
//					st3.setInt(1, sid);
//					//System.out.println("STR3="+st3);
//					ResultSet rs3 = st3.executeQuery();
//					//System.out.println("student ID="+sid+"score="+st3);
//					int marks=-1;
//					while (rs3.next()) {
//						marks=rs3.getInt("marks");
//						//System.out.println(marks);
//							//System.out.println("marks="+marks);
//					}
//					int maxScore=0;
//					String Que4 = "select marks_"+topicid+" as maxScore from examapp.marks where (loginId =? and marks_"+topicid+">=0);";
//					PreparedStatement st4 = con.prepareStatement(Que4);
//					st4.setInt(1,tid);
//					ResultSet rs4 = st4.executeQuery();
//					while (rs4.next()) {
//						maxScore=rs4.getInt("maxScore");
//							//System.out.println("maxScore="+maxScore);
//						if(maxScore>0)
//						{
//							therequestions=1;
//						}
//					}
//				double Percentage=(((double)marks)/maxScore)*100;
//				System.out.println("teachID="+tid+" topicId="+topicid+" studentId="+sid+" marks="+marks+" maxscore="+maxScore+" percentage"+Percentage);
//				if(Percentage<50 && marks!=-1 )
//				{
//					status = -1;
//					break;
//				}
//					
//				}
//				if(status==-1)
//				{
//					break;
//				}
				int maxScore = -1;
				String Que3 = "select marks as maxScore from examapp.marks where (topicId=? and loginId=?);";
				PreparedStatement st3 = con.prepareStatement(Que3);
				st3.setInt(1, topicid);
				st3.setInt(2, tid);
				ResultSet rs3 = st3.executeQuery();
				while (rs3.next()) {
					maxScore = rs3.getInt("maxScore");
					// System.out.println("maxScore="+maxScore);
				}

				if (maxScore != -1) {
					String Que4 = "select * from examapp.marks where (topicId=? and loginId!=? );";
					PreparedStatement st4 = con.prepareStatement(Que4);
					st4.setInt(1, topicid);
					st4.setInt(2, tid);
					ResultSet rs4 = st4.executeQuery();
					if (rs4.next()) {
						validTopicCount += 1;
					}
					String Que5 = "select * from examapp.marks where (topicId=? and loginId!=? and marks<0.5*?);";
					PreparedStatement st5 = con.prepareStatement(Que5);
					st5.setInt(1, topicid);
					st5.setInt(2, tid);
					st5.setInt(3, maxScore);
					ResultSet rs5 = st5.executeQuery();
					if (rs5.next()) {
						status = -1;
					}
				}
				if (status == -1) {
					break;
				}

			}
			if (status != -1 && validTopicCount != 0) {
				System.out.println("best teacher=" + this.getUname(tid));
				JSONObject obj = new JSONObject();
				obj.put("loginId", tid);
				obj.put("uname", this.getUname(tid));
				teacherswithallstudentpass.add(obj);
			} else {
				System.out.println("no good teacherteacher=" + this.getUname(tid));
			}

		}
		return teacherswithallstudentpass;
	}

	public void removeTopic(int topicId) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		Class.forName("com.mysql.cj.jdbc.Driver");
		System.out.println("topic id deleting is =" + topicId);
		Connection con = DriverManager.getConnection(url, usname, passw);
		String Que1 = "delete FROM examapp.examdesc where id=?";
		PreparedStatement st = con.prepareStatement(Que1);
		st.setInt(1, topicId);
		System.out.println(st);
		st.executeUpdate();

	}

	@SuppressWarnings("unchecked")
	public List<JSONObject> getHistory(int loginId) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		List<JSONObject> history = new ArrayList<JSONObject>();
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, usname, passw);
		String Que = "select examapp.examdesc.examtopic as topic,examapp.login.uname as name, examapp.marks.marks as marks from examapp.examdesc join  examapp.login on examapp.login.loginId=examapp.examdesc.loginId join examapp.marks on examapp.marks.topicId=examapp.examdesc.id where examapp.marks.loginId=?";
		PreparedStatement st = con.prepareStatement(Que);
		st.setInt(1, loginId);
		ResultSet rs = st.executeQuery();
		while (rs.next()) {
			JSONObject temp = new JSONObject();
			temp.put("topic", rs.getString("topic"));
			temp.put("teacher", rs.getString("name"));
			temp.put("marks", rs.getString("marks"));
			history.add(temp);
		}
		return history;
	}

	@SuppressWarnings("unchecked")
	public List<JSONObject> getTotalFast() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		List<JSONObject> fast = new ArrayList<JSONObject>();
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, usname, passw);
		String Que = "select * from examapp.marks where duration=(\r\n"
				+ "SELECT min(examapp.marks.duration) as Min FROM examapp.marks \r\n"
				+ "join examapp.login on examapp.marks.loginId=examapp.login.loginId \r\n"
				+ "where examapp.login.profile=0 ) ";
		PreparedStatement st = con.prepareStatement(Que);
		ResultSet rs = st.executeQuery();
		while (rs.next()) {
			JSONObject temp = new JSONObject();
			temp.put("studentName", this.getUname((int) rs.getInt("loginId")));
			temp.put("teacherName", this.getUname(this.getLoginId((int) rs.getInt("topicId"))));
			temp.put("topic", this.getTopicbyId(((int) rs.getInt("topicId")) + ""));
			temp.put("marks", rs.getInt("marks"));
			temp.put("duration", rs.getInt("duration"));
			System.out.println(temp);
			fast.add(temp);
		}
		return fast;
	}

	public List<JSONObject> getFastOfTopic() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		List<JSONObject> fast = new ArrayList<JSONObject>();
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, usname, passw);
		String Que = "select * from examapp.marks where duration in (\r\n"
				+ "SELECT min(examapp.marks.duration) as Min FROM examapp.marks \r\n"
				+ "join examapp.login on examapp.marks.loginId=examapp.login.loginId \r\n"
				+ "where examapp.login.profile=0 group by examapp.marks.topicId ) ";
		PreparedStatement st = con.prepareStatement(Que);
		ResultSet rs = st.executeQuery();
		while (rs.next()) {
			JSONObject temp = new JSONObject();
			temp.put("studentName", this.getUname((int) rs.getInt("loginId")));
			temp.put("teacherName", this.getUname(this.getLoginId((int) rs.getInt("topicId"))));
			temp.put("topic", this.getTopicbyId(((int) rs.getInt("topicId")) + ""));
			temp.put("marks", rs.getInt("marks"));
			temp.put("duration", rs.getInt("duration"));
			System.out.println(temp);
			fast.add(temp);
		}
		return fast;
	}

	public List<JSONObject> getFastOfTeacher() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		List<JSONObject> fastbyTeacher = new ArrayList<JSONObject>();
		List<Integer> teacherId = new ArrayList<Integer>();
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, usname, passw);
		String Que = "select loginId from examapp.login where profile=1";
		PreparedStatement st = con.prepareStatement(Que);
		ResultSet rs = st.executeQuery();
		while (rs.next()) {
			teacherId.add(rs.getInt("loginId"));
		}
		System.out.println(teacherId);
		for (Integer tid : teacherId) {
			String Que1 = "select * from examapp.marks where (topicId in \r\n"
					+ "(select id from examapp.examdesc where loginId=?) and duration=\r\n"
					+ "(SELECT min(duration) as Min FROM examapp.marks where topicId in \r\n"
					+ "(select id from examapp.examdesc where loginId=?) and duration>0));";
			PreparedStatement st1 = con.prepareStatement(Que1);
			st1.setInt(1, tid);
			st1.setInt(2, tid);
			ResultSet rs1 = st1.executeQuery();
			while (rs1.next()) {
				JSONObject temp = new JSONObject();
				temp.put("studentName", this.getUname((int) rs1.getInt("loginId")));
				temp.put("teacherName", this.getUname(this.getLoginId((int) rs1.getInt("topicId"))));
				temp.put("topic", this.getTopicbyId(((int) rs1.getInt("topicId")) + ""));
				temp.put("marks", rs1.getInt("marks"));
				temp.put("duration", rs1.getInt("duration"));
				System.out.println(temp);
				fastbyTeacher.add(temp);
			}

		}
		System.out.println(fastbyTeacher);
		return fastbyTeacher;
	}

	public void addVotes(String teacher,int studentId) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, usname, passw);
		int teacherId=this.getLoginId(teacher, 1+"");
		String Que = "update examapp.marks set votes= votes +1 where (loginId=?);";
		PreparedStatement st = con.prepareStatement(Que);
		st.setInt(1, teacherId);
//		st.setInt(2, studentId);
		System.out.println(st);
		int rs = st.executeUpdate();
		String Que1 = "update examapp.marks set votes=? where (loginId=?);";
		PreparedStatement st1 = con.prepareStatement(Que1);
		st1.setInt(1, teacherId);
		st1.setInt(2, studentId);
		int rs1 = st1.executeUpdate();
	}

	public JSONObject getVoteStatus(int studentId) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		List<String> teachers=new ArrayList<String>();
		JSONObject resp=new JSONObject();
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, usname, passw);
		//int teacherId=this.getLoginId(studentId, 1+"");
		String Que = "SELECT votes FROM examapp.marks where loginId=?;";
		PreparedStatement st = con.prepareStatement(Que);
		st.setInt(1, studentId);
		ResultSet rs=st.executeQuery();
		if(rs.next())
		{
			int vote=rs.getInt("votes");
			if(vote==0) {
				String Que1 = " select uname from examapp.login where loginId in \r\n"
						+ "			(select loginId from examapp.examdesc where id in\r\n"
						+ "            (select topicId from examapp.marks where loginId=?));";
				PreparedStatement st1 = con.prepareStatement(Que1);
				st1.setInt(1, studentId);
				ResultSet rs1=st1.executeQuery();
				
				while(rs1.next()) {
					String temp=rs1.getString("uname");
					teachers.add(temp);
				}
				resp.put("teachers", teachers);
				resp.put("studentStatus", "notvoted");
			}
			else
			{
				String Que1 = "select uname from examapp.login where loginId=?;";
				PreparedStatement st1 = con.prepareStatement(Que1);
				st1.setInt(1, vote);
				System.out.println("alread voted guy"+st1);
				ResultSet rs1=st1.executeQuery();
				if(rs1.next()) {
					teachers.add(rs1.getString("uname"));
				}
				resp.put("teachers", teachers);
				resp.put("studentStatus", "voted");
			}
		} 
		return resp;
	}

	@SuppressWarnings("unchecked")
	public List<JSONObject> getBestTeacherByVotes() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		List<JSONObject> fastbyvotes = new ArrayList<JSONObject>();
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, usname, passw);
		String Que = "select * from examapp.marks where votes in\r\n"
				+ " (SELECT max(votes) as max FROM examapp.marks where loginId in\r\n"
				+ "(select loginId from examapp.login where profile=1)and votes>0) \r\n"
				+ "group by loginId; ";
		PreparedStatement st = con.prepareStatement(Que);
		ResultSet rs = st.executeQuery();
		while (rs.next()) {
			JSONObject temp = new JSONObject();
			temp.put("teacherName", this.getUname(this.getLoginId((int) rs.getInt("topicId"))));
			System.out.println(temp);
			fastbyvotes.add(temp);
		}
		return fastbyvotes;
	}
}
