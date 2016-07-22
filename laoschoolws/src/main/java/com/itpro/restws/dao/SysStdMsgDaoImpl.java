package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.model.SysStdMsg;
import com.itpro.restws.service.SysTblName;

@Repository("sysStdMsgDao")
@Transactional
public class SysStdMsgDaoImpl extends AbstractDao<Integer, SysStdMsg> implements SysStdMsgDao {
	
	@Override
	public List<SysStdMsg> findAll() {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("actflg","A"));
	     @SuppressWarnings("unchecked")
		List<SysStdMsg> list = crit_list.list();
	     return list;
	}
	

	@Override
	public int countAll() {
		
		// Get row count
		int count = ((Long)getSession().createQuery("select count(*) from " + SysTblName.TBLNAME_SYS_STD_MSG.getModelName()+  " WHERE actflg = 'A'").uniqueResult()).intValue();
		return count;

	}




	
}
