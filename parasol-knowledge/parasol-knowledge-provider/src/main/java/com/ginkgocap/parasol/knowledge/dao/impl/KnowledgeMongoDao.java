package com.ginkgocap.parasol.knowledge.dao.impl;

import com.ginkgocap.parasol.knowledge.dao.IKnowledgeMongoDao;
import com.ginkgocap.parasol.knowledge.model.ColumnSys;
import com.ginkgocap.parasol.knowledge.model.KnowledgeMongo;
import com.mongodb.WriteResult;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Repository("knowledgeMongoDao")
public class KnowledgeMongoDao implements IKnowledgeMongoDao {
	
	@Resource
	private MongoTemplate mongoTemplate;
	
	@Override
	public KnowledgeMongo insert(KnowledgeMongo knowledgeMongo, Long userId,String... collectionName) throws Exception {
		
		if(knowledgeMongo == null || knowledgeMongo.getId() <= 0)
			return null;
		
		long currentDate = new Date().getTime();
		
		if(knowledgeMongo.getCreateUserId() <= 0)
			knowledgeMongo.setCreateUserId(userId);
        //TODO: check if the data is validate or not
		if(knowledgeMongo.getCreateDate() > 0) {
            knowledgeMongo.setCreateDate(currentDate);
        }
		knowledgeMongo.setModifyUserId(userId);
		knowledgeMongo.setModifyDate(currentDate);
		
		String currCollectionName = getCollectionName(knowledgeMongo.getColumnId(),collectionName);
		
		mongoTemplate.insert(knowledgeMongo,currCollectionName);
		
		return this.getByIdAndColumnId(knowledgeMongo.getId(),knowledgeMongo.getColumnId(),currCollectionName);
	}
	
	@Override
	public List<KnowledgeMongo> insertList(List<KnowledgeMongo> knowledgeMongoList, Long userId,String... collectionName) throws Exception {
		
		List<KnowledgeMongo> returnList = new ArrayList();

		if(knowledgeMongoList != null && !knowledgeMongoList.isEmpty()) {
			Iterator<KnowledgeMongo> it = knowledgeMongoList.iterator();
			while(it.hasNext()) {
				returnList.add(this.insert(it.next(), userId, collectionName));
			}
		}
        mongoTemplate.insert(knowledgeMongoList, KnowledgeMongo.class);
		
		return returnList;
	}

	@Override
	public KnowledgeMongo update(KnowledgeMongo knowledgeMongo, Long userId,String... collectionName)
			throws Exception {

		if(knowledgeMongo == null)
			return null;
		
		long knowledgeId = knowledgeMongo.getId();
		
		if(knowledgeId <= 0) {
			return this.insert(knowledgeMongo, userId);
		}
		
		long currentDate = new Date().getTime();

		knowledgeMongo.setModifyUserId(userId);
		knowledgeMongo.setModifyDate(currentDate);
		
		Criteria criteria = Criteria.where("_id").is(knowledgeId);
		Query query = new Query(criteria);
		
		String currCollectionName = getCollectionName(knowledgeMongo.getColumnId(),collectionName);
		
		WriteResult result = mongoTemplate.updateFirst(query, getUpdate(knowledgeMongo,userId), currCollectionName);
		
		return this.getByIdAndColumnId(knowledgeId,knowledgeMongo.getColumnId(),currCollectionName);
	}

	@Override
	public KnowledgeMongo insertAfterDelete(KnowledgeMongo knowledgeMongo,
			long knowledgeId, Long userId,String... collectionName) throws Exception {
		
		String currCollectionName = getCollectionName(knowledgeMongo.getColumnId(),collectionName);
		
		if(knowledgeMongo == null || knowledgeId <= 0)
			return null;
		
		KnowledgeMongo oldValue = this.getByIdAndColumnId(knowledgeId,knowledgeMongo.getColumnId(),currCollectionName);
		
		this.deleteByIdAndColumnId(knowledgeId,knowledgeMongo.getColumnId(),currCollectionName);
		
		try {
			
			this.insert(knowledgeMongo, userId,currCollectionName);
			
		} catch (Exception e) {
			
			if(oldValue != null)
				this.insert(oldValue, userId,currCollectionName);
			
			throw e;
		}
		
		
		return this.getByIdAndColumnId(knowledgeId,knowledgeMongo.getColumnId());
	}

	@Override
	public int deleteByIdAndColumnId(long id,long columnId, String...collectionName ) throws Exception {
		
		Criteria criteria = Criteria.where("_id").is(id);
		Query query = new Query(criteria);
		
		mongoTemplate.remove(query, getCollectionName(columnId,collectionName));
		
		return 0;
	}

	@Override
	public int deleteByIdsAndColumnId(List<Long> ids,long columnId,String... collectionName) throws Exception {
		
		Criteria criteria = Criteria.where("_id").in(ids);
		Query query = new Query(criteria);
		
		mongoTemplate.remove(query, getCollectionName(columnId,collectionName));
		
		return 0;
	}

	@Override
	public int deleteByCreateUserIdAndColumnId(long createUserId,long columnId,String... collectionName) throws Exception {
		
		Criteria criteria = Criteria.where("createUserId").is(createUserId);
		Query query = new Query(criteria);
		
		mongoTemplate.remove(query, getCollectionName(columnId,collectionName));
		
		return 0;
	}

	@Override
	public KnowledgeMongo getByIdAndColumnId(long id,long columnId,String... collectionName) throws Exception {
		
		Criteria criteria = Criteria.where("_id").is(id);
		Query query = new Query(criteria);
		
		return mongoTemplate.findOne(query,KnowledgeMongo.class, getCollectionName(columnId,collectionName));
		
	}
	
	@Override
	public List<KnowledgeMongo> getByIdsAndColumnId(List<Long> ids,long columnId,String... collectionName) throws Exception {
		
		Criteria criteria = Criteria.where("_id").in(ids);
		Query query = new Query(criteria);
		
		return mongoTemplate.find(query,KnowledgeMongo.class, getCollectionName(columnId,collectionName));
		
	}
	
	private String getCollectionName(long columnId) throws Exception {
		
		StringBuffer strBuf = new StringBuffer();
		
		strBuf.append(KNOWLEDGE_COLLECTION_NAME);
		
		//从缓存中获取系统栏目
		List<ColumnSys> columnSysList = new ArrayList<ColumnSys>();
		
		Iterator<ColumnSys> it = columnSysList.iterator();
		
		boolean columnCodeNotExistflag = true;
		
		while(it.hasNext()) {
			ColumnSys columnSys = it.next();
			if(columnId == columnSys.getId()) {
				if(StringUtils.isEmpty(columnSys.getColumnCode())) {
					break;
				}
				columnCodeNotExistflag = false;
				strBuf.append(columnSys.getColumnCode());
				break;
			}
		}
		
		if(columnCodeNotExistflag) {
			strBuf.append(KNOWLEDGE_COLLECTION_USERSELF_NAME);
		}
		
		return strBuf.toString();
		
	}
	
	private String getCollectionName(long columnId,String[] collectionName) throws Exception {
		return ArrayUtils.isEmpty(collectionName) && StringUtils.isEmpty(collectionName[0]) ? getCollectionName(columnId) : collectionName[0];
	}
	
	private Update getUpdate(KnowledgeMongo knowledgeMongo, Long userId) {
		
		//构建更新字段，目前默认是全字段更新
		Update update = new Update();
		
		JSONObject json = JSONObject.fromObject(knowledgeMongo);
		
		Iterator<String> it = json.keys();
		
		while(it.hasNext()) {
			String key = it.next();
			update.update(key, json.get(key));
		}
		
		return update;
		
	}
}