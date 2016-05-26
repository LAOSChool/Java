package com.itpro.restws.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.Term;


@Repository("termDao")
@Transactional
public class TermDaoImpl extends AbstractDao<Integer, Term> implements TermDao {

	@Override
	public Term findById(Integer id) {
		return getByKey(id.intValue());
	}


	@Override
	public ArrayList<Term> findBySchool(Integer school_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
		@SuppressWarnings("unchecked")
		List<Term> list = crit_list.list();
	     
		return  (ArrayList<Term>) list;
	}

	
	

	@Override
	public void saveTerm(Term term) {
		term.setActflg("A");
		term.setCtdusr("HuyNQ-test");
		term.setCtddtm(Utils.now());
		term.setCtdpgm("RestWS");
		term.setCtddtm(Utils.now());
		save(term);

		
	}

	@Override
	public void updateTerm(Term term) {
		term.setMdfusr("HuyNQ-test");
		term.setLstmdf(Utils.now());
		term.setMdfpgm("RestWS");
		update(term);

		
	}


	@Override
	public int countBySchool(Integer school_id) {
		int count = ((Long)getSession().createQuery("select count(*) from Term WHERE school_id= '" + school_id+ "'").uniqueResult()).intValue();
		return count;
		
	}


	@Override
	public ArrayList<Term> getLatestTerm(Integer school_id) {
		
		Criteria crit_list = createEntityCriteria();
		
		Criterion rest1= Restrictions.le("start_year", Utils.getCurrentYear()); 
		Criterion rest2= Restrictions.ge("end_year", Utils.getCurrentYear());
		
		crit_list.add(Restrictions.and(rest1, rest2));
		crit_list.addOrder(Order.asc("id"));
	     @SuppressWarnings("unchecked")
		List<Term> list = crit_list.list();
	     return (ArrayList<Term>) list;
	}




	

	
	
}
