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
import com.itpro.restws.model.ExamResult;

@Repository("examResultDao")
@Transactional
public class ExamResultDaoImpl extends AbstractDao<Integer, ExamResult> implements ExamResultDao {

	@Override
	public int countExamBySchool(Integer school_id) {
		// Get row count
		int count = ((Long) getSession()
				.createQuery("select count(*) from ExamResult WHERE actflg ='A' AND school_id= '" + school_id + "'").uniqueResult())
						.intValue();
		return count;

	}

	@Override
	public int countExamBySclass(Integer class_id) {
		// Get row count
		int count = ((Long) getSession()
				.createQuery("select count(*) from ExamResult WHERE actflg ='A' AND class_id= '" + class_id + "'").uniqueResult())
						.intValue();
		return count;
	}

	@Override
	public int countExamByUser(Integer student_user_id) {
		int count = ((Long) getSession()
				.createQuery("select count(*) from ExamResult WHERE actflg ='A' AND student_id= '" + student_user_id + "'")
				.uniqueResult()).intValue();
		return count;
	}

	@Override
	public ExamResult findById(Integer id) {
		return this.getByKey(id.intValue());
	}

	@Override
	public List<ExamResult> findBySchool(Integer school_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.setMaxResults(max_result);
		crit_list.setFirstResult(from_row);
		crit_list.addOrder(Order.desc("id"));
		
		
		@SuppressWarnings("unchecked")
		List<ExamResult> ret = crit_list.list();
		return ret;
	}

	@Override
	public List<ExamResult> findByClass(Integer class_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("class_id", class_id));
		crit_list.setMaxResults(max_result);
		crit_list.setFirstResult(from_row);
		crit_list.addOrder(Order.desc("id"));
		
		
		@SuppressWarnings("unchecked")
		List<ExamResult> ret = crit_list.list();
		return ret;
	}

	@Override
	public List<ExamResult> findByStudent(Integer user_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("student_id", user_id));
		crit_list.setMaxResults(max_result);
		crit_list.setFirstResult(from_row);
		crit_list.addOrder(Order.desc("id"));
		
		
		@SuppressWarnings("unchecked")
		List<ExamResult> ret = crit_list.list();
		return ret;
	}

	@Override
	public void saveExamResult(ExamResult examResult) {
		examResult.setActflg("A");
		examResult.setCtdusr("HuyNQ-test");
		examResult.setCtddtm(Utils.now());
		examResult.setCtdpgm("RestWS");
		examResult.setCtddtm(Utils.now());
		
		save(examResult);

	}

	@Override
	public void updateExamResult(ExamResult examResult) {
		examResult.setMdfusr("HuyNQ-test");
		examResult.setLstmdf(Utils.now());
		examResult.setMdfpgm("RestWS");
		update(examResult);

	}

		@Override
	public void deleteExamResult(ExamResult examResult) {
		examResult.setActflg("D");
		update(examResult);

	}

	@Override
	public ArrayList<ExamResult> findExamResultExt(Integer school_id, Integer class_id, Integer student_id, Integer subject_id, Integer sch_year_id) {
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

		// Filter by mon hoc
		if (subject_id != null && subject_id > 0) {
			crit_list.add(Restrictions.eq("subject_id", subject_id));
		}

		if (sch_year_id != null) {
			crit_list.add(Restrictions.eq("sch_year_id", sch_year_id));
		}
		crit_list.addOrder(Order.desc("id"));

		@SuppressWarnings("unchecked")
		ArrayList<ExamResult> list = (ArrayList<ExamResult>) crit_list.list();
		return list;
	}

	@Override
	public int countExamResultExt(Integer school_id, Integer class_id, Integer student_id, Integer subject_id,Integer sch_year_id) {
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
		
		 // Filter by mon hoc
		 if (subject_id != null && subject_id > 0) {
		 crit_list.add(Restrictions.eq("subject_id", subject_id));
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
