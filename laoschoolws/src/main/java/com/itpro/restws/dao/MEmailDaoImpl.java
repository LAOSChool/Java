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
import com.itpro.restws.model.MEmail;
import com.itpro.restws.model.User;

@Repository("memailDao")
@Transactional
public class MEmailDaoImpl extends AbstractDao<Integer, MEmail> implements MEmailDao {
	
	
	final String ModelName = "MEmail";
	public MEmail findById(Integer id) {
		return getByKey(id.intValue());
	}

	@Override
	public int countBySchool(Integer school_id) {
		// Get row count
		int count = ((Long)getSession().createQuery("select count(*) from " + ModelName+  " WHERE actflg ='A' AND school_id = '" + school_id + "'").uniqueResult()).intValue();
		return count;
	}

	@Override
	public List<MEmail> findBySchool(Integer school_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
        
        crit_list.addOrder(Order.asc("id"));
        
	     @SuppressWarnings("unchecked")
		List<MEmail> result = crit_list.list();
	     return result;
	}

	@Override
	public void saveEmail(User me,MEmail mail) {
	
		mail.setActflg("A");
		mail.setCtdusr(Constant.USER_SYS);
		mail.setCtddtm(Utils.now());
		mail.setCtdpgm(Constant.PGM_REST);
		
		if (me != null ){
			mail.setCtdusr(me.getSso_id());	
			ApiKey apiKey = me.getApi_key();
			String device = null;
			if (apiKey != null && apiKey.getApi_key() != null ){
				device = apiKey.getApi_key();
			}
			if (device != null ){
				mail.setCtdwks(device);
			}
			
		}
		
		
		
		this.persist(mail);
		
	}

	@Override
	public void updateEmail(User me,MEmail mail) {
		
		mail.setMdfusr(Constant.USER_SYS);
		mail.setLstmdf(Utils.now());
		mail.setMdfpgm(Constant.PGM_REST);
		
		if (me != null ){
			mail.setMdfusr(me.getSso_id());
			ApiKey apiKey = me.getApi_key();
			String device = null;
			if (apiKey != null && apiKey.getApi_key() != null ){
				device = apiKey.getApi_key();
			}
			if (device != null ){
				mail.setMdfwks(device);
			}
		}
		
		
		
		update(mail);
		
	}

	@Override
	public void delEmail(User me,MEmail mail) {
		mail.setActflg("D");
		updateEmail(me, mail);
		
	}

	
}
