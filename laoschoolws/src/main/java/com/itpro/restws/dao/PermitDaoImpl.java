package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.Permit;

@Repository("permitDao")
@Transactional
public class PermitDaoImpl extends AbstractDao<Integer, Permit> implements PermitDao {

	@Override
	public List<Permit> findByRole(String role,int school_id) {
		Criteria crit_list = createEntityCriteria();
		
		Criterion rest1= Restrictions.sqlRestriction(" roles LIKE '%"+role.toUpperCase()+"%' ");
		Criterion rest2= Restrictions.eq("roles", "--ALL--");
		Criterion rest3 = Restrictions.or(rest1, rest2);
		
		crit_list.add(rest3);
		crit_list.add(Restrictions.eq("school_id", school_id));
	     @SuppressWarnings("unchecked")
		List<Permit> result = crit_list.list();
	     return result;
	}

	@Override
	public void savePermission(Permit permission) {
		permission.setActflg("A");
		permission.setCtdusr("HuyNQ-test");
		permission.setCtddtm(Utils.now());
		permission.setCtdpgm("RestWS");
		permission.setCtddtm(Utils.now());
		save(permission);
		
	}

	@Override
	public void updatePermission(Permit permission) {
		permission.setMdfusr("HuyNQ-test");
		permission.setLstmdf(Utils.now());
		permission.setMdfpgm("RestWS");
		update(permission);
		
	}

	

	
}
