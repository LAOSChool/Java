package com.itpro.restws.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.SysDegreeDao;
import com.itpro.restws.dao.SysProvinceDao;
import com.itpro.restws.dao.SysRoleDao;
import com.itpro.restws.dao.SysWeekdayDao;
import com.itpro.restws.model.SysDegree;
import com.itpro.restws.model.SysProvince;
import com.itpro.restws.model.SysRole;
import com.itpro.restws.model.SysTemplate;
import com.itpro.restws.model.SysWeekday;
@Service("sysTblService")

@Transactional
public class SysTblServiceImpl implements SysTblService{

	
	@Autowired
	private SysDegreeDao sysDegreeDao;
	@Autowired
	private SysProvinceDao sysProvinceDao;
	@Autowired
	private SysRoleDao sysRoleDao;
	@Autowired
	private SysWeekdayDao sysWeekdayDao;
	

	@Override
	public ArrayList<SysTemplate> findAll(String tbl_name,int from_num, int max_result) {
		ArrayList<SysTemplate> list_ret = null;
		
		if (SysTblName.TBLNAME_SYS_DEGREE.equalsName(tbl_name) ){
			list_ret = new ArrayList<>();;
			ArrayList<SysDegree> list =  (ArrayList<SysDegree>)sysDegreeDao.findAll();
			for (int i = 0;i<list.size(); i++){
				SysDegree e = list.get(i);
				list_ret.add(e.convert2ToTemplate());
			}
			
		}
		else if (SysTblName.TBLNAME_SYS_PROVINCE.equalsName(tbl_name) ){
			list_ret = new ArrayList<>();;
			ArrayList<SysProvince> list =  (ArrayList<SysProvince>)sysProvinceDao.findAll();
			for (int i = 0;i<list.size(); i++){
				SysProvince e = list.get(i);
				list_ret.add(e.convert2ToTemplate());
			}
			
		}
		else if (SysTblName.TBLNAME_SYS_ROLE.equalsName(tbl_name) ){
			list_ret = new ArrayList<>();;
			ArrayList<SysRole> list =  (ArrayList<SysRole>)sysRoleDao.findAll();
			for (int i = 0;i<list.size(); i++){
				SysRole e = list.get(i);
				list_ret.add(e.convert2ToTemplate());
			}
			
		}
		else if (SysTblName.TBLNAME_SYS_WEEKDAY.equalsName(tbl_name) ){
			list_ret = new ArrayList<>();;
			ArrayList<SysWeekday> list =  (ArrayList<SysWeekday>)sysWeekdayDao.findAll();
			for (int i = 0;i<list.size(); i++){
				SysWeekday e = list.get(i);
				list_ret.add(e.convert2ToTemplate());
			}
			
		}
		
		
		return list_ret;
	}

	@Override
	public int  countAll(String tbl_name) {
		if (SysTblName.TBLNAME_SYS_DEGREE.equalsName(tbl_name) ){
			return sysDegreeDao.countAll();
		}
		if (SysTblName.TBLNAME_SYS_PROVINCE.equalsName(tbl_name) ){
			return sysProvinceDao.countAll();
		}
		if (SysTblName.TBLNAME_SYS_ROLE.equalsName(tbl_name) ){
			return sysRoleDao.countAll();
		}
		if (SysTblName.TBLNAME_SYS_WEEKDAY.equalsName(tbl_name) ){
			return sysWeekdayDao.countAll();
		}
		
		return 0;
	}
	

}
