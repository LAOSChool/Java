package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.FlushMode;

import com.itpro.restws.model.MSession;
import com.itpro.restws.model.User;


public interface MSessionDao {

	int countBySchool(Integer school_id);
	MSession findById(Integer id);
	List<MSession> findBySchool(Integer school_id,int from_row,int max_result) ;
	void saveSession(User me, MSession msession);
	void updateSession(User me, MSession msession);
	void delSession(User me, MSession msession);
	void setFlushMode(FlushMode mode);
	void clearChange();
}

