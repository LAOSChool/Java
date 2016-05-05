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
	public List<Attendance> findByUser(Integer user_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("user_id", user_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
	     @SuppressWarnings("unchecked")
		List<Attendance> attendances = crit_list.list();
	     return attendances;
	}

	@Override
	public int countAttendanceByUser(Integer user_id) {
		// Get row count
				int count = ((Long)getSession().createQuery("select count(*) from Attendance WHERE user_id= '" + user_id+ "'").uniqueResult()).intValue();
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
		attendance.setMdfusr("HuyNQ-test");
		attendance.setLstmdf(Utils.now());
		attendance.setMdfpgm("RestWS");
		update(attendance);
		
	}

	@Override
	public int countAttendanceExt(Integer school_id, Integer class_id, Integer user_id, Integer from_row_id) {
		String query = 	"select count(*)  from Attendance att where att.school_id ='"+school_id +"'";
		if (class_id != null && class_id > 0){
			query = query +" and att.class_id = '"+class_id.intValue()+"'"; 
		}
				
				
		if (user_id != null && user_id > 0){
			query = query +" and att.user_id = '"+user_id.intValue()+"'"; 
		}	
		
		if (from_row_id != null && from_row_id> 0){
			query = query +" and att.id > '"+from_row_id.intValue()+"'";
		}
		int count = ((Long)getSession().createQuery(query).uniqueResult()).intValue();
		return count;
	}

	@Override
	public List<Attendance> findAttendanceExt(Integer school_id, Integer class_id, Integer user_id, Integer from_row_id,
			int from_num, int max_result) {
		String str = 	"from Attendance att where att.school_id ='"+school_id +"'";
		if (class_id != null && class_id > 0){
			str = str +" and att.class_id = '"+class_id.intValue()+"'"; 
		}
		
		if (user_id != null && user_id > 0){
			str = str +" and att.user_id = '"+user_id.intValue()+"'"; 
		}	
		
		if (from_row_id != null && from_row_id> 0){
			str = str +" and att.id > '"+from_row_id.intValue()+"'";
		}
		
		Query query =  getSession().createQuery(str);
		query.setMaxResults(max_result);
		query.setFirstResult(from_num);
		
		
		
		@SuppressWarnings("unchecked")
		List<Attendance>  ret= query.list();
		return ret;
	}

	
	
}
