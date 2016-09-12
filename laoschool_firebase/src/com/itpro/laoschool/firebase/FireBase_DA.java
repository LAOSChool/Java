package  com.itpro.laoschool.firebase;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.itpro.laoschool.firebase.ent.ApiKey_ent;
import com.itpro.laoschool.firebase.ent.AuthKey_ent;
import com.itpro.laoschool.firebase.ent.FireBase_ent;

public class FireBase_DA {
	static final String DESC_DAILY = "daily charge";

	static final String	mysql_ds_path = "./laoschool_mysql_ds.xml";
	Connection conn = null;
	

	String driver;
	String username;
	String password;
	String url;

	public FireBase_DA(){
	
			getConnectInfo();
	}

	public void setCloseCnn()  {
		try {
			if(conn !=null && !conn.isClosed()){
				conn.close();
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}
	/***
	 * Get or create connection to callercrbt DB
	 * @return
	 */
	public Connection getConn() throws Exception {
		if (conn == null || conn.isClosed()) {
			 Utils.log("Create new connection to LaoSchool DB", 0);
			 conn = DriverManager.getConnection(url, username, password);
		}
		return conn;

	}
	/***
	 * Get or create connection to callercrbt DB
	 * @return
	 */
	public void create_new_Conn() throws Exception {
		if (conn != null && !conn.isClosed()){
			conn.close();
			conn = null;
		}
  	    Utils.log("Force create new connection to LaoSchool DB", 0);
		conn = DriverManager.getConnection(url, username, password);

	}

	/***
	 * Get callercrbt db connection info (username, pass, dburl) from XML file
	 */
	public void getConnectInfo()  {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try{
			DocumentBuilder db = dbf.newDocumentBuilder();
			File file = new File(mysql_ds_path);
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
						url = info.getTextContent();
						// System.out.println(crbt_url);
					} else if (info.getNodeName().equalsIgnoreCase(
							"driver-class")) {
						driver = info.getTextContent();
						// System.out.println(driver);
					} else if (info.getNodeName().equalsIgnoreCase("user-name")) {
						username = info.getTextContent();
						// System.out.println(user name);
					} else if (info.getNodeName().equalsIgnoreCase("password")) {
						password = (info.getTextContent() == null) ? null
								: info.getTextContent();
						// System.out.println(password);
					}
				}
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}

	}
	
	/*** Call stored procedure "sp_get_daily_charge_list"
	 * Get all items having status =1 and last_success_date < CURDATE() and processed = 0
	 * @return: List of charging items (DailyCharge_ent)
	 */
	public	Vector<FireBase_ent> get_firebase_list()throws Exception{
		Vector<FireBase_ent> charge_list = new Vector<FireBase_ent>();
	
	 	conn=getConn();
	 	
	 	CallableStatement call = conn.prepareCall("{call sp_get_firebase_list}");
		ResultSet rs = call.executeQuery();
		while(rs.next()){			
			FireBase_ent ent = new FireBase_ent();
			
			ent.setId(rs.getInt("id"));
			
			ent.setContent(rs.getString("content"));
			ent.setSchool_id(rs.getInt("school_id"));
			ent.setOrg_id(rs.getInt("org_id"));
			ent.setFrom_user_id(rs.getInt("from_user_id"));
			ent.setFrom_user_name(rs.getString("from_user_name"));
			ent.setTo_user_id(rs.getInt("to_user_id"));
			ent.setTo_user_name(rs.getString("to_user_name"));
			ent.setTo_sso_id(rs.getString("to_sso_id"));
			ent.setTitle(rs.getString("title"));
			ent.setContent(rs.getString("content"));
			ent.setIs_sent(rs.getInt("is_sent"));
			ent.setSent_dt(rs.getString("sent_dt"));
			ent.setSuccess(rs.getInt("success"));
			ent.setError(rs.getString("error"));
			
			charge_list.add(ent);
		}
		rs.close();
		call.close();
		return charge_list;
	}
	
	

	public void updateWhenFinish(FireBase_ent ent)throws Exception{
		conn=getConn();
//		`sp_update_firebase_when_finish`(
//				p_id BIGINT(20), 
//				p_success INT(11), 
//				p_error TEXT
//				 )		
		CallableStatement call = conn.prepareCall("{call sp_update_firebase_when_finish(?,?,?)}");
		Utils.log(String.format("{call sp_update_firebase_when_finish(p_id=%d,p_success=%d,p_error=%s)}", ent.getId(), ent.getSuccess(),ent.getError()==null?"":ent.getError()),0);
		
		//Utils.log(String.format("{call sp_update_firebase_when_finish(%d,%d,%s)}", ent.getId(), ent.getSuccess(),"error"),0);
		
		call.setInt(1, ent.getId());
		call.setInt(2, ent.getSuccess());
		call.setString(3,nvl( ent.getError()));
		//call.setString(3,"error");
		
		call.executeUpdate();
		call.close();
		
	}
	
	/***
	 * Select all promotion users having enddate > TODAY.
	 * @param msisdn
	 * @param cats
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<AuthKey_ent> get_auth_key_by_sso(String sso_id) throws Exception{
		ArrayList<AuthKey_ent> list = new ArrayList<AuthKey_ent>();
		
		

		conn = getConn();
		String sql = "select * from authen_key where sso_id =? ";
		Utils.log(String.format("select * from authen_key where sso_id =%s ",sso_id),0);
		PreparedStatement	pstm = conn.prepareStatement(sql);
		pstm.setString(1, sso_id);
		
		
		ResultSet rs = pstm.executeQuery();
		while (rs.next()) {
			AuthKey_ent auth_key_ent  = new AuthKey_ent();
			
			auth_key_ent.setId(rs.getInt("id"));
			auth_key_ent.setAuth_key(rs.getString("auth_key"));
			auth_key_ent.setSso_id(sso_id);
			
			list.add(auth_key_ent);
			
		}
		rs.close();
		pstm.close();
		return list;
	}
	
	public ApiKey_ent get_apikey_by_authkey(String auth_key) throws Exception{
		ApiKey_ent apikey =null;
		

		conn = getConn();
		String sql = "select * from api_key where auth_key =? AND active = 1 AND actflg = 'A'";
		
		
		Utils.log(String.format("select * from api_key where auth_key ='%s' AND active = 1 AND actflg = 'A' ",auth_key),0);
		
		
		PreparedStatement	pstm = conn.prepareStatement(sql);
		pstm.setString(1, auth_key);
		
		ResultSet rs = pstm.executeQuery();
		if (rs.next()) {
			apikey = new ApiKey_ent();
			
			apikey.setId(rs.getInt("id"));
			apikey.setAuth_key(auth_key);
			apikey.setApi_key(rs.getString("api_key"));
			apikey.setCld_token(rs.getString("cld_token"));
			apikey.setSso_id(rs.getString("sso_id"));
			apikey.setActive(rs.getInt("active"));
//			apikey.setFirst_request_dt(rs.getString("first_request_dt"));
//			apikey.setLast_request_dt(rs.getString("last_request_dt"));
//			apikey.setNotice(rs.getString("notice"));
			
			
			
		}
		rs.close();
		pstm.close();
		return apikey;
	}
	
	public String nvl(String value){
		if ( value == null ){
			return "";
		}
		return value;
			
	}
	public void ping_db()throws Exception{
		conn = getConn();
		
		String sql = "SELECT * FROM api_key LIMIT 1";
		PreparedStatement	pstm = conn.prepareStatement(sql);
		pstm.executeQuery();
		pstm.close();
		

	}

	/***
	 * Insert CDR_MT to send sms to user
	 * @param callerid
	 * @param service
	 * @param datetime
	 * @param smscontent
	 * @param serviceID
	 * @param smscode
	 * @throws SQLException
	 */
//	public void insertCDR_MT(String callerid, String service, String datetime,
//		String smscontent, int serviceID, String smscode) throws Exception{
//		sms_conn = getSmsConn();
//		
//		String sql = "INSERT INTO cdr_mt (callerid, service, datetime, smscontent, serviceid ,smscode) VALUES (?,?,?,?,?,?)";
//		PreparedStatement	pstm = sms_conn.prepareStatement(sql);
//		pstm.setString(1, callerid);
//		pstm.setString(2, service);
//		pstm.setString(3, datetime);
//		pstm.setString(4, smscontent);
//		pstm.setInt(5, serviceID);
//		pstm.setString(6, smscode);	
//		
//		pstm.executeUpdate();
//		pstm.close();
//		
//	}
	
	
}
