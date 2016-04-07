package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.MUser2Class;

@Repository("muser2classDao")
@Transactional
public class MUser2ClassDaoImpl extends AbstractDao<Integer, MUser2Class> implements MUser2ClassDao {

	
	final String ModelName = "MUser2Class";
	public MUser2Class findById(int id) {
		return getByKey(id);
	}

	@Override
	public int countBySchool(int school_id) {
		// Get row count
		int count = ((Long)getSession().createQuery("select count(*) from " + ModelName+  " WHERE school_id = '" + school_id + "'").uniqueResult()).intValue();
		return count;
	}

	@Override
	public List<MUser2Class> findBySchool(int school_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
	     @SuppressWarnings("unchecked")
		List<MUser2Class> result = crit_list.list();
	     return result;
	}

	@Override
	public void saveUser2Class(MUser2Class muser2class) {
		muser2class.setActflg("A");
		muser2class.setCtdusr("HuyNQ-test");
		muser2class.setCtddtm(Utils.now());
		muser2class.setCtdpgm("RestWS");
		muser2class.setCtddtm(Utils.now());
		this.persist(muser2class);
		
	}

	@Override
	public void updateUser2Class(MUser2Class muser2class) {
		muser2class.setMdfusr("HuyNQ-test");
		muser2class.setLstmdf(Utils.now());
		muser2class.setMdfpgm("RestWS");
		update(muser2class);
		
		
	}

	
	
}
