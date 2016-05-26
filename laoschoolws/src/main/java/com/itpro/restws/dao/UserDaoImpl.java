package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.Message;
import com.itpro.restws.model.User;

@Repository("userDao")
@Transactional
public class UserDaoImpl extends AbstractDao<Integer, User> implements UserDao {

	public User findById(Integer id) {
		return getByKey(id.intValue());
	}

	public User findBySSO(String sso) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("sso_id", sso));
		return (User) crit.uniqueResult();
	}
	
	
	public int countUserBySchool(Integer school_id){
		// Get row count
		int count = ((Long)getSession().createQuery("select count(*) from User WHERE school_id= '" + school_id.intValue()+ "'").uniqueResult()).intValue();
		return count;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<User> findBySchool(Integer school_id,int from_row,int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
	     List<User> users = crit_list.list();
	     
		return  users;
		
	}

	@Override
	public List<User> findByClass(Integer class_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.createAlias("classes", "classesAlias");
		crit_list.add(Restrictions.eq("classesAlias.id", class_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
	     @SuppressWarnings("unchecked")
		List<User> users = crit_list.list();
	     return users;
	}

	@Override
	public void saveUser(User user) {
		user.setActflg("A");
		user.setCtdusr("HuyNQ-test");
		user.setCtddtm(Utils.now());
		user.setCtdpgm("RestWS");
		
		save(user);

		
	}

	@Override
	public void updateUser(User user) {
		user.setMdfusr("HuyNQ-test");
		user.setMdfpgm("RestWS");
		update(user);
	}

	@Override
	public int countUserByClass(Integer class_id) {
		Criteria crit_list = createEntityCriteria();
		crit_list.createAlias("classes", "classesAlias");
		crit_list.add(Restrictions.eq("classesAlias.id", class_id));
		crit_list.setProjection(Projections.rowCount());
		Long resultCount = (Long)crit_list.uniqueResult();
		
	
	     return resultCount.intValue();
	}


	
//	 public void setUser(User a) throws HibernateException {
//		    try {
//		    	User tmp = (User) session.get(User.class, a.getId());
//		        tmp.setEmail(getNotNull(a.getEmail(), tmp.getEmail()));            
//		        ...
//		        tmp.setVersion(getNotNull(a.getVersion(), tmp.getVersion()));
//		        session.beginTransaction();
//		        session.update(tmp);
//		        session.getTransaction().commit();
//		    } catch (HibernateException e) {
//		        logger.error(e.toString());
//		        throw e;
//		    }
//		}
//
//		public static <T> T getNotNull(T a, T b) {
//		    return b != null && a != null && !a.equals(b) ? a : b;
//		}

	
//	oUp = Updated row with null fields & oDb = Row fetched from database at update time
//	public static <T> T updateChanges(T oDb, T oUp) {
//	    try {
//	        java.lang.reflect.Field[] fields = oDb.getClass().getDeclaredFields();
//	        for (Field field : fields) {
//	            field.setAccessible(true);
//	            if (field.get(oUp) != null) {
//	                field.set(oDb, field.get(oUp));
//	            }
//	        }
//	    } catch (IllegalAccessException e) {
//	        e.printStackTrace();
//	    }
//	    return oDb;
//	}
	
	@Override				
	public Integer countUserExt(
			Integer school_id, 
			// Filter
			Integer class_id, 
			String role,
			Integer state,
			Integer from_row_id
			) {
		
		//int count = ((Long)getSession().createQuery("select count(*) from User WHERE school_id= '" + school_id.intValue()+ "'").uniqueResult()).intValue();
		String query = 	"select count(*)  from User user join user.classes cls where user.school_id ='"+school_id +"'";
		if (class_id != null && class_id > 0){
			query = query +" and cls.id = '"+class_id.intValue()+"'"; 
		}
		if (role != null && role.length() > 0){
			query = query +" and user.roles = '"+role+"'"; 
		}		
				
		if (state != null && state > 0){
			query = query +" and user.state = '"+state.intValue()+"'"; 
		}	
		if (from_row_id != null && from_row_id> 0){
			query = query +" and user.id > '"+from_row_id.intValue()+"'";
		}
		int count = ((Long)getSession().createQuery(query).uniqueResult()).intValue();
		return count;

	}
	@Override
	public List<User> findUserExt(
			Integer school_id, 
			int from_row, 
			int max_result,
			// Filter
			Integer class_id, 
			String role,
			Integer state,
			Integer from_row_id
			) {
		
		String str = 	"SELECT user from User user join user.classes cls where user.school_id ='"+school_id +"'";
		if (class_id != null && class_id > 0){
			str = str +" and cls.id = '"+class_id.intValue()+"'"; 
		}
		if (role != null && role.length() > 0){
			str = str +" and user.roles = '"+role+"'"; 
		}		
				
		if (state != null && state > 0){
			str = str +" and user.state = '"+state.intValue()+"'"; 
		}
		
		if (from_row_id != null && from_row_id> 0){
			str = str +" and user.id > '"+from_row_id.intValue()+"'";
		}
		
		Query query =  getSession().createQuery(str);
		query.setMaxResults(max_result);
		query.setFirstResult(from_row);
		
		
		
		@SuppressWarnings("unchecked")
		List<User> users= query.list();
		return users;

	}

}
