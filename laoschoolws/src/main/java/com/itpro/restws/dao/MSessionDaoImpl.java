package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.MSession;

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
		int count = ((Long)getSession().createQuery("select count(*) from " + ModelName+  " WHERE school_id = '" + school_id + "'").uniqueResult()).intValue();
		return count;
	}

	@Override
	public List<MSession> findBySchool(Integer school_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
	     @SuppressWarnings("unchecked")
		List<MSession> result = crit_list.list();
	     return result;
	}

	@Override
	public void saveSession(MSession msession) {
		msession.setActflg("A");
		msession.setCtdusr("HuyNQ-test");
		msession.setCtddtm(Utils.now());
		msession.setCtdpgm("RestWS");
		msession.setCtddtm(Utils.now());
		this.persist(msession);
		
	}

	@Override
	public void updateSession(MSession msession) {
		msession.setMdfusr("HuyNQ-test");
		msession.setLstmdf(Utils.now());
		msession.setMdfpgm("RestWS");
		update(msession);
		
	}


	
}
