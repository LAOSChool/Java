package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.MClsLevel;

@Repository("mclslevelDao")
@Transactional
public class MClsLevelDaoImpl extends AbstractDao<Integer, MClsLevel> implements MClsLevelDao {
	
	
	final String ModelName = "MClsLevel";
	public MClsLevel findById(Integer id) {
		return getByKey(id.intValue());
	}

	@Override
	public int countBySchool(Integer school_id) {
		// Get row count
		int count = ((Long)getSession().createQuery("select count(*) from " + ModelName+  " WHERE actflg ='A' AND school_id = '" + school_id + "'").uniqueResult()).intValue();
		return count;
	}

	@Override
	public List<MClsLevel> findBySchool(Integer school_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
        crit_list.addOrder(Order.asc("id"));
        
	     @SuppressWarnings("unchecked")
		List<MClsLevel> result = crit_list.list();
	     return result;
	}

	@Override
	public void saveLevel(MClsLevel mClsLevel) {
		mClsLevel.setActflg("A");
		mClsLevel.setCtdusr("HuyNQ-test");
		mClsLevel.setCtddtm(Utils.now());
		mClsLevel.setCtdpgm("RestWS");
		mClsLevel.setCtddtm(Utils.now());
		this.persist(mClsLevel);
		
	}

	@Override
	public void updateLevel(MClsLevel mClsLevel) {
		mClsLevel.setMdfusr("HuyNQ-test");
		mClsLevel.setLstmdf(Utils.now());
		mClsLevel.setMdfpgm("RestWS");
		update(mClsLevel);
		
	}


	@Override
	public void clearChange() {
		getSession().clear();
		
	}
	@Override
	public void setFlushMode(FlushMode mode){
		getSession().setFlushMode(mode);
		
	}	
}
