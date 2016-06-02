package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.Attendance;


@Repository("attendanceDao")
@Transactional
public class AttendanceDaoImpl extends AbstractDao<Integer, Attendance> implements AttendanceDao {

	@Override
	public int countAttendanceBySchool(Integer school_id) {
		// Get row count
		int count = ((Long)getSession().createQuery("select count(*) from Attendance WHERE school_id= '" + school_id+ "'").uniqueResult()).intValue();
		return count;
	}

	@Override
	public int countAttendanceByClass(Integer class_id) {
		// Get row count
		int count = ((Long)getSession().createQuery("select count(*) from Attendance WHERE class_id= '" + class_id+ "'").uniqueResult()).intValue();
		return count;
	}

	@Override
	public Attendance findById(Integer id) {
		return getByKey(id.intValue());
	}

	@Override
	public List<Attendance> findBySchool(Integer school_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
	     @SuppressWarnings("unchecked")
		List<Attendance> attendances = crit_list.list();
	     return attendances;
	}

	@Override
	public List<Attendance> findByStudent(Integer student_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("student_id", student_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
	     @SuppressWarnings("unchecked")
		List<Attendance> attendances = crit_list.list();
	     return attendances;
	}

	@Override
	public int countAttendanceByStudent(Integer student_id) {
		// Get row count
				int count = ((Long)getSession().createQuery("select count(*) from Attendance WHERE student_id= '" + student_id+ "'").uniqueResult()).intValue();
				return count;
	}

	@Override
	public List<Attendance> findByClass(Integer class_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("class_id", class_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
	     @SuppressWarnings("unchecked")
		List<Attendance> attendances = crit_list.list();
	     return attendances;
	}

	@Override
	public void saveAttendance(Attendance attendance) {
		attendance.setActflg("A");
		attendance.setCtdusr("HuyNQ-test");
		attendance.setCtddtm(Utils.now());
		attendance.setCtdpgm("RestWS");
		attendance.setCtddtm(Utils.now());
		save(attendance);
		
	}

	@Override
	public void updateAttendance(Attendance attendance) {
//		Attendance current_att = getByKey(attendance.getId());
		
		attendance.setMdfusr("HuyNQ-test");
		attendance.setLstmdf(Utils.now());
		attendance.setMdfpgm("RestWS");
//		update(attendance);
		merge(attendance);
		
	}
	
	@Override
	public int countAttendanceExt(Integer school_id, Integer class_id, Integer student_id, Integer from_row_id,String att_dt,String from_dt, String to_dt, Integer session_id) {
		String query = 	"select count(*)  from Attendance att where att.school_id ='"+school_id +"'";
		if (class_id != null && class_id > 0){
			query = query +" and att.class_id = '"+class_id.intValue()+"'"; 
		}
				
				
		if (student_id != null && student_id > 0){
			query = query +" and att.student_id = '"+student_id.intValue()+"'"; 
		}	
		
		if (from_row_id != null && from_row_id> 0){
			query = query +" and att.id > '"+from_row_id.intValue()+"'";
		}
		if (att_dt != null ){
			query = query +" and att.att_dt = '"+att_dt+"'";
		}
		
		if (from_dt != null ){
			query = query +" and att.att_dt >= '"+from_dt+"'";
		}
		if (to_dt != null ){
			query = query +" and att.att_dt <= '"+to_dt+"'";
		}
		if (session_id != null ){
			query = query +" and att.session_id = '"+session_id.intValue()+"'";
		}
		int count = ((Long)getSession().createQuery(query).uniqueResult()).intValue();
		return count;
	}

	@Override
	public List<Attendance> findAttendanceExt(Integer school_id, Integer class_id, Integer student_id, Integer from_row_id,
			int from_num, int max_result,String att_dt,String from_dt, String to_dt,Integer session_id) {
		String str = 	"from Attendance att where att.school_id ='"+school_id +"'";
		if (class_id != null && class_id > 0){
			str = str +" and att.class_id = '"+class_id.intValue()+"'"; 
		}
		
		if (student_id != null && student_id > 0){
			str = str +" and att.student_id = '"+student_id.intValue()+"'"; 
		}	
		
		if (att_dt != null ){
			str = str +" and att.att_dt = '"+att_dt+"'";
		}
		if (from_row_id != null && from_row_id> 0){
			str = str +" and att.id > '"+from_row_id.intValue()+"'";
		}
		
		if (from_dt != null ){
			str = str +" and att.att_dt >= '"+from_dt+"'";
		}
		if (to_dt != null ){
			str = str +" and att.att_dt <= '"+to_dt+"'";
		}
		
		if (session_id != null ){
			str = str +" and att.session_id = '"+session_id.intValue()+"'";
		}
		
		Query query =  getSession().createQuery(str);
		query.setMaxResults(max_result);
		query.setFirstResult(from_num);
		
		
		
		@SuppressWarnings("unchecked")
		List<Attendance>  ret= query.list();
		return ret;
	}

	@Override
	public List<Attendance> findByAuditor(Integer auditor, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("auditor", auditor));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
	     @SuppressWarnings("unchecked")
		List<Attendance> attendances = crit_list.list();
	     return attendances;
	}

	
	
}
