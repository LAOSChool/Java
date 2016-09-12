package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Constant;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.ApiKey;
import com.itpro.restws.model.MGrade;
import com.itpro.restws.model.User;

@Repository("mgradeDao")
@Transactional
public class MGradeDaoImpl extends AbstractDao<Integer, MGrade> implements MGradeDao {
	
	
	final String ModelName = "MGrade";
	public MGrade findById(Integer id) {
		return getByKey(id.intValue());
	}

	@Override
	public int countBySchool(Integer school_id) {
		// Get row count
		int count = ((Long)getSession().createQuery("select count(*) from " + ModelName+  " WHERE actflg ='A' AND school_id = '" + school_id + "'").uniqueResult()).intValue();
		return count;
	}

	@Override
	public List<MGrade> findBySchool(Integer school_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
        
        crit_list.addOrder(Order.asc("id"));
        
	     @SuppressWarnings("unchecked")
		List<MGrade> result = crit_list.list();
	     return result;
	}

	@Override
	public void saveGrade(User me,MGrade mgrade) {
	
		mgrade.setActflg("A");
		mgrade.setCtdusr(Constant.USER_SYS);
		mgrade.setCtddtm(Utils.now());
		mgrade.setCtdpgm(Constant.PGM_REST);
		
		if (me != null ){
			mgrade.setCtdusr(me.getSso_id());	
			ApiKey apiKey = me.getApi_key();
			String device = null;
			if (apiKey != null && apiKey.getApi_key() != null ){
				device = apiKey.getApi_key();
			}
			if (device != null ){
				mgrade.setCtdwks(device);
			}
			
		}
		
		
		
		this.persist(mgrade);
		
	}

	@Override
	public void updateGrade(User me,MGrade mgrade) {
		
		mgrade.setMdfusr(Constant.USER_SYS);
		mgrade.setLstmdf(Utils.now());
		mgrade.setMdfpgm(Constant.PGM_REST);
		
		if (me != null ){
			mgrade.setMdfusr(me.getSso_id());
			ApiKey apiKey = me.getApi_key();
			String device = null;
			if (apiKey != null && apiKey.getApi_key() != null ){
				device = apiKey.getApi_key();
			}
			if (device != null ){
				mgrade.setMdfwks(device);
			}
		}
		
		
		
		update(mgrade);
		
	}

	@Override
	public void delGrade(User me,MGrade mgrade) {
		mgrade.setActflg("D");
		updateGrade(me, mgrade);
		
	}

	
}
