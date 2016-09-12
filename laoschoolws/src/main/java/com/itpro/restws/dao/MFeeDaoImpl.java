package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Constant;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.ApiKey;
import com.itpro.restws.model.MFee;
import com.itpro.restws.model.User;

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
		int count = ((Long)getSession().createQuery("select count(*) from " + ModelName+  " WHERE actflg ='A' AND school_id = '" + school_id + "'").uniqueResult()).intValue();
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
	public void saveFee(User me,MFee mfee) {

	
		
		mfee.setActflg("A");
		mfee.setCtdusr(Constant.USER_SYS);
		mfee.setCtddtm(Utils.now());
		mfee.setCtdpgm(Constant.PGM_REST);
		
		if (me != null ){
			mfee.setCtdusr(me.getSso_id());	
			ApiKey apiKey = me.getApi_key();
			String device = null;
			if (apiKey != null && apiKey.getApi_key() != null ){
				device = apiKey.getApi_key();
			}
			if (device != null ){
				mfee.setCtdwks(device);
			}
			
		}
		
		
		
		save(mfee);
	}

	@Override
	public void updateFee(User me,MFee mfee) {
		mfee.setMdfusr(Constant.USER_SYS);
		mfee.setLstmdf(Utils.now());
		mfee.setMdfpgm(Constant.PGM_REST);
		
		if (me != null ){
			mfee.setMdfusr(me.getSso_id());
			ApiKey apiKey = me.getApi_key();
			String device = null;
			if (apiKey != null && apiKey.getApi_key() != null ){
				device = apiKey.getApi_key();
			}
			if (device != null ){
				mfee.setMdfwks(device);
			}
		}
	
		update(mfee);
	}

	@Override
	public void delFee(User me,MFee mfee) {
		mfee.setActflg("D");
		updateFee(me, mfee);
		
	}
	

	
}
