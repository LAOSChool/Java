package com.itpro.restws.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
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
		int count = ((Long)getSession().createQuery("select count(*) from ExamResult WHERE school_id= '" + school_id+ "'").uniqueResult()).intValue();
		return count;

	}

	@Override
	public int countExamBySclass(Integer class_id) {
		// Get row count
		int count = ((Long)getSession().createQuery("select count(*) from ExamResult WHERE class_id= '" + class_id+ "'").uniqueResult()).intValue();
		return count;
	}

	@Override
	public int countExamByUser(Integer student_user_id) {
		int count = ((Long)getSession().createQuery("select count(*) from ExamResult WHERE student_id= '" + student_user_id+ "'").uniqueResult()).intValue();
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

//	@Override
//	public int countExamExt(Integer school_id, Integer class_id, Integer student_id, Integer from_row_id,
//			String exam_dt, String from_dt, String to_dt) {
//		String query = 	"select count(*)  from ExamResult ex where ex.school_id ='"+school_id +"'";
//		if (class_id != null && class_id > 0){
//			query = query +" and ex.class_id = '"+class_id.intValue()+"'"; 
//		}
//				
//				
//		if (student_id != null && student_id > 0){
//			query = query +" and ex.student_id = '"+student_id.intValue()+"'"; 
//		}	
//		
//		if (from_row_id != null && from_row_id> 0){
//			query = query +" ex att.id > '"+from_row_id.intValue()+"'";
//		}
//		
//		
//		if (exam_dt != null ){
//			query = query +" and att.exam_dt >= '"+exam_dt+"'";
//		}
//		
//		if (from_dt != null ){
//			query = query +" and att.exam_dt >= '"+from_dt+"'";
//		}
//		if (to_dt != null ){
//			query = query +" and att.exam_dt <= '"+to_dt+"'";
//		}
//		int count = ((Long)getSession().createQuery(query).uniqueResult()).intValue();
//		return count;
//	}
//
//	@Override
//	public List<ExamResult> findExamExt(Integer school_id, Integer class_id, Integer student_id, Integer from_row_id,
//			int from_num, int max_result,String exam_dt, String from_dt, String to_dt) {
//		
//		
//		String str = 	"from ExamResult ex where ex.school_id ='"+school_id +"'";
//		if (class_id != null && class_id > 0){
//			str = str +" and ex.class_id = '"+class_id.intValue()+"'"; 
//		}
//		
//		if (student_id != null && student_id > 0){
//			str = str +" and ex.student_id = '"+student_id.intValue()+"'"; 
//		}	
//		
//		if (from_row_id != null && from_row_id> 0){
//			str = str +" and ex.id > '"+from_row_id.intValue()+"'";
//		}
//
//		if (exam_dt != null ){
//			str = str +" and att.exam_dt >= '"+exam_dt+"'";
//		}
//		
//		if (from_dt != null ){
//			str = str +" and ex.exam_dt >= '"+from_dt+"'";
//		}
//		if (to_dt != null ){
//			str = str +" and xe.exam_dt <= '"+to_dt+"'";
//		}
//		Query query =  getSession().createQuery(str);
//		query.setMaxResults(max_result);
//		query.setFirstResult(from_num);
//		
//		
//		
//		@SuppressWarnings("unchecked")
//		List<ExamResult>  ret= query.list();
//		return ret;
//	}

//	@Override
//	public List<ExamResult> findByMonth(Integer user_id,int year, int month) {
//			//String str = 	"from ExamResult ex where ex.student_id=:student_id and year(ex.exam_dt) =:year and month(ex.exam_dt)=:month";
//		String str = 	"from ExamResult ex where ex.student_id=:student_id and ex.exam_year =:year and ex.exam_month=:month";
//			Query query =  getSession().createQuery(str);
//			
//			query.setParameter("student_id", user_id);
//			query.setParameter("year", year);
//			query.setParameter("month", month);
//			
//			@SuppressWarnings("unchecked")
//			List<ExamResult>  ret= query.list();
//			return ret;
//
//	}
	@Override
	public List<ExamResult> findSameExam(Integer school_id,Integer student_id,Integer year, Integer month, Integer subject_id,Integer exam_type_id)
	{	
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.add(Restrictions.eq("student_id", student_id));
		crit_list.add(Restrictions.eq("exam_year", year));
		crit_list.add(Restrictions.eq("exam_month", month));
		crit_list.add(Restrictions.eq("subject_id", subject_id));
		crit_list.add(Restrictions.eq("exam_type", exam_type_id));
		crit_list.addOrder(Order.desc("id"));
		@SuppressWarnings("unchecked")
		List<ExamResult> list = crit_list.list();
		return list;

	}

	@Override
	public int countExamResultExt(Integer school_id, Integer class_id, Integer user_id, Integer subject_id,
			Integer term_id, Integer exam_year, Integer exam_month, String exam_dt, String dateFrom, String dateTo,
			Integer from_row_id) {
		
		Criteria crit_list = createEntityCriteria();
		// Filter by school
		crit_list.add(Restrictions.eq("school_id", school_id));
		
		// Filter by class_id
		if (class_id != null && class_id > 0) {
			crit_list.add(Restrictions.eq("class_id", class_id));
		}
		
		// Filter by student
		if (user_id != null && user_id > 0) {
			crit_list.add(Restrictions.eq("student_id", class_id));
		}
		
		// Filter by mon hoc
		if (subject_id != null && subject_id > 0) {
			crit_list.add(Restrictions.eq("subject_id", class_id));
		}
		
		if (term_id != null && term_id > 0) {
			crit_list.add(Restrictions.eq("term_id", class_id));
		}
		
		if (exam_year != null && exam_year > 0) {
			crit_list.add(Restrictions.eq("exam_year", exam_year));
		}
		if (exam_month != null && exam_month > 0) {
			crit_list.add(Restrictions.eq("exam_month", exam_month));
		}
		if (exam_dt != null) {
			crit_list.add(Restrictions.eq("exam_dt", exam_dt));
		}
		

		// Filter by date
		if (dateFrom != null && dateTo != null) {
			crit_list.add(Restrictions.between("exam_dt", dateFrom, dateTo));
		} else if (dateFrom != null) {
			crit_list.add(Restrictions.gt("exam_dt", dateFrom));
		} else if (dateTo != null) {
			crit_list.add(Restrictions.lt("exam_dt", dateTo));
		}

		
		
		// 
		if (from_row_id != null && from_row_id >= 0) {
			crit_list.add(Restrictions.gt("id", from_row_id));
		}
	
				
		crit_list.setProjection(Projections.rowCount());
		Number numRows = (Number) crit_list.uniqueResult();
		return numRows == null ? 0 : numRows.intValue();
	
	}

	@Override
	public ArrayList<ExamResult> findExamResultExt(Integer school_id, int from_row, int max_result, Integer class_id,
			Integer user_id, Integer subject_id, Integer term_id, Integer exam_year, Integer exam_month,
			String exam_dt, String dateFrom, String dateTo, Integer from_row_id) {
		Criteria crit_list = createEntityCriteria();
		// Filter by school
		crit_list.add(Restrictions.eq("school_id", school_id));
		
		// Filter by class_id
		if (class_id != null && class_id > 0) {
			crit_list.add(Restrictions.eq("class_id", class_id));
		}
		
		// Filter by student
		if (user_id != null && user_id > 0) {
			crit_list.add(Restrictions.eq("student_id", class_id));
		}
		
		// Filter by mon hoc
		if (subject_id != null && subject_id > 0) {
			crit_list.add(Restrictions.eq("subject_id", class_id));
		}
		
		if (term_id != null && term_id > 0) {
			crit_list.add(Restrictions.eq("term_id", class_id));
		}
		
		if (exam_year != null && exam_year > 0) {
			crit_list.add(Restrictions.eq("exam_year", exam_year));
		}
		if (exam_month != null && exam_month > 0) {
			crit_list.add(Restrictions.eq("exam_month", exam_month));
		}
		if (exam_dt != null) {
			crit_list.add(Restrictions.eq("exam_dt", exam_dt));
		}
		

		// Filter by date
		if (dateFrom != null && dateTo != null) {
			crit_list.add(Restrictions.between("exam_dt", dateFrom, dateTo));
		} else if (dateFrom != null) {
			crit_list.add(Restrictions.gt("exam_dt", dateFrom));
		} else if (dateTo != null) {
			crit_list.add(Restrictions.lt("exam_dt", dateTo));
		}

		
		
		// 
		if (from_row_id != null && from_row_id >= 0) {
			crit_list.add(Restrictions.gt("id", from_row_id));
		}
		@SuppressWarnings("unchecked")
		ArrayList<ExamResult> list = (ArrayList<ExamResult>) crit_list.list();
		return list;
	}

	@Override
	public void deleteExamResult(ExamResult examResult) {
		examResult.setActflg("D");
		update(examResult);
		
	}
	



	
	
}
