package com.itpro.restws.dao;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.EduProfile;


@Repository("eduProfileDao")
@Transactional
public class EduProfileDaoImpl extends AbstractDao<Integer, EduProfile> implements EduProfileDao {

	
	@Override
	public EduProfile findByID(Integer id) {
		return getByKey(id);
	}

	

	
	@Override
	public void saveStudentProfile(EduProfile pro) {
		pro.setActflg("A");
		pro.setCtdusr("HuyNQ-test");
		pro.setCtddtm(Utils.now());
		pro.setCtdpgm("RestWS");
		pro.setCtddtm(Utils.now());
		persist(pro);
		
	}

	@Override
	public void updateStudentProfile(EduProfile pro) {
		pro.setMdfusr("HuyNQ-test");
		pro.setLstmdf(Utils.now());
		pro.setMdfpgm("RestWS");
		update(pro);
		
	}




	@Override
	public ArrayList<EduProfile> findBySchoolID(Integer school_id,int from_row, int max_result) {
		
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
        crit_list.addOrder(Order.asc("id"));
        
	     @SuppressWarnings("unchecked")
	     ArrayList<EduProfile>results = (ArrayList<EduProfile>) crit_list.list();
	     return  results;
	}




	@Override
	public ArrayList<EduProfile> findByStudentID(Integer student_id) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("student_id", student_id));
		crit_list.addOrder(Order.asc("id"));
		
	     @SuppressWarnings("unchecked")
	     ArrayList<EduProfile>results = (ArrayList<EduProfile>) crit_list.list();
	     return  results;
	}




	@Override
	public
	ArrayList<EduProfile> findEx(Integer student_id, Integer school_id, Integer cls_id, Integer school_year_id){
		Criteria crit_list = createEntityCriteria();
		if (student_id != null){
			crit_list.add(Restrictions.eq("student_id", student_id));
		}
		if (school_id != null){
			crit_list.add(Restrictions.eq("school_id", school_id));
		}
		if (cls_id != null){
			crit_list.add(Restrictions.eq("cls_id", cls_id));
		}
		if (school_year_id != null){
			crit_list.add(Restrictions.eq("school_year_id", school_year_id));
		}
		crit_list.addOrder(Order.asc("id"));
		//crit_list.addOrder(Order.desc("id"));
	     @SuppressWarnings("unchecked")
	     ArrayList<EduProfile>results = (ArrayList<EduProfile>) crit_list.list();
	     return  results;
	}




	@Override
	public EduProfile findLatestProfile(Integer student_id, Integer school_id) {
		
		ArrayList<EduProfile> list = findEx(student_id,school_id,null,null);
		if (list == null || list.size() <= 0 ){
			return null;
		}
		EduProfile max_eduProfile = list.get(0);
		for (EduProfile eduProfile : list){
			if (max_eduProfile.getId().intValue() < eduProfile.getId().intValue()){
				max_eduProfile = eduProfile;
			}
		}
		return max_eduProfile;
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
