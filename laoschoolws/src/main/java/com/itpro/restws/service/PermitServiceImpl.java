package com.itpro.restws.service;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.PermitDao;
import com.itpro.restws.model.Permit;
import com.itpro.restws.model.User;

@Service("permissionService")
@Transactional
public class PermitServiceImpl implements PermitService{
	private static final Logger logger = Logger.getLogger(PermitServiceImpl.class);
	
	@Autowired
	private PermitDao permitDao;

	@Override
	public ArrayList<Permit> loadPermit(User user) {
		ArrayList<Permit> ret = new ArrayList<>();
		String roles = user.getRoles();
		int school_id = user.getSchool_id();
		if (school_id > 0 && roles != null && !roles.equals("")){
			for (String role: roles.split(",")){
				ArrayList<Permit>  list = (ArrayList<Permit>) permitDao.findByRole(role, school_id);
				ret.addAll(list);
			}
		}else{
			
			logger.error("cannot get Roles of user: "+user.getId());
			logger.error("cannot get Roles of school_id="+user.getSchool_id());
			logger.error("cannot get Roles of roles="+user.getRoles()==null?"":user.getRoles());
			ret = null;
		}
		user.setPermisions(ret);
		return ret;
	}

}
