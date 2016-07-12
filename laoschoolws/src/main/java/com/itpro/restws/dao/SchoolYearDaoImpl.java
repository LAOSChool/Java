package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.SchoolYear;

@Repository("schoolYearDao")
@Transactional
public class SchoolYearDaoImpl extends AbstractDao<Integer, SchoolYear> implements SchoolYearDao {

	public SchoolYear findById(Integer id) {
		return getByKey(id.intValue());
	}

	


	@Override
	public void saveSchoolYear(SchoolYear schoolYear) {
		schoolYear.setActflg("A");
		schoolYear.setCtdusr("HuyNQ-test");
		schoolYear.setCtddtm(Utils.now());
		schoolYear.setCtdpgm("RestWS");
		schoolYear.setCtddtm(Utils.now());
		save(schoolYear);
		
	}


	@Override
	public void updateSchoolYear(SchoolYear schoolYear) {
		schoolYear.setMdfusr("HuyNQ-test");
		schoolYear.setLstmdf(Utils.now());
		schoolYear.setMdfpgm("RestWS");
		update(schoolYear);
		
	}




	@Override
	public List<SchoolYear> findBySchoolId(Integer school_id) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		@SuppressWarnings("unchecked")
		List<SchoolYear> ret = crit_list.list();
		return ret;
		
	}




//	@Override
//	public SchoolYear findLastestOfSchoolId(Integer school_id) {
//		Criteria crit_list = createEntityCriteria();
//		crit_list.add(Restrictions.eq("school_id", school_id));
//		crit_list.addOrder(Order.desc("id"));
//		@SuppressWarnings("unchecked")
//		List<SchoolYear> ret = crit_list.list();
//		if (ret != null && ret.size() >0){
//			return ret.get(0);
//		}
//		return null;
//	}



	
}
