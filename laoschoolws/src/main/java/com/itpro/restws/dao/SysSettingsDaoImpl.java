package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.model.SysSettings;
import com.itpro.restws.service.SysTblName;

@Repository("sysSettingsDao")
@Transactional
public class SysSettingsDaoImpl extends AbstractDao<Integer, SysSettings> implements SysSettingsDao {
	
	@Override
	public List<SysSettings> findAll() {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("actflg","A"));
		crit_list.addOrder(Order.asc("id"));
		
	     @SuppressWarnings("unchecked")
		List<SysSettings> list = crit_list.list();
	     return list;
	}
	

	@Override
	public int countAll() {
		
		// Get row count
		int count = ((Long)getSession().createQuery("select count(*) from " + SysTblName.TBLNAME_SYS_SETTINGS.getModelName()+  " WHERE actflg = 'A'").uniqueResult()).intValue();
		return count;

	}


	@Override
	public SysSettings findById(Integer id) {
		return getByKey(id);
	}



	
}
