package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.model.SysAttReason;
import com.itpro.restws.service.SysTblName;

@Repository("sysAttReasonDao")
@Transactional
public class SysAttReasonDaoImpl extends AbstractDao<Integer, SysAttReason> implements SysAttReasonDao {
	
	@Override
	public List<SysAttReason> findAll() {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("actflg","A"));
	     @SuppressWarnings("unchecked")
		List<SysAttReason> list = crit_list.list();
	     return list;
	}
	

	@Override
	public int countAll() {
		
		// Get row count
		int count = ((Long)getSession().createQuery("select count(*) from " + SysTblName.TBLNAME_SYS_ATT_REASON.getModelName()+  " WHERE actflg = 'A'").uniqueResult()).intValue();
		return count;

	}



	
}
