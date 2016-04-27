package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.MFee;


public interface MFeeDao {

	int countBySchool(Integer school_id);
	MFee findById(Integer id);
	List<MFee> findBySchool(Integer school_id,int from_row,int max_result) ;
	void saveFee(MFee mfee);
	public void updateFee(MFee mfee);
	
}

