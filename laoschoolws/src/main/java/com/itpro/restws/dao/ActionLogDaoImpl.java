package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.ActionLog;


@Repository("actionLogDao")
@Transactional
public class ActionLogDaoImpl extends AbstractDao<Integer, ActionLog> implements ActionLogDao {

	@Override
	public int countBySchool(Integer school_id) {
		int count = ((Long)getSession().createQuery("select count(*) from ActionLog WHERE school_id= '" + school_id+ "'").uniqueResult()).intValue();
		return count;
	}

	@Override
	public int countByUser(Integer user_id) {
		int count = ((Long)getSession().createQuery("select count(*) from ActionLog WHERE user_id= '" + user_id+ "'").uniqueResult()).intValue();
		return count;
	}

	@Override
	public List<ActionLog> findBySchool(Integer school_id, int from_row, int max_result) {
		
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
	     @SuppressWarnings("unchecked")
		List<ActionLog> list = crit_list.list();
	     return list;

	}

	@Override
	public List<ActionLog> findByUser(Integer user_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("user_id", user_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
	     @SuppressWarnings("unchecked")
		List<ActionLog> list = crit_list.list();
	     return list;
	}

	@Override
	public void saveAction(ActionLog actionLog) {
		actionLog.setActflg("A");
		actionLog.setCtdusr("HuyNQ-test");
		actionLog.setCtddtm(Utils.now());
		actionLog.setCtdpgm("RestWS");
		actionLog.setCtddtm(Utils.now());
		save(actionLog);
		
	}

	@Override
	public void updateAction(ActionLog actionLog) {
		actionLog.setMdfusr("HuyNQ-test");
		actionLog.setLstmdf(Utils.now());
		actionLog.setMdfpgm("RestWS");
		update(actionLog);

		
	}
	@Override
	public ActionLog findById(Integer id) {
		return getByKey(id.intValue());
	}


	
	@Override
	public int countActionLogExt(Integer school_id, Integer user_id, Integer from_row_id,String from_dt, String to_dt) {
		String query = 	"select count(*)  from ActionLog act where act.school_id ='"+school_id +"'";
				
				
		if (user_id != null && user_id > 0){
			query = query +" and act.user_id = '"+user_id.intValue()+"'"; 
		}	
		
		if (from_row_id != null && from_row_id> 0){
			query = query +" and act.id > '"+from_row_id.intValue()+"'";
		}
		
		
		if (from_dt != null ){
			query = query +" and act.request_dt >= '"+from_dt+"'";
		}
		if (to_dt != null ){
			query = query +" and act.request_dt <= '"+to_dt+"'";
		}
		int count = ((Long)getSession().createQuery(query).uniqueResult()).intValue();
		return count;
	}

	@Override
	public List<ActionLog> findActionLogExt(Integer school_id, Integer user_id, Integer from_row_id,
			int from_num, int max_result,String from_dt, String to_dt) {
		String str = 	"from ActionLog att where act.school_id ='"+school_id +"'";
	
		
		if (user_id != null && user_id > 0){
			str = str +" and act.user_id = '"+user_id.intValue()+"'"; 
		}	
		
		if (from_row_id != null && from_row_id> 0){
			str = str +" and act.id > '"+from_row_id.intValue()+"'";
		}
		
		if (from_dt != null ){
			str = str +" and act.request_dt >= '"+from_dt+"'";
		}
		if (to_dt != null ){
			str = str +" and act.request_dt <= '"+to_dt+"'";
		}
		Query query =  getSession().createQuery(str);
		query.setMaxResults(max_result);
		query.setFirstResult(from_num);
		
		
		
		@SuppressWarnings("unchecked")
		List<ActionLog>  ret= query.list();
		return ret;
	}	
}
