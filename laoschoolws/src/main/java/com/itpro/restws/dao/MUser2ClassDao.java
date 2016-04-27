package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.MUser2Class;
public interface MUser2ClassDao {
	int countBySchool(Integer school_id);
	MUser2Class findById(Integer id);
	List<MUser2Class> findBySchool(Integer school_id,int from_row,int max_result) ;
	void saveUser2Class(MUser2Class muser2class);
	void updateUser2Class(MUser2Class muser2class);
}
