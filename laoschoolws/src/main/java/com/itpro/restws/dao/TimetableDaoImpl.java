package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
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
		@SuppressWarnings("unchecked")
		List<Timetable> ret = crit_list.list();
		
		return ret;
		
	}

	
}
