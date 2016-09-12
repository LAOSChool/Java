package com.itpro.restws.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Constant;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.Command;


@Repository("commandDao")
@Transactional
public class CommandDaoImpl extends AbstractDao<Integer, Command> implements CommandDao {

	
	@Override
	public Command findByID(Integer id) {
		return getByKey(id);
	}

	

	@Override
	public List<Command> findUnProcessed() {

		
		@SuppressWarnings("unchecked")
		List<Command>  result = getSession().getNamedQuery("getCommandSQL").list();
		getSession().flush();
		return result;
		
	}

	@Override
	public void saveCommand(Command cmd) {
		cmd.setActflg("A");
		cmd.setCtdusr(Constant.USER_SYS);
		cmd.setCtddtm(Utils.now());
		cmd.setCtdpgm(Constant.PGM_REST);
		cmd.setCtddtm(Utils.now());
		persist(cmd);
		
	}

	@Override
	public void updateCommand(Command cmd) {
		cmd.setMdfusr(Constant.USER_SYS);
		cmd.setLstmdf(Utils.now());
		cmd.setMdfpgm(Constant.PGM_REST);
		update(cmd);
		
	}



	
	
}
