package com.itpro.restws.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.NotifyImg;
import com.itpro.restws.model.User;

@Repository("notifyImgDao")
@Transactional
public class NotifyImgDaoImpl extends AbstractDao<Integer, NotifyImg> implements NotifyImgDao {

	@Override
	public void saveImg(NotifyImg img) {
		img.setActflg("A");
		img.setCtdusr("HuyNQ-test");
		img.setCtddtm(Utils.now());
		img.setCtdpgm("RestWS");
		img.setCtddtm(Utils.now());
		save(img);
		
	}



	@Override
	public void updateImg(NotifyImg img) {
		img.setMdfusr("HuyNQ-test");
		img.setLstmdf(Utils.now());
		img.setMdfpgm("RestWS");
		update(img);
		
	}



	@Override
	public NotifyImg findById(Integer id) {
		
		return  getByKey(id.intValue());
	}



	@Override
	public ArrayList<NotifyImg> findByTaskId(Integer task_id) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("task_id", task_id));
		//crit_list.add(Restrictions.ne("is_sent", 99));
		@SuppressWarnings("unchecked")
		ArrayList<NotifyImg> list = (ArrayList<NotifyImg>) crit_list.list();
	     
		return  list;
	}

	

	
}
