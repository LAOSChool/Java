package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.Timetable;


@Repository("timetableDao")
@Transactional
public class TimetableDaoImpl extends AbstractDao<Integer, Timetable> implements TimetableDao {

	@Override
	public int countBySchool(Integer school_id) {
		// Get row count
		int count = ((Long)getSession().createQuery("select count(*) from Timetable WHERE school_id= '" + school_id+ "'").uniqueResult()).intValue();
		return count;

	}


	@Override
	public List<Timetable> findBySchool(Integer school_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.setMaxResults(max_result);
		crit_list.setFirstResult(from_row);
		
		crit_list.addOrder(Order.asc("id"));
		
		@SuppressWarnings("unchecked")
		List<Timetable> ret = crit_list.list();
		return ret;
	}

	@Override
	public List<Timetable> findByClass(Integer class_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("class_id", class_id));
		crit_list.setMaxResults(max_result);
		crit_list.setFirstResult(from_row);
		
		crit_list.addOrder(Order.asc("id"));
		
		@SuppressWarnings("unchecked")
		List<Timetable> ret = crit_list.list();
		return ret;
	}


	@Override
	public Timetable findById(Integer id) {
		return this.getByKey(id);
	}


	@Override
	public void saveTimeTable(Timetable timetable) {
		timetable.setActflg("A");
		timetable.setCtdusr("HuyNQ-test");
		timetable.setCtddtm(Utils.now());
		timetable.setCtdpgm("RestWS");
		timetable.setCtddtm(Utils.now());
		save(timetable);
		
	}


	@Override
	public void updateTimetable(Timetable timetable) {
		timetable.setMdfusr("HuyNQ-test");
		timetable.setLstmdf(Utils.now());
		timetable.setMdfpgm("RestWS");
		update(timetable);
		
	}


	@Override
	public List<Timetable> findByWeekDay(Integer class_id, Integer weekday_id) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("class_id", class_id));
		crit_list.add(Restrictions.eq("weekday_id", weekday_id));
		
		crit_list.addOrder(Order.asc("id"));
		
		@SuppressWarnings("unchecked")
		List<Timetable> ret = crit_list.list();
		
		return ret;
		
	}


	@Override
	public void delTimetable(Timetable timetable) {
		delete(timetable);
		
	}

	@Override
	public void clearChange() {
		getSession().clear();
		
	}
	@Override
	public void setFlushMode(FlushMode mode){
		getSession().setFlushMode(mode);
		
	}


	@Override
	public int countTimetableExt(
			Integer school_id, 
	Integer class_id, 
	Integer weekday_id, 
	Integer session_id,
	Integer year_id, 
	Integer term_val) {
		Criteria crit_list = createEntityCriteria();
		// Filter by school
		crit_list.add(Restrictions.eq("school_id", school_id));
		// Limit data return


		// Filter by class_id
		if (class_id != null && class_id > 0) {
			crit_list.add(Restrictions.eq("class_id", class_id));
		}

	
		// Filter by from user
		if (weekday_id != null && weekday_id > 0) {
			crit_list.add(Restrictions.eq("weekday_id", weekday_id));
		}
		
		// Filter by from user
		if (session_id != null && session_id > 0) {
			crit_list.add(Restrictions.eq("session_id", session_id));
		}
		// Filter by from user
		if (year_id != null && year_id > 0) {
			crit_list.add(Restrictions.eq("year_id", year_id));
		}
		if (term_val != null && term_val > 0) {
			crit_list.add(Restrictions.eq("term_val", term_val));
		}

		//crit_list.setProjection(Projections.countDistinct("task_id"));
		crit_list.setProjection(Projections.rowCount());
		Number numRows = (Number) crit_list.uniqueResult();
		return numRows == null ? 0 : numRows.intValue();
	}	
	@Override
	public  List<Timetable> findTimetableExt(
			Integer school_id, 
			Integer class_id, 
			Integer weekday_id, 
			Integer session_id,
			Integer year_id, 
			Integer term_val) {
		Criteria crit_list = createEntityCriteria();
		// Filter by school
		crit_list.add(Restrictions.eq("school_id", school_id));
		// Limit data return


		// Filter by class_id
		if (class_id != null && class_id > 0) {
			crit_list.add(Restrictions.eq("class_id", class_id));
		}

	
		// Filter by from user
		if (weekday_id != null && weekday_id > 0) {
			crit_list.add(Restrictions.eq("weekday_id", weekday_id));
		}
		// Filter by from user
		if (session_id != null && session_id > 0) {
			crit_list.add(Restrictions.eq("session_id", session_id));
		}
		// Filter by from user
		if (year_id != null && year_id > 0) {
			crit_list.add(Restrictions.eq("year_id", year_id));
		}
		if (term_val != null && term_val > 0) {
			crit_list.add(Restrictions.eq("term_val", term_val));
		}
		crit_list.addOrder(Order.asc("id"));
		
		@SuppressWarnings("unchecked")
		List<Timetable> ret = crit_list.list();
		return ret;
	}	
}
