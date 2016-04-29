package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.model.SysDegree;
import com.itpro.restws.service.SysTblName;

@Repository("sysDegreeDao")
@Transactional
public class SysDegreeDaoImpl extends AbstractDao<Integer, SysDegree> implements SysDegreeDao {
	
	@Override
	public List<SysDegree> findAll() {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("actflg","A"));
	     @SuppressWarnings("unchecked")
		List<SysDegree> list = crit_list.list();
	     return list;
	}
	

	@Override
	public int countAll() {
		
		// Get row count
		int count = ((Long)getSession().createQuery("select count(*) from " + SysTblName.TBLNAME_SYS_DEGREE.getModelName()+  " WHERE actflg = 'A'").uniqueResult()).intValue();
		return count;

	}



	
}
