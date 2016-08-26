package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.model.SysMsgSamp;
import com.itpro.restws.service.SysTblName;

@Repository("sysMsgSampDao")
@Transactional
public class SysMsgSampDaoImpl extends AbstractDao<Integer, SysMsgSamp> implements SysMsgSampDao {

	
	@Override
	public List<SysMsgSamp> findAll() {
		Criteria crit_list = createEntityCriteria();
		// crit_list.add(Restrictions.eq("actflg","A"));
	     @SuppressWarnings("unchecked")
		List<SysMsgSamp> list = crit_list.list();
	     return list;
	}

	@Override
	public int countAll() {
		
		// Get row count
		int count = ((Long)getSession().createQuery("select count(*) from " + SysTblName.TBLNAME_SYS_MSG_SAMP.getModelName()+  " WHERE actflg = 'A'").uniqueResult()).intValue();
		return count;

	}

	@Override
	public List<SysMsgSamp> findByNotice(String notice) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("actflg","A"));
		crit_list.add(Restrictions.eq("notice",notice));
	     @SuppressWarnings("unchecked")
		List<SysMsgSamp> list = crit_list.list();
	     return list;
	}

	@Override
	public SysMsgSamp findById(Integer id) {
		return getByKey(id);
	}

	
}
