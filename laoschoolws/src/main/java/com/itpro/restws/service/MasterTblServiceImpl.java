package com.itpro.restws.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.MExamDao;
import com.itpro.restws.dao.MFeeDao;
import com.itpro.restws.dao.MGradeDao;
import com.itpro.restws.dao.MSessionDao;
import com.itpro.restws.dao.MSubjectDao;
import com.itpro.restws.dao.MTermDao;
import com.itpro.restws.dao.MUser2ClassDao;
import com.itpro.restws.model.MExam;
import com.itpro.restws.model.MFee;
import com.itpro.restws.model.MGrade;
import com.itpro.restws.model.MSession;
import com.itpro.restws.model.MSubject;
import com.itpro.restws.model.MTemplate;
import com.itpro.restws.model.MTerm;
import com.itpro.restws.model.MUser2Class;
@Service("masterTblService")

@Transactional
public class MasterTblServiceImpl implements MasterTblService{

	
	@Autowired
	private MExamDao mexamDao;
	@Autowired
	private MFeeDao mfeeDao;
	@Autowired
	private MGradeDao mgradeDao;
	@Autowired
	private MSessionDao msessionDao;
	@Autowired
	private MSubjectDao msubjectDao;
	@Autowired
	private MTermDao mtermDao;
	@Autowired
	private MUser2ClassDao muser2classDao;
	

	@Override
	public MTemplate findById(String tbl_name, int id) {
		if (MasterTblName.TBLNAME_M_EXAM.equalsName(tbl_name) ){
			return mexamDao.findById(id).convertToTemp();
		}
		else if (MasterTblName.TBLNAME_M_FEE.equalsName(tbl_name) ){
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
		else if (MasterTblName.TBLNAME_M_TERM.equalsName(tbl_name) ){
			return mtermDao.findById(id).convertToTemplate();
		} 
		else if (MasterTblName.TBLNAME_M_USER2CLASS.equalsName(tbl_name) ){
			return muser2classDao.findById(id).convertToTemplate();
		} 
		 
		return null;
	}

	@Override
	public int countBySchool(String tbl_name, int school_id) {
		if (MasterTblName.TBLNAME_M_EXAM.equalsName(tbl_name) ){
			return mexamDao.countBySchool(school_id);
		}
		else if (MasterTblName.TBLNAME_M_FEE.equalsName(tbl_name) ){
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
		else if (MasterTblName.TBLNAME_M_TERM.equalsName(tbl_name) ){
			return mtermDao.countBySchool(school_id);
		} 
		else if (MasterTblName.TBLNAME_M_USER2CLASS.equalsName(tbl_name) ){
			return muser2classDao.countBySchool(school_id);
		} 
		return 0;
	}


//	@Override
//	public ArrayList<? extends MasterBase> findBySchool(String tbl_name, int school_id, int from_num, int max_result) {
//		if (MasterTblName.TBLNAME_M_EXAM.equals(tbl_name) ){
//			return (ArrayList<MExam>) mexamDao.findBySchool(school_id, from_num, max_result);
//		}
//		else if (MasterTblName.TBLNAME_M_FEE.equals(tbl_name) ){
//			return (ArrayList<MFee>) mfeeDao.findBySchool(school_id, from_num, max_result);
//		} 
//		else if (MasterTblName.TBLNAME_M_GRADE.equals(tbl_name) ){
//			return (ArrayList<MGrade>) mgradeDao.findBySchool(school_id, from_num, max_result);
//		} 
//		else if (MasterTblName.TBLNAME_M_SESSION.equals(tbl_name) ){
//			return (ArrayList<MSession>) msessionDao.findBySchool(school_id, from_num, max_result);
//		} 
//		else if (MasterTblName.TBLNAME_M_SUBJECT.equals(tbl_name) ){
//			return (ArrayList<MSubject>) msubjectDao.findBySchool(school_id, from_num, max_result);
//		} 
//		else if (MasterTblName.TBLNAME_M_TERM.equals(tbl_name) ){
//			return (ArrayList<MTerm>) mtermDao.findBySchool(school_id, from_num, max_result);
//		} 
//		else if (MasterTblName.TBLNAME_M_USER2CLASS.equals(tbl_name) ){
//			return (ArrayList<MUser2Class>) muser2classDao.findBySchool(school_id, from_num, max_result);
//		} 
//		return null;
//	}
	
	@Override
	public ArrayList<MTemplate> findBySchool(String tbl_name, int school_id, int from_num, int max_result) {
		ArrayList<MTemplate> list_ret = new ArrayList<>(); 
		if (MasterTblName.TBLNAME_M_EXAM.equalsName(tbl_name) ){
			ArrayList<MExam> list =  (ArrayList<MExam>) mexamDao.findBySchool(school_id, from_num, max_result);
			for (int i = 0;i<list.size(); i++){
				MExam e = list.get(i);
				list_ret.add(e.convertToTemp());
			}
			return list_ret;
		}
		else if (MasterTblName.TBLNAME_M_FEE.equalsName(tbl_name) ){
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
		else if (MasterTblName.TBLNAME_M_TERM.equalsName(tbl_name) ){
			ArrayList<MTerm> list = (ArrayList<MTerm>) mtermDao.findBySchool(school_id, from_num, max_result);
			for (int i = 0;i<list.size(); i++){
				MTerm e = list.get(i);
				list_ret.add(e.convertToTemplate());
			}
			return list_ret;
		} 
		else if (MasterTblName.TBLNAME_M_USER2CLASS.equalsName(tbl_name) ){
			ArrayList<MUser2Class> list = (ArrayList<MUser2Class>) muser2classDao.findBySchool(school_id, from_num, max_result);
			for (int i = 0;i<list.size(); i++){
				MUser2Class e = list.get(i);
				list_ret.add(e.convertToTemplate());
			}
			return list_ret;
		} 
		
		return null;
	}

	@Override
	public MTemplate insertMTemplate(String tbl_name, MTemplate temp) {
		if (MasterTblName.TBLNAME_M_EXAM.equalsName(tbl_name) ){
			MExam mexam = new MExam();
			mexam.saveFromTemp(temp);
			mexamDao.saveExam(mexam);
			
			return mexam.convertToTemp();
			
		}
		else if (MasterTblName.TBLNAME_M_FEE.equalsName(tbl_name) ){
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
		else if (MasterTblName.TBLNAME_M_TERM.equalsName(tbl_name) ){
			MTerm mterm = new MTerm();
			mterm.saveFromTemplate(temp);
			mtermDao.saveTerm(mterm);
			return mterm.convertToTemplate();
		} 
		else if (MasterTblName.TBLNAME_M_USER2CLASS.equalsName(tbl_name) ){
			MUser2Class muser2class = new MUser2Class();
			muser2class.saveFromTemplate(temp);
			muser2classDao.saveUser2Class(muser2class);
			return muser2class.convertToTemplate();
		} 
		return null;
	}

	
	@Override
	public MTemplate updateMTemplate(String tbl_name, MTemplate temp) {
		if (MasterTblName.TBLNAME_M_EXAM.equalsName(tbl_name) ){
			MExam mexam = mexamDao.findById(temp.getId());
			mexam.saveFromTemp(temp);
			mexamDao.updateExam(mexam);
			return mexam.convertToTemp();
		}
		
		// Tiep tuc lam tu day bo xung update
		else if (MasterTblName.TBLNAME_M_FEE.equalsName(tbl_name) ){
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
		else if (MasterTblName.TBLNAME_M_TERM.equalsName(tbl_name) ){
			MTerm mterm = mtermDao.findById(temp.getId());
			mterm.saveFromTemplate(temp);
			mtermDao.updateTerm(mterm);
			return mterm.convertToTemplate();
		} 
		else if (MasterTblName.TBLNAME_M_USER2CLASS.equalsName(tbl_name) ){
			MUser2Class muser2class =muser2classDao.findById(temp.getId());
			muser2class.saveFromTemplate(temp);
			muser2classDao.updateUser2Class(muser2class);
			return muser2class.convertToTemplate();
		} 
		return null;
	}

}
