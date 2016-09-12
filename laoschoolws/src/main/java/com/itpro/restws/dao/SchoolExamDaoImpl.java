package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Constant;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.ApiKey;
import com.itpro.restws.model.SchoolExam;
import com.itpro.restws.model.User;


@Repository("schoolExamDao")
@Transactional
public class SchoolExamDaoImpl extends AbstractDao<Integer, SchoolExam> implements SchoolExamDao {

	@Override
	public SchoolExam findById(Integer id) {
		return getByKey(id.intValue());
	}


	@Override
	public List<SchoolExam> findBySchool(Integer school_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
        
        crit_list.addOrder(Order.asc("id"));
        
	     @SuppressWarnings("unchecked")
		List<SchoolExam> list = crit_list.list();
	     
		return  list;
	}

	

	@Override
	public void saveSchoolExam(User me,SchoolExam schoolExam) {
		
		
		schoolExam.setActflg("A");
		schoolExam.setCtdusr(Constant.USER_SYS);
		schoolExam.setCtddtm(Utils.now());
		schoolExam.setCtdpgm(Constant.PGM_REST);
		
		if (me != null ){
			schoolExam.setCtdusr(me.getSso_id());	
			ApiKey apiKey = me.getApi_key();
			String device = null;
			if (apiKey != null && apiKey.getApi_key() != null ){
				device = apiKey.getApi_key();
			}
			if (device != null ){
				schoolExam.setCtdwks(device);
			}
			
		}
		
		
		
		save(schoolExam);

		
	}

	@Override
	public void updateSchoolExam(User me,SchoolExam schoolExam) {

		schoolExam.setMdfusr(Constant.USER_SYS);
		schoolExam.setLstmdf(Utils.now());
		schoolExam.setMdfpgm(Constant.PGM_REST);
		
		if (me != null ){
			schoolExam.setMdfusr(me.getSso_id());
			ApiKey apiKey = me.getApi_key();
			String device = null;
			if (apiKey != null && apiKey.getApi_key() != null ){
				device = apiKey.getApi_key();
			}
			if (device != null ){
				schoolExam.setMdfwks(device);
			}
		}
		update(schoolExam);

		
	}


	@Override
	public int countBySchool(Integer school_id) {
		int count = ((Long)getSession().createQuery("select count(*) from ExamMonth WHERE actflg ='A' AND school_id= '" + school_id+ "'").uniqueResult()).intValue();
		return count;
		
	}


	@Override
	public List<SchoolExam> findByMonth(Integer school_id, Integer ex_year, Integer ex_month) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		if (ex_month != null){
			crit_list.add(Restrictions.eq("ex_month", ex_month));
		}
		if (ex_year != null) {
			crit_list.add(Restrictions.eq("ex_year", ex_year));
		}
		
		crit_list.addOrder(Order.asc("id"));
		
	     @SuppressWarnings("unchecked")
		List<SchoolExam> list = crit_list.list();
	     
		return  list;
	}


	@Override
	public List<SchoolExam> findByExamType(Integer school_id, Integer ex_type, Integer term_val) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));

		if (ex_type != null && ex_type.intValue() > 0){
			crit_list.add(Restrictions.eq("ex_type", ex_type));
		}
		if (term_val != null && term_val.intValue() > 0) {
			crit_list.add(Restrictions.eq("term_val", term_val));
		}
		
		crit_list.addOrder(Order.asc("id"));
		
	     @SuppressWarnings("unchecked")
		List<SchoolExam> list = crit_list.list();
	     
		return  list;
	}


	@Override
	public SchoolExam findByExKey(Integer school_id, String ex_key) {
		
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.add(Restrictions.eq("ex_key", ex_key));

		crit_list.addOrder(Order.asc("id"));
	     @SuppressWarnings("unchecked")
		List<SchoolExam> list = crit_list.list();
	    if (list !=null && list.size() > 0){
	    	return list.get(0);
	    }
		return  null;
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
