package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Constant;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.ApiKey;
import com.itpro.restws.model.MSession;
import com.itpro.restws.model.User;

@Repository("msessionDao")
@Transactional
public class MSessionDaoImpl extends AbstractDao<Integer, MSession> implements MSessionDao {
	
	
	final String ModelName = "MSession";
	public MSession findById(Integer id) {
		return getByKey(id.intValue());
	}

	@Override
	public int countBySchool(Integer school_id) {
		// Get row count
		int count = ((Long)getSession().createQuery("select count(*) from " + ModelName+  " WHERE actflg ='A' AND school_id = '" + school_id + "'").uniqueResult()).intValue();
		return count;
	}

	@Override
	public List<MSession> findBySchool(Integer school_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
        
        crit_list.addOrder(Order.asc("id"));
        
	     @SuppressWarnings("unchecked")
		List<MSession> result = crit_list.list();
	     return result;
	}

	@Override
	public void saveSession(User me, MSession msession) {
		
		
		
		msession.setActflg("A");
		msession.setCtdusr(Constant.USER_SYS);
		msession.setCtddtm(Utils.now());
		msession.setCtdpgm(Constant.PGM_REST);
		
		if (me != null ){
			msession.setCtdusr(me.getSso_id());	
			ApiKey apiKey = me.getApi_key();
			String device = null;
			if (apiKey != null && apiKey.getApi_key() != null ){
				device = apiKey.getApi_key();
			}
			if (device != null ){
				msession.setCtdwks(device);
			}
			
		}
		
		
		this.persist(msession);
		
	}

	@Override
	public void updateSession(User me, MSession msession) {
		
		
		
		msession.setMdfusr(Constant.USER_SYS);
		msession.setLstmdf(Utils.now());
		msession.setMdfpgm(Constant.PGM_REST);
		
		if (me != null ){
			msession.setMdfusr(me.getSso_id());
			ApiKey apiKey = me.getApi_key();
			String device = null;
			if (apiKey != null && apiKey.getApi_key() != null ){
				device = apiKey.getApi_key();
			}
			if (device != null ){
				msession.setMdfwks(device);
			}
		}
		
		
		update(msession);
		
	}

	@Override
	public void delSession(User me, MSession msession) {
		msession.setActflg("D");
		updateSession(me, msession);
		
		
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
