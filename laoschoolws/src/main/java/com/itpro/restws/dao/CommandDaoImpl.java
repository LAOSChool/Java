package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.Command;


@Repository("commandDao")
@Transactional
public class CommandDaoImpl extends AbstractDao<Integer, Command> implements CommandDao {

	
	@Override
	public Command findByID(Integer id) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("id", id.intValue()));
		
	     Command cmd = (Command) crit_list.uniqueResult();
	     
		return  cmd;
	}

	

	@Override
	public List<Command> findUnProcessed() {
//		Criteria crit_list = createEntityCriteria();
//		crit_list.add(Restrictions.eq("processed", 0));
//	     @SuppressWarnings("unchecked")
//		List<Command> keylist = crit_list.list();
//	     
//		return  keylist;
		
		
//		Query query = session.createSQLQuery(
//				"CALL GetStocks(:stockCode)")
//				.addEntity(Stock.class)
//				.setParameter("stockCode", "7277");
//						
//			List result = query.list();
//			for(int i=0; i<result.size(); i++){
//				Stock stock = (Stock)result.get(i);
//				System.out.println(stock.getStockCode());
//			}
		
		Query query = getSession().createQuery("CALL get_unproc_command()");
		@SuppressWarnings("unchecked")
		List<Command>  result = query.list();
		return result;
		
	}

	@Override
	public void saveCommand(Command cmd) {
		cmd.setActflg("A");
		cmd.setCtdusr("HuyNQ-test");
		cmd.setCtddtm(Utils.now());
		cmd.setCtdpgm("RestWS");
		cmd.setCtddtm(Utils.now());
		persist(cmd);
		
	}

	@Override
	public void updateCommand(Command cmd) {
		cmd.setMdfusr("HuyNQ-test");
		cmd.setLstmdf(Utils.now());
		cmd.setMdfpgm("RestWS");
		update(cmd);
		
	}



	
	
}
