package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.Permit;

@Repository("permitDao")
@Transactional
public class PermitDaoImpl extends AbstractDao<Integer, Permit> implements PermitDao {

	@Override
	public List<Permit> findPermit(String role,Integer school_id) {
		Criteria crit_list = createEntityCriteria();
		
		//Criterion rest1= Restrictions.sqlRestriction(" roles LIKE '%"+role.toUpperCase()+"%' ");
		Criterion rest1 = Restrictions.like("roles", role.toUpperCase(),MatchMode.ANYWHERE);
		
		Criterion rest2= Restrictions.eq("roles", "--ALL--");
		Criterion rest3 = Restrictions.or(rest1, rest2);
		
		crit_list.add(rest3);
		crit_list.add(Restrictions.eq("school_id", school_id));
	     @SuppressWarnings("unchecked")
		List<Permit> result = crit_list.list();
	     return result;
	}

	@Override
	public void savePermission(Permit permission) {
		permission.setActflg("A");
		permission.setCtdusr("HuyNQ-test");
		permission.setCtddtm(Utils.now());
		permission.setCtdpgm("RestWS");
		permission.setCtddtm(Utils.now());
		save(permission);
		
	}

	@Override
	public void updatePermission(Permit permission) {
		permission.setMdfusr("HuyNQ-test");
		permission.setLstmdf(Utils.now());
		permission.setMdfpgm("RestWS");
		update(permission);
		
	}

	@Override
	public List<Permit> findPermit(String role, Integer school_id,String entity) {
		Criteria crit_list = createEntityCriteria();
		
		//Criterion rest1= Restrictions.sqlRestriction(" roles LIKE '%"+role.toUpperCase()+"%' ");
		Criterion rest1 = Restrictions.like("roles", role.toUpperCase(),MatchMode.ANYWHERE);
		Criterion rest2= Restrictions.eq("roles", "--ALL--");
		Criterion rest3 = Restrictions.or(rest1, rest2);
		
		crit_list.add(rest3);
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.add(Restrictions.eq("entity", entity));
		
	     @SuppressWarnings("unchecked")
		List<Permit> result = crit_list.list();
	     return result;
	}

	@Override
	public List<Permit> findPermit(String role, Integer school_id,String entity, int scope) {
		
		Criteria crit_list = createEntityCriteria();
		
		//Criterion rest1= Restrictions.sqlRestriction(" roles LIKE '%"+role.toUpperCase()+"%' ");
		Criterion rest1 = Restrictions.like("roles", role.toUpperCase(),MatchMode.ANYWHERE);
		Criterion rest2= Restrictions.eq("roles", "--ALL--");
		Criterion rest3 = Restrictions.or(rest1, rest2);
		
		crit_list.add(rest3);
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.add(Restrictions.eq("entity", entity));
		// crit_list.add(Restrictions.ge("scope", scope)); // >=
		crit_list.add( Restrictions.sqlRestriction( "{alias}.scope & ? = ?", 
				   new Integer[]{ scope,  scope }, 
				   new Type[]{ StandardBasicTypes.INTEGER, StandardBasicTypes.INTEGER } ) );
		
	     @SuppressWarnings("unchecked")
		List<Permit> result = crit_list.list();
	     return result;
	}

	@Override
	public List<Permit> findPermit(String role, Integer school_id, String entity,int scope, String right) {
		Criteria crit_list = createEntityCriteria();
		//Criterion rest1= Restrictions.sqlRestriction(" roles LIKE '%"+role.toUpperCase()+"%' ");
		Criterion rest1 = Restrictions.like("roles", role.toUpperCase(),MatchMode.ANYWHERE);
		Criterion rest2= Restrictions.eq("roles", "--ALL--");
		Criterion rest3 = Restrictions.or(rest1, rest2);
		
		crit_list.add(rest3);
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.add(Restrictions.eq("entity", entity));
		//crit_list.add(Restrictions.ge("scope", scope)); // >=
		// crit_list.add(Restrictions.ge("scope", scope)); // >=
		crit_list.add( Restrictions.sqlRestriction( "{alias}.scope & ? = ?", 
				   new Integer[]{ scope,  scope }, 
				   new Type[]{ StandardBasicTypes.INTEGER, StandardBasicTypes.INTEGER } ) );		
		crit_list.add(Restrictions.eq("right", right));
		
	     @SuppressWarnings("unchecked")
		List<Permit> result = crit_list.list();
	     return result;
	}


//	private Criteria getCriteria(Criteria criteria,Collection<Integer> ints, Integer longVal) {
//		  
//
//		   if( longVal != null ) {
//			   criteria.add( Restrictions.sqlRestriction( "{alias}.longField & ? = ?", 
//					   new Integer[]{ longVal,  longVal }, 
//					   new Type[]{ StandardBasicTypes.LONG, StandardBasicTypes.LONG } ) );
//		   }
//
//		
//		   return criteria;
//		 }

//	

	@Override
	public void clearChange() {
		getSession().clear();
		
	}
	@Override
	public void setFlushMode(FlushMode mode){
		getSession().setFlushMode(mode);
		
	}
}
