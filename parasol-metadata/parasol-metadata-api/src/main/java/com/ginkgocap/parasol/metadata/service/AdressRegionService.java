package com.ginkgocap.parasol.metadata.service;

import java.util.List;

import com.ginkgocap.parasol.metadata.exception.CodeRegionServiceException;
import com.ginkgocap.parasol.metadata.model.CodeRegion;


/**
 * 操作CodeRegion的接口定义
 * 
 * @author allenshen
 * @date 2015年11月11日
 * @time 下午5:46:44
 * @Copyright Copyright©2015 www.gintong.com
 */
public interface AdressRegionService {

	/**
	 * 创建根节点
	 * 
	 * @param codeRegion
	 * @return
	 * @throws CodeServiceException
	 *             不能重名
	 */
	public Long createCodeRegionForRoot(CodeRegion codeRegion) throws CodeRegionServiceException;

	/**
	 * 创建父节点的子节点
	 * 
	 * @param codeRegion
	 * @return
	 * @throws CodeServiceException
	 *             不能重名
	 */
	public Long createCodeForChildren(Long parentId, CodeRegion codeRegion) throws CodeRegionServiceException;

	/**
	 * 删除一个Code 会把Code下边的子Code也删除
	 * 
	 * @param codeRegionId
	 * @return
	 * @throws CodeServiceException
	 */
	public boolean removeCodeRegion(Long codeRegionId) throws CodeRegionServiceException;

	/**
	 * 删除一个Code 会把Code下边的子Code也删除
	 * 
	 * @param codeRegion
	 * @return
	 * @throws CodeServiceException
	 */
	public boolean updateCodeRegion(CodeRegion codeRegion) throws CodeRegionServiceException;

	/**
	 * 通过一个Id查询一个Code
	 * 
	 * @param codeRegionId
	 * @return
	 * @throws CodeServiceException
	 */
	public CodeRegion getCodeRegionById(Long codeRegionId) throws CodeRegionServiceException;

	/**
	 * 查询一个父节点下边的一级子节点
	 * 
	 * @param parentId
	 *            父节点ID
	 * @param displayDisabled
	 *            是否显示禁用的Code，默认为false，不显示
	 * @return
	 * @throws CodeServiceException
	 */
	public List<CodeRegion> getCodeRegionsByParentId(Long parentId) throws CodeRegionServiceException;

	/**
	 * 查询一个父节点下边的一级子节点数量
	 * 
	 * @param parentId
	 *            父节点ID
	 * @param displayDisabled
	 *            是否显示禁用的Code，默认为false，不显示
	 * @return
	 * @throws CodeServiceException
	 */
	public int countCodeRegionsByParentId(Long parentId) throws CodeRegionServiceException;

	

	/**
	 * 查询大类（根节点，比如：海外、国内省、香港、澳门、马来西亚）
	 * @param parentId
	 * @param type
	 * @return
	 * @throws CodeRegionServiceException
	 */
	public List<CodeRegion> getCodeRegionsForRootByType(long parentId, int type) throws CodeRegionServiceException;
	
	
}
