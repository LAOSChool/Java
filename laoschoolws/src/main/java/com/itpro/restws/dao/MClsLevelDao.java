package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.FlushMode;

import com.itpro.restws.model.MClsLevel;
import com.itpro.restws.model.User;


public interface MClsLevelDao {

	int countBySchool(Integer school_id);
	MClsLevel findById(Integer id);
	List<MClsLevel> findBySchool(Integer school_id,int from_row,int max_result) ;
	void saveLevel(User me,MClsLevel mclslevel);
	void updateLevel(User me,MClsLevel mclslevel);
	void setFlushMode(FlushMode mode);
	void clearChange();
}

