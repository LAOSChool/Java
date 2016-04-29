package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.MFee;

@Repository("mfeeDao")
@Transactional
public class MFeeDaoImpl extends AbstractDao<Integer, MFee> implements MFeeDao {
	
	final String ModelName = "MFee";
	public MFee findById(Integer id) {
		return getByKey(id.intValue());
	}

	@Override
	public int countBySchool(Integer school_id) {
		// Get row count
		int count = ((Long)getSession().createQuery("select count(*) from " + ModelName+  " WHERE school_id = '" + school_id + "'").uniqueResult()).intValue();
		return count;
	}

	@Override
	public List<MFee> findBySchool(Integer school_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
	     @SuppressWarnings("unchecked")
		List<MFee> result = crit_list.list();
	     return result;
	}

	@Override
	public void saveFee(MFee mfee) {
		mfee.setActflg("A");
		mfee.setCtdusr("HuyNQ-test");
		mfee.setCtddtm(Utils.now());
		mfee.setCtdpgm("RestWS");
		mfee.setCtddtm(Utils.now());
		save(mfee);
	}

	@Override
	public void updateFee(MFee mfee) {
		mfee.setMdfusr("HuyNQ-test");
		mfee.setLstmdf(Utils.now());
		mfee.setMdfpgm("RestWS");
		update(mfee);
	}
	

	
}
