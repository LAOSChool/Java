package com.itpro.restws.service;

import java.util.ArrayList;
import java.util.HashSet;

import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.MessageDao;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.E_DEST_TYPE;
import com.itpro.restws.helper.E_ENTITY;
import com.itpro.restws.helper.E_MSG_CHANNEL;
import com.itpro.restws.helper.E_RIGHT;
import com.itpro.restws.helper.E_ROLE;
import com.itpro.restws.helper.E_SCOPE;
import com.itpro.restws.helper.MessageFilter;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.EClass;
import com.itpro.restws.model.Message;
import com.itpro.restws.model.Permit;
import com.itpro.restws.model.SysTemplate;
import com.itpro.restws.model.User;

@Service("messageService")
@Transactional
public class MessageServiceImpl implements MessageService{

	private static final Logger logger = Logger.getLogger(MessageServiceImpl.class);
	@Autowired
	private MessageDao messageDao;
	
	//@Autowired
	//protected CommandDao commandDao;
	@Autowired
	private UserService userService;
	@Autowired
	protected PermitService permitService;

	@Autowired
	protected ClassService classService;
	
	@Autowired
	protected SchoolService schoolService;
	
	@Autowired
	protected SysTblService sysTblService;
	
	@Autowired
	protected CommandService commandService;
	
	@Autowired
	protected FirebaseMsgService firebaseMsgService;

	
	
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


	@Override
	public Message updateMessage(User me, Message message) {
//   20160823 START		
//		String error_msg = checkUpdateMessage(message);
//		if (error_msg!= null ){
//			throw new ESchoolException(error_msg, HttpStatus.BAD_REQUEST);
//		}
//		Message messageDB = messageDao.findById(message.getId());
//		messageDB = Message.updateChanges(messageDB, message);
//		messageDao.updateMessage(messageDB);
//		return messageDB;
		
		Message messageDB = messageDao.findById(message.getId());
		 try {
			  	messageDao.setFlushMode(FlushMode.MANUAL);
			  	messageDB = Message.updateChanges(messageDB, message);
			  	
			  	valid_message(me, messageDB,false);
			  	
	        } catch (Exception e){
	        	messageDao.clearChange();
	        	throw e;
	        }
		   finally {
			   messageDao.setFlushMode(FlushMode.AUTO);
	        }
		  
		
		 messageDao.updateMessage(me,messageDB);
		 return messageDB;
	//   20160823 END		
		
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
	public void secureCheckNewMessage(User me,Message msg) {
		
		if (me.hasRole(E_ROLE.SYS_ADMIN.getRole_short())){
			return;
		}
			
		Permit permit = permitService.loadEntityPermit(me,E_ENTITY.MESSAGE);
		
		msg.setFrom_user_id(me.getId());
		msg.setFrom_user_name(me.getFullname());
		
		// Validation mandatory parameters
		
		if (msg.getTo_user_id() == null || msg.getTo_user_id() == 0  ){
			throw new ESchoolException("Message's to_user_id is not valid="+msg.getTo_user_id()==null?"null":msg.getTo_user_id().toString(), HttpStatus.BAD_REQUEST);
		}
		User toUser = userService.findById(msg.getTo_user_id());
		if (toUser == null ){
			throw new ESchoolException("ToUser_ID not existing:"+msg.getTo_user_id().intValue(), HttpStatus.BAD_REQUEST);
		}
		if (toUser.getSchool_id().intValue() != me.getSchool_id().intValue()){
			throw new ESchoolException("to_user_id not belong to same school with current user:"+msg.getTo_user_id().intValue(), HttpStatus.BAD_REQUEST);
		}
		msg.setTo_sso_id(toUser.getSso_id());
		msg.setSchool_id(toUser.getSchool_id());

		if (!userService.isSameSChool(me.getId(), msg.getCCList())){
			throw new ESchoolException("User's role="+me.getRoles()+" cannot CC to other SCHOOL. From User's school=="+me.getSchool_id()+"/// CC user ="+msg.getCc_list(), HttpStatus.UNAUTHORIZED);			
		}		

		if (me.hasRole(E_ROLE.ADMIN.getRole_short())){
			
		}else if (	(me.hasRole(E_ROLE.TEACHER.getRole_short())) ||   
					(me.hasRole(E_ROLE.CLS_PRESIDENT.getRole_short())) 
					){
			// Check if class_id existing
			if (msg.getClass_id() == null ||  msg.getClass_id().intValue() ==0   ){
				throw new ESchoolException("Message's class_id is NULL or zero", HttpStatus.BAD_REQUEST);
			}
			else if (!userService.isBelongToClass(msg.getTo_user_id(),msg.getClass_id() )){
				throw new ESchoolException("Message's to_user_id="+msg.getTo_user_id()+" is not belong to class_id="+msg.getClass_id(), HttpStatus.BAD_REQUEST);
			}
			if ( !userService.isSameClass(me.getId(), msg.getCCList())){
				throw new ESchoolException("User's role="+me.getRoles()+" cannot CC to other CLASS. From User's class=="+me.eClassesToString()+"/// CC user ="+msg.getCc_list(), HttpStatus.UNAUTHORIZED);			
			}
		}else {// STUDENT send message to teacher
			if (!userService.isHeadTeacherOf(me, msg.getTo_user_id())){
				throw new ESchoolException("To User:"+msg.getTo_user_id()+" is not Head teacher of class", HttpStatus.UNAUTHORIZED);
			}
			msg.setCc_list(null);
		}

		if (msg.getContent() == null || msg.getContent().equals("")  ){
			throw new ESchoolException("Message's content is BLANK="+msg.getContent()==null?"null":msg.getContent(), HttpStatus.BAD_REQUEST);
		}
		
		// Validation user's right

		if ( E_RIGHT.RW.getValue() != (permit.getRights() & E_RIGHT.RW.getValue())){
			throw new ESchoolException("User's role="+me.getRoles()+" do not have RIGHTS to create message", HttpStatus.UNAUTHORIZED);
		}
		
		// Validate access scope
		
		if (E_SCOPE.SCHOOL.getValue() == (permit.getScope() & E_SCOPE.SCHOOL.getValue() ) ){
		// check school
			
			if ( !userService.isSameSChool(me,toUser)){
				throw new ESchoolException("User's role="+me.getRoles()+" cannot send to other SCHOOL. From User's school=="+me.getSchool_id()+"/// toUser's school="+toUser.getSchool_id(), HttpStatus.UNAUTHORIZED);
			}
			
			
		}
		
		else if ( E_SCOPE.CLASS.getValue() == (permit.getScope() &  E_SCOPE.CLASS.getValue() ) ){
			if ( 
					!userService.isSameClass(me,toUser)){
				throw new ESchoolException("User's role="+me.getRoles()+" cannot send to other CLASS. From User's class=="+me.eClassesToString()+"/// toUser's class="+toUser.eClassesToString(), HttpStatus.UNAUTHORIZED);
			}
			
			if ( !userService.isSameClass(me.getId(), msg.getCCList())){
				throw new ESchoolException("User's role="+me.getRoles()+" cannot CC to other CLASS. From User's class=="+me.eClassesToString()+"/// CC user ="+msg.getCc_list(), HttpStatus.UNAUTHORIZED);			
			}
			
		}
		else if ( E_SCOPE.PERSON.getValue() == (permit.getScope() & E_SCOPE.PERSON.getValue() )){
			if (!userService.isHeadTeacherOf(me, msg.getTo_user_id())){
		    	throw new ESchoolException("To User:"+msg.getTo_user_id()+" is not Head teacher of class", HttpStatus.UNAUTHORIZED);
		    }
			
			if ((msg.getCc_list() != null && !msg.getCCList().equals(""))){
				throw new ESchoolException("PERSON scop cannot CC to other users", HttpStatus.UNAUTHORIZED);
			}
		}
		    	
	}

	@Override
	public MessageFilter secureGetMessages(User me) {
		if (me.hasRole(E_ROLE.SYS_ADMIN.getRole_short())){
			return null;
		}
		// Get data access right for entity
		MessageFilter secure_filter = new MessageFilter();
		Permit permit = permitService.loadEntityPermit(me,E_ENTITY.MESSAGE);
				
		if ( E_RIGHT.R.getValue() != (permit.getRights() & E_RIGHT.R.getValue())){
			throw new ESchoolException("User role="+me.getRoles()+" do not have READ permission for messages", HttpStatus.UNAUTHORIZED);
		}
		if ( E_SCOPE.SCHOOL.getValue() == (permit.getScope() &  E_SCOPE.SCHOOL.getValue() ) ){
			// Do nothing
		}
		else if ( E_SCOPE.CLASS.getValue() == (permit.getScope() &  E_SCOPE.CLASS.getValue() ) ){
	    	secure_filter.setClasses(me.eClassesListID());
		}else if ( E_SCOPE.PERSON.getValue() == (permit.getScope() & E_SCOPE.PERSON.getValue() )){
			// Create filter for Permit
	    	ArrayList<Integer>filter_users = new ArrayList<>();
	    	filter_users.add(me.getId());
	    	secure_filter.setUsers(filter_users);
		}else{
			throw new ESchoolException("Invalide access SCOPE, role="+me.getRoles()+", scope="+permit.getScope(), HttpStatus.UNAUTHORIZED);
		}
		
		return secure_filter;
	}

	@Override
	public void secureCheckClassMessage(User me, Message msg, String class_list,String fileter_roles) {
		
		if (me.hasRole(E_ROLE.SYS_ADMIN.getRole_short())){
			return ;
		}
		
		Permit permit = permitService.loadEntityPermit(me,E_ENTITY.MESSAGE);
		// enable admin role only
		if (!permit.hasRole("ADMIN")){
			throw new ESchoolException("User is not ADMIN of school, cannot send message to class levle", HttpStatus.UNAUTHORIZED);
		}
		
		// check school
		if (msg == null || msg.getSchool_id() == null || msg.getSchool_id().intValue() == 0 ){
			throw new ESchoolException("Message's school_id is NULL or zero", HttpStatus.BAD_REQUEST);
		}else if (msg.getSchool_id() != me.getSchool_id() ){
			throw new ESchoolException("Message's school_id:"+msg.getSchool_id()+" is not belong to user's school:"+me.getSchool_id(), HttpStatus.BAD_REQUEST);
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
					throw new ESchoolException("Class id="+txt_cls_id+" is not belong to sender's school,id="+me.getSchool_id(), HttpStatus.BAD_REQUEST);
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
			throw new ESchoolException("User's role="+me.getRoles()+" do not have RIGHTS to create message", HttpStatus.UNAUTHORIZED);
		}
		
		
		if (!( E_SCOPE.CLASS.getValue() == (permit.getScope() &  E_SCOPE.CLASS.getValue() ) )){
				throw new ESchoolException("User's role="+me.getRoles()+" dont have permission to send message to class", HttpStatus.UNAUTHORIZED);			
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


	@Override
	public Message newSimpleMessage(Integer from_user_id, Integer to_user_id, String content, Integer channel, Integer class_id) {
		Message msg = new Message();
		User frm_user = userService.findById(from_user_id);
		User to_user = userService.findById(to_user_id);
		
		if (frm_user == null || to_user == null ){
			throw new ESchoolException("newMessage() from_user_id or to_user_id is not exsiting", HttpStatus.BAD_REQUEST);
		}
		
	
		
		if (       (!frm_user.hasRole(E_ROLE.SYS_ADMIN.getRole_short()))
				&&  (frm_user.getSchool_id().intValue() != to_user.getSchool_id().intValue())){
			throw new RuntimeException("From user_id:"+frm_user.getId().intValue()+" is not in same school with current user:"+to_user.getId().intValue());
		}
		
		msg.setSchool_id(to_user.getSchool_id());
		msg.setFrom_user_id(from_user_id);
		msg.setTo_user_id(to_user_id);
		msg.setChannel(channel);
		if (class_id != null || class_id.intValue() > 0){
			msg.setClass_id(class_id);
		}
		
		msg.setContent(content);
		
		insertUserMessage(frm_user,msg);
 		
		return msg;
	}

	@Override
	public ArrayList<Message> findUnProcSMS(User me, String api_key) {
		if (!valid_sms_user_device(me,api_key)){
			throw new ESchoolException("sso_id:"+me.getSso_id()+"// device:"+api_key+" is not authorized to send SMS", HttpStatus.BAD_REQUEST);
		}
		ArrayList<Message> lst = (ArrayList<Message>) messageDao.findUnProcSms();
		return lst;
	}

	boolean valid_sms_user_device(User me, String api_key){
		
		ArrayList<SysTemplate> list = sysTblService.findAll("sys_settings", 0, 99999);
		String setting_sso  = null;
		String setting_imei  = null;
		for (SysTemplate sysTemplate : list){
			
			
			if (sysTemplate.getSval() != null && sysTemplate.getSval().equalsIgnoreCase("SMS_USER")){
				setting_sso = sysTemplate.getLval();
			}
			if (sysTemplate.getSval() != null && sysTemplate.getSval().equalsIgnoreCase("SMS_DEVICE")){
				setting_imei = sysTemplate.getLval();
			}
		}
		if ( (setting_sso != null && setting_sso.equals(me.getSso_id())) && 
				(setting_imei != null && setting_imei.equals(api_key)) ){
			return true;
		}
		
		return false;
	}

	@Override
	public void smsDone(User me, String api_key,Integer id) {
		if (!valid_sms_user_device(me,api_key)){
			throw new ESchoolException("sso_id:"+me.getSso_id()+"// device:"+api_key+" is not authorized to send SMS", HttpStatus.BAD_REQUEST);
		}
		
		Message msg = messageDao.findById(id);
		if (msg.getChannel() == null || msg.getChannel().intValue() != 1){
			throw new ESchoolException("message_id:"+id.intValue()+" have sms_channel != 1, not SMS channel", HttpStatus.BAD_REQUEST);
		}
		msg.setSms_sent_dt(Utils.currenDate());
		msg.setSms_sent_sts(1);
		
	}
	
	
	@Override
	public ArrayList<Message> sendClassMessage(User me, Message message, String filter_roles) {
		ArrayList<Message> list = new ArrayList<Message>();
		boolean ignored_school =false;
		if (me.hasRole(E_ROLE.SYS_ADMIN.getRole_short())){
			ignored_school =true;
		}
		//validate mandatory
		if (me == null || message == null){
			throw new ESchoolException(" Input mandatory paramester is NULL", HttpStatus.BAD_REQUEST);
		}
		if (message.getDest_type().intValue() != E_DEST_TYPE.CLASS.getValue()){
			throw new ESchoolException("Unknow Dest_type="+ message.getDest_type(), HttpStatus.BAD_REQUEST);
		}
		if (message.getClass_id() == null){
			throw new ESchoolException("message.class_id = NULL", HttpStatus.BAD_REQUEST);
		}
		
		//Insert message DB
		EClass eclass = classService.findById(message.getClass_id());
		if (eclass == null  ){
			throw new ESchoolException(" sendClassMessage(): message.getClass_id() is not existing:"+message.getClass_id().intValue(), HttpStatus.BAD_REQUEST);
		}
		if (!ignored_school && 
				eclass.getSchool_id().intValue() != me.getSchool_id().intValue()){
			throw new ESchoolException("sendClassMessage(): class's school_id is not same with me.school_id", HttpStatus.BAD_REQUEST);
		}
		//validate message vs school
		message.setSchool_id(eclass.getSchool_id());
			
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
		if (users == null || users.size() <= 0)	{
			
			//throw new ESchoolException("class_id:"+eclass.getId().intValue()+" has no users to send", HttpStatus.BAD_REQUEST);
			return null;
		}
			
		// Get list of users from class				
		for (User to_user : users){
				Message new_message = message.copy();
				// Send to Class, always set from Me user, only ADMIN can send to class or school
				// TEACHER: can only send personal message with CC
				new_message.setFrom_user_id(me.getId());
				new_message.setTo_user_id(to_user.getId());
				insertUserMessage(me,new_message);
				list.add(new_message);
		}
			
		return list;
	}

	@Override
	public ArrayList<Message> createClassMessageTaskWithCC(User me, Message message, String filter_roles) {
		
		ArrayList<Message> ret = new ArrayList<>();
		if (message.getClass_id() == null || message.getClass_id().intValue() == 0){
			throw new ESchoolException("sendClassMessageWithCC() Error, message.class_id =null", HttpStatus.BAD_REQUEST);
		}
		if (message.getContent() == null || message.getContent().trim().length() == 0){
			throw new ESchoolException("createTaskMsg(): message.Content is BLANK", HttpStatus.BAD_REQUEST);
		}
		
		boolean ignored_school = false;
		if (me.hasRole(E_ROLE.SYS_ADMIN.getRole_short())){
			ignored_school =true;
		}
		String[] to_class_list =null; 
		String cc_list = message.getCc_list()==null?"": message.getCc_list();
		
		cc_list = cc_list + ","+message.getClass_id().intValue() ;
		cc_list.replaceAll(",+",",");
		// Remove duplicated class_id
		to_class_list = Utils.duplicateRemove(cc_list.split(","));
			
		for (String class_id : to_class_list){
				EClass eclass = classService.findById(message.getClass_id());
			
				if (eclass == null ){
					throw new ESchoolException("createTaskMsg(): class_id not existing:"+message.getClass_id().intValue(), HttpStatus.BAD_REQUEST);
				}
				if (!ignored_school && 
						eclass.getSchool_id().intValue() != me.getSchool_id().intValue()){
					throw new ESchoolException("createTaskMsg(): class_id not in same school with current user, me_id="+me.getId().intValue()+"///class_id="+message.getClass_id().intValue(), HttpStatus.BAD_REQUEST);
				}
				// Copy all nessesary info if needed
				Message msg_task = message.copy();
				// Mandatory reset all other info of Task (id, school, class,to_user)
				msg_task.setId(null); // 20160822
				msg_task.setSchool_id(me.getSchool_id());
				msg_task.setClass_id(Utils.parseInteger(class_id));
				msg_task.setTo_user_id(null);// MUST BE NULL
				msg_task.setSent_dt(Utils.now());
				msg_task.setIs_sent(99); // Used to identify main task only

				// This is Main Task, not a real message
				messageDao.saveMessage(me,msg_task); // ==> gen task ID
				// Update task ID
				msg_task.setTask_id(msg_task.getId());
				
				// New crontab command
				commandService.create_class_message_cmd(me, msg_task, filter_roles);
				// Return value
				ret.add(msg_task);
				
		}
		return ret;
	}
	void valid_message(User me, Message message, boolean is_new){
		
		
		if (!is_new){
			if (message.getId() == null || message.getId().intValue() == 0){
				throw new ESchoolException("message.id == NULL, cannot update",HttpStatus.BAD_REQUEST);
			}	
		}
		
		// From user_id
		if (message.getFrom_user_id() == null || message.getFrom_user_id().intValue() <= 0){
			throw new ESchoolException("from_user_id is NUll",HttpStatus.BAD_REQUEST);
		}
		User frm_user = userService.findById(Integer.valueOf(message.getFrom_user_id()));
		if (frm_user == null){
			throw new ESchoolException("From user_id not existing",HttpStatus.BAD_REQUEST);
		}
		boolean ignored_school =false;
		if (me.hasRole(E_ROLE.SYS_ADMIN.getRole_short())){
			ignored_school =true;
			
		}
		else if (frm_user.hasRole(E_ROLE.SYS_ADMIN.getRole_short())){
			ignored_school =true;
		}
		
		if (	!ignored_school && 
				frm_user.getSchool_id().intValue() != me.getSchool_id().intValue()){
			throw new ESchoolException("From user_id:"+frm_user.getId().intValue()+" is not in same school with current user:"+me.getId().intValue(),HttpStatus.BAD_REQUEST);
		}
		
		message.setFrom_user_name(frm_user.getFullname());
		
		// To user_id
		if (message.getTo_user_id() == null || message.getTo_user_id().intValue() <= 0){
			throw new ESchoolException("to_user_id is NUll",HttpStatus.BAD_REQUEST);
		}
		User to_user = userService.findById(Integer.valueOf(message.getTo_user_id()));
		if (to_user == null){
			throw new ESchoolException("to_user_id not existing",HttpStatus.BAD_REQUEST);
		}
		
		if (!ignored_school && 
				to_user.getSchool_id().intValue() != me.getSchool_id().intValue()){
			throw new ESchoolException("ToUserId"+to_user.getId().intValue()+" is not in same school with current user:"+me.getId().intValue(),HttpStatus.BAD_REQUEST);
		}
		
		message.setSchool_id(to_user.getSchool_id());
		message.setTo_user_name(to_user.getFullname());		
		message.setTo_sso_id(to_user.getSso_id());
		message.setTo_phone(to_user.getPhone());
		if (is_new){
			message.setSent_dt(Utils.now());
			message.setIs_sent(1);
		}
		
		
		// Check channel
		
		if (message.getChannel() != null && 
				message.getChannel().intValue()==1 && // SEND SMS
				message.getTo_phone() == null || 
				message.getTo_phone().trim().length() == 0){
			throw new ESchoolException("to_phone is NULL, cannot send SMS",HttpStatus.BAD_REQUEST);
		}
		
		
		// Message content
		if (message.getContent() == null || message.getContent().trim().length()==0){
			throw new ESchoolException("Message content is BLANK",HttpStatus.BAD_REQUEST);
		}
		
				
	}

	@Override
	public Message sendUserMessageWithCC(User me, Message message) {
		Message new_msg = null;
		String[] to_user_list =null; 
		String cc_list = message.getCc_list()==null?"": message.getCc_list();
		if (message.getTo_user_id() > 0 ){
			cc_list = cc_list + ","+message.getTo_user_id() ;
			cc_list.replaceAll(",+",",");
			
			// Remove duplicated user_id
			
			to_user_list = Utils.duplicateRemove(cc_list.split(","));
			
		}
		User frm_user = userService.findById(Integer.valueOf(message.getFrom_user_id()));
		if (frm_user == null){
			throw new RuntimeException("From user is NUll");
		}
		boolean frm_sys_admin =false;
		if (frm_user.hasRole(E_ROLE.SYS_ADMIN.getRole_short())){
			frm_sys_admin =true;
		}
		for (String to_id : to_user_list){
			User to_user = userService.findById(Integer.valueOf(to_id));
			if (	frm_sys_admin || 
					(to_user != null && (to_user.getSchool_id() == frm_user.getSchool_id()))) {
				new_msg = message.copy();
				
				new_msg.setFrom_user_id(frm_user.getId());
				new_msg.setTo_user_id(Integer.valueOf(to_id));
				// Insert to DB
				insertUserMessage(me, new_msg);
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

	
	public Message insertUserMessage(User me, Message message) {
		// Valid message before send
		valid_message(me,message,true);
		messageDao.saveMessage(me,message);
		if (message.getChannel() == null ){
			message.setChannel(E_MSG_CHANNEL.FIREBASE.getValue());
		}
		// Send firebase
		if  (    message.getChannel().intValue() == E_MSG_CHANNEL.FIREBASE.getValue() ||
				message.getChannel().intValue() == E_MSG_CHANNEL.BOTH_FIREBASE_SMS.getValue()){
			firebaseMsgService.create_from_message(message);	
		}
		
		return message;
	}
}
