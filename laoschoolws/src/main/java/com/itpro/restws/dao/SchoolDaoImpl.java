package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.School;

@Repository("schoolDao")
@Transactional
public class SchoolDaoImpl extends AbstractDao<Integer, School> implements SchoolDao {

	public School findById(Integer id) {
		return getByKey(id.intValue());
	}

	
	@Override
	public List<School> findAll() {
		Criteria crit_list = createEntityCriteria();
		
		Criterion c1 = Restrictions.eq("state", "Active");
	    Criterion c2 = Restrictions.eq("state", "1");
	      
		
		Criterion c3 = Restrictions.or(c1,c2);
		
		crit_list.add(c3);
		
		
	     @SuppressWarnings("unchecked")
		List<School> schools = crit_list.list();
	     return schools;
	}


	@Override
	public void saveSchool(School school) {
		school.setActflg("A");
		school.setCtdusr("HuyNQ-test");
		school.setCtddtm(Utils.now());
		school.setCtdpgm("RestWS");
		school.setCtddtm(Utils.now());
		save(school);
		
	}


	@Override
	public void updateSchool(School school) {
		school.setMdfusr("HuyNQ-test");
		school.setLstmdf(Utils.now());
		school.setMdfpgm("RestWS");
		update(school);
		
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
