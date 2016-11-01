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
import com.itpro.restws.model.EClass;
import com.itpro.restws.model.User;


@Repository("classesDao")
@Transactional
public class ClassDaoImpl extends AbstractDao<Integer, EClass> implements ClassDao {

	@Override
	public EClass findById(Integer id) {
		return getByKey(id.intValue());
	}

	@Override
	public int countClassBySchool(Integer school_id) {
		int count = ((Long)getSession().createQuery("select count(*) from EClass WHERE actflg ='A' AND school_id= '" + school_id+ "'").uniqueResult()).intValue();
		return count;
	}

	@Override
	public List<EClass> findBySchool(Integer school_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
        
        crit_list.addOrder(Order.asc("id"));
        crit_list.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	     @SuppressWarnings("unchecked")
		List<EClass> classes = crit_list.list();
	     
		return  classes;
	}
	
	@Override
	public List<EClass> findActiveBySchool(Integer school_id) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
        
        crit_list.addOrder(Order.asc("id"));
        crit_list.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	     @SuppressWarnings("unchecked")
		List<EClass> classes = crit_list.list();
	     
		return  classes;
	}

	@Override
	public List<EClass> findByUser(Integer user_id, int from_row, int max_result) {
		
		Criteria crit_list = createEntityCriteria();
		crit_list.createAlias("users", "usersAlias");
		crit_list.add(Restrictions.eq("usersAlias.id", user_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
        
        crit_list.addOrder(Order.asc("id"));
        
	     @SuppressWarnings("unchecked")
		List<EClass> classes = crit_list.list();
	     
		return  classes;
	}

	@Override
	public void saveClass(User me, EClass eClass) {

		
		eClass.setActflg("A");
		eClass.setCtdusr(Constant.USER_SYS);
		eClass.setCtddtm(Utils.now());
		eClass.setCtdpgm(Constant.PGM_REST);
		if (me != null ){
			eClass.setCtdusr(me.getSso_id());	
			ApiKey apiKey = me.getApi_key();
			String device = null;
			if (apiKey != null && apiKey.getApi_key() != null ){
				device = apiKey.getApi_key();
			}
			if (device != null ){
				eClass.setCtdwks(device);
			}
			
		}
		
		save(eClass);

		
	}

	@Override
	public void updateClass(User me, EClass eClass) {

		eClass.setMdfusr(Constant.USER_SYS);
		eClass.setLstmdf(Utils.now());
		eClass.setMdfpgm(Constant.PGM_REST);
		if (me != null ){
			eClass.setMdfusr(me.getSso_id());
			ApiKey apiKey = me.getApi_key();
			String device = null;
			if (apiKey != null && apiKey.getApi_key() != null ){
				device = apiKey.getApi_key();
			}
			if (device != null ){
				eClass.setMdfwks(device);
			}
		}
		
		
		update(eClass);

		
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
