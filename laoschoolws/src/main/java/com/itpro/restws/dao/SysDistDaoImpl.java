package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.model.SysDist;
import com.itpro.restws.service.SysTblName;

@Repository("sysDistDao")
@Transactional
public class SysDistDaoImpl extends AbstractDao<Integer, SysDist> implements SysDistDao {
	
	@Override
	public List<SysDist> findAll() {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("actflg","A"));
		crit_list.addOrder(Order.asc("id"));
		
	     @SuppressWarnings("unchecked")
		List<SysDist> list = crit_list.list();
	     return list;
	}
	

	@Override
	public int countAll() {
		
		// Get row count
		int count = ((Long)getSession().createQuery("select count(*) from " + SysTblName.TBLNAME_SYS_DEGREE.getModelName()+  " WHERE actflg = 'A'").uniqueResult()).intValue();
		return count;

	}


	@Override
	public SysDist findById(Integer id) {
		return getByKey(id);
	}



	
}
