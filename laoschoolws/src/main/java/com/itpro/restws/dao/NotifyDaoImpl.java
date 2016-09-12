package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.ScrollableResults;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Constant;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.ApiKey;
import com.itpro.restws.model.Notify;
import com.itpro.restws.model.User;

@Repository("notifyDao")
@Transactional
public class NotifyDaoImpl extends AbstractDao<Integer, Notify> implements NotifyDao {

	public Notify findById(Integer id) {
		return getByKey(id.intValue());
	}

	
	
	@Override
	public int countByFromUser(Integer from_user) {
		// Get row count
		int count = ((Long)getSession().createQuery("select count(*) from Notify WHERE actflg ='A' AND from_user_id= '" + from_user+ "'").uniqueResult()).intValue();
		return count;

	}


	@Override
	public int countByToUser(Integer to_user) {
		// Get row count
				int count = ((Long)getSession().createQuery("select count(*) from Notify WHERE to_user_id= '" + to_user+ "'").uniqueResult()).intValue();
				return count;
	}


	@Override
	public List<Notify> findByFromUser(Integer from_user_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("from_user_id", from_user_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
        
        crit_list.addOrder(Order.asc("id"));
	     @SuppressWarnings("unchecked")
		List<Notify> notifies = crit_list.list();
	     return notifies;
	}


	@Override
	public List<Notify> findByToUser(Integer to_user_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("to_user_id", to_user_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
        
        crit_list.addOrder(Order.asc("id"));
	     @SuppressWarnings("unchecked")
		List<Notify> notifies = crit_list.list();
	     return notifies;
	}



	@Override
	public int countBySchool(Integer school_id) {
		
		int count = ((Long)getSession().createQuery("select count(*) from Notify WHERE school_id= '" + school_id+ "'").uniqueResult()).intValue();
		return count;
	}



	@Override
	public int countByClass(Integer class_id) {
		
		int count = ((Long)getSession().createQuery("select count(*) from Notify WHERE class_id= '" + class_id+ "'").uniqueResult()).intValue();
		return count;
	}



	@Override
	public List<Notify> findBySchool(Integer school_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
        
        crit_list.addOrder(Order.asc("id"));
	     @SuppressWarnings("unchecked")
		List<Notify> notifies = crit_list.list();
	     return notifies;
	}



	@Override
	public List<Notify> findByClass(Integer class_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("class_id", class_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
        crit_list.addOrder(Order.asc("id"));
        
	     @SuppressWarnings("unchecked")
		List<Notify> notifies = crit_list.list();
	     return notifies;
	}



	@Override
	public void saveNotify(User me,Notify notify) {
				
		
		notify.setActflg("A");
		notify.setCtdusr(Constant.USER_SYS);
		notify.setCtddtm(Utils.now());
		notify.setCtdpgm(Constant.PGM_REST);
		
		if (me != null ){
			notify.setCtdusr(me.getSso_id());	
			ApiKey apiKey = me.getApi_key();
			String device = null;
			if (apiKey != null && apiKey.getApi_key() != null ){
				device = apiKey.getApi_key();
			}
			if (device != null ){
				notify.setCtdwks(device);
			}
			
		}
		
		
		save(notify);
		//getSession().flush() ;
		
	}



	@Override
	public void updateNotify(User me,Notify notify) {

	
		notify.setMdfusr(Constant.USER_SYS);
		notify.setLstmdf(Utils.now());
		notify.setMdfpgm(Constant.PGM_REST);
		
		if (me != null ){
			notify.setMdfusr(me.getSso_id());
			ApiKey apiKey = me.getApi_key();
			String device = null;
			if (apiKey != null && apiKey.getApi_key() != null ){
				device = apiKey.getApi_key();
			}
			if (device != null ){
				notify.setMdfwks(device);
			}
		}
		
		
		update(notify);
		//getSession().flush() ;
		
	}


	@Override
	public int sendTask(List<Integer> task_id_list) {
        
		Criteria crit = createEntityCriteria();
		// Filter by school
		crit.add(Restrictions.in("task_id", task_id_list));
		crit.add(Restrictions.eq("is_sent", 0));
		
		
		ScrollableResults items = crit.scroll();
        int count=0;
        while ( items.next() ) {
          Notify e = (Notify)items.get(0);
          e.setIs_sent(0);
          update(e);
          if ( ++count % 100 == 0 ) {
        	  getSession().flush();
        	  getSession().clear();
         }
        }
        return count;
	}
	
	
	@Override
	public Integer countNotifyExt(Integer school_id, 
			//int from_row, int max_result,
			// Secure filter
			List<Integer> classes, 
			List<Integer> users,
			// User filter
			Integer class_id, String dateFrom, String dateTo, Integer fromUserID, Integer toUserID, Integer channel,
			Integer is_read,Integer from_row_id) {
		Criteria crit_list = createEntityCriteria();
		// Filter by school
		crit_list.add(Restrictions.eq("school_id", school_id));
		// Limit data return

		// Filter by class
		if (classes != null && !classes.isEmpty()) {
			crit_list.add(Restrictions.in("class_id", classes));
		}

		// Filter by users
		if (users != null && !users.isEmpty()) {
//			crit_list.add(Restrictions.in("from_user_id", users));
//			crit_list.add(Restrictions.in("to_user_id", users));
			Criterion rest1= Restrictions.in("from_user_id", users);
			Criterion rest2= Restrictions.in("to_user_id", users);
			crit_list.add(Restrictions.or(rest1, rest2));

		}

		// Filter by class_id
		if (class_id != null && class_id > 0) {
			crit_list.add(Restrictions.eq("class_id", class_id));
		}

		// Filter by date
		if (dateFrom != null && dateTo != null) {
			crit_list.add(Restrictions.between("sent_dt", dateFrom, dateTo));
		} else if (dateFrom != null) {
			crit_list.add(Restrictions.gt("sent_dt", dateFrom));
		} else if (dateTo != null) {
			crit_list.add(Restrictions.lt("sent_dt", dateTo));
		}

		// Filter by from user
		if (fromUserID != null && fromUserID > 0) {
			crit_list.add(Restrictions.eq("from_user_id", fromUserID));
		}

		// Filter by from user
		if (toUserID != null && toUserID > 0) {
			crit_list.add(Restrictions.eq("to_user_id", toUserID));
		}

		// Filter by from channel
		if (channel != null && channel >= 0) {
			crit_list.add(Restrictions.eq("channel", channel));
		}

		// Filter by from channel
		if (is_read != null && is_read >= 0) {
			crit_list.add(Restrictions.eq("is_read", is_read));
		}
		// Filter by from channel
		if (from_row_id != null && from_row_id >= 0) {
			crit_list.add(Restrictions.gt("id", from_row_id));
		}
		
		// Group by task_id
		//crit_list.setProjection(Projections.projectionList().add(Projections.groupProperty("task_id")).add(Projections.rowCount()));
		
		crit_list.setProjection(Projections.countDistinct("task_id"));
		//crit_list.setProjection(Projections.rowCount());
		Number numRows = (Number) crit_list.uniqueResult();
		return numRows == null ? 0 : numRows.intValue();

	}

	@Override
	public List<Object> findNotifyExt(Integer school_id, int from_row, int max_result,
			// Secure filter
			List<Integer> classes, 
			List<Integer> users,
			// User filter
			Integer class_id, 
			String dateFrom, 
			String dateTo, 
			Integer fromUserID, 
			Integer toUserID, 
			Integer channel,
			Integer is_read, 
			Integer from_row_id) {

		Criteria crit_list = createEntityCriteria();
		// Filter by school
		crit_list.add(Restrictions.eq("school_id", school_id));
		// Limit data return
		crit_list.setMaxResults(max_result);
		crit_list.setFirstResult(from_row);

		// Filter by class
		if (classes != null && !classes.isEmpty()) {
			crit_list.add(Restrictions.in("class_id", classes));
		}

		// Filter by users
		if (users != null && !users.isEmpty()) {
//			crit_list.add(Restrictions.in("from_user_id", users));
//			crit_list.add(Restrictions.in("to_user_id", users));
			Criterion rest1= Restrictions.in("from_user_id", users);
			Criterion rest2= Restrictions.in("to_user_id", users);
			crit_list.add(Restrictions.or(rest1, rest2));
		}

		// Filter by class_id
		if (class_id != null && class_id > 0) {
			crit_list.add(Restrictions.eq("class_id", class_id));
		}

		// Filter by date
		if (dateFrom != null && dateTo != null) {
			crit_list.add(Restrictions.between("sent_dt", dateFrom, dateTo));
		} else if (dateFrom != null) {
			crit_list.add(Restrictions.gt("sent_dt", dateFrom));
		} else if (dateTo != null) {
			crit_list.add(Restrictions.lt("sent_dt", dateTo));
		}

		// Filter by from user
		if (fromUserID != null && fromUserID > 0) {
			crit_list.add(Restrictions.eq("from_user_id", fromUserID));
		}

		// Filter by from user
		if (toUserID != null && toUserID > 0) {
			crit_list.add(Restrictions.eq("to_user_id", toUserID));
		}

		// Filter by from channel
		if (channel != null && channel >= 0) {
			crit_list.add(Restrictions.eq("channel", channel));
		}

		// Filter by from channel
		if (is_read != null && is_read >= 0) {
			crit_list.add(Restrictions.eq("is_read", is_read));
		}

		
		// Filter by from channel
		if (from_row_id != null && from_row_id >= 0) {
			crit_list.add(Restrictions.gt("id", from_row_id));
		}

		// Group by task_id

		 ProjectionList projList = Projections.projectionList();
		projList.add(Projections.max("id"));
		//projList.add(Projections.property("id"));
        projList.add(Projections.groupProperty("task_id"));
        crit_list.setProjection(projList);     
	//	crit_list.addOrder( Order.asc("task_id") );
        
        // Task_ID >0
        crit_list.add(Restrictions.gt("task_id", 0));
		      
        crit_list.addOrder(Order.asc("id"));
        
		@SuppressWarnings("unchecked")
		List<Object> results = crit_list.list();
		return results;

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
