package com.itpro.restws.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.SysAttMsgDao;
import com.itpro.restws.dao.SysDegreeDao;
import com.itpro.restws.dao.SysDistDao;
import com.itpro.restws.dao.SysLateReasonDao;
import com.itpro.restws.dao.SysMsgSampDao;
import com.itpro.restws.dao.SysProvinceDao;
import com.itpro.restws.dao.SysRoleDao;
import com.itpro.restws.dao.SysSettingsDao;
import com.itpro.restws.dao.SysStdMsgDao;
import com.itpro.restws.dao.SysStsDao;
import com.itpro.restws.dao.SysWeekdayDao;
import com.itpro.restws.model.SysAttMsg;
import com.itpro.restws.model.SysDegree;
import com.itpro.restws.model.SysDist;
import com.itpro.restws.model.SysLateReason;
import com.itpro.restws.model.SysMsgSamp;
import com.itpro.restws.model.SysProvince;
import com.itpro.restws.model.SysRole;
import com.itpro.restws.model.SysSettings;
import com.itpro.restws.model.SysStdMsg;
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
	private SysLateReasonDao sysLateReasonDao;
	
	@Autowired
	private SysAttMsgDao sysAttMsgDao;
	@Autowired
	private SysDistDao sysDistDao;
	@Autowired
	private SysStsDao sysStsDao;
	@Autowired
	private SysStdMsgDao sysAbsentMsgDao;
	
	@Autowired
	private SysMsgSampDao sysMsgSampDao;
	@Autowired
	private SysSettingsDao sysSettingsDao;
	
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
		else if (SysTblName.TBLNAME_SYS_MSG_SAMP.equalsName(tbl_name) ){
			list_ret = new ArrayList<>();;
			ArrayList<SysMsgSamp> list =  (ArrayList<SysMsgSamp>)sysMsgSampDao.findAll();
			for (int i = 0;i<list.size(); i++){
				SysMsgSamp e = list.get(i);
				list_ret.add(e.convertToTemplate());
			}
			
		}
		else if (SysTblName.TBLNAME_SYS_SETTINGS.equalsName(tbl_name) ){
			list_ret = new ArrayList<>();;
			ArrayList<SysSettings> list =  (ArrayList<SysSettings>)sysSettingsDao.findAll();
			for (int i = 0;i<list.size(); i++){
				SysSettings e = list.get(i);
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
		if (SysTblName.TBLNAME_SYS_MSG_SAMP.equalsName(tbl_name) ){
			return sysMsgSampDao.countAll();
		}
		if (SysTblName.TBLNAME_SYS_SETTINGS.equalsName(tbl_name) ){
			return sysSettingsDao.countAll();
		}
		return 0;
	}

	@Override
	public SysTemplate findByID(String tbl_name, Integer id) {
		if (SysTblName.TBLNAME_SYS_DEGREE.equalsName(tbl_name) ){
			SysDegree e = sysDegreeDao.findById(id);
			return e.convertToTemplate();
		}
		else if (SysTblName.TBLNAME_SYS_PROVINCE.equalsName(tbl_name) ){
			SysProvince e = sysProvinceDao.findById(id);
			return e.convertToTemplate();
		}
		else if (SysTblName.TBLNAME_SYS_ROLE.equalsName(tbl_name) ){
			SysRole e = sysRoleDao.findById(id);
			return e.convertToTemplate();
		}
		else if (SysTblName.TBLNAME_SYS_WEEKDAY.equalsName(tbl_name) ){
			SysWeekday e = sysWeekdayDao.findById(id);
			return e.convertToTemplate();
			
		}
		
		else if (SysTblName.TBLNAME_SYS_LATE_REASON.equalsName(tbl_name) ){
			SysLateReason e = sysLateReasonDao.findById(id);
			return e.convertToTemplate();
		}
		else if (SysTblName.TBLNAME_SYS_ATT_MSG.equalsName(tbl_name) ){
			SysAttMsg e = sysAttMsgDao.findById(id);
			return e.convertToTemplate();
		}
		else if (SysTblName.TBLNAME_SYS_DIST.equalsName(tbl_name) ){
			SysDist e = sysDistDao.findById(id);
			return e.convertToTemplate();
			
		}
		else if (SysTblName.TBLNAME_SYS_STS.equalsName(tbl_name) ){
			SysSts e = sysStsDao.findById(id);
			return e.convertToTemplate();
			
		}
		else if (SysTblName.TBLNAME_SYS_STD_MSG.equalsName(tbl_name) ){
			SysStdMsg e = sysAbsentMsgDao.findById(id);
			return e.convertToTemplate();
		
		
			
		}
		else if (SysTblName.TBLNAME_SYS_MSG_SAMP.equalsName(tbl_name) ){
			SysMsgSamp e = sysMsgSampDao.findById(id);
			return e.convertToTemplate();
		
		}
		else if (SysTblName.TBLNAME_SYS_SETTINGS.equalsName(tbl_name) ){
			SysSettings e = sysSettingsDao.findById(id);
			return e.convertToTemplate();
			
		}
		return null;
	}

	@Override
	public SysTemplate findBySval(String tbl_name, String sval) {
		if (SysTblName.TBLNAME_SYS_DEGREE.equalsName(tbl_name) ){
			ArrayList<SysDegree> elist = (ArrayList<SysDegree>) sysDegreeDao.findAll();
			for (SysDegree e:elist){
				if (e.getSval() != null && e.getSval().equalsIgnoreCase(sval)){
					return e.convertToTemplate();
				}
			}
		}
		else if (SysTblName.TBLNAME_SYS_PROVINCE.equalsName(tbl_name) ){
			
			ArrayList<SysProvince> elist = (ArrayList<SysProvince>) sysProvinceDao.findAll();
			for (SysProvince e:elist){
				if (e.getSval() != null && e.getSval().equalsIgnoreCase(sval)){
					return e.convertToTemplate();
				}
			}
			
			
		}
		else if (SysTblName.TBLNAME_SYS_ROLE.equalsName(tbl_name) ){
			
			ArrayList<SysRole> elist = (ArrayList<SysRole>) sysRoleDao.findAll();
			for (SysRole e:elist){
				if (e.getSval() != null && e.getSval().equalsIgnoreCase(sval)){
					return e.convertToTemplate();
				}
			}

			
			
		}
		else if (SysTblName.TBLNAME_SYS_WEEKDAY.equalsName(tbl_name) ){
			ArrayList<SysWeekday> elist = (ArrayList<SysWeekday>) sysWeekdayDao.findAll();
			for (SysWeekday e:elist){
				if (e.getSval() != null && e.getSval().equalsIgnoreCase(sval)){
					return e.convertToTemplate();
				}
			}

			
			
		}
		
		else if (SysTblName.TBLNAME_SYS_LATE_REASON.equalsName(tbl_name) ){
			ArrayList<SysLateReason> elist = (ArrayList<SysLateReason>) sysLateReasonDao.findAll();
			for (SysLateReason e:elist){
				if (e.getSval() != null && e.getSval().equalsIgnoreCase(sval)){
					return e.convertToTemplate();
				}
			}

		}
		else if (SysTblName.TBLNAME_SYS_ATT_MSG.equalsName(tbl_name) ){
			ArrayList<SysAttMsg> elist = (ArrayList<SysAttMsg>) sysAttMsgDao.findAll();
			for (SysAttMsg e:elist){
				if (e.getSval() != null && e.getSval().equalsIgnoreCase(sval)){
					return e.convertToTemplate();
				}
			}
			
			
		}
		else if (SysTblName.TBLNAME_SYS_DIST.equalsName(tbl_name) ){
			
			
			ArrayList<SysDist> elist = (ArrayList<SysDist>) sysDistDao.findAll();
			for (SysDist e:elist){
				if (e.getSval() != null && e.getSval().equalsIgnoreCase(sval)){
					return e.convertToTemplate();
				}
			}
			

			
			
		}
		else if (SysTblName.TBLNAME_SYS_STS.equalsName(tbl_name) ){
			
			ArrayList<SysSts> elist = (ArrayList<SysSts>) sysStsDao.findAll();
			for (SysSts e:elist){
				if (e.getSval() != null && e.getSval().equalsIgnoreCase(sval)){
					return e.convertToTemplate();
				}
			}
						
			
		}
		else if (SysTblName.TBLNAME_SYS_STD_MSG.equalsName(tbl_name) ){
			
			ArrayList<SysStdMsg> elist = (ArrayList<SysStdMsg>) sysAbsentMsgDao.findAll();
			for (SysStdMsg e:elist){
				if (e.getSval() != null && e.getSval().equalsIgnoreCase(sval)){
					return e.convertToTemplate();
				}
			}

		
		
			
		}
		else if (SysTblName.TBLNAME_SYS_MSG_SAMP.equalsName(tbl_name) ){
			
			
			ArrayList<SysMsgSamp> elist = (ArrayList<SysMsgSamp>) sysMsgSampDao.findAll();
			for (SysMsgSamp e:elist){
				if (e.getSval() != null && e.getSval().equalsIgnoreCase(sval)){
					return e.convertToTemplate();
				}
			}

			
			
		
		}
		else if (SysTblName.TBLNAME_SYS_SETTINGS.equalsName(tbl_name) ){
			
			ArrayList<SysSettings> elist = (ArrayList<SysSettings>) sysSettingsDao.findAll();
			for (SysSettings e:elist){
				if (e.getSval() != null && e.getSval().equalsIgnoreCase(sval)){
					return e.convertToTemplate();
				}
			}
			
		}
		return null;
	}
	

}
