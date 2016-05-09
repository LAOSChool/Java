package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
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

	@Override
	public int countExamExt(Integer school_id, Integer class_id, Integer student_id, Integer from_row_id,
			String from_dt, String to_dt) {
		String query = 	"select count(*)  from ExamResult ex where ex.school_id ='"+school_id +"'";
		if (class_id != null && class_id > 0){
			query = query +" and ex.class_id = '"+class_id.intValue()+"'"; 
		}
				
				
		if (student_id != null && student_id > 0){
			query = query +" and ex.student_id = '"+student_id.intValue()+"'"; 
		}	
		
		if (from_row_id != null && from_row_id> 0){
			query = query +" ex att.id > '"+from_row_id.intValue()+"'";
		}
		
		
		if (from_dt != null ){
			query = query +" and att.exam_dt >= '"+from_dt+"'";
		}
		if (to_dt != null ){
			query = query +" and att.exam_dt <= '"+to_dt+"'";
		}
		int count = ((Long)getSession().createQuery(query).uniqueResult()).intValue();
		return count;
	}

	@Override
	public List<ExamResult> findExamExt(Integer school_id, Integer class_id, Integer student_id, Integer from_row_id,
			int from_num, int max_result, String from_dt, String to_dt) {
		
		
		String str = 	"from ExamResult ex where ex.school_id ='"+school_id +"'";
		if (class_id != null && class_id > 0){
			str = str +" and ex.class_id = '"+class_id.intValue()+"'"; 
		}
		
		if (student_id != null && student_id > 0){
			str = str +" and ex.student_id = '"+student_id.intValue()+"'"; 
		}	
		
		if (from_row_id != null && from_row_id> 0){
			str = str +" and ex.id > '"+from_row_id.intValue()+"'";
		}
		
		if (from_dt != null ){
			str = str +" and ex.exam_dt >= '"+from_dt+"'";
		}
		if (to_dt != null ){
			str = str +" and xe.exam_dt <= '"+to_dt+"'";
		}
		Query query =  getSession().createQuery(str);
		query.setMaxResults(max_result);
		query.setFirstResult(from_num);
		
		
		
		@SuppressWarnings("unchecked")
		List<ExamResult>  ret= query.list();
		return ret;
	}

	



	
	
}
