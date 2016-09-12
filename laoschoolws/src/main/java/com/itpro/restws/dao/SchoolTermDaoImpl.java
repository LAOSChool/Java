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
import com.itpro.restws.model.SchoolTerm;
import com.itpro.restws.model.User;

@Repository("schoolTermDao")
@Transactional
public class SchoolTermDaoImpl extends AbstractDao<Integer, SchoolTerm> implements SchoolTermDao {

	public SchoolTerm findById(Integer id) {
		return getByKey(id.intValue());
	}

	


	@Override
	public void saveSchoolTerm(User me,SchoolTerm schoolTerm) {
			
		
		schoolTerm.setActflg("A");
		schoolTerm.setCtdusr(Constant.USER_SYS);
		schoolTerm.setCtddtm(Utils.now());
		schoolTerm.setCtdpgm(Constant.PGM_REST);
		
		if (me != null ){
			schoolTerm.setCtdusr(me.getSso_id());	
			ApiKey apiKey = me.getApi_key();
			String device = null;
			if (apiKey != null && apiKey.getApi_key() != null ){
				device = apiKey.getApi_key();
			}
			if (device != null ){
				schoolTerm.setCtdwks(device);
			}
			
		}
		
		
		save(schoolTerm);
		
	}


	@Override
	public void updateSchoolTerm(User me,SchoolTerm schoolTerm) {

		schoolTerm.setMdfusr(Constant.USER_SYS);
		schoolTerm.setLstmdf(Utils.now());
		schoolTerm.setMdfpgm(Constant.PGM_REST);
		
		if (me != null ){
			schoolTerm.setMdfusr(me.getSso_id());
			ApiKey apiKey = me.getApi_key();
			String device = null;
			if (apiKey != null && apiKey.getApi_key() != null ){
				device = apiKey.getApi_key();
			}
			if (device != null ){
				schoolTerm.setMdfwks(device);
			}
		}
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
