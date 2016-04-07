package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.Message;

@Repository("messageDao")
@Transactional
public class MessageDaoImpl extends AbstractDao<Integer, Message> implements MessageDao {

	public Message findById(int id) {
		return getByKey(id);
	}

	
	
	@Override
	public int countByFromUser(int from_user) {
		// Get row count
		int count = ((Long)getSession().createQuery("select count(*) from Message WHERE from_user_id= '" + from_user+ "'").uniqueResult()).intValue();
		return count;

	}


	@Override
	public int countByToUser(int to_user) {
		// Get row count
				int count = ((Long)getSession().createQuery("select count(*) from Message WHERE to_user_id= '" + to_user+ "'").uniqueResult()).intValue();
				return count;
	}


	@Override
	public List<Message> findByFromUser(int from_user_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("from_user_id", from_user_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
	     @SuppressWarnings("unchecked")
		List<Message> messages = crit_list.list();
	     return messages;
	}


	@Override
	public List<Message> findByToUser(int to_user_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("to_user_id", to_user_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
	     @SuppressWarnings("unchecked")
		List<Message> messages = crit_list.list();
	     return messages;
	}



	@Override
	public int countBySchool(int school_id) {
		
		int count = ((Long)getSession().createQuery("select count(*) from Message WHERE school_id= '" + school_id+ "'").uniqueResult()).intValue();
		return count;
	}



	@Override
	public int countByClass(int class_id) {
		
		int count = ((Long)getSession().createQuery("select count(*) from Message WHERE class_id= '" + class_id+ "'").uniqueResult()).intValue();
		return count;
	}



	@Override
	public List<Message> findBySchool(int school_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
	     @SuppressWarnings("unchecked")
		List<Message> messages = crit_list.list();
	     return messages;
	}



	@Override
	public List<Message> findByClass(int class_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("class_id", class_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
	     @SuppressWarnings("unchecked")
		List<Message> messages = crit_list.list();
	     return messages;
	}



	@Override
	public void saveMessage(Message message) {
		message.setActflg("A");
		message.setCtdusr("HuyNQ-test");
		message.setCtddtm(Utils.now());
		message.setCtdpgm("RestWS");
		message.setCtddtm(Utils.now());
		save(message);
		
	}



	@Override
	public void updateMessage(Message message) {
		message.setMdfusr("HuyNQ-test");
		message.setLstmdf(Utils.now());
		message.setMdfpgm("RestWS");
		update(message);
		
	}



	
}
