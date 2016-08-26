package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.Command;


public interface CommandDao {
	List<Command> findUnProcessed();
	 Command findByID(Integer id);
	 
	void saveCommand(Command cmd);
	void updateCommand(Command cmd);
}

