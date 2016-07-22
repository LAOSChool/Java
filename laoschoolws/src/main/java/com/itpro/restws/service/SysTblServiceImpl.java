package com.itpro.restws.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.SysStdMsgDao;
import com.itpro.restws.dao.SysAttMsgDao;
import com.itpro.restws.dao.SysAttReasonDao;
import com.itpro.restws.dao.SysDegreeDao;
import com.itpro.restws.dao.SysDistDao;
import com.itpro.restws.dao.SysLateReasonDao;
import com.itpro.restws.dao.SysProvinceDao;
import com.itpro.restws.dao.SysRoleDao;
import com.itpro.restws.dao.SysStsDao;
import com.itpro.restws.dao.SysWeekdayDao;
import com.itpro.restws.model.SysStdMsg;
import com.itpro.restws.model.SysAttMsg;
import com.itpro.restws.model.SysAttReason;
import com.itpro.restws.model.SysDegree;
import com.itpro.restws.model.SysDist;
import com.itpro.restws.model.SysLateReason;
import com.itpro.restws.model.SysProvince;
import com.itpro.restws.model.SysRole;
import com.itpro.restws.model.SysSts;
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
	
	@Autowired
	private SysAttReasonDao sysAttReasonDao;

	@Autowired
	private SysLateReasonDao sysLateReasonDao;
	
	@Autowired
	private SysAttMsgDao sysAttMsgDao;
	@Autowired
	private SysDistDao sysDistDao;
	@Autowired
	private SysStsDao sysStsDao;
	@Autowired
	private SysStdMsgDao sysAbsentMsgDao;
	
	
	@Override
	public ArrayList<SysTemplate> findAll(String tbl_name,int from_num, int max_result) {
		ArrayList<SysTemplate> list_ret = null;
		
		if (SysTblName.TBLNAME_SYS_DEGREE.equalsName(tbl_name) ){
			list_ret = new ArrayList<>();;
			ArrayList<SysDegree> list =  (ArrayList<SysDegree>)sysDegreeDao.findAll();
			for (int i = 0;i<list.size(); i++){
				SysDegree e = list.get(i);
				list_ret.add(e.convertToTemplate());
			}
			
		}
		else if (SysTblName.TBLNAME_SYS_PROVINCE.equalsName(tbl_name) ){
			list_ret = new ArrayList<>();;
			ArrayList<SysProvince> list =  (ArrayList<SysProvince>)sysProvinceDao.findAll();
			for (int i = 0;i<list.size(); i++){
				SysProvince e = list.get(i);
				list_ret.add(e.convertToTemplate());
			}
			
		}
		else if (SysTblName.TBLNAME_SYS_ROLE.equalsName(tbl_name) ){
			list_ret = new ArrayList<>();;
			ArrayList<SysRole> list =  (ArrayList<SysRole>)sysRoleDao.findAll();
			for (int i = 0;i<list.size(); i++){
				SysRole e = list.get(i);
				list_ret.add(e.convertToTemplate());
			}
			
		}
		else if (SysTblName.TBLNAME_SYS_WEEKDAY.equalsName(tbl_name) ){
			list_ret = new ArrayList<>();;
			ArrayList<SysWeekday> list =  (ArrayList<SysWeekday>)sysWeekdayDao.findAll();
			for (int i = 0;i<list.size(); i++){
				SysWeekday e = list.get(i);
				list_ret.add(e.convertToTemplate());
			}
			
		}
		else if (SysTblName.TBLNAME_SYS_ATT_REASON.equalsName(tbl_name) ){
			list_ret = new ArrayList<>();;
			ArrayList<SysAttReason> list =  (ArrayList<SysAttReason>)sysAttReasonDao.findAll();
			for (int i = 0;i<list.size(); i++){
				SysAttReason e = list.get(i);
				list_ret.add(e.convertToTemplate());
			}
			
		}
		else if (SysTblName.TBLNAME_SYS_LATE_REASON.equalsName(tbl_name) ){
			list_ret = new ArrayList<>();;
			ArrayList<SysLateReason> list =  (ArrayList<SysLateReason>)sysLateReasonDao.findAll();
			for (int i = 0;i<list.size(); i++){
				SysLateReason e = list.get(i);
				list_ret.add(e.convertToTemplate());
			}
			
		}
		else if (SysTblName.TBLNAME_SYS_ATT_MSG.equalsName(tbl_name) ){
			list_ret = new ArrayList<>();;
			ArrayList<SysAttMsg> list =  (ArrayList<SysAttMsg>)sysAttMsgDao.findAll();
			for (int i = 0;i<list.size(); i++){
				SysAttMsg e = list.get(i);
				list_ret.add(e.convertToTemplate());
			}
			
		}
		else if (SysTblName.TBLNAME_SYS_DIST.equalsName(tbl_name) ){
			list_ret = new ArrayList<>();;
			ArrayList<SysDist> list =  (ArrayList<SysDist>)sysDistDao.findAll();
			for (int i = 0;i<list.size(); i++){
				SysDist e = list.get(i);
				list_ret.add(e.convertToTemplate());
			}
			
		}
		else if (SysTblName.TBLNAME_SYS_STS.equalsName(tbl_name) ){
			list_ret = new ArrayList<>();;
			ArrayList<SysSts> list =  (ArrayList<SysSts>)sysStsDao.findAll();
			for (int i = 0;i<list.size(); i++){
				SysSts e = list.get(i);
				list_ret.add(e.convertToTemplate());
			}
			
		}
		else if (SysTblName.TBLNAME_SYS_STD_MSG.equalsName(tbl_name) ){
			list_ret = new ArrayList<>();;
			ArrayList<SysStdMsg> list =  (ArrayList<SysStdMsg>)sysAbsentMsgDao.findAll();
			for (int i = 0;i<list.size(); i++){
				SysStdMsg e = list.get(i);
				list_ret.add(e.convertToTemplate());
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
		if (SysTblName.TBLNAME_SYS_ATT_REASON.equalsName(tbl_name) ){
			return sysAttReasonDao.countAll();
		}
		if (SysTblName.TBLNAME_SYS_LATE_REASON.equalsName(tbl_name) ){
			return sysLateReasonDao.countAll();
		}
		if (SysTblName.TBLNAME_SYS_ATT_MSG.equalsName(tbl_name) ){
			return sysAttMsgDao.countAll();
		}
		if (SysTblName.TBLNAME_SYS_DIST.equalsName(tbl_name) ){
			return sysDistDao.countAll();
		}
		if (SysTblName.TBLNAME_SYS_STS.equalsName(tbl_name) ){
			return sysStsDao.countAll();
		}
		if (SysTblName.TBLNAME_SYS_STD_MSG.equalsName(tbl_name) ){
			return sysAbsentMsgDao.countAll();
		}
		return 0;
	}
	

}
