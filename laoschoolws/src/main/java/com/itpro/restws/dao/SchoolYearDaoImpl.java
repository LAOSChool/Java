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
import com.itpro.restws.model.SchoolYear;
import com.itpro.restws.model.User;

@Repository("schoolYearDao")
@Transactional
public class SchoolYearDaoImpl extends AbstractDao<Integer, SchoolYear> implements SchoolYearDao {

	public SchoolYear findById(Integer id) {
		return getByKey(id.intValue());
	}

	


	@Override
	public void saveSchoolYear(User me,SchoolYear schoolYear) {
		
		schoolYear.setActflg("A");
		schoolYear.setCtdusr(Constant.USER_SYS);
		schoolYear.setCtddtm(Utils.now());
		schoolYear.setCtdpgm(Constant.PGM_REST);
		
		if (me != null ){
			schoolYear.setCtdusr(me.getSso_id());	
			ApiKey apiKey = me.getApi_key();
			String device = null;
			if (apiKey != null && apiKey.getApi_key() != null ){
				device = apiKey.getApi_key();
			}
			if (device != null ){
				schoolYear.setCtdwks(device);
			}
			
		}
		
		save(schoolYear);
		
	}


	@Override
	public void updateSchoolYear(User me,SchoolYear schoolYear) {
		

		schoolYear.setMdfusr(Constant.USER_SYS);
		schoolYear.setLstmdf(Utils.now());
		schoolYear.setMdfpgm(Constant.PGM_REST);
		
		if (me != null ){
			schoolYear.setMdfusr(me.getSso_id());
			ApiKey apiKey = me.getApi_key();
			String device = null;
			if (apiKey != null && apiKey.getApi_key() != null ){
				device = apiKey.getApi_key();
			}
			if (device != null ){
				schoolYear.setMdfwks(device);
			}
		}
		update(schoolYear);
		
	}




	@Override
	public List<SchoolYear> findBySchoolId(Integer school_id) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.addOrder(Order.asc("id"));
		
		@SuppressWarnings("unchecked")
		List<SchoolYear> ret = crit_list.list();
		return ret;
		
	}




	@Override
	public List<SchoolYear> findBySchoolAndYear(Integer school_id, Integer year_id) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.add(Restrictions.eq("id", year_id));
		crit_list.addOrder(Order.asc("id"));
		
		@SuppressWarnings("unchecked")
		List<SchoolYear> ret = crit_list.list();
		return ret;

	}




	@Override
	public List<SchoolYear> findFromOrTo(Integer school_id, Integer frm_year, Integer to_year) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		
		if (frm_year != null && frm_year.intValue() > 0){
			crit_list.add(Restrictions.eq("from_year", frm_year));	
		}
		if (to_year != null && to_year.intValue() > 0){
			crit_list.add(Restrictions.eq("to_year", to_year));	
		}
		crit_list.addOrder(Order.asc("id"));
		
		
		@SuppressWarnings("unchecked")
		List<SchoolYear> ret = crit_list.list();
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
}
