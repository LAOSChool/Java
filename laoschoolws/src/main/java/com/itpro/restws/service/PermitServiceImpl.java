package com.itpro.restws.service;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.PermitDao;
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

	

//	@Override
//	public List<Permit> loadPermit(User user, E_ENTITY entity, E_SCOPE scope) {
//		ArrayList<Permit> ret = new ArrayList<>();
//		String roles = user.getRoles();
//		int school_id = user.getSchool_id();
//		if (school_id > 0 && roles != null && !roles.equals("")){
//			for (String role: roles.split(",")){
//				ArrayList<Permit>  list = (ArrayList<Permit>) permitDao.findPermit(role, school_id, entity.getValue(),scope.getValue());
//				ret.addAll(list);
//			}
//		}else{
//			logger.error("cannot get Roles of user: "+user.getId());
//			logger.error("cannot get Roles of school_id="+user.getSchool_id());
//			logger.error("cannot get Roles of roles="+user.getRoles()==null?"":user.getRoles());
//			ret = null;
//		}
//		user.setPermisions(ret);
//		return ret;
//	}
//	@Override
//	public List<Permit> loadPermit(User user, E_ENTITY entity, E_SCOPE scope, E_RIGHT right) {
//		ArrayList<Permit> ret = new ArrayList<>();
//		String roles = user.getRoles();
//		int school_id = user.getSchool_id();
//		if (school_id > 0 && roles != null && !roles.equals("")){
//			for (String role: roles.split(",")){
//				ArrayList<Permit>  list = (ArrayList<Permit>) permitDao.findPermit(role, school_id, entity.getValue(),scope.getValue(),right.getValue());
//				ret.addAll(list);
//			}
//		}else{
//			logger.error("cannot get Roles of user: "+user.getId());
//			logger.error("cannot get Roles of school_id="+user.getSchool_id());
//			logger.error("cannot get Roles of roles="+user.getRoles()==null?"":user.getRoles());
//			ret = null;
//		}
//		user.setPermisions(ret);
//		return ret;
//	}
//	@Override
//	public void checkPermit(User user, E_ENTITY entity, E_SCOPE scope, E_RIGHT right) {
//		List<Permit>  list = loadPermit( user,  entity,  scope,  right) ;
//		if (list == null ){
//			throw new ESchoolException("Username="+user==null?"":user.getSso_id()+ ", role="+user.getRoles()+" cannot access entity:"+entity.toString(), HttpStatus.UNAUTHORIZED);
//		}else{
//			logger.info("checkPermit success, user="+user.getSso_id()+"role="+user.getRoles()+" have permition to access entity ");
//			for (Permit permit :list){
//				permit.toString();
//			}
//		}
//	}
//	
//	@Override
//	public
//	E_SCOPE getDataScope(String roles){
//		E_SCOPE ret=null;
//		ArrayList<E_SCOPE> list = new ArrayList<>();
//		if (roles != null){
//			for (String role : roles.split(",")){
//				if (role.equalsIgnoreCase(E_ROLE.ADMIN.getRole())){
//					list.add(E_SCOPE.SCHOOL);
//				}
//				if (role.equalsIgnoreCase(E_ROLE.TEACHER.getRole())){
//					list.add(E_SCOPE.CLASS);
//				}
//				if (role.equalsIgnoreCase(E_ROLE.STUDENT.getRole())){
//					list.add(E_SCOPE.PERSON);
//				}
//			}
//			// Get max value
//			if (list.size() > 0 ){
//				E_SCOPE max = list.get(0);
//				for (E_SCOPE e : list){
//					if (e.getValue() > max.getValue()){
//						max = e;
//					}
//				}
//				ret = max;
//			}
//		}
//		return ret;
//	}

}
