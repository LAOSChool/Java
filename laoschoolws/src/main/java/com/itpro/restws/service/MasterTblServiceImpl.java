package com.itpro.restws.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.MFeeDao;
import com.itpro.restws.dao.MGradeDao;
import com.itpro.restws.dao.MSessionDao;
import com.itpro.restws.dao.MSubjectDao;
import com.itpro.restws.model.MFee;
import com.itpro.restws.model.MGrade;
import com.itpro.restws.model.MSession;
import com.itpro.restws.model.MSubject;
import com.itpro.restws.model.MTemplate;


@Service("masterTblService")

@Transactional
public class MasterTblServiceImpl implements MasterTblService{

	
	@Autowired
	private MFeeDao mfeeDao;
	@Autowired
	private MGradeDao mgradeDao;
	@Autowired
	private MSessionDao msessionDao;
	@Autowired
	private MSubjectDao msubjectDao;
	
	

	@Override
	public MTemplate findById(String tbl_name, Integer id) {
		if (MasterTblName.TBLNAME_M_FEE.equalsName(tbl_name) ){
			return mfeeDao.findById(id).convertToTemplate();
		} 
		else if (MasterTblName.TBLNAME_M_GRADE.equalsName(tbl_name) ){
			return mgradeDao.findById(id).convertToTemplate();
		} 
		else if (MasterTblName.TBLNAME_M_SESSION.equalsName(tbl_name) ){
			return msessionDao.findById(id).convertToTemplate();
		} 
		else if (MasterTblName.TBLNAME_M_SUBJECT.equalsName(tbl_name) ){
			return msubjectDao.findById(id).convertToTemplate();
		} 
		 
		 
		 
		return null;
	}

	@Override
	public int countBySchool(String tbl_name, Integer school_id) {
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
		 
		 
		return 0;
	}



	
	@Override
	public ArrayList<MTemplate> findBySchool(String tbl_name, Integer school_id, int from_num, int max_result) {
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
	
		return null;
	}

	@Override
	public MTemplate insertMTemplate(String tbl_name, MTemplate temp) {
		if (MasterTblName.TBLNAME_M_FEE.equalsName(tbl_name) ){
			MFee mfee = new MFee();
			mfee.saveFromTemplate(temp);
			mfeeDao.saveFee(mfee);
			return mfee.convertToTemplate();
		} 
		else if (MasterTblName.TBLNAME_M_GRADE.equalsName(tbl_name) ){
			MGrade mgrade = new MGrade();
			mgrade.saveFromTemplate(temp);
			mgradeDao.saveGrade(mgrade);
			return mgrade.convertToTemplate();
		} 
		else if (MasterTblName.TBLNAME_M_SESSION.equalsName(tbl_name) ){
			MSession msession = new MSession();
			msession.saveFromTemplate(temp);
			msessionDao.saveSession(msession);
			return msession.convertToTemplate();
		} 
		else if (MasterTblName.TBLNAME_M_SUBJECT.equalsName(tbl_name) ){
			MSubject msubject = new MSubject();
			msubject.saveFromTemplate(temp);
			msubjectDao.saveSubject(msubject);
			return msubject.convertToTemplate();
		} 
		return null;
	}

	
	@Override
	public MTemplate updateMTemplate(String tbl_name, MTemplate temp) {
		if (MasterTblName.TBLNAME_M_FEE.equalsName(tbl_name) ){
			MFee mfee = mfeeDao.findById(temp.getId());
			mfee.saveFromTemplate(temp);
			mfeeDao.updateFee(mfee);
			return mfee.convertToTemplate();
		} 
		else if (MasterTblName.TBLNAME_M_GRADE.equalsName(tbl_name) ){
			MGrade mgrade = mgradeDao.findById(temp.getId());
			mgrade.saveFromTemplate(temp);
			mgradeDao.updateGrade(mgrade);
			return mgrade.convertToTemplate();
		} 
		else if (MasterTblName.TBLNAME_M_SESSION.equalsName(tbl_name) ){
			MSession msession = msessionDao.findById(temp.getId());
			msession.saveFromTemplate(temp);
			msessionDao.updateSession(msession);
			return msession.convertToTemplate();
		} 
		else if (MasterTblName.TBLNAME_M_SUBJECT.equalsName(tbl_name) ){
			MSubject msubject = msubjectDao.findById(temp.getId());
			msubject.saveFromTemplate(temp);
			msubjectDao.updateSubject(msubject);
			return msubject.convertToTemplate();
		} 
		 
		return null;
	}

}
