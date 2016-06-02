package com.itpro.restws.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
		
		
		@SuppressWarnings("unchecked")
		List<Command>  result = getSession().getNamedQuery("getCommandSQL").list();
		getSession().flush();
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
