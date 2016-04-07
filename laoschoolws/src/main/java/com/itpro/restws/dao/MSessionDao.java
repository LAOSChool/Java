package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.MSession;


public interface MSessionDao {

	int countBySchool(int school_id);
	MSession findById(int id);
	List<MSession> findBySchool(int school_id,int from_row,int max_result) ;
	void saveSession(MSession msession);
	void updateSession(MSession msession);
}

