package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.model.SysBase;
import com.itpro.restws.model.SysSts;
import com.itpro.restws.service.SysTblName;

@Repository("sysStsgDao")
@Transactional
public class SysStsDaoImpl extends AbstractDao<Integer, SysSts> implements SysStsDao {
	
	@Override
	public List<SysSts> findAll() {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("actflg","A"));
	     @SuppressWarnings("unchecked")
		List<SysSts> list = crit_list.list();
	     return list;
	}
	

	@Override
	public int countAll() {
		
		// Get row count
		int count = ((Long)getSession().createQuery("select count(*) from " + SysTblName.TBLNAME_SYS_STS.getModelName()+  " WHERE actflg = 'A'").uniqueResult()).intValue();
		return count;

	}


	@Override
	public List<SysSts>  findByFval(Integer fval) {
		if (fval == null ){
			return null;
		}

		
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("fval1",fval.floatValue()));
	     @SuppressWarnings("unchecked")
		List<SysSts> list = crit_list.list();
	     return list;
	     
	}



	
}
