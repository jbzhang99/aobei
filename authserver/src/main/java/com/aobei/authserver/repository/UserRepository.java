package com.aobei.authserver.repository;

import com.aobei.authserver.model.LoginLog;
import com.aobei.authserver.model.PhoneUser;
import com.aobei.authserver.model.User;
import com.aobei.authserver.model.UserWx;
import com.aobei.authserver.model.UserWxInfo;

public interface UserRepository {
	
	/**
	 * 根据id 获取用户
	 * @param userId
	 * @return
	 */
	User findById(Long userId);
    
	/**
	 * 根据wxid 获取用户
	 * @param wxid
	 * @return
	 */
	User findByWxid(String wxid);
	
	/**
	 * 根据用户名获取用户
	 * @param username
	 * @return
	 */
	User findByUsername(String username);
	
	/**
	 * 保存用户
	 * @param user
	 * @return
	 */
	int save(User user);
	
	/**
	 * 更新wxid
	 * @param to_id
	 * @param from_id
	 * @return
	 */
	int updateWxid(String to_id, String from_id);
	
	/**
	 * 更新nickname
	 * @param nickname
	 * @param user_id
	 * @return
	 */
	int updateNickName(String nickname,Long user_id);
	
	/**
	 * 更新wxid
	 * @param to_id
	 * @param from_id
	 * @return
	 */
	int updateUserWxWxid(String to_id, String from_id);

	/**
	 * 通过appid openid 获取微信用户
	 * @param appid
	 * @param openid
	 * @return
	 */
	UserWx findUserWx(String appid,String openid);

	/**
	 * 保存微信用户
	 * @param userWx
	 * @return
	 */
	int save(UserWx userWx);
	
	/**
	 * 合并老用户数据
	 * @param oldUserid
	 * @param userid
	 * @param roles 用户角色
	 * @return
	 */
	int updateOldUserData(long oldUserid,long userid,String roles);
	
	/**
	 * 保存微信用户信息
	 * @param userWxInfo
	 * @return
	 */
	int saveUserWxInfo(UserWxInfo userWxInfo);
	
	/**
	 * 删除微信用户数据
	 * @param unionId
	 * @return
	 */
	int deleteUserWxInfoByUnionId(String unionId);
	
	/**
	 * 保存登录日志
	 * @param loginLog
	 * @return
	 */
	int saveLoginLog(LoginLog loginLog);
	
	/**
	 * 根据手机号找用户id
	 * @param group  [1 学员 ,2 老师,3 合伙人,4 浦尔家用户]
	 * @param phone 手机号
	 * @return id
	 */
	PhoneUser findUuidByPhone(int group ,String phone);
	
	/**
	 * 根据手机号创建用户
	 * @param group  [1 学员 ,2 老师,3 合伙人,4 浦尔家用户]
	 * @param phone 手机号
	 * @param channel 渠道号
	 * @param createUid 就否创建业务用户
	 * @return id
	 */
	Long createUuidByPhone(int group ,String phone,int channel,boolean createUid);
	
}
