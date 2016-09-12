package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.MFee;
import com.itpro.restws.model.User;


public interface MFeeDao {

	int countBySchool(Integer school_id);
	MFee findById(Integer id);
	List<MFee> findBySchool(Integer school_id,int from_row,int max_result) ;
	void saveFee(User me,MFee mfee);
	void updateFee(User me,MFee mfee);
	void delFee(User me,MFee mfee);
	
}

