package com.itpro.restws.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.itpro.restws.dao.CommandDao;
import com.itpro.restws.dao.EmailMsgDao;
import com.itpro.restws.dao.MSubjectDao;
import com.itpro.restws.helper.Constant;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.E_DEST_TYPE;
import com.itpro.restws.helper.E_ROLE;
import com.itpro.restws.helper.E_STATE;
import com.itpro.restws.helper.FinishExamInfo;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.ActionLogVIP;
import com.itpro.restws.model.Command;
import com.itpro.restws.model.EClass;
import com.itpro.restws.model.EmailMsg;
import com.itpro.restws.model.ExamRank;
import com.itpro.restws.model.ExamResult;
import com.itpro.restws.model.MSubject;
import com.itpro.restws.model.MTemplate;
import com.itpro.restws.model.Message;
import com.itpro.restws.model.Notify;
import com.itpro.restws.model.School;
import com.itpro.restws.model.SchoolExam;
import com.itpro.restws.model.SysTemplate;
import com.itpro.restws.model.User;

@Service("asyncRunner")
public class AsyncRunner {
	protected static final Logger logger = Logger.getLogger(AsyncRunner.class);
	@Autowired
	protected ApplicationContext applicationContext;
	@Autowired
	protected CommandDao commandDao;
	
	@Autowired
	protected UserService userService;
	
	@Autowired
	protected MessageService messageService;
	
	@Autowired
	protected NotifyService notifyService;
	@Autowired
	protected ExamResultService examResultService;
	@Autowired
	private MSubjectDao msubjectDao;
	@Autowired 
	protected SchoolService schoolService;
	
	@Autowired 
	protected ClassService classService;
	
	@Autowired 
	protected ActionLogVIPService actionLogVIPService;
	
	@Autowired 
	protected SchoolExamService schoolExamService;
	
	@Autowired 
	protected EmailMsgDao emailMsgDao;
//	@Autowired 
//	protected SendMailDao sendMailDao;
	
	@Autowired
	protected SysTblService sysTblService;
	@Autowired 
	protected MasterTblService masterTblService;
	
	static int cron_id=0;
	
	@Async
	public void execCommands(){
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(" *** " + method_name + "() START");
		 // task execution logic
			try {
				cron_id++;
				//logger.info("++++++ AsyncRunner START,ID="+cron_id);
				ArrayList<Command> list = (ArrayList<Command>) commandDao.findUnProcessed();
				if (list.size() > 0){
					logger.info("execCommands[ID="+cron_id+ "] found :"+list.size()+" tasks");
				}
				for (Command command:list){
					try{
						command.setProcessed(1);
						command.setProcessed_dt(Utils.now());
						logger.info("execCommands[cron_id]="+cron_id+"///"+command.toString());
						// Forgot Password
						if (Constant.CMD_FOROT_PASS.equals(command.getCommand())){
							proc_gorgot_pass(command);
								
						}
						// Send Message To Classes
						else if (Constant.CMD_MESSAGE.equals(command.getCommand())){
							proc_message(command);
								
						}else if (Constant.CMD_NOTIFY.equals(command.getCommand())){
							proc_notify(command);
								
						}else if (Constant.CMD_RANK_PROCESS.equals(command.getCommand())){
							proc_rank_process(command);
								
						}
	
					}catch (ESchoolException e){
						command.setSuccess(0);
						command.setMessage(e.getError_msg());
					}catch (Exception e){
						command.setSuccess(0);
						command.setMessage(e.getMessage()+"///cause:"+(e.getCause()==null?"---":e.getCause().toString()));
					}finally{
						commandDao.updateCommand(command);
						logger.info("++++++ execCommands END,ID="+cron_id);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}finally{
				cron_id--;
			}
			
	     }
		
		
		
		private void proc_rank_process(Command command) {
			logger.info("proc_rank_process() START, command:"+command.toString());
			if  (command.getParams() == null ){
				throw new ESchoolException("RANK_PROCESS, no params: please check param format: me_id=[0-9]+&class_ids=x,y,z&ex_key=mXYZ",HttpStatus.BAD_REQUEST);
			}
			
			if (command.getParams().matches("me_id=[0-9]+&class_ids=[0-9,]+&ex_key=[a-zA-Z0-9-_]+")){
				String[] params = command.getParams().split("&");
				
				Integer me_id = Utils.parseInteger(params[0].split("=")[1]);
				String class_ids = params[1].split("=")[1];
				String ex_key = params[2].split("=")[1];
				
				String[] ids = class_ids.split("=");
					
				// Check user
				if (me_id == null || me_id.intValue()==0){
					command.setSuccess(0);
					command.setMessage("Error:me_id is null");
					return;
				}
				User me = userService.findById(me_id);
				if (me == null ){
					command.setSuccess(0);
					command.setMessage("user_id not existing:"+me_id.intValue());
					return;
				}
				// Check class_id
				if (ids == null || ids.length == 0){
					command.setSuccess(0);
					command.setMessage("Error: class_ids is null or blank");
					return;
				}
				
				command.setSuccess(0);
				command.setMessage("Unknow error");
				int cnt = 0;
				for (String str_id: ids){
					Integer class_id = Utils.parseInteger(str_id);
					if (class_id != null && class_id.intValue() > 0){
						// Calculate Average value by Month (m1,m2..m20)
						ArrayList<ExamRank> examRanks = examResultService.execClassMonthAve(me, class_id,ex_key);
						// Ranking average value by Month (m1,m2..m20)		
						if (examRanks != null && examRanks.size() > 0){
							examResultService.procAllocation(me,examRanks);
							cnt += examRanks.size();
						}
					}
				}
				
				String info ="result: results items:"+cnt;
				logger.info("proc_rank_process():"+info);
				command.setSuccess(1);
				command.setMessage("Finished at:"+Utils.now()+"///"+info);
				
				
			}else{
				throw new ESchoolException("NOTIFY, invalid params: correct format:me_id=[0-9]+&task_id=[0-9]+&filter_roles=[^=]*",HttpStatus.BAD_REQUEST);
			}
			logger.info("proc_notify() START, command:"+command.toString());
		
		}



		private void proc_gorgot_pass(Command command){
			logger.info("proc_gorgot_pass() START, command:"+command.toString());
			if  (command.getParams() == null ){
				throw new ESchoolException("FORGOT Password, no params: sso_id=xxx&phone=xxx",HttpStatus.BAD_REQUEST);
			}
			
			if (command.getParams().matches("sso_id=[-a-zA-Z0-9]+&phone=\\d{3,20}")){
				String[] params = command.getParams().split("&");//sso_id=xxxx&phone=xxxx
				String sso_id = params[0].split("=")[1];
				String phone = params[1].split("=")[1];
				logger.info("sso_id:"+sso_id);
				logger.info("phone:"+phone);
				
				User user = userService.findBySso(sso_id);
				if (user == null || (user.getState() != E_STATE.ACTIVE.value())){
					throw new ESchoolException("sso_id:("+sso_id+") is not exising",HttpStatus.BAD_REQUEST);
				}
				if (user.getPhone() == null ||  (!user.getPhone().equals(phone))){
					throw new ESchoolException("phone:("+phone+") is not mapped with user's phone",HttpStatus.BAD_REQUEST);
				}
				String ret = userService.forgotPassword(user,sso_id, phone);
				logger.info("result:"+ret);
				command.setSuccess(1);
				command.setMessage(ret);
			}else{
				throw new ESchoolException("FORGOT Password, invalid params: correct format: sso_id=xxx&phone=xxx",HttpStatus.BAD_REQUEST);
			}
			logger.info("proc_gorgot_pass() END, command:"+command.toString());
			
		}
		private void proc_notify(Command command) {
			logger.info("proc_notify() START, command:"+command.toString());
			if  (command.getParams() == null ){
				throw new ESchoolException("NOTIFY, no params: please check param format: me_id=[0-9]+&task_id=[0-9]+&filter_roles=[^=]*",HttpStatus.BAD_REQUEST);
			}
			
			if (command.getParams().matches("me_id=[0-9]+&task_id=[0-9]+&filter_roles=[^=]*")){
				String[] params = command.getParams().split("&");//me_id=yyy&task_id=xxxx&filter_rolese=xxxx
				
				Integer me_id = Utils.parseInteger(params[0].split("=")[1]);
				Integer task_id = Utils.parseInteger(params[1].split("=")[1]);
				
				String filter_roles = null;
				String[] roles = params[2].split("=");
				
				if (roles != null && roles.length > 1){
					filter_roles = params[2].split("=")[1];
				}
				
				logger.info("proc_notify():task_id:"+task_id.intValue());
				logger.info("proc_notify():filter_roles:"+filter_roles);
				if (filter_roles !=null && filter_roles.trim().length() == 0){
					filter_roles = null;
				}
				if (filter_roles !=null && filter_roles.equalsIgnoreCase("null")){
					filter_roles = null;
				}
				if (me_id ==  null || me_id.intValue() <= 0){
					throw new ESchoolException("NOTIFY,me_id=NULL",HttpStatus.BAD_REQUEST);
				}
				if (task_id ==  null || task_id.intValue() <= 0){
					throw new ESchoolException("NOTIFY,task_id=NULL",HttpStatus.BAD_REQUEST);
				}
				// Send message
				User me = userService.findById(me_id);
				if (me == null ){
					throw new ESchoolException("NOTIFY, me_id not existing: task_id="+task_id.intValue()+"///me_id:"+me_id.intValue(),HttpStatus.BAD_REQUEST);
				}
				Notify notifyTask = notifyService.findById(task_id);
				if (notifyTask == null ){
					throw new ESchoolException("NOTIFY, task not existing: task_id="+task_id.intValue(),HttpStatus.BAD_REQUEST);
				}
				
				ArrayList<Notify> list  = notifyService.broadcastNotify(me, notifyTask, filter_roles); //20160823
				String info ="result: list.size()="+(list == null ?"0":list.size()+"");
				logger.info("proc_notify(): result: list.size()="+list == null ?0:list.size());
				command.setSuccess(1);
				command.setMessage("Finished at:"+Utils.now()+"///"+info);
				
				
			}else{
				throw new ESchoolException("NOTIFY, invalid params: correct format:me_id=[0-9]+&task_id=[0-9]+&filter_roles=[^=]*",HttpStatus.BAD_REQUEST);
			}
			logger.info("proc_notify() START, command:"+command.toString());
			
		}
		private void proc_message(Command command) {
			logger.info("proc_message() START, command:"+command.toString());
			if  (command.getParams() == null ){
				throw new ESchoolException("MESSAGE, no params please check params format: me_id=[0-9]+&task_id=[0-9]+&filter_roles=[^=]*",HttpStatus.BAD_REQUEST);
			}
			
			if (command.getParams().matches("me_id=[0-9]+&task_id=[0-9]+&filter_roles=[^=]*")){
				String[] params = command.getParams().split("&");//me_id=yyy&task_id=xxxx&filter_rolese=xxxx
				
				Integer me_id = Utils.parseInteger(params[0].split("=")[1]);
				Integer task_id = Utils.parseInteger(params[1].split("=")[1]);
				
				String filter_roles = null;
				String[] roles = params[2].split("=");
				
				if (roles != null && roles.length > 1){
					filter_roles = params[2].split("=")[1];
				}
				
				logger.info("proc_message():task_id:"+task_id.intValue());
				logger.info("proc_message():filter_roles:"+filter_roles);
				if (filter_roles !=null && filter_roles.trim().length() == 0){
					filter_roles = null;
				}
				if (filter_roles !=null && filter_roles.equalsIgnoreCase("null")){
					filter_roles = null;
				}
				if (me_id ==  null || me_id.intValue() <= 0){
					throw new ESchoolException("MESSAGE,me_id=NULL",HttpStatus.BAD_REQUEST);
				}
				if (task_id ==  null || task_id.intValue() <= 0){
					throw new ESchoolException("MESSAGE,task_id=NULL",HttpStatus.BAD_REQUEST);
				}
				// Send message
				User me = userService.findById(me_id);
				if (me == null ){
					throw new ESchoolException("MESSAGE, me_id not existing: task_id="+task_id.intValue()+"///me_id:"+me_id.intValue(),HttpStatus.BAD_REQUEST);
				}
				Message messageTask = messageService.findById(task_id);
				if (messageTask == null ){
					throw new ESchoolException("MESSAGE, task not existing: task_id="+task_id.intValue(),HttpStatus.BAD_REQUEST);
				}
				
				
				if (messageTask.getDest_type() == E_DEST_TYPE.CLASS.getValue()){
					
					ArrayList<Message> list  = messageService.sendClassMessage(me, messageTask, filter_roles);
					
					String info ="result: list.size()="+(list == null ?"0":list.size()+"");
					logger.info("proc_message(): result: list.size()="+list == null ?0:list.size());
					command.setSuccess(1);
					command.setMessage("Finished at:"+Utils.now()+"///"+info);
				}else{
					throw new ESchoolException("MESSAGE, message.dest_type not supported, dest_type =="+messageTask.getDest_type().intValue(),HttpStatus.BAD_REQUEST);
				}
				
				
			}else{
				throw new ESchoolException("MESSAGE, invalid params: correct format:me_id=[0-9]+&task_id=[0-9]+&filter_roles=[^=]*",HttpStatus.BAD_REQUEST);
			}
			logger.info("proc_message() END, command:"+command.toString());
			
		}
		
		
		@Async
		public void execDailyReport(Integer from_school_id, Integer to_school_id){
			String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
			logger.info(" *** " + method_name + "() START");
			 // task execution logic
				try {
					cron_id++;
					ArrayList<School> schools = schoolService.findActive();
					if (schools != null && schools.size() > 0 ){
						for (School school : schools){
							if (from_school_id != null && from_school_id.intValue() > 0){
								if (school.getId().intValue() < from_school_id.intValue()){
									continue;
								}	
							}
							if (to_school_id != null && to_school_id.intValue() > 0){
								if (school.getId().intValue() > to_school_id.intValue()){
									continue;
								}	
							}
							
							
							// Get inactive presidents
							 ArrayList<EClass> no_president_classes = get_no_president_classes(school);

							// Get inactive presidents
							 ArrayList<EClass> no_rollup_classes = get_no_rollup_classes(school);
							 // Get finish exam
							 ArrayList<FinishExamInfo> finish_scores = get_finish_input_score(school);
							 /***
							  * LaoSchool - [Ten Truong] [Thoi gian gui]
								[Ten lop]
								Noi dung:
								[Khong diem danh] (Truong hop lop truong ko diem danh
								
								[Cham diem thang:....
								Mon:.....] (truong hop admin hoac giao vien cham diem)
								
								vi du
								LaoSchool - truong tieu hoc thanh xuan
								
								[Khong assign giao vien - lop truong]
								Lop 1A
								Lopt 1B
								Lopt 1C
								Lopt 2A
								Lopt 2B
								
								[Khong diem danh]
								Lop 1A
								Lopt 1B
								Lopt 1C
								Lopt 2A
								Lopt 2B
								
								[Finish cham diem:]
								Thang: 1
								Toan: Lop 1A,Lop 1C,Lop 2C
															
								Thang: 2
								Toan: Lop 1A,Lop 1C,Lop 2C																

							  */
							 String lacking_msg = "";
							 String attendance_msg = "";
							 String exam_msg = "";
							 String msg = "";
							 // No assigned president or teacher
							 if ( no_president_classes!= null && no_president_classes.size() > 0){
								 for (EClass eclass: no_president_classes){
									    lacking_msg += "  " + eclass.getTitle() + "\n";
									 // lacking_msg += "  " + eclass.getTitle() + " / id = "+eclass.getId().intValue()+" \n";
								 }
							 }
							 
							 // No Attendance
							 if (no_rollup_classes != null && no_rollup_classes.size() > 0){
								 for (EClass eclass: no_rollup_classes){
									    attendance_msg += "  " + eclass.getTitle() + "\n";
									 // attendance_msg += "  " + eclass.getTitle() + " / id = "+eclass.getId().intValue()+" \n";
								 }
							 }
								 
							// Report finish exam info by Month - Subject
							// Key: Month-Subject
							 // Value: Class1, Class 2
							 /***
							  *[Fnish cham diem:]
										Thang: 1
										Toan: Lop 1A,Lop 1C,Lop 2C
																	
										Thang: 2
										Toan: Lop 1A,Lop 1C,Lop 2C
							  */								 
							 if (finish_scores != null && finish_scores.size() > 0){
								 // Group By Month
								 //    Group by Subject
								 //        value: ArrayList<FinishExamInfo>  
								 HashMap<String, HashMap<String, ArrayList<FinishExamInfo>>> month_grp = new HashMap<String, HashMap<String, ArrayList<FinishExamInfo>>>();
								
								 for (FinishExamInfo examInfo : finish_scores){
									 String month = examInfo.getEx_key(); // Sep, Oct, Nov
									 String subject = ""+examInfo.getSubject_id().intValue();// Toan, Ly, Toa
									 // Group by Month
									 HashMap<String, ArrayList<FinishExamInfo>> subject_grp = month_grp.get(month);
									  if (subject_grp == null){
										  subject_grp = new HashMap<String, ArrayList<FinishExamInfo>>();
										  month_grp.put(month, subject_grp);
									  }
									  // Group by Subject
									  ArrayList<FinishExamInfo> infos = subject_grp.get(subject);
									  if (infos == null ){
										  infos = new ArrayList<FinishExamInfo>();
										  subject_grp.put(subject, infos);
									  }
									  infos.add(examInfo);
									  
								 }
								 /***
								  * Thang: 2
									   Toan: Lop 1A,Lop 1C,Lop 2C
								  */
								 
								 if (month_grp != null  && month_grp.size() > 0){
									 for (String month : month_grp.keySet()) {
										 HashMap<String, ArrayList<FinishExamInfo>> subject_grp =  month_grp.get(month);
										 
										 if (subject_grp != null && subject_grp.size() > 0){
											 int cnt_sub = 0;
											 for (String subject : subject_grp.keySet()) {
												 cnt_sub ++;
												 ArrayList<FinishExamInfo> finishExamInfos = subject_grp.get(subject);
												 if (cnt_sub == 1){
													 exam_msg +="  "+finishExamInfos.get(0).getEx_name()+"\n"; 						       // Sep
												 }												 
												 
												 if (finishExamInfos != null && finishExamInfos.size() > 0){
													 int cnt_exam=0;
													 for (FinishExamInfo finishExamInfo : finishExamInfos){
														 cnt_exam ++;
														 if (cnt_exam == 1){
															 exam_msg +="    "+finishExamInfo.getSubject_name()+": "; // Toan : 
														 }
														 exam_msg+= finishExamInfo.getClass_title()+",";  //  Lop 1A,Lop 1C,Lop 2C
													 }
												 }
												 exam_msg = Utils.removeTxtLastComma(exam_msg);
												 exam_msg +="\n";
											 }
										 }
									 }
								 }
							 } 
							 // Format Email Content
							 msg = "LaoSchool - " + school.getTitle() + "\n\n";
							 // Lacking teachers
							 if (lacking_msg.trim().length() > 0){
								 msg += "[No assigned class president]\n";							 
								 msg +=lacking_msg.trim().length()==0?"  Not found\n":lacking_msg;
								 msg += "-----------------------\n";
							 }
							 
							 // Lacking attendance
							 msg += "[No rollup]\n";
							 msg +=attendance_msg.trim().length()==0?"  Not found\n":attendance_msg;
							 msg += "-----------------------\n";
							 
							 // Finish input diem
							 msg += "[Finish input score]\n";								 
							 msg +=exam_msg.trim().length()==0?"  Not found\n":exam_msg;
							 msg += "-----------------------\n";
							 
							 
							 // Send email
							 if (msg.trim().length() > 0){
								 logger.info(" -  msg:"+msg);
								 
								 String receivers = "";
								 // Get receivers from DB 
								 ArrayList<MTemplate> m_emails = masterTblService.findBySchool(MasterTblName.TBLNAME_M_EMAIL.getTblName(), school.getId(),0,999);
								 if (m_emails != null && m_emails.size() > 0){
									 for (MTemplate temp: m_emails){
										 receivers+= temp.getSval();
									 }
								 }
								 ArrayList<SysTemplate> list = sysTblService.findBySvalAll("sys_settings", "SYS_RECEIVERS");
								 for (SysTemplate symTemplate: list){
									if (symTemplate != null  && 
											symTemplate.getLval() != null )
									{
										receivers+= symTemplate.getLval();
									}
								}
								 logger.info(" -  receivers:"+receivers);
								 logger.info(" -  msg:"+msg);
								
								 if (receivers.trim().length() == 0){
									 logger.warn(" -  receivers is blank, ignored"); 
								 }else{
									 if (
											 no_rollup_classes.size() > 0 ||
											 finish_scores.size() > 0 ||
											 no_president_classes.size() > 0)
									 {
										 EmailMsg emailMsg = new EmailMsg();
										 emailMsg.setSchool_id(school.getId());
										 emailMsg.setContent(msg);
										 emailMsg.setReceivers(receivers);
										 emailMsgDao.saveMsg(emailMsg);
									 }else{
										 logger.warn(" -  not issue found, ignored");
									 }
								 }
								// Saving Email to DB any way
//								 SendMail sendMail = new SendMail();
//								 sendMail.setReceivers(receivers);
//								 sendMail.setBody(msg);
//								 sendMail.setSub("[LaoSchool] Daily Report");
//								 sendMailDao.saveEmail(sendMail);
								 
							 }
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e.getMessage());
				}finally{
					cron_id--;
				}
				logger.info(" *** " + method_name + "() END");
					
		}

		private ArrayList<EClass> get_no_president_classes(School school) {
			String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
			logger.info(" *** " + method_name + "() START");
			logger.info(" - school_id[" + school.getId() + "], title:"+school.getTitle());
			
			ArrayList<EClass> lacking = new ArrayList<EClass>();
			// Lay danh sach class
			ArrayList<EClass> actives =  classService.findActiveBySchool(school.getId());
			
			if (actives == null || actives.size() <= 0){
				logger.info(" - school_id[" + school.getId() + "], no active class found");
				return null;
			}
			for (EClass eclass : actives){
				ArrayList<User> checkers = new ArrayList<User>();
				logger.info(" - school_id[" + school.getId() + "], eclass["+eclass.getId()+"], title:"+eclass.getTitle());
				// Lay account lop truong
				ArrayList<User> presidents = userService.findUserExt(school.getId(), 0, 999999, eclass.getId(), E_ROLE.CLS_PRESIDENT.getRole_short(), E_STATE.ACTIVE.value(),null);
				if (presidents == null || presidents.size() <= 0){
					logger.info(" - school_id[" + school.getId() + "], eclass["+eclass.getId()+"], no president found");
//					logger.info(" - school_id[" + school.getId() + "], eclass["+eclass.getId()+"],  find head teacher instead");
//					// continue;
//					// Lay account giao vien
//					Integer tea_id =  eclass.getHead_teacher_id();
//					if (tea_id != null && tea_id.intValue() > 0){
//						User h_teacher = userService.findById(tea_id);
//						if (h_teacher != null && 
//								h_teacher.getSchool_id().intValue() == school.getId().intValue() &&
//								h_teacher.getState().intValue() == E_STATE.ACTIVE.value()){
//							checkers.add(h_teacher);
//						}
//					}
				}else{
					checkers.addAll(presidents);
				}
				
			
				if (checkers == null || checkers.size() <= 0){
					logger.info(" - school_id[" + school.getId() + "], eclass["+eclass.getId()+"], lacking CLS_PRESIDENT and HEAD TEACHER");
					lacking.add(eclass);
				}
			}
				
			return lacking;
		}



		/***
		 * * Lop truong khong thuc hien diem danh 
		 *   Khong co request nao den server trong muc diem danh cua lop truong
		 *                
		 */
		private ArrayList<EClass> get_no_rollup_classes(School school) {
			String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
			logger.info(" *** " + method_name + "() START");
			logger.info(" - school_id[" + school.getId() + "], title:"+school.getTitle());
			
			ArrayList<EClass> inactives = new ArrayList<EClass>();
			// Lay danh sach class
			ArrayList<EClass> classes =  classService.findActiveBySchool(school.getId());
			
			if (classes == null || classes.size() <= 0){
				logger.info(" - school_id[" + school.getId() + "], no class found");
				return null;
			}
			for (EClass eclass : classes){
				ArrayList<User> checkers = new ArrayList<User>();
				logger.info(" - school_id[" + school.getId() + "], eclass["+eclass.getId()+"], title:"+eclass.getTitle());
				// Lay account lop truong
				ArrayList<User> presidents = userService.findUserExt(school.getId(), 0, 999999, eclass.getId(), E_ROLE.CLS_PRESIDENT.getRole_short(), E_STATE.ACTIVE.value(),null);
				if (presidents == null || presidents.size() <= 0){
					logger.info(" - school_id[" + school.getId() + "], eclass["+eclass.getId()+"], no president found");
					// continue;
				}else{
					checkers.addAll(presidents);
				}
				
				// Lay account giao vien
				Integer head_teacher_id =  eclass.getHead_teacher_id();
				if (head_teacher_id != null && head_teacher_id.intValue() > 0){
					User h_teacher = userService.findById(head_teacher_id);
					if (h_teacher != null && 
							h_teacher.getSchool_id().intValue() == school.getId().intValue() &&
							h_teacher.getState().intValue() == E_STATE.ACTIVE.value()){
						checkers.add(h_teacher);
					}
				}
				if (checkers == null || checkers.size() <= 0){
					logger.info(" - school_id[" + school.getId() + "], eclass["+eclass.getId()+"], no president or head_teacher found, rollup checkers is NULL");
					continue;
				}
				
				int cnt = 0;
				
				for (User checker: checkers){
					cnt += actionLogVIPService.countActionLogExt(school.getId(), checker.getSso_id(), Utils.currenDate(), null, Constant.ACTION_TYPE_ROLLUP);
					if (cnt <= 0){
						// Lop truong chi co the gui message trong chuc nang diem danh
						// Cho nen neu thay message tu lop truong, tuc la co diem danh roi
						if (checker.hasRole(E_ROLE.CLS_PRESIDENT.getRole_short())){
							cnt += actionLogVIPService.countActionLogExt(school.getId(), checker.getSso_id(), Utils.currenDate(), null, Constant.ACTION_TYPE_MESSAGE);
						}
					}
					
					if (cnt > 0){
						break;
					}
				}
				
				
				// kiem tra diem danh class
				if (cnt <= 0){
					inactives.add(eclass);
				}
			}
				
			return inactives;
				
		}

		/***
		 *  Chi gui email khi cham diem hoan thien cho 
		 *  tat ca cac hoc sinh trong lop theo thang - mon hoc 
		 */
		private ArrayList<FinishExamInfo> get_finish_input_score(School school) {
			String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
			logger.info(" *** " + method_name + "() START");
			logger.info(" - school_id[" + school.getId() + "], title:"+school.getTitle());
			/***
			 * 1. Lay danh sach cham diem hom na
			 * 2. Filter rieng class_id
			 * 3. Filter rieng subject list
			 * 4. Filter rieng ex_key list
			 * 5. Loop school_id, class_id,subject, ex_key
			 *     5.1 checkInputComplated(school_id, class_id, subject_id, ex_key)
			 *     input to finishedList(FinishExamInfo)
			 */
			
			ArrayList<FinishExamInfo> finish_list = new ArrayList<FinishExamInfo>();
			
			// Lay danh sach cham diem hom nay
			ArrayList<ActionLogVIP> logs = actionLogVIPService.findActionLogExt(school.getId(), 0, 99999, null, Utils.currenDate(), null, Constant.ACTION_TYPE_MARK);
			if (logs == null || logs.size() <= 0){
				return null;
			}
			
			// Lay danh sach ExamResults
			ArrayList<ExamResult> examResults = new ArrayList<ExamResult>();
			for (ActionLogVIP actionLogVIP : logs){
				String json = actionLogVIP.getStr_json();
				if (json == null || json.trim().length() <= 0){
					continue;
				}
				examResults.add(ExamResult.jsonToObject(json));
			}
			if (examResults == null || examResults.size() <= 0){
				logger.info(" - school_id[" + school.getId() + "], examResults is BLANK, ignored");
				return null;
			}
			
			
			ArrayList<SchoolExam> schoolExams = (ArrayList<SchoolExam>) schoolExamService.findBySchool(school.getId());
			if (schoolExams == null || schoolExams.size() <= 0){
				logger.error(" - school_id[" + school.getId() + "], schoolExams is BLANK, ignored");
				return null;
			}
			
			// Filter danh sach class, subject, ex_key
//			HashSet<Integer> class_ids  = new HashSet<Integer>();
//			HashSet<Integer> subject_ids  = new HashSet<Integer>();
//			HashSet<String> ex_keys  = new HashSet<String>();
//			for (ExamResult examResult: examResults){
//				if (examResult.getClass_id() != null && examResult.getClass_id().intValue() > 0){
//					class_ids.add(examResult.getClass_id());
//				}
//				if (examResult.getSubject_id() != null && examResult.getSubject_id().intValue() > 0){
//					subject_ids.add(examResult.getSubject_id());
//				}
//				 // List exam : m1 .. m20
//				 ArrayList<SchoolExam> schoolExams = (ArrayList<SchoolExam>) schoolExamService.findBySchool(school.getId());
//				 for (SchoolExam schoolExam :schoolExams){
//					 String ex_key = schoolExam.getEx_key();
//					 if (ex_key == null || ex_key.equals("")){
//						 continue;
//					 }
//					 if (ex_keys.contains(ex_key)){
//						 continue;
//					 }
//					 
//					 ex_key = ex_key.toLowerCase();
//					 if (examResultService.is_inputted(examResult, ex_key)){
//						 ex_keys.add(ex_key);
//					 }
//				 }
//			}
//			
//			
//			 // * 5. Loop school_id, class_id,subject, ex_key
//			if (class_ids == null || class_ids.size() <= 0){
//				logger.info(" - school_id[" + school.getId() + "], class_ids =  BLANK, ignored");
//				return null;
//			} 
//		
//			if (subject_ids == null || subject_ids.size() <= 0){
//				logger.info(" - school_id[" + school.getId() + "], subject_ids =  BLANK, ignored");
//				return null;
//			}
//			
//			if (ex_keys == null || ex_keys.size() <= 0){
//				logger.info(" - school_id[" + school.getId() + "],  ex_keys = BLANK, ignored");
//				return null;
//			}
//			for (Integer class_id : class_ids){
//				for (Integer subject_id : subject_ids){
//					for (String ex_key: ex_keys){
//						if (examResultService.is_completed(school.getId(), class_id, subject_id, ex_key)){
//							// Create info
//							EClass eclass = classService.findById(class_id);
//							MSubject subject = msubjectDao.findById(subject_id);
//							SchoolExam  schoolExam = schoolExamService.findBySchoolAndKey(school.getId(), ex_key);
//							
//							if (eclass != null &&
//								eclass.getSchool_id().intValue() == school.getId().intValue() && 
//								subject != null &&
//								subject.getSchool_id().intValue() == school.getId().intValue() && 
//								schoolExam != null &&
//								schoolExam.getSchool_id().intValue() == school.getId().intValue() ){
//										FinishExamInfo finishExamInfo = new FinishExamInfo();
//										finishExamInfo.setSchool_id(school.getId());
//										
//										finishExamInfo.setClass_id(class_id);
//										finishExamInfo.setClass_title(eclass.getTitle());
//										
//										
//										finishExamInfo.setSubject_id(subject_id);
//										finishExamInfo.setSubject_name(subject.getSval());
//										
//										finishExamInfo.setEx_id(schoolExam.getId());
//										finishExamInfo.setEx_key(ex_key);
//										finishExamInfo.setEx_name(schoolExam.getEx_displayname());
//										
//										finish_list.add(finishExamInfo);
//							}
//						}
//					}
//				}
//			}
			HashSet<String> done_keys  = new HashSet<String>(); // class_id,subject_id,ex_key
			for (ExamResult examResult: examResults){
				if (examResult.getClass_id() == null ){
					continue;
				}
				String cls_id = ""+examResult.getClass_id().intValue();
				String sub_id=""+examResult.getSubject_id().intValue();
				
				 for (SchoolExam schoolExam :schoolExams){
					 String ex_key = schoolExam.getEx_key().toLowerCase();
					 if (examResultService.is_inputted(examResult, ex_key)){
						 done_keys.add(cls_id+","+sub_id+","+ex_key);  // class_id,subject_id,ex_key
					 }
				 }
				
			}
			for (String done_key:done_keys){ //// class_id,subject_id,ex_key
				String[]tmp_list = done_key.split(",");
				if (tmp_list == null || tmp_list.length< 3){
					continue;
				}
				Integer class_id = Integer.valueOf(tmp_list[0]);
				Integer subject_id = Integer.valueOf(tmp_list[1]);
				String ex_key = tmp_list[2];
				if (examResultService.is_completed(school.getId(), class_id, subject_id, ex_key)){
					// Create info
					EClass eclass = classService.findById(class_id);
					MSubject subject = msubjectDao.findById(subject_id);
					SchoolExam  schoolExam = schoolExamService.findBySchoolAndKey(school.getId(), ex_key);
					
					if (eclass != null && eclass.getSchool_id().intValue() == school.getId().intValue() && 
						subject != null && subject.getSchool_id().intValue() == school.getId().intValue() && 
						schoolExam != null && schoolExam.getSchool_id().intValue() == school.getId().intValue() )
					{
								FinishExamInfo finishExamInfo = new FinishExamInfo();
								finishExamInfo.setSchool_id(school.getId());
								
								finishExamInfo.setClass_id(class_id);
								finishExamInfo.setClass_title(eclass.getTitle());
								
								
								finishExamInfo.setSubject_id(subject_id);
								finishExamInfo.setSubject_name(subject.getSval());
								
								finishExamInfo.setEx_id(schoolExam.getId());
								finishExamInfo.setEx_key(ex_key);
								finishExamInfo.setEx_name(schoolExam.getEx_displayname());
								
								finish_list.add(finishExamInfo);
					}
				}				
			}
			return finish_list;
			
		}



					
		
}
