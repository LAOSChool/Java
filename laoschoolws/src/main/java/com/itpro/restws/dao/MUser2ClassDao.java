package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.MUser2Class;
public interface MUser2ClassDao {
	int countBySchool(int school_id);
	MUser2Class findById(int id);
	List<MUser2Class> findBySchool(int school_id,int from_row,int max_result) ;
	void saveUser2Class(MUser2Class muser2class);
	void updateUser2Class(MUser2Class muser2class);
}
