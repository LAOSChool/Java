package com.itpro.restws.dao;

import java.util.ArrayList;
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
import com.itpro.restws.model.MSubject;
import com.itpro.restws.model.User;

@Repository("msubjectDao")
@Transactional
public class MSubjectDaoImpl extends AbstractDao<Integer, MSubject> implements MSubjectDao {

	

	final String ModelName = "MSubject";
	public MSubject findById(Integer id) {
		return getByKey(id.intValue());
	}

	@Override
	public int countBySchool(Integer school_id) {
		// Get row count
		int count = ((Long)getSession().createQuery("select count(*) from " + ModelName+  " WHERE actflg ='A' AND  school_id = '" + school_id + "'").uniqueResult()).intValue();
		return count;
	}

	@Override
	public ArrayList<MSubject> findBySchool(Integer school_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
        
        crit_list.addOrder(Order.asc("id"));
        
	     @SuppressWarnings("unchecked")
		List<MSubject> result = crit_list.list();
	     return (ArrayList<MSubject>) result;
	}

	@Override
	public void saveSubject(User me,MSubject msubject) {
		
		
		msubject.setActflg("A");
		msubject.setCtdusr(Constant.USER_SYS);
		msubject.setCtddtm(Utils.now());
		msubject.setCtdpgm(Constant.PGM_REST);
		
		if (me != null ){
			msubject.setCtdusr(me.getSso_id());	
			ApiKey apiKey = me.getApi_key();
			String device = null;
			if (apiKey != null && apiKey.getApi_key() != null ){
				device = apiKey.getApi_key();
			}
			if (device != null ){
				msubject.setCtdwks(device);
			}
			
		}
		
		
		
		this.persist(msubject);
		
	}

	@Override
	public void updateSubject(User me,MSubject msubject) {
		
		msubject.setMdfusr(Constant.USER_SYS);
		msubject.setLstmdf(Utils.now());
		msubject.setMdfpgm(Constant.PGM_REST);
		
		if (me != null ){
			msubject.setMdfusr(me.getSso_id());
			ApiKey apiKey = me.getApi_key();
			String device = null;
			if (apiKey != null && apiKey.getApi_key() != null ){
				device = apiKey.getApi_key();
			}
			if (device != null ){
				msubject.setMdfwks(device);
			}
		}
		
		
		update(msubject);
		
	}

	@Override
	public void delSubject(User me,MSubject msubject) {
		delete(msubject);
		
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
