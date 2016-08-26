package com.itpro.restws.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itpro.restws.dao.NotifyDao;
import com.itpro.restws.dao.NotifyImgDao;
import com.itpro.restws.helper.Constant;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.E_DEST_TYPE;
import com.itpro.restws.helper.E_ENTITY;
import com.itpro.restws.helper.E_RIGHT;
import com.itpro.restws.helper.E_ROLE;
import com.itpro.restws.helper.E_SCOPE;
import com.itpro.restws.helper.MessageFilter;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.EClass;
import com.itpro.restws.model.Notify;
import com.itpro.restws.model.NotifyImg;
import com.itpro.restws.model.Permit;
import com.itpro.restws.model.School;
import com.itpro.restws.model.User;

@Service("notifyService")
@Transactional
public class NotifyServiceImpl implements NotifyService{
	protected static final Logger logger = Logger.getLogger(NotifyServiceImpl.class);
	@Autowired
    private Environment environment;
	
	@Autowired
	private NotifyDao notifyDao;
	
	@Autowired
	private NotifyImgDao notifyImgDao;
		
	@Autowired
	protected PermitService permitService;
	
	@Override
	public Notify findById(Integer id) {
		return notifyDao.findById(id);
	}

	@Autowired
	protected ClassService classService;
	
	@Autowired
	protected UserService userService;
	
	
	
	@Autowired
	protected SchoolService schoolService;
	
	@Autowired
	protected FirebaseMsgService firebaseMsgService;
	@Override
	public int countFromUser(Integer from_user_id) {
		return notifyDao.countByFromUser(from_user_id);
	}

	@Override
	public int countToUser(Integer to_user_id) {
		return notifyDao.countByToUser(to_user_id);
	}

	@Override
	public ArrayList<Notify> findFromUser(Integer from_userid, int from_num, int max_result) {
		
		return (ArrayList<Notify>) notifyDao.findByFromUser(from_userid, from_num, max_result);
	}

	@Override
	public ArrayList<Notify> findTomUser(Integer to_userid, int from_num, int max_result) {
		
		return (ArrayList<Notify>) notifyDao.findByToUser(to_userid, from_num, max_result);
	}

	@Override
	public int countBySchool(Integer school_id) {
		
		return notifyDao.countBySchool(school_id);
	}

	@Override
	public int countByClass(Integer class_id) {
		return notifyDao.countByClass(class_id);
	}

	@Override
	public ArrayList<Notify> findBySchool(Integer school_id, int from_num, int max_result) {
		return (ArrayList<Notify>) notifyDao.findBySchool(school_id, from_num, max_result);
	}

	@Override
	public ArrayList<Notify> findByClass(Integer class_id, int from_num, int max_result) {
		return (ArrayList<Notify>) notifyDao.findBySchool(class_id, from_num, max_result);
	}


	
	

	@Override
	public String productFile(String filename, byte[] bytes) {
		
		if (bytes == null ){
            try {
            	String dest_file = "./"+filename;// YYYY/MM/DD/UserID_YYYYMMDDSSMMSS_name;
                // Save to server
                BufferedOutputStream stream =new BufferedOutputStream(new FileOutputStream(new File(dest_file)));
                stream.write(bytes);
                stream.close();
                return "dest_file:" + dest_file;
            } catch (Exception e) {
            	String err = "You failed to upload " + filename + " => " + e.getMessage();
                throw new ESchoolException(err, HttpStatus.INTERNAL_SERVER_ERROR) ;
            }
        } else {
        	String err = "You failed to upload " + filename + " because the file was empty.";
            throw new ESchoolException(err, HttpStatus.NO_CONTENT) ;
        }
	}

	@Override
	public String productFile(String filename, MultipartFile multipartFile) {
		  if (!multipartFile.isEmpty()) {
                byte[] bytes;
				try {
					bytes = multipartFile.getBytes();
					return productFile(filename,bytes);
				} catch (IOException e) {
					String err = "You failed to upload " + filename + " => " + e.getMessage();
	                throw new ESchoolException(err, HttpStatus.INTERNAL_SERVER_ERROR) ;
				}
	        } else {
	        	String err = "You failed to upload " + filename + " because the file was empty.";
	        	throw new ESchoolException(err, HttpStatus.NO_CONTENT) ;
	        }
    }

	@Override
	public FileSystemResource getFile(String filename) {
		return new FileSystemResource(filename);
	}

	
//	private Notify insertNotify(Notify notify) {
//		notifyDao.saveNotify(notify);
//		return notify;
//	}

//	@Override
//	public Notify saveUploadData(MultipartFile file, String jsonInString ) {
//		Notify notify = null;
//		String fileName = null;
//		String filePath = null;
//		String upload_dir = environment.getRequiredProperty("upload_base");
//		String urlbase = environment.getRequiredProperty("file_url_base");
//    	if (!file.isEmpty()) {
//            try {
//            	
//            	String str_dir = Utils.makeFolderByTime(upload_dir);
//            	
//                fileName = file.getOriginalFilename();
//                filePath = str_dir+ "/" + fileName;
//                byte[] bytes = file.getBytes();
//                
//                
//                BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
//                buffStream.write(bytes);
//                buffStream.close();
//                logger.info(" *** successfully uploaded:"+filePath);
//            } catch (Exception e) {
//            	logger.error("Failed to upload " + filePath== null?"":filePath + ": " + e.getMessage());
//            }
//        } else {
//        	logger.error("Unable to upload. File is empty.");
//        	throw new ESchoolException("Unable to upload. File is empty.",HttpStatus.BAD_REQUEST);
//        }
//    	
//    	
//    	
//    	//JSON from String to Object
//    	ObjectMapper mapper = new ObjectMapper();
//    	try {
//			notify = mapper.readValue(jsonInString, Notify.class);
////			notify.setFile_path(filePath);
////			notify.setFile_url(filePath.replaceFirst(upload_dir, urlbase));
//			
//			insertNotify(notify);
//		} catch (IOException e) {
//			// 
//			e.printStackTrace();
//		}
//		return notify;
//	}

//	@Override
//	public Notify saveUploadData(
//			User user,
//			MultipartFile[] files, 
//			String[] captions, 
//			String content, 
//			String title,
//			String json_str_notify) {
//		// Validation Data
//		if (files == null  || captions== null || content== null ||title == null ||json_str_notify == null){
//			throw new ESchoolException("Input mandatory data is null",HttpStatus.BAD_REQUEST);
//		}
//		
//		if (files.length != captions.length){
//			throw new ESchoolException("file.length != captions.leghth",HttpStatus.BAD_REQUEST);
//		}
//
//		
//		//Convert JSON from String to Object
//    	ObjectMapper mapper = new ObjectMapper();
//    	Notify notify=null;
//    	try {
//			notify = mapper.readValue(json_str_notify, Notify.class);
//			// Save to DB
//			insertNotify(notify);
//		} catch (IOException e) {
//			throw new ESchoolException("Cannot convert STRING to JSON =>" + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//    	
//    	if (notify == null || notify.getId() <= 0){
//    		throw new ESchoolException("Cannot convert JSON to Notify, Notify is NULL",HttpStatus.INTERNAL_SERVER_ERROR);
//    	}
//		String upload_dir = environment.getRequiredProperty("upload_base");
//		String urlbase = environment.getRequiredProperty("file_url_base");
//		String fileName="";
//		String filePath ="";
//		for (int i = 0; i < files.length; i++) {
//			
//			MultipartFile file = files[i];
//			String caption = captions[i];
//			
//			if (file.isEmpty() ){
//				throw new ESchoolException("File is emtpy", HttpStatus.BAD_REQUEST);
//			}
//			try {
//				byte[] bytes = file.getBytes();
//				String str_dir = Utils.makeFolderByTime(upload_dir);
//				fileName = file.getOriginalFilename();            	
//                filePath = str_dir+ "/" + fileName;
//                
//				// Create the file on server
//				File serverFile = new File(filePath);
//				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
//				stream.write(bytes);
//				stream.close();
//				logger.info("Server File Location="+ serverFile.getAbsolutePath());
//				// Save NotifyImg to DB
//				NotifyImg  notifyImg = new NotifyImg();
//				notifyImg.setFile_name(fileName);
//				notifyImg.setFile_path(filePath);
//				notifyImg.setFile_url(filePath.replaceFirst(upload_dir, urlbase));
//				notifyImg.setCaption(caption);
//				notifyImg.setUpload_dt(Utils.now());
//				notifyImg.setUser_id(user.getId());
//				notifyImg.setNotify_id(notify.getId());
//				// Default fields
//				notifyImg.setActflg("A");
//				notifyImg.setCtdusr("HuyNQ-test");
//				notifyImg.setCtddtm(Utils.now());
//				notifyImg.setCtdpgm("RestWS");
//				notifyImg.setCtddtm(Utils.now());
//				notifyImg.setMdfusr("HuyNQ-test");
//				notifyImg.setLstmdf(Utils.now());
//				notifyImg.setMdfpgm("RestWS");
//				
//				// Save to DB
//				notify.addImageToNotify(notifyImg);
//				notifyDao.updateNotify(notify);
//				
//			} catch (Exception e) {
//				throw new ESchoolException("You failed to upload " + fileName + " => " + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
//			}
//		}
//		
//		
//		return notify;
//	}

	@Override
	public Notify saveUploadData(User me, MultipartFile[] files, String[] captions,String[]orders, String json_str_notify) {
		String err_msg = "";
		
		// Validation Data
		if (files == null  || captions== null ||json_str_notify == null){
//			throw new ESchoolException("Input mandatory data is null",HttpStatus.BAD_REQUEST);
			err_msg = "Some mandatory input fields are null, please check file - caption and json_str_notify,";
		}
		
		if (files.length != captions.length){
//			throw new ESchoolException("file.length != captions.leghth",HttpStatus.BAD_REQUEST);
			err_msg = err_msg + " number of uploaded files <> number of captions,";
		}
		
		if (files.length != orders.length){
//			throw new ESchoolException("file.length != captions.leghth",HttpStatus.BAD_REQUEST);
			err_msg = err_msg + " number of uploaded files <> number of orders,";
		}

		
		for (String order: orders){
			if (Utils.parseInteger(order) == null || Utils.parseInteger(order) <= 0){
//				throw new ESchoolException("Invalid Order:"+orders,HttpStatus.BAD_REQUEST);
				err_msg = err_msg + " Invalid order number, must be integer.";
				break;
			}
		}
		
		if (err_msg.length() > 0){
			throw new ESchoolException(err_msg,HttpStatus.BAD_REQUEST);
		}
		//Convert JSON from String to Object
		ObjectMapper mapper = new ObjectMapper();
		Notify notify=null;
		try {
			//Charset.forName("UTF-8").encode(json_str_notify);
			byte ptext[] = json_str_notify.getBytes(Constant.ISO_8859_1); 
			String value = new String(ptext, Constant.UTF_8); 
			notify = mapper.readValue(value, Notify.class);

			// Valid upload date
			valid_save_upload_notify(me,notify);
			// Create task, not real notify
			notify.setSent_dt(Utils.now());
			notify.setIs_sent(99);// Will not sent this
			notifyDao.saveNotify(notify);
			notify.setTask_id(notify.getId());
		} catch (IOException e) {
			throw new ESchoolException("Cannot convert STRING to JSON =>" + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (notify == null || notify.getId() <= 0){
			throw new ESchoolException("Cannot convert JSON to Notify, Notify is NULL",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		String upload_dir = environment.getRequiredProperty("upload_base");
		String urlbase = environment.getRequiredProperty("file_url_base");
		String fileName="";
		String filePath ="";
		for (int i = 0; i < files.length; i++) {
			
			MultipartFile file = files[i];
			String caption = captions[i];
			// convert to UTF-8
			byte ptext[] = caption.getBytes(Constant.ISO_8859_1); 
			String value = new String(ptext, Constant.UTF_8);
			
			caption =value;
			
			int order =Utils.parseInteger(orders[i]).intValue();
			if (file.isEmpty() ){
				throw new ESchoolException("File is emtpy", HttpStatus.BAD_REQUEST);
			}
			try {
				byte[] bytes = file.getBytes();
				String str_dir = Utils.makeFolderByTime(upload_dir);
				//fileName = file.getOriginalFilename();
				fileName = Utils.getFileName(me.getSso_id(),file.getOriginalFilename());            	
	            filePath = str_dir+ "/" + fileName;
	            
				// Create the file on server
				File serverFile = new File(filePath);
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();
				logger.info("Server File Location="+ serverFile.getAbsolutePath());
				// Save NotifyImg to DB
				NotifyImg  notifyImg = new NotifyImg();
				notifyImg.setFile_name(fileName);
				notifyImg.setFile_path(filePath);
				notifyImg.setFile_url(filePath.replaceFirst(upload_dir, urlbase));
				notifyImg.setCaption(caption);
				notifyImg.setUpload_dt(Utils.now());
				notifyImg.setUser_id(me.getId());
				notifyImg.setNotify_id(notify.getId());
				notifyImg.setTask_id(notify.getTask_id());
				notifyImg.setIdx(order);
				
				
				// Default fields
				notifyImg.setActflg("A");
				notifyImg.setCtdusr("HuyNQ-test");
				notifyImg.setCtddtm(Utils.now());
				notifyImg.setCtdpgm("RestWS");
				notifyImg.setCtddtm(Utils.now());
				notifyImg.setMdfusr("HuyNQ-test");
				notifyImg.setLstmdf(Utils.now());
				notifyImg.setMdfpgm("RestWS");
				
				// Save to DB
				notifyImgDao.saveImg(notifyImg);
				//notify.addImageID(notifyImg.getId());
				notify.addImageToNotify(notifyImg);
				//notifyDao.updateNotify(notify);
			} catch (Exception e) {
				throw new ESchoolException("You failed to upload " + fileName + " => " + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		// Persist Notify & Images
		notifyDao.updateNotify(notify);
		
	
		return notify;
	}

	
	private void valid_user_notify(User me, Notify notify, boolean is_new) {
		
		if (!is_new){
			if (notify.getId() == null || notify.getId().intValue() == 0){
				throw new RuntimeException("notify.id == NULL, cannot update");
			}	
		}
		
		// From user_id
		if (notify.getFrom_usr_id() == null || notify.getFrom_usr_id().intValue() <= 0){
			throw new RuntimeException("from_user_id is NUll");
		}
		User frm_user = userService.findById(Integer.valueOf(notify.getFrom_usr_id()));
		if (frm_user == null){
			throw new RuntimeException("From user_id not existing");
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
			throw new RuntimeException("From user_id:"+frm_user.getId().intValue()+" is not in same school with current user:"+me.getId().intValue());
		}
		notify.setFrom_user_name(frm_user.getFullname());
		
		// To user_id
		if (notify.getTo_user_id() == null || notify.getTo_user_id().intValue() <= 0){
			throw new RuntimeException("to_user_id is NUll");
		}
		User to_user = userService.findById(Integer.valueOf(notify.getTo_user_id()));
		if (to_user == null){
			throw new RuntimeException("to_user_id not existing");
		}
		
		if (!ignored_school && 
				to_user.getSchool_id().intValue() != me.getSchool_id().intValue()){
			throw new RuntimeException("ToUserId"+to_user.getId().intValue()+" is not in same school with current user:"+me.getId().intValue());
		}
		
		notify.setSchool_id(to_user.getSchool_id());
		notify.setTo_user_name(to_user.getFullname());		
		notify.setTo_sso_id(to_user.getSso_id());
		notify.setSent_dt(Utils.now());
		notify.setIs_sent(1);
	}

	private ArrayList<Notify> insertClassNotify(User me,Notify notify,EClass eclass, String filter_roles) {
		ArrayList<Notify> list = new ArrayList<>();
		
		//validate mandatory
		if (me == null || notify == null || eclass == null ){
			throw new ESchoolException(" Input mandatory paramester is NULL", HttpStatus.BAD_REQUEST);
		}
			
		//validate user vs school
		notify.setSchool_id(me.getSchool_id());

		if (me.getSchool_id() != eclass.getSchool_id()){
			throw new ESchoolException("From User and Class are not in the same School, user_id="+me.getId()+", class_id="+eclass.getId(), HttpStatus.BAD_REQUEST);
		}
		
		if (me.hasRole(E_ROLE.TEACHER.getRole_short())){
			if (!me.is_belong2class(eclass.getId())){
				throw new ESchoolException("TEACHER cannot send notify to class that is not assigned="+me.getId()+", class_id="+eclass.getId(), HttpStatus.BAD_REQUEST);
			}
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
		
		
		
		
		if (users == null)	{
			throw new ESchoolException("There isn't any user in Class id="+eclass.getId(), HttpStatus.BAD_REQUEST);
		}
		
		// Get list of users from class				
		for (User to_user : users){
			// Disable sending notify to current user
				if (me.getId() == to_user.getId()){
					continue;
				}
				Notify new_notify = notify.copy();
				
				new_notify.setFrom_usr_id(me.getId());
				new_notify.setTo_user_id(to_user.getId());
				
				insertUserNotify(me,new_notify);
				list.add(new_notify);
			
		}
		return list;
		
	}

	
	private ArrayList<Notify> insertClassNotifyExt(User me,Notify notify,String filter_roles) {
		
		if (notify.getDest_type() != E_DEST_TYPE.CLASS.getValue() ){
			throw new ESchoolException("Notify destination type is not for whole class, dest_type="+notify.getDest_type(), HttpStatus.BAD_REQUEST);
		}

		
		EClass eclass= classService.findById(notify.getClass_id());
		if (eclass == null){
			throw new ESchoolException("Class is not existing, class_id="+notify.getClass_id(), HttpStatus.BAD_REQUEST);
		}
		
		return insertClassNotify(me, notify, eclass, filter_roles);
		
	}
	
	

	private ArrayList<Notify> insertSchoolNotifyExt(User user,Notify notify,String filter_roles) {
		
		ArrayList<Notify> list = new ArrayList<>();
		if (notify.getDest_type() != E_DEST_TYPE.SCHOOL.getValue() ){
			throw new ESchoolException("Notify destination type is not for whole school, dest_type="+notify.getDest_type(), HttpStatus.BAD_REQUEST);
		}
		//validate user vs school
		if (user.getSchool_id() != notify.getSchool_id()){
			throw new ESchoolException("User id:"+user.getId()+" is not belong to school_id="+notify.getSchool_id(), HttpStatus.BAD_REQUEST);
		}
		
		//validate class vs school
		School school = schoolService.findById(notify.getSchool_id());
		
		if (school ==  null){
			throw new ESchoolException("School not existing, id="+ notify.getSchool_id(), HttpStatus.BAD_REQUEST);
		}
		
		int max_count = classService.countBySchoolID(school.getId());
		
		ArrayList<EClass> classes = classService.findBySchool(school.getId(), 0, max_count);
		
		for (EClass eclass :classes){
			list.addAll(insertClassNotify(user,notify,eclass,filter_roles));
		}
		return list;
	}

	@Override
	public ArrayList<Notify> broadcastNotify(User me, Notify notify, String filter_roles) {
		ArrayList<Notify> list = null;
		if (notify.getDest_type() == E_DEST_TYPE.CLASS.getValue()){
			list = insertClassNotifyExt(me, notify, filter_roles);
		}else if (notify.getDest_type() == E_DEST_TYPE.SCHOOL.getValue()){
			list = insertSchoolNotifyExt(me, notify, filter_roles);
		}else {
			throw new ESchoolException("Unknow notify.dest_type="+ notify.getDest_type(), HttpStatus.BAD_REQUEST);
		}
		// Batch update is_sent flg to 0
		if (list != null){
			boolean keep_cc_frm_user = false;
			if (list !=null && list.size() > 0){
				Notify e = list.get(0);
				if ((e.getTo_user_id() != null)   && (e.getTo_user_id() == me.getId())){
					keep_cc_frm_user = true;
				}
			}
			// CC to Current user 
			if (!keep_cc_frm_user){
				Notify new_notify = notify.copy();
				new_notify.setFrom_usr_id(me.getId());
				new_notify.setTo_user_id(me.getId());
				
				insertUserNotify(me, new_notify);
				
				list.add(new_notify);
			}

		}
		return list;
	}
	
	public HashSet<NotifyImg> findImgByTaskID(Integer task_id){
		 HashSet<NotifyImg> ret = new HashSet<NotifyImg>(0);
		 ArrayList<NotifyImg> list = notifyImgDao.findByTaskId(task_id);
		 if (list != null){
			 ret = new HashSet<NotifyImg>(list);
		 }
		 return ret;
	}
	
	
	@Override
	public int countNotifyExt(Integer school_id,
			//int from_row, int max_result, 
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
					Integer from_row_id) {
		
		Integer ret = notifyDao.countNotifyExt(
				school_id, 
//				from_row, 
//				max_result, 
//				// Secure
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
	public ArrayList<Notify> findNotifyExt(
			Integer school_id, 
			int from_row,
			int max_result,
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
			Integer from_row_id){
		
		
		ArrayList<Notify> notifies  = new ArrayList<Notify>();
		 List<Object> list = notifyDao.findNotifyExt(
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
		 
		 @SuppressWarnings("rawtypes")
		Iterator iter = list.iterator();
		 while (iter.hasNext())
	        {
	            Object[] obj = (Object[]) iter.next();
	            Integer max_id = (Integer)obj[0];
	            Integer task_id =(Integer)obj[1];
	            if ((task_id != null  ) &&    (task_id >0  ) ){
	            	Notify notify = this.findById(max_id);
	            	notify.setNotifyImages(findImgByTaskID(notify.getTask_id()));
	            	notifies.add(notify);
	            }
	        }

		return notifies;
	}

	@Override
	public MessageFilter secureGetMessages(User user) {
		
		// Get data access right for entity
		MessageFilter secure_filter = new MessageFilter();
		Permit permit = permitService.loadEntityPermit(user,E_ENTITY.NOTIFY);
			
		if (permit == null ){
			throw new ESchoolException("User role="+user.getRoles()+" do not have Access permission for NOTIFY", HttpStatus.UNAUTHORIZED);
		}
		if ( E_RIGHT.R.getValue() != (permit.getRights() & E_RIGHT.R.getValue())){
			throw new ESchoolException("User role="+user.getRoles()+" do not have READ permission for NOTIFY", HttpStatus.UNAUTHORIZED);
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
	public Notify updateNotify(Notify notify) {
		notifyDao.updateNotify(notify);
		
		return notify;
	}

	public Notify insertUserNotify(User me, Notify new_notify) {
		// Valid message before send
		valid_user_notify(me, new_notify,true);
		
		notifyDao.saveNotify(new_notify);
		// Send firebase
		firebaseMsgService.create_from_notify(new_notify);
		return new_notify;
		
	}

	private void valid_save_upload_notify(User me, Notify notify) {
        //validate mandatory
        if (me == null || notify == null  ){
            throw new ESchoolException("User-me or Input Notify is NULL", HttpStatus.BAD_REQUEST);
        }
        //School ID
        if (me.hasRole(E_ROLE.SYS_ADMIN.getRole_short())){
        	 if (notify.getSchool_id() == null || 
 	                notify.getSchool_id().intValue() == 0 ){
        		 throw new ESchoolException("notify.school_id = NULL", HttpStatus.BAD_REQUEST);
 	        }
        }else{
        	notify.setSchool_id(me.getSchool_id());
        }
         
        // Check dest type, only class is acccepted 
         
        if (notify.getDest_type() == null || notify.getDest_type().intValue() == 0){
            throw new ESchoolException("notify.dest_type is NULL", HttpStatus.BAD_REQUEST);
        }
        
        if (notify.getDest_type().intValue() == E_DEST_TYPE.CLASS.getValue()){
            if (notify.getClass_id() == null || notify.getClass_id().intValue() == 0){
                throw new ESchoolException("notify.class_id is NULL", HttpStatus.BAD_REQUEST);  
            }
            EClass eclass = classService.findById(notify.getClass_id());
            if (notify.getSchool_id() != eclass.getSchool_id()){
                throw new ESchoolException("notify.school_id != class.school_id", HttpStatus.BAD_REQUEST);
            }
             
            if (me.hasRole(E_ROLE.TEACHER.getRole_short())){
                if (!me.is_belong2class(eclass.getId())){
                    throw new ESchoolException("TEACHER cannot send notify to class that is not assigned="+me.getId()+", class_id="+eclass.getId(), HttpStatus.BAD_REQUEST);
                }
            }
             
        }else if (notify.getDest_type().intValue() == E_DEST_TYPE.SCHOOL.getValue()){
        	// Do nothing
        }else{
        	throw new ESchoolException("Unknow notify.dest_type="+ notify.getDest_type(), HttpStatus.BAD_REQUEST);
        }
        		
         
    }
 
}
