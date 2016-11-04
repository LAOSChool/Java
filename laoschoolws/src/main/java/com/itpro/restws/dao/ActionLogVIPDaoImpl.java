package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Constant;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.ActionLogVIP;


@Repository("actionLogVIPDao")
@Transactional
public class ActionLogVIPDaoImpl extends AbstractDao<Integer, ActionLogVIP> implements ActionLogVIPDao {

	@Override
	public int countBySchool(Integer school_id) {
		int count = ((Long)getSession().createQuery("select count(*) from ActionLogVIP WHERE  actflg ='A' AND  school_id= '" + school_id+ "'").uniqueResult()).intValue();
		return count;
	}

	@Override
	public int countBySSO(String sso) {
		int count = ((Long)getSession().createQuery("select count(*) from ActionLogVIP WHERE sso_id= '" + sso+ "'").uniqueResult()).intValue();
		return count;
	}

	@Override
	public List<ActionLogVIP> findBySchool(Integer school_id, int from_row, int max_result) {
		
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
	     @SuppressWarnings("unchecked")
		List<ActionLogVIP> list = crit_list.list();
	     return list;

	}

	@Override
	public List<ActionLogVIP> findBySSO(String sso_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("sso_id", sso_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
	     @SuppressWarnings("unchecked")
		List<ActionLogVIP> list = crit_list.list();
	     return list;
	}

	@Override
	public void saveAction(ActionLogVIP actionLogVIP) {
		actionLogVIP.setActflg("A");
		
		actionLogVIP.setCtddtm(Utils.now());
		actionLogVIP.setCtdusr(Constant.USER_SYS);
		actionLogVIP.setCtddtm(Utils.now());
		actionLogVIP.setCtdpgm(Constant.PGM_REST);
		save(actionLogVIP);
		
	}

	@Override
	public void updateAction(ActionLogVIP actionLogVIP) {
		actionLogVIP.setMdfusr(Constant.USER_SYS);
		actionLogVIP.setMdfpgm(Constant.PGM_REST);
		actionLogVIP.setLstmdf(Utils.now());
		
		update(actionLogVIP);

		
	}
	@Override
	public ActionLogVIP findById(Integer id) {
		return getByKey(id.intValue());
	}


	

		
	

	public List<ActionLogVIP> findActionLogExt(
			Integer school_id, 
			Integer from_num, 
			Integer max_result,
			String filter_sso_id, 
			String filter_from_dt,
			String filter_to_dt,
			String filter_type
			){
		
		String str = 	"from ActionLogVIP act  where act.school_id ='"+school_id +"'";
	
	
		if (filter_sso_id != null){
			str = str +" and act.sso_id = '"+filter_sso_id+"'";
		}
		
		
		if (filter_from_dt != null ){
			str = str +" and DATE(act.request_dt) >= DATE('"+filter_from_dt+"')";
		}
		if (filter_to_dt != null ){
			str = str +" and DATE(act.request_dt) <= DATE('"+filter_to_dt+"')";
		}

		
		if (filter_type != null){
			str = str +" and act.act_type = '"+filter_type+"'";
		}
		
		str = str +" order by act.id ";
		
		Query query =  getSession().createQuery(str);
		query.setMaxResults(max_result);
		query.setFirstResult(from_num);
		
		
		
		@SuppressWarnings("unchecked")
		List<ActionLogVIP>  ret= query.list();
		return ret;
	}

	@Override
	public int countActionLogExt(
			Integer school_id, 
			
			String filter_sso_id, 
			
			String filter_from_dt,
			String filter_to_dt,
			String filter_type
			 
			) {
		
		String query = 	"select count(*)  from ActionLogVIP act where act.school_id ='"+school_id +"'";
		
		if (filter_sso_id != null){
			query = query +" and act.sso_id = '"+filter_sso_id+"'";
		}
		
		
		
		if (filter_from_dt != null ){
			query = query +" and DATE(act.request_dt) >= DATE('"+filter_from_dt+"')";
		}
		if (filter_to_dt != null ){
			query = query +" and DATE(act.request_dt) <= DATE('"+filter_to_dt+"')";
		}
		
		if (filter_type != null){
			query = query +" and act.act_type = '"+filter_type+"'";
		}

		
		int count = ((Long)getSession().createQuery(query).uniqueResult()).intValue();
		return count;
	}

	
}
