package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
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

	
	
}
