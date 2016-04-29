package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.Message;

@Repository("messageDao")
@Transactional
public class MessageDaoImpl extends AbstractDao<Integer, Message> implements MessageDao {

	public Message findById(Integer id) {
		return getByKey(id.intValue());
	}

	@Override
	public int countByFromUser(Integer from_user) {
		// Get row count
		int count = ((Long) getSession()
				.createQuery("select count(*) from Message WHERE from_user_id= '" + from_user + "' AND is_sent != 99").uniqueResult())
						.intValue();
		return count;

	}

	@Override
	public int countByToUser(Integer to_user) {
		// Get row count
		int count = ((Long) getSession().createQuery("select count(*) from Message WHERE to_user_id= '" + to_user + "' AND is_sent != 99")
				.uniqueResult()).intValue();
		return count;
	}

	@Override
	public List<Message> findByFromUser(Integer from_user_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("from_user_id", from_user_id));
		crit_list.setMaxResults(max_result);
		crit_list.setFirstResult(from_row);
		
		// Ignore base message
		crit_list.add(Restrictions.ne("is_sent", 99));

		
		@SuppressWarnings("unchecked")
		List<Message> messages = crit_list.list();
		return messages;
	}

	@Override
	public List<Message> findByToUser(Integer to_user_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("to_user_id", to_user_id));
		crit_list.setMaxResults(max_result);
		crit_list.setFirstResult(from_row);
		
		// Ignore base message
		crit_list.add(Restrictions.ne("is_sent", 99));

		
		@SuppressWarnings("unchecked")
		List<Message> messages = crit_list.list();
		return messages;
	}

	@Override
	public int countBySchool(Integer school_id) {

		int count = ((Long) getSession()
				.createQuery("select count(*) from Message WHERE school_id= '" + school_id + "' AND is_sent != 99").uniqueResult())
						.intValue();
		return count;
	}

	@Override
	public int countByClass(Integer class_id) {

		int count = ((Long) getSession().createQuery("select count(*) from Message WHERE class_id= '" + class_id + "' AND is_sent != 99")
				.uniqueResult()).intValue();
		return count;
	}

	@Override
	public List<Message> findBySchool(Integer school_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.setMaxResults(max_result);
		crit_list.setFirstResult(from_row);
		
		// Ignore base message
		crit_list.add(Restrictions.ne("is_sent", 99));

		
		@SuppressWarnings("unchecked")
		List<Message> messages = crit_list.list();
		return messages;
	}

	@Override
	public List<Message> findByClass(Integer school_id, Integer class_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.add(Restrictions.eq("class_id", class_id));
		
		// Ignore base message
		crit_list.add(Restrictions.ne("is_sent", 99));

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
		//Integer id = save(message);
		save(message);
		// message.setId(id);

	}

	@Override
	public void updateMessage(Message message) {
		message.setMdfusr("HuyNQ-test");
		message.setLstmdf(Utils.now());
		message.setMdfpgm("RestWS");
		update(message);

	}

	@Override
	public List<Message> findMessagesExt(
			Integer school_id,
			int from_row, 
			int max_result,
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
			crit_list.add(Restrictions.between("lstmdf", dateFrom, dateTo));
		} else if (dateFrom != null) {
			crit_list.add(Restrictions.gt("lstmdf", dateFrom));
		} else if (dateTo != null) {
			crit_list.add(Restrictions.lt("lstmdf", dateTo));
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

		// Ignore base message
		crit_list.add(Restrictions.ne("is_sent", 99));
		
		@SuppressWarnings("unchecked")
		List<Message> messages = crit_list.list();
		return messages;

	}

	@Override
	public Integer countMessagesExt(Integer school_id, int from_row, int max_result,
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
			crit_list.add(Restrictions.between("lstmdf", dateFrom, dateTo));
		} else if (dateFrom != null) {
			crit_list.add(Restrictions.gt("lstmdf", dateFrom));
		} else if (dateTo != null) {
			crit_list.add(Restrictions.lt("lstmdf", dateTo));
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
		// Ignore base message
		crit_list.add(Restrictions.ne("is_sent", 99));
				
		crit_list.setProjection(Projections.rowCount());
		Number numRows = (Number) crit_list.uniqueResult();
		return numRows == null ? 0 : numRows.intValue();

	}

	//
	// private Criteria getCriteria(Collection<Integer> ints, Integer longVal, Date
	// dateFrom, Date dateTo) {
	//
	//
	// if( ints != null && !ints.isEmpty()) {
	// criteria.add( Restrictions.in( "intField", ints ) );
	// }
	//
	// if( longVal != null ) {
	// criteria.add( Restrictions.sqlRestriction( "{alias}.longField & ? = ?",
	// new Integer[]{ longVal, longVal }, new Type[]{ StandardBasicTypes.LONG,
	// StandardBasicTypes.LONG } ) );
	// }
	//
	// if(dateFrom != null && dateTo != null) {
	// criteria.add( Restrictions.between( "dateField", dateFrom, dateTo ) );
	// } else if(dateFrom != null) {
	// criteria.add( Restrictions.gt( "dateField", dateFrom ) );
	// } else if(dateTo != null) {
	// criteria.add( Restrictions.lt( "dateField", dateTo ) );
	// }
	//
	// return criteria;
	// }

	// public List<TimeSeriesPowerPK> getPowerUsageForUser(String minutes) {
	// Query query = sessionFactory.getCurrentSession().createQuery("From
	// TimeSeriesPowerPK where dateTime <=:param1 AND dateTime >= :parma2 ");
	// query.setParameter("param1",currentDateObject);
	// query.setParameter("param2",userEnteredDateObject);
	// List<TimeSeriesPowerPK> powerUsedList=query.list();
	// return powerUsedList;
	// }

}
