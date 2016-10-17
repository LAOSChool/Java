package com.itpro.restws.service;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.MClsLevelDao;
import com.itpro.restws.dao.MFeeDao;
import com.itpro.restws.dao.MGradeDao;
import com.itpro.restws.dao.MSessionDao;
import com.itpro.restws.dao.MSubjectDao;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.model.MClsLevel;
import com.itpro.restws.model.MFee;
import com.itpro.restws.model.MGrade;
import com.itpro.restws.model.MSession;
import com.itpro.restws.model.MSubject;
import com.itpro.restws.model.MTemplate;
import com.itpro.restws.model.User;


@Service("masterTblService")

@Transactional
public class MasterTblServiceImpl implements MasterTblService{
	private static final Logger logger = Logger.getLogger(MasterTblServiceImpl.class);
	
	@Autowired
	private MFeeDao mfeeDao;
	@Autowired
	private MGradeDao mgradeDao;
	@Autowired
	private MSessionDao msessionDao;
	@Autowired
	private MSubjectDao msubjectDao;
	
	@Autowired
	private MClsLevelDao mclslevelDao;
	
	

	@Override
	public MTemplate findById(String tbl_name, Integer id) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("tbl_name:"+(tbl_name==null?"null":tbl_name));
		logger.info("id:"+(id==null?"null":id.intValue()));
		
		if (MasterTblName.TBLNAME_M_FEE.equalsName(tbl_name) ){
			MFee ret = mfeeDao.findById(id);
			return ret==null?null:ret.convertToTemplate();
		} 
		else if (MasterTblName.TBLNAME_M_GRADE.equalsName(tbl_name) ){
			//return mgradeDao.findById(id).convertToTemplate();
			MGrade ret = mgradeDao.findById(id);
			return ret==null?null:ret.convertToTemplate();
			
		} 
		else if (MasterTblName.TBLNAME_M_SESSION.equalsName(tbl_name) ){
			MSession ret = msessionDao.findById(id);
			return ret==null?null:ret.convertToTemplate();
			//return msessionDao.findById(id).convertToTemplate();
		} 
		else if (MasterTblName.TBLNAME_M_SUBJECT.equalsName(tbl_name) ){
			MSubject ret = msubjectDao.findById(id);
			return ret==null?null:ret.convertToTemplate();
			//return msubjectDao.findById(id).convertToTemplate();
		} 
		else if (MasterTblName.TBLNAME_M_CLSLEVEL.equalsName(tbl_name) ){
			//return mclslevelDao.findById(id).convertToTemplate();
			MClsLevel ret = mclslevelDao.findById(id);
			return ret==null?null:ret.convertToTemplate();
		} 
		 
		 
		return null;
	}

	@Override
	public int countBySchool(String tbl_name, Integer school_id) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("tbl_name:"+(tbl_name==null?"null":tbl_name));
		logger.info("school_id:"+(school_id==null?"null":school_id.intValue()));
		
		if (MasterTblName.TBLNAME_M_FEE.equalsName(tbl_name) ){
			return mfeeDao.countBySchool(school_id);
		} 
		else if (MasterTblName.TBLNAME_M_GRADE.equalsName(tbl_name) ){
			return mgradeDao.countBySchool(school_id);
		} 
		else if (MasterTblName.TBLNAME_M_SESSION.equalsName(tbl_name) ){
			return msessionDao.countBySchool(school_id);
		} 
		else if (MasterTblName.TBLNAME_M_SUBJECT.equalsName(tbl_name) ){
			return msubjectDao.countBySchool(school_id);
		} 
		else if (MasterTblName.TBLNAME_M_CLSLEVEL.equalsName(tbl_name) ){
			return mclslevelDao.countBySchool(school_id);
		}
		 
		return 0;
	}



	
	@Override
	public ArrayList<MTemplate> findBySchool(String tbl_name, Integer school_id, int from_num, int max_result) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("tbl_name:"+(tbl_name==null?"null":tbl_name));
		logger.info("school_id:"+(school_id==null?"null":school_id.intValue()));
		
		ArrayList<MTemplate> list_ret = new ArrayList<>(); 
		if (MasterTblName.TBLNAME_M_FEE.equalsName(tbl_name) ){
			ArrayList<MFee> list =  (ArrayList<MFee>) mfeeDao.findBySchool(school_id, from_num, max_result);
			for (int i = 0;i<list.size(); i++){
				MFee e = list.get(i);
				list_ret.add(e.convertToTemplate());
			}
			return list_ret;
		} 
		else if (MasterTblName.TBLNAME_M_GRADE.equalsName(tbl_name) ){
			ArrayList<MGrade> list =  (ArrayList<MGrade>) mgradeDao.findBySchool(school_id, from_num, max_result);
			for (int i = 0;i<list.size(); i++){
				MGrade e = list.get(i);
				list_ret.add(e.convertToTemplate());
			}
			return list_ret;
		} 
		else if (MasterTblName.TBLNAME_M_SESSION.equalsName(tbl_name) ){
			ArrayList<MSession> list = (ArrayList<MSession>) msessionDao.findBySchool(school_id, from_num, max_result);
			for (int i = 0;i<list.size(); i++){
				MSession e = list.get(i);
				list_ret.add(e.convertToTemplate());
			}
			return list_ret;
		} 
		else if (MasterTblName.TBLNAME_M_SUBJECT.equalsName(tbl_name) ){
			ArrayList<MSubject> list = (ArrayList<MSubject>) msubjectDao.findBySchool(school_id, from_num, max_result);
			for (int i = 0;i<list.size(); i++){
				MSubject e = list.get(i);
				list_ret.add(e.convertToTemplate());
			}
			return list_ret;
		} 
		else if (MasterTblName.TBLNAME_M_CLSLEVEL.equalsName(tbl_name) ){
			ArrayList<MClsLevel> list = (ArrayList<MClsLevel>) mclslevelDao.findBySchool(school_id, from_num, max_result);
			for (int i = 0;i<list.size(); i++){
				MClsLevel e = list.get(i);
				list_ret.add(e.convertToTemplate());
			}
			return list_ret;
		} 
	
		return null;
	}

	@Override
	public MTemplate insertMTemplate(User me, String tbl_name, MTemplate temp) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("tbl_name:"+(tbl_name==null?"null":tbl_name));
		
		
		validMTemplate(me,tbl_name,temp);
		
		if (MasterTblName.TBLNAME_M_FEE.equalsName(tbl_name) ){
			MFee mfee = new MFee();
			mfee.saveFromTemplate(temp);
			mfeeDao.saveFee(me,mfee);
			return mfee.convertToTemplate();
		} 
		else if (MasterTblName.TBLNAME_M_GRADE.equalsName(tbl_name) ){
			MGrade mgrade = new MGrade();
			mgrade.saveFromTemplate(temp);
			mgradeDao.saveGrade(me,mgrade);
			return mgrade.convertToTemplate();
		} 
		else if (MasterTblName.TBLNAME_M_SESSION.equalsName(tbl_name) ){
			MSession msession = new MSession();
			msession.saveFromTemplate(temp);
			msessionDao.saveSession(me,msession);
			return msession.convertToTemplate();
		} 
		else if (MasterTblName.TBLNAME_M_SUBJECT.equalsName(tbl_name) ){
			MSubject msubject = new MSubject();
			msubject.saveFromTemplate(temp);
			msubjectDao.saveSubject(me,msubject);
			return msubject.convertToTemplate();
		} 
		else if (MasterTblName.TBLNAME_M_CLSLEVEL.equalsName(tbl_name) ){
			MClsLevel mclslevel = new MClsLevel();
			mclslevel.saveFromTemplate(temp);
			mclslevelDao.saveLevel(me,mclslevel);
			return mclslevel.convertToTemplate();
		} 
		return null;
	}

	
	@Override
	public MTemplate updateTranMTemplate(User me, String tbl_name, MTemplate temp) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("tbl_name:"+(tbl_name==null?"null":tbl_name));
		
		
		validMTemplate(me, tbl_name, temp);
		
		if (MasterTblName.TBLNAME_M_FEE.equalsName(tbl_name) ){
			MFee mfee = mfeeDao.findById(temp.getId());
			if (mfee == null ){
				throw new ESchoolException("table:"+tbl_name+"/id:"+temp.getId().intValue()+" is not existing", HttpStatus.BAD_REQUEST);
			}
			if (mfee.getSchool_id().intValue() != me.getSchool_id().intValue()){
				throw new ESchoolException("tableDB:"+tbl_name+"/id:"+temp.getId().intValue()+" is not in same school with user", HttpStatus.BAD_REQUEST);
			}
			
			mfee.saveFromTemplate(temp);
			mfeeDao.updateFee(me,mfee);
			return mfee.convertToTemplate();
		} 
		else if (MasterTblName.TBLNAME_M_GRADE.equalsName(tbl_name) ){
			MGrade mgrade = mgradeDao.findById(temp.getId());
			if (mgrade == null ){
				throw new ESchoolException("table:"+tbl_name+"/id:"+temp.getId().intValue()+" is not existing", HttpStatus.BAD_REQUEST);
			}
			if (mgrade.getSchool_id().intValue() != me.getSchool_id().intValue()){
				throw new ESchoolException("tableDB:"+tbl_name+"/id:"+temp.getId().intValue()+" is not in same school with user", HttpStatus.BAD_REQUEST);
			}
			
			mgrade.saveFromTemplate(temp);
			mgradeDao.updateGrade(me,mgrade);
			return mgrade.convertToTemplate();
		} 
		else if (MasterTblName.TBLNAME_M_SESSION.equalsName(tbl_name) ){
			MSession msession = msessionDao.findById(temp.getId());
			if (msession == null ){
				throw new ESchoolException("table:"+tbl_name+"/id:"+temp.getId().intValue()+" is not existing", HttpStatus.BAD_REQUEST);
			}
			if (msession.getSchool_id().intValue() != me.getSchool_id().intValue()){
				throw new ESchoolException("tableDB:"+tbl_name+"/id:"+temp.getId().intValue()+" is not in same school with user", HttpStatus.BAD_REQUEST);
			}
			
			msession.saveFromTemplate(temp);
			msessionDao.updateSession(me,msession);
			return msession.convertToTemplate();
		} 
		else if (MasterTblName.TBLNAME_M_SUBJECT.equalsName(tbl_name) ){
			MSubject msubject = msubjectDao.findById(temp.getId());
			if (msubject == null ){
				throw new ESchoolException("table:"+tbl_name+"/id:"+temp.getId().intValue()+" is not existing", HttpStatus.BAD_REQUEST);
			}
			if (msubject.getSchool_id().intValue() != me.getSchool_id().intValue()){
				throw new ESchoolException("tableDB:"+tbl_name+"/id:"+temp.getId().intValue()+" is not in same school with user", HttpStatus.BAD_REQUEST);
			}			
			msubject.saveFromTemplate(temp);
			msubjectDao.updateSubject(me,msubject);
			return msubject.convertToTemplate();
		} 
		else if (MasterTblName.TBLNAME_M_CLSLEVEL.equalsName(tbl_name) ){
			MClsLevel mclslevel = mclslevelDao.findById(temp.getId());
			if (mclslevel == null ){
				throw new ESchoolException("table:"+tbl_name+"/id:"+temp.getId().intValue()+" is not existing", HttpStatus.BAD_REQUEST);
			}
			if (mclslevel.getSchool_id().intValue() != me.getSchool_id().intValue()){
				throw new ESchoolException("tableDB:"+tbl_name+"/id:"+temp.getId().intValue()+" is not in same school with user", HttpStatus.BAD_REQUEST);
			}				
			mclslevel.saveFromTemplate(temp);
			mclslevelDao.updateLevel(me,mclslevel);
			return mclslevel.convertToTemplate();
		} 
		return null;
	}

	@Override
	public void deleteMTemplate(User me, String tbl_name, Integer id) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("tbl_name:"+(tbl_name==null?"null":tbl_name));
		logger.info("id:"+(id==null?"null":id.intValue()));
		
		MTemplate temp = findById(tbl_name, id);
		if (temp == null ){
			throw new ESchoolException("id is not existing: "+id.intValue(), HttpStatus.BAD_REQUEST);
		}
		if (temp.getSchool_id().intValue() != me.getSchool_id().intValue() ){
			throw new ESchoolException("tbl_name:"+tbl_name+"/id:"+id.intValue()+" is not in same school_id with user", HttpStatus.BAD_REQUEST);
		}
			
		
		if (MasterTblName.TBLNAME_M_FEE.equalsName(tbl_name) ){
			MFee mfee = mfeeDao.findById(id);
			
			//mfeeDao.delFee(mfee);
			mfee.setActflg("D");
			mfeeDao.updateFee(me,mfee);
		} 
		else if (MasterTblName.TBLNAME_M_GRADE.equalsName(tbl_name) ){
			MGrade mgrade = mgradeDao.findById(id);
			//mgradeDao.delGrade(mgrade);
			mgrade.setActflg("D");
			mgradeDao.updateGrade(me,mgrade);
		} 
		else if (MasterTblName.TBLNAME_M_SESSION.equalsName(tbl_name) ){
			MSession msession = msessionDao.findById(id);
			//msessionDao.delSession(msession);
			msession.setActflg("D");
			msessionDao.updateSession(me,msession);
		} 
		else if (MasterTblName.TBLNAME_M_SUBJECT.equalsName(tbl_name) ){
			MSubject msubject = msubjectDao.findById(id);
			//msubjectDao.delSubject(msubject);
			msubject.setActflg("D");
			msubjectDao.updateSubject(me,msubject);
		} 
		else if (MasterTblName.TBLNAME_M_CLSLEVEL.equalsName(tbl_name) ){
			MClsLevel mclslevel = mclslevelDao.findById(id);
			//mclslevelDao.updateLevel(mclslevel);
			mclslevel.setActflg("D");
			mclslevelDao.updateLevel(me,mclslevel);
		} 
	}

	@Override
	public void validMTemplate(User user, String tbl_name, MTemplate template) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("tbl_name:"+(tbl_name==null?"null":tbl_name));
		
		
		// update school_id
		template.setSchool_id(user.getSchool_id());
		
		
		if (template.getId() != null && template.getId().intValue() >0){
//			MTemplate tmpDB = findById(tbl_name, template.getId());
//			if (tmpDB == null ){
//				throw new ESchoolException("table:"+tbl_name+"/id:"+template.getId().intValue()+" is not existing", HttpStatus.BAD_REQUEST);
//			}
//			if (tmpDB.getSchool_id().intValue() != user.getSchool_id().intValue()){
//				throw new ESchoolException("tableDB:"+tbl_name+"/id:"+template.getId().intValue()+" is not in same school with user", HttpStatus.BAD_REQUEST);
//			}
			
		}
		
		if (template.getSchool_id().intValue() != user.getSchool_id().intValue()){
			throw new ESchoolException("table:"+tbl_name+"/id:"+template.getId().intValue()+" is not in same school with user", HttpStatus.BAD_REQUEST);
		}
		if (template.getSval() == null || template.getSval().trim().length() <= 0){
			throw new ESchoolException("table:"+tbl_name+"/sval is required", HttpStatus.BAD_REQUEST);
		}

		if (MasterTblName.TBLNAME_M_FEE.equalsName(tbl_name) ){
			
		} 
		else if (MasterTblName.TBLNAME_M_GRADE.equalsName(tbl_name) ){
			
		} 
		else if (MasterTblName.TBLNAME_M_SESSION.equalsName(tbl_name) ){
			if (template.getFval1() == null || template.getFval1().floatValue() <= 0){
				throw new ESchoolException("table:"+tbl_name+"/fval1 is required (1:AM, 2:PM, 3: Evening)", HttpStatus.BAD_REQUEST);
			}
			if (template.getFval2() == null || template.getFval2().floatValue() <= 0){
				throw new ESchoolException("table:"+tbl_name+"/fval2 is required (Order of sessions)", HttpStatus.BAD_REQUEST);
			}
//			if (template.getNotice() == null || template.getNotice().trim().length() <= 0){
//				throw new ESchoolException("table:"+tbl_name+"/notice is required (duration of sessions)", HttpStatus.BAD_REQUEST);
//			}
			
		} 
		else if (MasterTblName.TBLNAME_M_SUBJECT.equalsName(tbl_name) ){
			
		} 
		else if (MasterTblName.TBLNAME_M_CLSLEVEL.equalsName(tbl_name) ){
			
		} 
		
	}

	
}
