package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.Notify;


public interface NotifyDao {

	int countBySchool(Integer school_id);
	int countByClass(Integer class_id);
	int countByFromUser(Integer from_user);
	int countByToUser(Integer to_user);
	
	Notify findById(Integer id);
	
	List<Notify> findByFromUser(Integer from_user_id,int from_row,int max_result) ;
	List<Notify> findByToUser(Integer to_user_id,int from_row,int max_result) ;
	List<Notify> findBySchool(Integer school_id,int from_row,int max_result) ;
	List<Notify> findByClass(Integer class_id,int from_row,int max_result) ;
	
	int sendTask(List<Integer> task_id_list);
	
	void saveNotify(Notify notify);
	void updateNotify(Notify notify);
	public Integer countNotifyExt(Integer school_id, int from_row, int max_result,
			// Secure filter
			List<Integer> classes, 
			List<Integer> users,
			// User filter
			Integer class_id, String dateFrom, String dateTo, Integer fromUserID, Integer toUserID, Integer channel,
			Integer is_read,Integer from_row_id);
	
	List<Object> findNotifyExt(Integer school_id, int from_row, int max_result,
			// Secure filter
			List<Integer> classes, 
			List<Integer> users,
			// User filter
			Integer class_id, String dateFrom, String dateTo, Integer fromUserID, Integer toUserID, Integer channel,
			Integer is_read, Integer from_row_id);
}



