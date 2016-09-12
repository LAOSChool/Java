package com.itpro.restws.dao;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Constant;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.ApiKey;
import com.itpro.restws.model.NotifyImg;
import com.itpro.restws.model.User;

@Repository("notifyImgDao")
@Transactional
public class NotifyImgDaoImpl extends AbstractDao<Integer, NotifyImg> implements NotifyImgDao {

	@Override
	public void saveImg(User me,NotifyImg img) {
	
		
		img.setActflg("A");
		img.setCtdusr(Constant.USER_SYS);
		img.setCtddtm(Utils.now());
		img.setCtdpgm(Constant.PGM_REST);
		
		if (me != null ){
			img.setCtdusr(me.getSso_id());	
			ApiKey apiKey = me.getApi_key();
			String device = null;
			if (apiKey != null && apiKey.getApi_key() != null ){
				device = apiKey.getApi_key();
			}
			if (device != null ){
				img.setCtdwks(device);
			}
			
		}
		
		
		save(img);
		
	}



	@Override
	public void updateImg(User me,NotifyImg img) {

		
		img.setMdfusr(Constant.USER_SYS);
		img.setLstmdf(Utils.now());
		img.setMdfpgm(Constant.PGM_REST);
		
		if (me != null ){
			img.setMdfusr(me.getSso_id());
			ApiKey apiKey = me.getApi_key();
			String device = null;
			if (apiKey != null && apiKey.getApi_key() != null ){
				device = apiKey.getApi_key();
			}
			if (device != null ){
				img.setMdfwks(device);
			}
		}
		
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
