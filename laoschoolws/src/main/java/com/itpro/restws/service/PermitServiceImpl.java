package com.itpro.restws.service;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.PermitDao;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.E_ENTITY;
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
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		ArrayList<Permit> ret = new ArrayList<>();
		String roles = user.getRoles();
		Integer school_id = user.getSchool_id();
		if (school_id > 0 && roles != null && !roles.equals("")){
			for (String role: roles.split(",")){
				ArrayList<Permit>  list = (ArrayList<Permit>) permitDao.findPermit(role, school_id);
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

	@Override
	public Permit loadEntityPermit(User user, E_ENTITY entity) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		ArrayList<Permit>  list = new ArrayList<>();
		String roles = user.getRoles();
		Integer school_id = user.getSchool_id();
		if (school_id > 0 && roles != null && !roles.equals("")){
			for (String role: roles.split(",")){
				list.addAll((ArrayList<Permit>) permitDao.findPermit(role, school_id, entity.getValue()));
			}
		}else{
			logger.error("cannot get Roles of user: "+user.getId());
			logger.error("cannot get Roles of school_id="+user.getSchool_id());
			logger.error("cannot get Roles of roles="+user.getRoles()==null?"":user.getRoles());
		}
		// Get max permit 
		if (list.size() > 0){
			Permit ret = list.get(0); 
			for (Permit permit : list){
				if (ret.getRights() < permit.getRights()){
					ret= permit;
				}
			}
			return ret;
		}
		return null;
	}

	@Override
	public void checkPermit(User user, HttpServletRequest request) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		
		// Get entity
		E_ENTITY reqest_entity = get_entity(request.getServletPath());
		// Load permit from DB
		Permit permit_db = loadEntityPermit(user, reqest_entity);
		if (permit_db== null ){
			throw new ESchoolException("No access permission", HttpStatus.FORBIDDEN);
		}
		permit_db.getSchool_id();
		permit_db.getScope();
		permit_db.getRights();
		permit_db.getRoles();
		// permit_db with access data
		Integer school_id = user.getSchool_id();
		int scope = getScopeFromRequest(request);
		int right = getRightsFromRequest(request);
		//String roles = user.getRoles();
		// Compare
		//Permit request_permit = new Permit(school_id,entity.getValue(),right,roles,scope);
		if (school_id.intValue() ==  permit_db.getSchool_id().intValue()){
			if ((scope & permit_db.getScope()) == scope){
				if ((right & permit_db.getRights()) == right){
					if (user.hasRole(permit_db.getRoles())){
						return;
					}
				}
			}
		}
		
		throw new ESchoolException("No access permission", HttpStatus.FORBIDDEN);
	}

	private int getRightsFromRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return 0;
	}

	private int getScopeFromRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return 0;
	}

	private E_ENTITY get_entity(String servletPath) {
		// TODO Auto-generated method stub
		return null;
	}





}
