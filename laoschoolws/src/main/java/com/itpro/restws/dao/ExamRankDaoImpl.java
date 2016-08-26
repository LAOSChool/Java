package com.itpro.restws.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.ExamRank;

@Repository("examRankDao")
@Transactional
public class ExamRankDaoImpl extends AbstractDao<Integer, ExamRank> implements ExamRankDao {

	@Override
	public int countExamBySchool(Integer school_id) {
		// Get row count
		int count = ((Long) getSession()
				.createQuery("select count(*) from ExamRank WHERE actflg ='A' AND school_id= '" + school_id + "'").uniqueResult())
						.intValue();
		return count;

	}

	@Override
	public int countExamBySclass(Integer class_id) {
		// Get row count
		int count = ((Long) getSession()
				.createQuery("select count(*) from ExamRank WHERE actflg ='A' AND class_id= '" + class_id + "'").uniqueResult())
						.intValue();
		return count;
	}

	@Override
	public int countExamByUser(Integer student_user_id) {
		int count = ((Long) getSession()
				.createQuery("select count(*) from ExamRank WHERE actflg ='A' AND student_id= '" + student_user_id + "'")
				.uniqueResult()).intValue();
		return count;
	}

	@Override
	public ExamRank findById(Integer id) {
		return this.getByKey(id.intValue());
	}

	@Override
	public List<ExamRank> findBySchool(Integer school_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.setMaxResults(max_result);
		crit_list.setFirstResult(from_row);
		
		crit_list.addOrder(Order.asc("id"));
		
		@SuppressWarnings("unchecked")
		List<ExamRank> ret = crit_list.list();
		return ret;
	}

	@Override
	public List<ExamRank> findByClass(Integer class_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("class_id", class_id));
		crit_list.setMaxResults(max_result);
		crit_list.setFirstResult(from_row);
		
		crit_list.addOrder(Order.asc("id"));
		@SuppressWarnings("unchecked")
		List<ExamRank> ret = crit_list.list();
		return ret;
	}

	@Override
	public List<ExamRank> findByStudent(Integer user_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("student_id", user_id));
		crit_list.setMaxResults(max_result);
		crit_list.setFirstResult(from_row);
		
		crit_list.addOrder(Order.asc("id"));
		
		@SuppressWarnings("unchecked")
		List<ExamRank> ret = crit_list.list();
		return ret;
	}

	@Override
	public void saveExamRank(ExamRank examRank) {
		examRank.setActflg("A");
		examRank.setCtdusr("HuyNQ-test");
		examRank.setCtddtm(Utils.now());
		examRank.setCtdpgm("RestWS");
		examRank.setCtddtm(Utils.now());
		save(examRank);

	}

	@Override
	public void updateExamRank(ExamRank examRank) {
		examRank.setMdfusr("HuyNQ-test");
		examRank.setLstmdf(Utils.now());
		examRank.setMdfpgm("RestWS");
		update(examRank);

	}
	@Override
	public void deleteExamRank(ExamRank examRank) {
		examRank.setActflg("D");
		update(examRank);

	}

	@Override
	public ArrayList<ExamRank> findExamRankExt(Integer school_id, Integer class_id, Integer student_id, Integer sch_year_id) {
		Criteria crit_list = createEntityCriteria();
		// Filter by school
		crit_list.add(Restrictions.eq("school_id", school_id));

		// Filter by class_id
		if (class_id != null && class_id > 0) {
			crit_list.add(Restrictions.eq("class_id", class_id));
		}

		// Filter by student
		if (student_id != null && student_id > 0) {
			crit_list.add(Restrictions.eq("student_id", student_id));
		}

		

		if (sch_year_id != null) {
			crit_list.add(Restrictions.eq("sch_year_id", sch_year_id));
		}
		
		crit_list.addOrder(Order.asc("id"));
		
		@SuppressWarnings("unchecked")
		ArrayList<ExamRank> list = (ArrayList<ExamRank>) crit_list.list();
		return list;
	}

	@Override
	public int countExamRankExt(Integer school_id, Integer class_id, Integer student_id,Integer sch_year_id) {
		 Criteria crit_list = createEntityCriteria();
		 // Filter by school
		 crit_list.add(Restrictions.eq("school_id", school_id));
		
		 // Filter by class_id
		 if (class_id != null && class_id > 0) {
		 crit_list.add(Restrictions.eq("class_id", class_id));
		 }
		
		 // Filter by student
		 if (student_id != null && student_id > 0) {
		 crit_list.add(Restrictions.eq("student_id", student_id));
		 }
		
	
		 if (sch_year_id != null && sch_year_id > 0) {
			 crit_list.add(Restrictions.eq("sch_year_id", sch_year_id));
		 }
		
		
		
		 crit_list.setProjection(Projections.rowCount());
		 Number numRows = (Number) crit_list.uniqueResult();
		 return numRows == null ? 0 : numRows.intValue();
	}

	
	@Override
	public void clearChange() {
		getSession().clear();
		
	}
	@Override
	public void setFlushMode(FlushMode mode){
		getSession().setFlushMode(mode);
		
	}
}
