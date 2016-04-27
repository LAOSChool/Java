package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.model.SysRole;
import com.itpro.restws.service.SysTblName;

@Repository("sysRoleDao")
@Transactional
public class SysRoleDaoImpl extends AbstractDao<Integer, SysRole> implements SysRoleDao {

	
	@Override
	public List<SysRole> findAll() {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("actflg","A"));
	     @SuppressWarnings("unchecked")
		List<SysRole> list = crit_list.list();
	     return list;
	}

	@Override
	public int countAll() {
		
		// Get row count
		int count = ((Integer)getSession().createQuery("select count(*) from " + SysTblName.TBLNAME_SYS_ROLE.getModelName()+  " WHERE actflg = 'A'").uniqueResult()).intValue();
		return count;

	}

	
}
