package com.itpro.restws.controller;

import java.util.ArrayList;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.itpro.restws.dao.EduProfileDao;
import com.itpro.restws.dao.SchoolYearDao;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.E_ROLE;
import com.itpro.restws.helper.RespInfo;
import com.itpro.restws.model.EClass;
import com.itpro.restws.model.EduProfile;
import com.itpro.restws.model.ExamResult;
import com.itpro.restws.model.SchoolYear;
import com.itpro.restws.model.User;
import com.itpro.restws.service.EduProfileService;
import com.itpro.restws.service.ExamResultService;
import com.itpro.restws.service.SchoolYearService;
/**
 * Controller with REST API. Access to login is generally permitted, stuff in
 * /secure/ sub-context is protected by configuration. Some security annotations are
 * thrown in just to make a point.
 * Notice:
 * Consume should be the type of data that the web service expects to receive
 * Produces should be the type of data that the web service will return
 */
//Here @RestController is shorthand of = @Controller + @ResponseBody
// Where every method returns a domain object instead of a view
@RestController 
public class EduProfileController extends BaseController {
	
	
	
	@Autowired
	protected ExamResultService examResultService;
	
	@Autowired
	protected EduProfileDao eduProfileDao;
	
	@Autowired
	protected EduProfileService eduProfileService;
	
	@Autowired
	protected SchoolYearService schoolYearService;
	
	// Hien thi ca du lieu Ao va co trong DB
	@Secured({ "ROLE_STUDENT"})
	@RequestMapping(value="/api/edu_profiles/myprofile",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)
	public RespInfo getMyProfile(
			@RequestParam(value="filter_year_id",required =false) Integer filter_year_id,
			
			 @Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			
			) {
		//	 Get User info 
			User student = getCurrentUser();
			SchoolYear schoolYear = null;
			// Neu ko co filter year, tra ve diem DB merger voi diem Ao
			if (filter_year_id == null){
				schoolYear = schoolYearService.findLatestYearByStudent(student.getId());
				
				ArrayList<ExamResult> exam_results = new ArrayList<ExamResult>();
				EduProfile max_profile = eduProfileDao.findLatestProfile(student.getId(), student.getSchool_id());
				if (max_profile != null){
					Set<EClass> classes = student.getClasses();
					for (EClass eclass: classes){
						ArrayList<ExamResult> sub_results_list = examResultService.getUserResult_Mark(student, eclass.getId(), null, true);
						if (sub_results_list != null && sub_results_list.size() > 0){
							exam_results.addAll(sub_results_list);
						}
					}
					// Cal ave
					//examResultService.calAverage(exam_results, student, schoolYear);
					max_profile.setExam_results(exam_results);
					
					
					
				}
				RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
				rsp.setMessageObject(max_profile);
			    return rsp;
			}else{
				schoolYear = schoolYearService.findById(filter_year_id);
				
				// Neu co filter year, tra ve diem DB only
				ArrayList<ExamResult> exam_results = new ArrayList<ExamResult>();
				EduProfile max_profile = null;
				
				// Get school year
				ArrayList<EduProfile> profiles = eduProfileDao.findEx(student.getId(), student.getSchool_id(), null, filter_year_id);
				if (profiles != null && profiles.size() > 0){
					max_profile = profiles.get(0);
					for (EduProfile profile : profiles){
						if (max_profile.getId().intValue() < profile.getId().intValue()){
							max_profile = profile;
						}
					}
					// Get exam data
					exam_results = examResultService.findExamResultExt(student.getSchool_id(),0,999999, null, student.getId(), null, null, null, null, null, null, null, null, null, filter_year_id);
//					// Calculate average
//					examResultService.calAverage(exam_results, student, schoolYear);
					// Return profile
					max_profile.setExam_results(exam_results);
				}
				
				RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
				rsp.setMessageObject(max_profile);
			    return rsp;
			}
				
			
		}

	// Get edu profiles ( chi lay DB de hien thi)
	@Secured({ "ROLE_ADMIN","ROLE_TEACHER"})
	@RequestMapping(value="/api/edu_profiles",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)
	public RespInfo getEduProfile(
			@RequestParam(value="filter_user_id",required =false) Integer filter_student_id,
			@RequestParam(value="filter_class_id",required =false) Integer filter_class_id,
			
			
			 @Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			
			) {
		
			if ( filter_class_id==null){
				throw new ESchoolException("filter_class_id is required ", HttpStatus.BAD_REQUEST);
			}
			User user = getCurrentUser();
			
			ArrayList<ExamResult> exam_results = null;
			ArrayList<EduProfile> profiles = new ArrayList<EduProfile>();
			
			// Get Class
			com.itpro.restws.model.EClass eclass = classService.findById(filter_class_id);
			if (eclass == null){
				throw new ESchoolException(" filter_class_id:"+filter_class_id.intValue()+" is  not exsiting", HttpStatus.BAD_REQUEST);
			}
			// Get SchoolYear
			SchoolYear schoolYear = schoolYearService.findById(eclass.getYear_id());
			// Get exam data
			profiles = eduProfileDao.findEx(filter_student_id, user.getSchool_id(), filter_class_id, eclass.getYear_id());
			for (EduProfile profile : profiles){
				exam_results = examResultService.findExamResultExt(user.getSchool_id(),0,999999, filter_class_id, filter_student_id, null, null, null, null, null, null, null, null, null, schoolYear.getId());
				// Calculate average
//				examResultService.calAverage(exam_results, null, schoolYear);
				
				profile.setExam_results(exam_results);
			}
			
					
			RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
			rsp.setMessageObject(profiles);
		    return rsp;
		}
	
	@Secured({"ROLE_ADMIN","ROLE_TEACHER"})
	@RequestMapping(value="/api/edu_profile/school_years",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)
	public RespInfo getSchoolYears(
			@RequestParam(value="filter_user_id",required =true) Integer filter_user_id,
			
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			
			) {
			
			User current_user = getCurrentUser();
			
			if (current_user.getId().intValue() != filter_user_id.intValue()){
				if (current_user.hasRole(E_ROLE.STUDENT.getRole_short())){
					throw new ESchoolException(" user:"+current_user.getId().intValue()+" is a STUDENT, cannot access to this data of other user_id: "+filter_user_id.intValue(), HttpStatus.BAD_REQUEST);
				}
			}
			User filter_user= userService.findById(filter_user_id);
			if (filter_user.getSchool_id().intValue() != current_user.getSchool_id().intValue()){
				throw new ESchoolException(" Current user school_id:"+current_user.getSchool_id().intValue()+" is differ from filter_user: "+filter_user.getSchool_id().intValue(), HttpStatus.BAD_REQUEST);
			}
			
		
			ArrayList<SchoolYear> years = eduProfileService.findSchoolYearByStudentID(filter_user_id);
				
			RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
			
			rsp.setMessageObject(years);
		    return rsp;
		}
	
}
