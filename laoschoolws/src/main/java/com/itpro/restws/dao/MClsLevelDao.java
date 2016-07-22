package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.MClsLevel;


public interface MClsLevelDao {

	int countBySchool(Integer school_id);
	MClsLevel findById(Integer id);
	List<MClsLevel> findBySchool(Integer school_id,int from_row,int max_result) ;
	void saveLevel(MClsLevel mclslevel);
	void updateLevel(MClsLevel mclslevel);
}

