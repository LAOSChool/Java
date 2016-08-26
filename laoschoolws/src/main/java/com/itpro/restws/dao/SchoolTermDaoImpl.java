package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.SchoolTerm;

@Repository("schoolTermDao")
@Transactional
public class SchoolTermDaoImpl extends AbstractDao<Integer, SchoolTerm> implements SchoolTermDao {

	public SchoolTerm findById(Integer id) {
		return getByKey(id.intValue());
	}

	


	@Override
	public void saveSchoolTerm(SchoolTerm schoolTerm) {
		schoolTerm.setActflg("A");
		schoolTerm.setCtdusr("HuyNQ-test");
		schoolTerm.setCtddtm(Utils.now());
		schoolTerm.setCtdpgm("RestWS");
		schoolTerm.setCtddtm(Utils.now());
		save(schoolTerm);
		
	}


	@Override
	public void updateSchoolTerm(SchoolTerm schoolTerm) {
		schoolTerm.setMdfusr("HuyNQ-test");
		schoolTerm.setLstmdf(Utils.now());
		schoolTerm.setMdfpgm("RestWS");
		update(schoolTerm);
		
	}




	@Override
	public List<SchoolTerm> findBySchoolId(Integer school_id, Integer actived) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		if (actived != null && actived.intValue() >= 0 ){
			crit_list.add(Restrictions.eq("actived", actived));
		}
		
		crit_list.addOrder(Order.asc("id"));
		
		@SuppressWarnings("unchecked")
		List<SchoolTerm> ret = crit_list.list();
		return ret;
		
	}




	@Override
	public List<SchoolTerm> findBySchoolAndYear(Integer school_id, Integer year_id, Integer actived) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		
		if (year_id != null && year_id.intValue() > 0 ){
			crit_list.add(Restrictions.eq("year_id", year_id));
		}
		
		if (actived != null  && actived.intValue() >= 0){
			crit_list.add(Restrictions.eq("actived", actived));
		}
		
		crit_list.addOrder(Order.asc("id"));
		@SuppressWarnings("unchecked")
		List<SchoolTerm> ret = crit_list.list();
		return ret;

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
	public int countTermsBySchool(Integer school_id, Integer actived){
		// Get row count
		int count =  0;
		if (actived == null){
			count = ((Long)getSession().createQuery("select count(*) from SchoolTerm WHERE actflg = 'A' AND school_id= '" + school_id.intValue()+ "'").uniqueResult()).intValue();
		}else{
			count = ((Long)getSession().createQuery("select count(*) from SchoolTerm WHERE actflg = 'A' AND school_id= '" + school_id.intValue()+ "' AND actived= '" + actived.intValue()+"'").uniqueResult()).intValue();
		}
		return count;
	}	
	
}
