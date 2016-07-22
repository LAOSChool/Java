package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.MSession;


public interface MSessionDao {

	int countBySchool(Integer school_id);
	MSession findById(Integer id);
	List<MSession> findBySchool(Integer school_id,int from_row,int max_result) ;
	void saveSession(MSession msession);
	void updateSession(MSession msession);
	void delSession(MSession msession);
}

