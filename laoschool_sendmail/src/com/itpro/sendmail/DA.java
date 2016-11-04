package com.itpro.sendmail;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.itpro.ent.MailEnt;


public class DA {
	static final String	email_path = "/opt/itpro/laoschool/runnable/sendmail/mysql_ds/laoschool_sendmail-mysql-ds.xml";
	static final String	laoschool_path = "/opt/itpro/laoschool/runnable/sendmail/mysql_ds/laoschool_mysql-ds.xml";
	
//	static final String	email_path = "D:\\jar\\mysql_ds\\mysql_ds\\laoschool_sendmail-mysql-ds.xml";
//	static final String	laoschool_path = "D:\\jar\\mysql_ds\\mysql_ds\\laoschool_mysql-ds.xml";
	
	Connection sendmail_conn = null;
	String sendmail_driver;
	String sendmail_username;
	String sendmail_password;
	String sendmail_url;

	Connection laoschool_conn = null;
	String laoschool_driver;
	String laoschool_username;
	String laoschool_password;
	String laoschool_url;
	
	public DA(){
		getConnectInfo_sendmail();
		getConnectInfo_laoschool();
	}
	private void getConnectInfo_laoschool() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			File file = new File(laoschool_path);
			Document doc = db.parse(file);
			NodeList trunk = doc.getElementsByTagName("local-tx-datasource");
			Element root = (Element) trunk.item(0);
			NodeList child = root.getChildNodes();
			// read node list
			for (int i = 0; i < child.getLength(); i++) {
				Node info = child.item(i);
				if (info.getNodeType() == Node.ELEMENT_NODE) {// node tytpe is
																// // element
																// type
					// System.out.println(info.getTextContent());
					if (info.getNodeName().equalsIgnoreCase("connection-url")) {
						laoschool_url = info.getTextContent();
						// System.out.println(crbt_url);
					} else if (info.getNodeName().equalsIgnoreCase("driver-class")) {
						laoschool_driver = info.getTextContent();
						// System.out.println(driver);
					} else if (info.getNodeName().equalsIgnoreCase("user-name")) {
						laoschool_username = info.getTextContent();
						// System.out.println(user name);
					} else if (info.getNodeName().equalsIgnoreCase("password")) {
						laoschool_password = (info.getTextContent() == null) ? null
								: info.getTextContent();
						// System.out.println(password);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public void setCloseCnn(){
		try {
			if(sendmail_conn !=null && !sendmail_conn.isClosed()){
				sendmail_conn.close();
				sendmail_conn = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public void setCloseLaoschoolCnn(){
		try {
			if(laoschool_conn !=null && !laoschool_conn.isClosed()){
				laoschool_conn.close();
				laoschool_conn = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/***
	 * Get or create connection to callercrbt DB
	 * @return
	 */
	public Connection get_sendmail_conn()  {
		try {
			if (sendmail_conn == null || sendmail_conn.isClosed() || (!sendmail_conn.isValid(2))) {
				 try {
					 Utils.log("Create new connection to laoschool_email DB", 0);
					 sendmail_conn = DriverManager.getConnection(sendmail_url, sendmail_username, sendmail_password);
	
				 } catch (Exception e) {
					 e.printStackTrace();
				 }

			}			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return sendmail_conn;

	}
	
	/***
	 * Get or create connection to callercrbt DB
	 * @return
	 */
	public Connection get_laoschool_conn()  {
		try {
			if (laoschool_conn == null || laoschool_conn.isClosed() || (!laoschool_conn.isValid(2))) {
				 try {
					 Utils.log("Create new connection to laoschool_conn DB", 0);
					 laoschool_conn = DriverManager.getConnection(laoschool_url, laoschool_username, laoschool_password);
	
				 } catch (Exception e) {
					 e.printStackTrace();
				 }

			}			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return laoschool_conn;

	}
	
	public void getConnectInfo_sendmail() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			File file = new File(email_path);
			Document doc = db.parse(file);
			NodeList trunk = doc.getElementsByTagName("local-tx-datasource");
			Element root = (Element) trunk.item(0);
			NodeList child = root.getChildNodes();
			// read node list
			for (int i = 0; i < child.getLength(); i++) {
				Node info = child.item(i);
				if (info.getNodeType() == Node.ELEMENT_NODE) {// node tytpe is
																// // element
																// type
					// System.out.println(info.getTextContent());
					if (info.getNodeName().equalsIgnoreCase("connection-url")) {
						sendmail_url = info.getTextContent();
						// System.out.println(crbt_url);
					} else if (info.getNodeName().equalsIgnoreCase(
							"driver-class")) {
						sendmail_driver = info.getTextContent();
						// System.out.println(driver);
					} else if (info.getNodeName().equalsIgnoreCase("user-name")) {
						sendmail_username = info.getTextContent();
						// System.out.println(user name);
					} else if (info.getNodeName().equalsIgnoreCase("password")) {
						sendmail_password = (info.getTextContent() == null) ? null
								: info.getTextContent();
						// System.out.println(password);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	

	public void ping_db()throws SQLException{
		sendmail_conn = get_sendmail_conn();
		
		String sql = "SELECT *  FROM sendmail LIMIT 1";
		PreparedStatement	pstm = sendmail_conn.prepareStatement(sql);
		pstm.executeQuery();
		pstm.close();
		
		laoschool_conn = get_laoschool_conn();
		
		sql = "SELECT *  FROM email_msg LIMIT 1";
		pstm = laoschool_conn.prepareStatement(sql);
		pstm.executeQuery();
		pstm.close();
	}
	public void force_make_new_Conn ()throws SQLException{
		try {
			if (sendmail_conn != null) {
				 try {
					 Utils.log("Force close connection to SENDMAIL DB", 0);
					 sendmail_conn.close();
					 sendmail_conn = null;
	
				 } catch (Exception e) {
					 e.printStackTrace();
				 }
			}
			Utils.log("Froce create new connection to SENDMAIL DB", 0);
			sendmail_conn = DriverManager.getConnection(sendmail_url, sendmail_username, sendmail_password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void force_make_new_laoschool_Conn ()throws SQLException{
		try {
			if (laoschool_conn != null) {
				 try {
					 Utils.log("Force close connection to laoschool", 0);
					 laoschool_conn.close();
					 laoschool_conn = null;
	
				 } catch (Exception e) {
					 e.printStackTrace();
				 }
			}
			Utils.log("Froce create new connection to SENDMAIL DB", 0);
			laoschool_conn = DriverManager.getConnection(laoschool_url, laoschool_username, laoschool_password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public Vector<MailEnt> get_mail_list() throws SQLException {
		Vector<MailEnt> list = new Vector<MailEnt>();
		
	 	sendmail_conn=get_sendmail_conn();
	 	CallableStatement call = sendmail_conn.prepareCall("{call sp_get_sendmail_list()}");
		ResultSet rs = call.executeQuery();
		while(rs.next()){			
			MailEnt ent = new MailEnt();
			ent.setId(rs.getLong("id"));
			ent.setSub(rs.getString("sub"));
			ent.setBody(rs.getString("body"));
			ent.setReceivers(rs.getString("receivers"));
			ent.setDate_time(rs.getString("date_time"));
			ent.setDescription(rs.getString("description"));
			ent.setSuccess(rs.getInt("success"));
			//ent.setSchool_id(rs.getInt("school_id"));
			
			list.add(ent);
		}
		rs.close();
		call.close();
		return list;
	}
	public void update_sendmail_result(MailEnt ent) throws SQLException {
		Utils.log(String.format("ThreadMail update_sendmail_result: ent.getDescription(): %s",ent.getDescription()),0);
		sendmail_conn = get_sendmail_conn();
		
		if (ent.getDescription() != null && ent.getDescription().length() >= 1000 ){
			ent.setDescription(ent.getDescription().substring(0,998));
		}
		String sql = "UPDATE sendmail SET date_time=?, success=?, description=? WHERE id=?";
		PreparedStatement	pstm =sendmail_conn.prepareStatement(sql);
		pstm.setString(1, ent.getDate_time());
		pstm.setInt(2, ent.getSuccess());
		pstm.setString(3, ent.getDescription());
		
		pstm.setLong(4, ent.getId());	
		
		pstm.executeUpdate();
		pstm.close();
		
	}
	public void insert_send_mail(MailEnt ent) throws SQLException {
		String sql ="INSERT INTO sendmail(sub,body,receivers,imp_sp,imp_id,imp_datetime,school_id)VALUES(?,?,?,?,?,?,?)";
		sendmail_conn = get_sendmail_conn();
		
		PreparedStatement	pstm =sendmail_conn.prepareStatement(sql);
		pstm.setString(1, ent.getSub());
		pstm.setString(2, ent.getBody());
		pstm.setString(3, ent.getReceivers());
		pstm.setString(4, ent.getImp_sp());	
		pstm.setLong(5, ent.getImp_id());
		pstm.setString(6, ent.getImp_datetime());
		pstm.setInt(7, ent.getSchool_id());

		pstm.executeUpdate();
		pstm.close();
	}
	
	public Vector<MailEnt> move_email_msg() throws SQLException {
		Vector<MailEnt> list = new Vector<MailEnt>();
		
		laoschool_conn=get_laoschool_conn();
	 	CallableStatement call = laoschool_conn.prepareCall("{call get_email_msg()}");
		ResultSet rs = call.executeQuery();
		while(rs.next()){			
			MailEnt ent = new MailEnt();
			ent.setSchool_id(rs.getInt("school_id"));
			ent.setSub("[LAOSChool]Daily Report");
			ent.setReceivers(rs.getString("receivers"));
			ent.setBody(rs.getString("content"));
			
			if (ent.getReceivers() != null  && ent.getReceivers().trim().length() > 0){			
				list.add(ent);
			}else{
				Utils.log("Cannot send due to receivers is BLank", 1);
				Utils.log("EmailMsg:"+ent.toString(), 1);
			}
		}
		rs.close();
		call.close();
		return list;
	}
}
