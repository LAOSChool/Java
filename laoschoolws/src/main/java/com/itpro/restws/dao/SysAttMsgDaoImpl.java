package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.model.SysAttMsg;
import com.itpro.restws.service.SysTblName;

@Repository("sysAttMsgDao")
@Transactional
public class SysAttMsgDaoImpl extends AbstractDao<Integer, SysAttMsg> implements SysAttMsgDao {
	
	@Override
	public List<SysAttMsg> findAll() {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("actflg","A"));
		crit_list.addOrder(Order.asc("id"));
		
	     @SuppressWarnings("unchecked")
		List<SysAttMsg> list = crit_list.list();
	     return list;
	}
	

	@Override
	public int countAll() {
		
		// Get row count
		int count = ((Long)getSession().createQuery("select count(*) from " + SysTblName.TBLNAME_SYS_ATT_MSG.getModelName()+  " WHERE actflg = 'A'").uniqueResult()).intValue();
		return count;

	}


	@Override
	public SysAttMsg findById(Integer id) {
		return getByKey(id);
	}



	
}
