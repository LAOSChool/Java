package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.model.SysWeekday;
import com.itpro.restws.service.SysTblName;

@Repository("sysWeekdayDao")
@Transactional
public class SysWeekdayDaoImpl extends AbstractDao<Integer, SysWeekday> implements SysWeekdayDao {

	
	@Override
	public List<SysWeekday> findAll() {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("actflg","A"));
		
		crit_list.addOrder(Order.asc("id"));
		
	     @SuppressWarnings("unchecked")
		List<SysWeekday> list = crit_list.list();
	     return list;
	}
	@Override
	public int countAll() {
		
		// Get row count
		int count = ((Long)getSession().createQuery("select count(*) from " + SysTblName.TBLNAME_SYS_WEEKDAY.getModelName()+  " WHERE actflg = 'A'").uniqueResult()).intValue();
		return count;

	}
	@Override
	public SysWeekday findById(Integer id) {
		return getByKey(id);
	}


	
}
