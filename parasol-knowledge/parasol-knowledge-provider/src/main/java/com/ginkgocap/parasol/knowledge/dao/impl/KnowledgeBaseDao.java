package com.ginkgocap.parasol.knowledge.dao.impl;

import com.ginkgocap.parasol.common.service.impl.BaseService;
import com.ginkgocap.parasol.knowledge.dao.IKnowledgeBaseDao;
import com.ginkgocap.parasol.knowledge.model.KnowledgeBase;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository("knowledgeBaseDao")
public class KnowledgeBaseDao extends BaseService<KnowledgeBase> implements IKnowledgeBaseDao {

	@Override
	public KnowledgeBase insert(KnowledgeBase knowledgeBase,Long userId) throws Exception {
		
		if(knowledgeBase == null)
			return null;
		
		long id = (Long) this.saveEntity(knowledgeBase);
		
		return this.getById(id);
	}
	
	@Override
	public List<KnowledgeBase> insertList(List<KnowledgeBase> knowledgeBaseList,Long userId) throws Exception {
		
		if(knowledgeBaseList == null || knowledgeBaseList.isEmpty())
			return null;
		
		return this.saveEntitys(knowledgeBaseList);
	}

	@Override
	public KnowledgeBase update(KnowledgeBase knowledgeBase,Long userId) throws Exception {
		
		if(knowledgeBase == null)
			return null;
		
		this.updateEntity(knowledgeBase);
		
		return this.getById(knowledgeBase.getId());
	}

	@Override
	public KnowledgeBase insertAfterDelete(KnowledgeBase knowledgeBase,Long userId)
			throws Exception {
		
		long id = knowledgeBase.getId();
		
		KnowledgeBase oldValue = null;
		
		if(id <= 0) {
			oldValue = this.getById(id);
			
			this.deleteById(id);
		}
		
		try {
            this.insert(knowledgeBase, userId);
		} catch (Exception e) {
			if(oldValue != null && oldValue.getId() > 0)
				this.insert(oldValue, userId);
			throw e;
		}
		
		return this.getById(id);
	}

	@Override
	public int deleteById(long id) throws Exception {
		
		boolean deleteStatus = this.deleteEntity(id);
		
		return deleteStatus ? 1 : 0;
	}

	@Override
	public int deleteByIds(List<Long> ids) throws Exception {
		
		boolean deleteStatus = this.deleteEntityByIds(ids);
		
		return deleteStatus ? 1 : 0;
	}

	@Override
	public int deleteByCreateUserId(long createUserId) throws Exception {
		
		int deleteStatus = this.deleteList("delete_by_createUserId", createUserId);
		
		return deleteStatus;
	}

	@Override
	public KnowledgeBase getById(long id) throws Exception {
		
		return this.getEntity(id);
	}

	@Override
	public List<KnowledgeBase> getByIds(List<Long> ids) throws Exception {
		
		return this.getEntityByIds(ids);
	}

	@Override
	public List<KnowledgeBase> getAll(int start,int size)
			throws Exception {
		
		return this.getEntitys("get_by_start_size", new Object[]{start,size});
	}

	@Override
	public List<KnowledgeBase> getByCreateUserId(long createUserId,int start,int size)
			throws Exception {
		
		return this.getEntitys("get_by_createUserId",  new Object[]{createUserId,start,size});
	}

	@Override
	public List<KnowledgeBase> getByCreateUserIdAndType(long createUserId,
			String type,int start,int size) throws Exception {
		
		return this.getEntitys("get_by_createUserId_type", new Object[]{createUserId,type,start,size});
	}

	@Override
	public List<KnowledgeBase> getByCreateUserIdAndColumnId(long createUserId,
			long columnId,int start,int size) throws Exception {
		
		return this.getEntitys("get_by_createUserId_columnId", new Object[]{createUserId,columnId,start,size});
	}

	@Override
	public List<KnowledgeBase> getByColumnId(long columnId,int start,int size) throws Exception {

		return this.getEntitys("get_by_columnId", new Object[]{columnId,start,size});
	}

	@Override
	public List<KnowledgeBase> getByTypeAndColumnId(String type, long columnId,int start,int size)
			throws Exception {

		return this.getEntitys("get_by_type_columnId", new Object[]{type,columnId,start,size});
	}
	
	@Override
	public List<KnowledgeBase> getByType(String type,int start,int size)
			throws Exception {

		return this.getEntitys("get_by_type", new Object[]{type,start,size});
	}

	@Override
	public List<KnowledgeBase> getByCreateUserIdAndTypeAndColumnId(
			long createUserId, String type, long columnId,int start,int size) throws Exception {

		return this.getEntitys("get_by_createUserId_type_columnId", new Object[]{createUserId,type,columnId,start,size});
	}

	@Override
	public List<KnowledgeBase> getByBetweenCreateDate(Date beginDate,
			Date endDate,int start,int size) throws Exception {
		
		return this.getEntitys("get_by_beginDate_endDate", new Object[]{getDate(beginDate,true),getDate(endDate,false),start,size});
	}

	@Override
	public List<KnowledgeBase> getByTypeAndBetweenCreateDate(String type,
			Date beginDate, Date endDate,int start,int size) throws Exception {
		
		return this.getEntitys("get_by_type_beginDate_endDate", new Object[]{type,getDate(beginDate,true),getDate(endDate,false),start,size});
	}

	@Override
	public List<KnowledgeBase> getByCreateUserIdAndBetweenCreateDate(
			long createUserId, Date beginDate, Date endDate,int start,int size) throws Exception {
		
		return this.getEntitys("get_by_createUserId_beginDate_endDate", new Object[]{createUserId,getDate(beginDate,true),getDate(endDate,false),start,size});
	}

	@Override
	public List<KnowledgeBase> getByCreateUserIdAndColumnIdAndBetweenCreateDate(
			long createUserId, long columnId, Date beginDate, Date endDate,int start,int size)
			throws Exception {
		
		return this.getEntitys("get_by_createUserId_columnId_beginDate_endDate", new Object[]{createUserId,columnId,getDate(beginDate,true),getDate(endDate,false),start,size});
	}

	@Override
	public List<KnowledgeBase> getByStatus(String status,int start,int size) throws Exception {
		
		return this.getEntitys("get_by_status", new Object[]{status,start,size});
	}

	@Override
	public List<KnowledgeBase> getByAuditStatus(String auditStatus,int start,int size)
			throws Exception {
		
		return this.getEntitys("get_by_auditStatus", new Object[]{auditStatus,start,size});
	}

	@Override
	public List<KnowledgeBase> getByReportStatus(String reportStatus,int start,int size)
			throws Exception {
		
		return this.getEntitys("get_by_reportStatus", new Object[]{reportStatus,start,size});
	}

	@Override
	public List<KnowledgeBase> getByCreateUserIdAndStatus(long createUserId,
			String status,int start,int size) throws Exception {
		
		return this.getEntitys("get_by_createUserId_status", new Object[]{createUserId,status,start,size});
	}

	@Override
	public List<KnowledgeBase> getByCreateUserIdAndAuditStatus(
			long createUserId, String auditStatus,int start,int size) throws Exception {
		
		return this.getEntitys("get_by_createUserId_auditStatus", new Object[]{createUserId,auditStatus,start,size});
	}

	@Override
	public List<KnowledgeBase> getByCreateUserIdAndReportStatus(
			long createUserId, String reportStatus,int start,int size) throws Exception {
		
		return this.getEntitys("get_by_createUserId_reportStatus", new Object[]{createUserId,reportStatus,start,size});
	}

	@Override
	public List<KnowledgeBase> getByColumnIdAndStatus(long columnId,
			String status,int start,int size) throws Exception {
		
		return this.getEntitys("get_by_columnId_status", new Object[]{columnId,status,start,size});
	}

	@Override
	public List<KnowledgeBase> getByColumnIdAndAuditStatus(long columnId,
			String auditStatus,int start,int size) throws Exception {
		
		return this.getEntitys("get_by_columnId_auditStatus", new Object[]{columnId,auditStatus,start,size});
	}

	@Override
	public List<KnowledgeBase> getByColumnIdAndReportStatus(long columnId,
			String reportStatus,int start,int size) throws Exception {
		
		return this.getEntitys("get_by_columnId_reportStatus", new Object[]{columnId,reportStatus,start,size});
	}
	
	private Date getDate(Date date,boolean beginOrEnd) {
		
		if(date != null) 
			return date;
		
		if(beginOrEnd) {
			return new Date("9999-00-00 00:00:00");
		} else {
			return new Date("0000-00-00 00:00:00");
		}
		
	}
	
}