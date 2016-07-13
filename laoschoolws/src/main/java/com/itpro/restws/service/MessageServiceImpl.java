package com.itpro.restws.service;

import java.util.ArrayList;
import java.util.HashSet;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.MessageDao;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.E_DEST_TYPE;
import com.itpro.restws.helper.E_ENTITY;
import com.itpro.restws.helper.E_RIGHT;
import com.itpro.restws.helper.E_ROLE;
import com.itpro.restws.helper.E_SCOPE;
import com.itpro.restws.helper.MessageFilter;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.EClass;
import com.itpro.restws.model.Message;
import com.itpro.restws.model.Permit;
import com.itpro.restws.model.School;
import com.itpro.restws.model.User;

@Service("messageService")
@Transactional
public class MessageServiceImpl implements MessageService{

	private static final Logger logger = Logger.getLogger(MessageServiceImpl.class);
	@Autowired
	private MessageDao messageDao;
	@Autowired
	private UserService userService;
	@Autowired
	protected PermitService permitService;

	@Autowired
	protected ClassService classService;
	
	@Autowired
	protected SchoolService schoolService;
	@Override
	public Message findById(Integer id) {
		return messageDao.findById(id);
		
	}

	@Override
	public int countMsgFromUser(Integer from_user_id) {
		return messageDao.countByFromUser(from_user_id);
	}

	@Override
	public int countMsgToUser(Integer to_user_id) {
		return messageDao.countByToUser(to_user_id);
	}

	@Override
	public ArrayList<Message> findMsgFromUser(Integer from_userid, int from_num, int max_result) {
		
		return (ArrayList<Message>) messageDao.findByFromUser(from_userid, from_num, max_result);
	}

	@Override
	public ArrayList<Message> findMsgTomUser(Integer to_userid, int from_num, int max_result) {
		
		return (ArrayList<Message>) messageDao.findByToUser(to_userid, from_num, max_result);
	}

	@Override
	public int countMsgBySchool(Integer school_id) {
		
		return messageDao.countBySchool(school_id);
	}

	@Override
	public int countMsgByClass(Integer class_id) {
		return messageDao.countByClass(class_id);
	}

	@Override
	public ArrayList<Message> findMsgBySchool(Integer school_id, int from_num, int max_result) {
		return (ArrayList<Message>) messageDao.findBySchool(school_id, from_num, max_result);
	}

	@Override
	public ArrayList<Message> findMsgByClass(Integer school_id,Integer class_id, int from_num, int max_result) {
		return (ArrayList<Message>) messageDao.findByClass(school_id,class_id, from_num, max_result);
	}

//	@Override
//	public Message insertMessage(Message message) {
//		messageDao.saveMessage(message);
//		return message;
//	}
//
	@Override
	public Message updateMessage(Message message) {
		
		String error_msg = checkUpdateMessage(message);
		if (error_msg!= null ){
			throw new ESchoolException(error_msg, HttpStatus.BAD_REQUEST);
		}
		Message messageDB = messageDao.findById(message.getId());
		messageDB = Message.updateChanges(messageDB, message);
		messageDao.updateMessage(messageDB);
		return messageDB;
	}

	@Override
	/***
	 * This message insert message but checking the cc_list to send for other users
	 */
	public Message insertMessageExt(Message message) {
		Message new_msg = null;
		String[] to_user_list =null; 
		String cc_list = message.getCc_list()==null?"": message.getCc_list();
		if (message.getTo_usr_id() > 0 ){
			cc_list = cc_list + ","+message.getTo_usr_id() ;
			cc_list.replaceAll(",+",",");
			
			// Remove duplicated user_id
			
			to_user_list = Utils.duplicateRemove(cc_list.split(","));
			
		}
		User frm_user = userService.findById(Integer.valueOf(message.getFrom_usr_id()));
		if (frm_user == null){
			throw new RuntimeException("From user is NUll");
		}
		for (String to_id : to_user_list){
			User to_user = userService.findById(Integer.valueOf(to_id));
			if (to_user != null && (to_user.getSchool_id() == frm_user.getSchool_id())) {
				new_msg = message.copy();
				
				new_msg.setFrom_usr_id(frm_user.getId());
				new_msg.setFrom_user_name(frm_user.getFullname());
				
				new_msg.setTo_usr_id(Integer.valueOf(to_id));
				new_msg.setTo_user_name(to_user.getFullname());
				
				new_msg.setSchool_id(to_user.getSchool_id());
				new_msg.setSent_dt(Utils.now());
				//new_msg.setClass_id(to_user.getSchool_id());
				
				messageDao.saveMessage(new_msg);
			}else{
				if (to_user == null ){
					logger.error("invalid To_user = null");
				}else if ( to_user.getSchool_id() != frm_user.getSchool_id() ){
					logger.error("to_user.school_id="+ to_user.getSchool_id() +"  != frm_user.school_id = "+frm_user.getSchool_id() );
				}
			}
		}
		return new_msg;
	}

	@Override
  public ArrayList<Message> findMsgExt(
		  Integer school_id, int from_row, int max_result,
		  // Secure filter
		  MessageFilter filter,
				// User filter
		  Integer class_id, 
				String dateFrom, 
				String dateTo,
				Integer fromUserID, 
				Integer toUserID, 
				Integer channel, 
				Integer is_read,
				Integer from_row_id
				) 
	{
		return (ArrayList<Message>) messageDao.findMessagesExt(
				school_id, 
				from_row, 
				max_result, 
				// Secure
				filter.getClasses(),
				filter.getUsers(), 
				// User filter
				class_id,
				dateFrom,
				dateTo,
				fromUserID, 
				toUserID,
				channel,
				is_read,
				from_row_id);
		
		
		
	}

	
	@Override
	public int countMsgExt(Integer school_id, 
			int from_row, 
			int max_result, // Secure filter
			  MessageFilter filter,
					// User filter
					Integer class_id, 
					String dateFrom, 
					String dateTo,
					Integer fromUserID, 
					Integer toUserID, 
					Integer channel, 
					Integer is_read,
					Integer from_row_id) {
		
		Integer ret = messageDao.countMessagesExt(
				school_id, 
				from_row, 
				max_result, 
				// Secure
				filter.getClasses(),
				filter.getUsers(), 
				// User filter
				class_id,
				dateFrom,
				dateTo,
				fromUserID, 
				toUserID,
				channel,
				is_read,
				from_row_id);
		
		return ret.intValue();
	}

	@Override
	/***
	 * 		Validation new message parameters	
	 */
	public void secureCheckNewMessage(User user,Message msg) {
		
		Permit permit = permitService.loadEntityPermit(user,E_ENTITY.MESSAGE);
		
		msg.setFrom_user_id(user.getId());
		msg.setFrom_user_name(user.getFullname());
		msg.setSchool_id(user.getSchool_id());
		
		
		// Validation mandatory parameters
		if (msg == null || msg.getSchool_id() == null || msg.getSchool_id().intValue() == 0 ){
			throw new ESchoolException("Message's school_id is NULL or zero", HttpStatus.BAD_REQUEST);
		}else if (msg.getSchool_id() != user.getSchool_id() ){
			throw new ESchoolException("Message's school_id:"+msg.getSchool_id()+" is not belong to user's school:"+user.getSchool_id(), HttpStatus.BAD_REQUEST);
		}
				
		if (msg.getFrom_usr_id() == null || msg.getFrom_usr_id() == 0 || msg.getFrom_usr_id() != user.getId()){
			String err = "Message's from_user_id="+msg.getFrom_usr_id()==null?"null":msg.getFrom_usr_id().intValue()+" is not valid (user_id="+user.getId()+")";
			throw new ESchoolException(err, HttpStatus.BAD_REQUEST);
		}

		
		
		if (msg.getTo_usr_id() == null || msg.getTo_usr_id() == 0  ){
			throw new ESchoolException("Message's to_user_id is not valid="+msg.getTo_usr_id()==null?"null":msg.getTo_usr_id().toString(), HttpStatus.BAD_REQUEST);
		}
		// Check if class_id existing
		if (msg.getClass_id() == null ||  msg.getClass_id().intValue() ==0   ){
			throw new ESchoolException("Message's class_id is NULL or zero", HttpStatus.BAD_REQUEST);
		}
		else if (!userService.isBelongToClass(msg.getTo_usr_id(),msg.getClass_id() )){
			throw new ESchoolException("Message's to_user_id="+msg.getTo_usr_id()+" is not belong to class_id="+msg.getClass_id(), HttpStatus.BAD_REQUEST);
		}

		if (msg.getContent() == null || msg.getContent().equals("")  ){
			throw new ESchoolException("Message's content is BLANK="+msg.getContent()==null?"null":msg.getContent(), HttpStatus.BAD_REQUEST);
		}
		
		// Validation user's right

		if ( E_RIGHT.RW.getValue() != (permit.getRights() & E_RIGHT.RW.getValue())){
			throw new ESchoolException("User's role="+user.getRoles()+" do not have RIGHTS to create message", HttpStatus.UNAUTHORIZED);
		}
		
		// Validate access scope
		User toUser = userService.findById(msg.getTo_usr_id());
		if (E_SCOPE.SCHOOL.getValue() == (permit.getScope() & E_SCOPE.SCHOOL.getValue() ) ){
		// check school
			
			if (!userService.isSameSChool(user,toUser)){
				throw new ESchoolException("User's role="+user.getRoles()+" cannot send to other SCHOOL. From User's school=="+user.getSchool_id()+"/// toUser's school="+toUser.getSchool_id(), HttpStatus.UNAUTHORIZED);
			}
			
			if (!userService.isSameSChool(user.getId(), msg.getCCList())){
				throw new ESchoolException("User's role="+user.getRoles()+" cannot CC to other SCHOOL. From User's school=="+user.getSchool_id()+"/// CC user ="+msg.getCc_list(), HttpStatus.UNAUTHORIZED);			
			}
		}
		
		else if ( E_SCOPE.CLASS.getValue() == (permit.getScope() &  E_SCOPE.CLASS.getValue() ) ){
			if (!userService.isSameClass(user,toUser)){
				throw new ESchoolException("User's role="+user.getRoles()+" cannot send to other CLASS. From User's class=="+user.eClassesToString()+"/// toUser's class="+toUser.eClassesToString(), HttpStatus.UNAUTHORIZED);
			}
			
			if (!userService.isSameClass(user.getId(), msg.getCCList())){
				throw new ESchoolException("User's role="+user.getRoles()+" cannot CC to other CLASS. From User's class=="+user.eClassesToString()+"/// CC user ="+msg.getCc_list(), HttpStatus.UNAUTHORIZED);			
			}
			
		}
		
		
		else if ( E_SCOPE.PERSON.getValue() == (permit.getScope() & E_SCOPE.PERSON.getValue() )){
			if (!userService.isHeadTeacherOf(user, msg.getTo_usr_id())){
		    	throw new ESchoolException("To User:"+msg.getTo_usr_id()+" is not Head teacher of class", HttpStatus.UNAUTHORIZED);
		    }
			
			if ((msg.getCc_list() != null && !msg.getCCList().equals(""))){
				throw new ESchoolException("PERSON scop cannot CC to other users", HttpStatus.UNAUTHORIZED);
			}
		}
		    	
	}

	@Override
	public MessageFilter secureGetMessages(User user) {
		
		// Get data access right for entity
		MessageFilter secure_filter = new MessageFilter();
		Permit permit = permitService.loadEntityPermit(user,E_ENTITY.MESSAGE);
				
		if ( E_RIGHT.R.getValue() != (permit.getRights() & E_RIGHT.R.getValue())){
			throw new ESchoolException("User role="+user.getRoles()+" do not have READ permission for messages", HttpStatus.UNAUTHORIZED);
		}
		if ( E_SCOPE.SCHOOL.getValue() == (permit.getScope() &  E_SCOPE.SCHOOL.getValue() ) ){
			// Do nothing
		}
		else if ( E_SCOPE.CLASS.getValue() == (permit.getScope() &  E_SCOPE.CLASS.getValue() ) ){
	    	secure_filter.setClasses(user.eClassesListID());
		}else if ( E_SCOPE.PERSON.getValue() == (permit.getScope() & E_SCOPE.PERSON.getValue() )){
			// Create filter for Permit
	    	ArrayList<Integer>filter_users = new ArrayList<>();
	    	filter_users.add(user.getId());
	    	secure_filter.setUsers(filter_users);
		}else{
			throw new ESchoolException("Invalide access SCOPE, role="+user.getRoles()+", scope="+permit.getScope(), HttpStatus.UNAUTHORIZED);
		}
		
		return secure_filter;
	}

	@Override
	public void secureCheckClassMessage(User user, Message msg, String class_list,String fileter_roles) {
		
		Permit permit = permitService.loadEntityPermit(user,E_ENTITY.MESSAGE);
		// enable admin role only
		if (!permit.hasRole("ADMIN")){
			throw new ESchoolException("User is not ADMIN of school, cannot send message to class levle", HttpStatus.UNAUTHORIZED);
		}
		
		// check school
		if (msg == null || msg.getSchool_id() == null || msg.getSchool_id().intValue() == 0 ){
			throw new ESchoolException("Message's school_id is NULL or zero", HttpStatus.BAD_REQUEST);
		}else if (msg.getSchool_id() != user.getSchool_id() ){
			throw new ESchoolException("Message's school_id:"+msg.getSchool_id()+" is not belong to user's school:"+user.getSchool_id(), HttpStatus.BAD_REQUEST);
		}
		// check lass
		if (class_list == null || class_list.equals("")){
			throw new ESchoolException("Class list is blank", HttpStatus.BAD_REQUEST);
		}
		for (String txt_cls_id : class_list.split(",")){
			Integer id = Utils.parseLong(txt_cls_id);
			if ((id != null ) && (id > 0)){
				EClass eclass= classService.findById(id);
				if (eclass.getSchool_id() != msg.getSchool_id()){
					throw new ESchoolException("Class id="+txt_cls_id+" is not belong to sender's school,id="+user.getSchool_id(), HttpStatus.BAD_REQUEST);
				}
			}else{
				throw new ESchoolException("Class ID is not valid"+txt_cls_id, HttpStatus.BAD_REQUEST);
			}
		}
		
			if (msg.getContent() == null || msg.getContent().equals("")  ){
			throw new ESchoolException("Message's content is BLANK="+msg.getContent()==null?"null":msg.getContent(), HttpStatus.BAD_REQUEST);
		}
		
		// Validation user's right

		if ( E_RIGHT.RW.getValue() != (permit.getRights() & E_RIGHT.RW.getValue())){
			throw new ESchoolException("User's role="+user.getRoles()+" do not have RIGHTS to create message", HttpStatus.UNAUTHORIZED);
		}
		
		
		if (!( E_SCOPE.CLASS.getValue() == (permit.getScope() &  E_SCOPE.CLASS.getValue() ) )){
				throw new ESchoolException("User's role="+user.getRoles()+" dont have permission to send message to class", HttpStatus.UNAUTHORIZED);			
		}
		
		// Validate roles
		if (fileter_roles == null || fileter_roles.equals("")){
			throw new ESchoolException("filter_roles is not correct:"+ fileter_roles, HttpStatus.UNAUTHORIZED);
		}
		for (String role: fileter_roles.split(",")){
			if (!(com.itpro.restws.helper.E_ROLE.contain(role))){
				throw new ESchoolException("filter_roles is not correct:"+ fileter_roles, HttpStatus.UNAUTHORIZED);
			}
		}
		
	}

//	@Override
//	public void insertClassMessageExt(Message msg,String class_list,String filter_roles) {
//		for (String txt_cls_id : class_list.split(",")){
//			// Reset class data
//			Integer id = Utils.parseInteger(txt_cls_id);
//			int last_id = 0;
//			String cc_list = "";
//			msg.setTo_usr_id(0);
//			msg.setCc_list("");
//			if ((id != null ) && (id > 0)){
//				EClass eclass= classService.findById(id.intValue());
//				if (eclass.getSchool_id() != msg.getSchool_id()){
//					throw new ESchoolException("Class id="+txt_cls_id+" is not belong to school,id="+msg.getSchool_id(), HttpStatus.BAD_REQUEST);
//				}
//				// Get list of users from class
//				HashSet<User> users = (HashSet<User>) eclass.getUserByRoles(filter_roles);
//				
//				
//				for (User user : users){
//					cc_list = cc_list+user.getId()+",";
//					last_id = user.getId();
//				}
//				if (last_id > 0 ){
//					cc_list = Utils.removeTxtLastComma(cc_list);
//					msg.setTo_usr_id(last_id);
//					msg.setCc_list(cc_list);
//					insertMessageExt(msg);
//				}
//				
//			}else{
//				throw new ESchoolException("Class ID is not valid"+txt_cls_id, HttpStatus.BAD_REQUEST);
//			}
//			
//		}
//	}
	/***
	 * Check message before update, OK return NULL
	 * @param msg
	 * @return
	 */

	String checkUpdateMessage(Message msg){
		if (msg == null ){
			return  "Message is not existing";
		}
		if 	(msg.getIs_read() == null){
			return "is_read == NULL";
		}
		if (msg.getIs_read() !=0 &&  msg.getIs_read() !=1){
			return "is_read must be 0 or 1 only";
		}
		
		if 	(msg.getImp_flg() == null){
			return "imp_flg == NULL";
		}
		if (msg.getImp_flg() !=0 &&  msg.getImp_flg() !=1){
			return "imp_flg must be 0 or 1 only";
		}
		return null;
	}

	private ArrayList<Message> insertClassMessage(User user,Message message,EClass eclass, String filter_roles) {
		ArrayList<Message> list = new ArrayList<>();
		
		//validate mandatory
		if (user == null || message == null || eclass == null ){
			throw new ESchoolException(" Input mandatory paramester is NULL", HttpStatus.BAD_REQUEST);
		}
			
		//validate user vs school
		if (user.getSchool_id() != message.getSchool_id()){
			throw new ESchoolException("From User id:"+user.getId()+" is not belong to school_id="+message.getSchool_id(), HttpStatus.BAD_REQUEST);
		}
		

		if (user.getSchool_id() != eclass.getSchool_id()){
			throw new ESchoolException("From User and Class are not in the same School, user_id="+user.getId()+", class_id="+eclass.getId(), HttpStatus.BAD_REQUEST);
		}
		// Get list of users from class
		HashSet<User> users = null; 
		// validate filter_roles
		if (filter_roles != null && !filter_roles.equals("")){
			for (String role: filter_roles.split(",")){
				if (!E_ROLE.contain(role)){
					throw new ESchoolException("Invalid filter_roles="+filter_roles, HttpStatus.BAD_REQUEST);
				}
			}
			users = (HashSet<User>) eclass.getUserByRoles(filter_roles);
		}else{
			// Get list of users from class
			users = new HashSet<User>(eclass.getUsers());
		}
		
		
		
		if (users != null && users.size() > 0)	{
			// Get list of users from class				
			for (User to_user : users){
					
					Message new_message = message.copy();
					
					new_message.setFrom_usr_id(user.getId());
					new_message.setFrom_user_name(user.getFullname());
					
					new_message.setTo_usr_id(to_user.getId());
					new_message.setTo_user_name(to_user.getFullname());
					
					new_message.setSchool_id(to_user.getSchool_id());
					
					new_message.setSent_dt(Utils.now());
					new_message.setIs_sent(1);// Disable sent			
					messageDao.saveMessage(new_message);
					list.add(new_message);
				
			}
		}
		return list;
		
	}
	
	private ArrayList<Message> insertClassMessageExt(User user,Message message,String filter_roles) {
		
		if (message.getDest_type() != E_DEST_TYPE.CLASS.getValue() ){
			throw new ESchoolException("Destination type is not for whole class, dest_type="+message.getDest_type(), HttpStatus.BAD_REQUEST);
		}

		
		EClass eclass= classService.findById(message.getClass_id());
		if (eclass == null){
			throw new ESchoolException("Class is not existing, class_id="+message.getClass_id(), HttpStatus.BAD_REQUEST);
		}

//		if (message.getCc_list() != null && (!message.getCCList().equals(""))){
//			ArrayList<Integer> list = new ArrayList<Integer>(message.getCCList());
//			
//		} Disalbe CC list when seding to Class
		return insertClassMessage(user, message, eclass, filter_roles);
		
	}
	
	

	private ArrayList<Message> insertSchoolMessageyExt(User user,Message message,String filter_roles) {
		
		ArrayList<Message> list = new ArrayList<>();
		if (message.getDest_type() != E_DEST_TYPE.SCHOOL.getValue() ){
			throw new ESchoolException("Message destination type is not for whole school, dest_type="+message.getDest_type(), HttpStatus.BAD_REQUEST);
		}
		//validate user vs school
		if (user.getSchool_id() != message.getSchool_id()){
			throw new ESchoolException("User id:"+user.getId()+" is not belong to school_id="+message.getSchool_id(), HttpStatus.BAD_REQUEST);
		}
		
		//validate class vs school
		School school = schoolService.findById(message.getSchool_id());
		
		if (school ==  null){
			throw new ESchoolException("School not existing, id="+ message.getSchool_id(), HttpStatus.BAD_REQUEST);
		}
		
		int max_count = classService.countBySchoolID(school.getId());
		
		ArrayList<EClass> classes = classService.findBySchool(school.getId(), 0, max_count);
		
		for (EClass eclass :classes){
			list.addAll(insertClassMessage(user,message,eclass,filter_roles));
		}
		return list;
	}
	
	@Override
	public ArrayList<Message> broadcastMessage(User user, Message message, String filter_roles) {
		ArrayList<Message> list = null;
		if (message.getDest_type() == E_DEST_TYPE.CLASS.getValue()){
			
			
			message = createTaskMsg(user,message);
			list = insertClassMessageExt(user, message, filter_roles);
			// 
			String cc_list = message.getCc_list();
			if (cc_list != null ){
				message.setCc_list("");// rest cc list
				// CC List
				for (String str : cc_list.split(",")){
					Integer class_id = Utils.parseInteger(str);
					if (class_id != null &&  (class_id.intValue() != message.getClass_id())){
						Message new_msg = message.copy();
						new_msg.setSchool_id(message.getSchool_id());
						new_msg.setClass_id(class_id);
						new_msg = createTaskMsg(user,new_msg);
						list.addAll(insertClassMessageExt(user, new_msg, filter_roles));
					}
				}	
			}
//		}
//		else if (message.getDest_type() == E_DEST_TYPE.SCHOOL.getValue()){
//			message = createTaskMsg(user,message);
//			list = insertSchoolMessageyExt(user, message, filter_roles);
//			
			
		}else {
			throw new ESchoolException("Unknow Dest_type="+ message.getDest_type(), HttpStatus.BAD_REQUEST);
		}
		// Reset is_sent flg = 0
		if (list != null){
			for (Message e : list){
				if (e.getIs_sent() == 1){
					e.setIs_sent(0);
					messageDao.updateMessage(e);
				}
			}
		}
		return list;
	}

	private Message createTaskMsg(User user, Message message) {
		message.setTo_usr_id(null);
		message.setSent_dt(Utils.now());
		message.setIs_sent(99);
		messageDao.saveMessage(message);
		message.setTask_id(message.getId());
		return message;
	}

	@Override
	public Message newMessage(Integer from_user_id, Integer to_user_id, String content) {
		Message msg = new Message();
		User frm_user = userService.findById(from_user_id);
		User to_user = userService.findById(to_user_id);
		
		if (frm_user == null || to_user == null ){
			throw new ESchoolException("newMessage() from_user_id or to_user_id is not exsiting", HttpStatus.BAD_REQUEST);
		}
		msg.setSchool_id(frm_user.getSchool_id());
		msg.setFrom_user_id(from_user_id);
		msg.setTo_user_id(to_user_id);
		msg.setContent(content);
		
		return msg;
	}

	
	
}
