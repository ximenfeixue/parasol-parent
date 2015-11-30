package com.ginkgocap.parasol.user.service;

import com.ginkgocap.parasol.user.exception.UserFriendlyServiceException;
import com.ginkgocap.parasol.user.model.UserFriendly;

/**
 * 操作用户好友关系
 * 
 */
public interface UserFriendlyService {

	/**
	 * 添加用户好友关系
	 * @param userFriendly 
	 * @return Long
	 * @throws UserFriendlyServiceException
	 */
	public Long createUserFriendly(UserFriendly userFriendly) throws UserFriendlyServiceException; 
	

	/**
	 * 修改好友关系是否通过验证状态
	 * @param friendId
	 * @param status
	 * @return boolean
	 * @throws UserFriendlyServiceException 
	 */
	public boolean updateStatus(Long userId,Long friendId, Byte status) throws UserFriendlyServiceException; 

	/**
	 * 删除用户好友
	 * @param userId
	 * @param friendId
	 * @return Boolean
	 * @throws UserFriendlyServiceException
	 */
	public Boolean deleteFriendly(Long userId,Long friendId) throws UserFriendlyServiceException;	
	

}