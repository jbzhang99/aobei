package com.aobei.authserver.repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.aobei.authserver.model.LoginLog;
import com.aobei.authserver.model.PhoneUser;
import com.aobei.authserver.model.User;
import com.aobei.authserver.model.UserWx;
import com.aobei.authserver.model.UserWxInfo;
import com.aobei.authserver.util.IdGenerator;

@Repository
public class UserRepositoryImpl implements UserRepository {
	
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	StringRedisTemplate stringRedisTemplate;


	private RowMapper<User> rowMapper = new BeanPropertyRowMapper<User>(User.class);
	private RowMapper<UserWx> userWxRowMapper = new BeanPropertyRowMapper<UserWx>(UserWx.class);
	private RowMapper<PhoneUser> phoneUserRowMapper = new BeanPropertyRowMapper<PhoneUser>(PhoneUser.class);
	

	
	
	@Override
	public User findById(Long userId) {
		List<User> userList = jdbcTemplate.query("select * from users where user_id=?", rowMapper, userId);
		return DataAccessUtils.singleResult(userList);
	}

	@Override
	public User findByUsername(String username) {
		List<User> userList = jdbcTemplate.query("select * from users where username=?", rowMapper, username);
		return DataAccessUtils.singleResult(userList);
	}

	@Override
	public User findByWxid(String wxid) {
		List<User> userList = jdbcTemplate.query("select * from users where wx_id=?", rowMapper, wxid);
		return DataAccessUtils.singleResult(userList);
	}

	@Override
	public int updateWxid(String to_id, String from_id) {
		String sql = "update users set wx_id=? where wx_id=?";
		return jdbcTemplate.update(sql, to_id, from_id);
	}

	@Transactional(timeout = 3)
	@Override
	public int updateNickName(String nickname, Long user_id) {
		String sql = "update users set nickname=? where user_id=?";
		return jdbcTemplate.update(sql, nickname, user_id);
	}

	@Transactional(timeout = 3)
	@Override
	public int updateUserWxWxid(String to_id, String from_id) {
		String sql = "update users_wx set wx_id=? where wx_id=?";
		return jdbcTemplate.update(sql, to_id, from_id);
	}

	@Transactional(timeout = 3)
	@Override
	public int save(User user) {
		String sql = "insert into users(user_id,username,password,status,wx_id,nickname,create_datetime) values(?,?,?,?,?,?,?)";
		return jdbcTemplate.update(sql, user.getUser_id(), user.getUsername(), user.getPassword(), user.getStatus(),
				user.getWx_id(), user.getNickname(), user.getCreate_datetime());
	}

	@Override
	public UserWx findUserWx(String appid, String openid) {
		String sql = "select wx_id,appid,openid from users_wx  where appid = ? and openid = ?";
		List<UserWx> userWxes = jdbcTemplate.query(sql, userWxRowMapper, appid, openid);
		return DataAccessUtils.singleResult(userWxes);
	}

	@Transactional(timeout = 3)
	@Override
	public int save(UserWx userWx) {
		String sql = "insert into users_wx (wx_id,appid,openid,create_datetime) values(?,?,?,?)";
		return jdbcTemplate.update(sql, userWx.getWx_id(), userWx.getAppid(), userWx.getOpenid(),
				userWx.getCreate_datetime());
	}

	@Override
	@Transactional(timeout = 6)
	public int updateOldUserData(long oldUserid, long userid, String roles) {
		int count = 0;
		// 更新学员信息
		count += jdbcTemplate.update("update student set user_id=? where user_id=?", userid, oldUserid);
		// 更新老师信息
		count += jdbcTemplate.update("update teacher set user_id=? where user_id=?", userid, oldUserid);
		// 更新合伙人信息
		count += jdbcTemplate.update("update partner set user_id=? where user_id=?", userid, oldUserid);
		// 更新顾客信息
		count += jdbcTemplate.update("update customer set user_id=? where user_id=?", userid, oldUserid);
		// 更新系统用户信息
		count += jdbcTemplate.update("update sys_users set user_id=? where user_id=?", userid, oldUserid);
		// 更新支付日志
		count += jdbcTemplate.update("update pay_order_log set user_id=? where user_id=?", userid, oldUserid);
		// 删除老的用户信息
		count += jdbcTemplate.update("delete from users where user_id=?", oldUserid);
		if (roles != null) {
			// 更新用户角色字段
			count += jdbcTemplate.update("update users set roles=? where user_id=?", roles, userid);
		}
		return count;
	}

	@Transactional(timeout = 3)
	@Override
	public int saveUserWxInfo(UserWxInfo userWxInfo) {
		return jdbcTemplate.update(
				"insert into users_wx_info(unionId,avatarUrl,city,country,gender,language,nickName,province,create_datetime) values(?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE avatarUrl=?,city=?,country=?,gender=?,language=?,nickName=?,province=?",
				userWxInfo.getUnionId(), userWxInfo.getAvatarUrl(), userWxInfo.getCity(), userWxInfo.getCountry(),
				userWxInfo.getGender(), userWxInfo.getLanguage(), userWxInfo.getNickName(), userWxInfo.getProvince(),
				userWxInfo.getCreate_datetime(),

				userWxInfo.getAvatarUrl(), userWxInfo.getCity(), userWxInfo.getCountry(), userWxInfo.getGender(),
				userWxInfo.getLanguage(), userWxInfo.getNickName(), userWxInfo.getProvince());
	}

	@Transactional(timeout = 3)
	@Override
	public int deleteUserWxInfoByUnionId(String unionId) {
		return jdbcTemplate.update("delete from users_wx_info where unionId=?", unionId);
	}

	@Transactional(timeout = 3)
	@Override
	public int saveLoginLog(LoginLog loginLog) {

		String sql = "insert into login_log(login_log_id,user_id,create_datetime,ip,duuid,user_agent,client_type,login_type,logout,active_time,country,province,city) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		return jdbcTemplate.update(sql, loginLog.getLogin_log_id(), loginLog.getUser_id(),
				loginLog.getCreate_datetime(), loginLog.getIp(), loginLog.getDuuid(), loginLog.getUser_agent(),
				loginLog.getClient_type(), loginLog.getLogin_type(), loginLog.getLogout(), loginLog.getActive_time(),
				loginLog.getCountry(), loginLog.getProvince(), loginLog.getCity());

	}

	@Override
	public PhoneUser findUuidByPhone(int group, String phone) {
		String sql = null;
		switch (group) {
		case 1:
			sql = "select user_id, phone from student where phone=?";
			break;
		case 2:
			sql = "select user_id, phone from teacher where phone=?";
			break;
		case 3:
			sql = "select user_id, phone from partner where phone=?";
			break;
		case 4:
			sql = "select user_id, phone from customer where phone=?";
			break;
		default:
			return null;
		}
		return DataAccessUtils.singleResult(jdbcTemplate.query(sql, phoneUserRowMapper, phone));
	}

	@Transactional(timeout = 3)
	@Override
	public Long createUuidByPhone(int group, String phone,int channel, boolean createUid) {
		long userid = IdGenerator.generateId();
		String role = null;
		int count = 0;
		switch (group) {
		case 1:
			if(createUid){
				count += jdbcTemplate.update("insert into student(student_id,phone,user_id,cdate) select ?,?,?,? from dual where not exists (select 1 from student where phone=?)",IdGenerator.generateId(),phone,userid,new Date(),phone);
			}else{
				count += jdbcTemplate.update("update student set user_id=? where phone=?",userid,phone);
			}
			role = "ROLE_STUDENT";
			break;
		case 2:
			if(createUid){
				count += jdbcTemplate.update("insert into teacher(teacher_id,phone,user_id,create_time) select ?,?,?,? from dual where not exists (select 1 from teacher where phone=?)",IdGenerator.generateId(),phone,userid,new Date(),phone);
			}else{
				count += jdbcTemplate.update("update teacher set user_id=? where phone=?",userid,phone);
			}
			role = "ROLE_TEACHER";
			break;
		case 3:
			if(createUid){
				count += jdbcTemplate.update("insert into partner(partner_id,phone,user_id,cdate) select ?,?,?,? from dual where not exists (select 1 from partner where phone=?)",IdGenerator.generateId(),phone,userid,new Date(),phone);
			}else{
				count += jdbcTemplate.update("update partner set user_id=? where phone=?",userid,phone);
			}
			role = "ROLE_PARTNER";
			break;
		case 4:
			if(createUid){
				count += jdbcTemplate.update("insert into customer(customer_id,phone,user_id,create_datetime,channel_id) select ?,?,?,?,? from dual where not exists (select 1 from customer where phone=?)",IdGenerator.generateId(),phone,userid,new Date(),channel,phone);
			}else{
				count += jdbcTemplate.update("update customer set user_id=? where phone=?",userid,phone);
			}
			role = "ROLE_CUSTOMER";
			break;
		}
		if(count > 1){
			throw new RuntimeException("Multiple phone users " + phone);
		}else if(count == 1){
			// 添加统一用户
			count += jdbcTemplate.update("insert into users(user_id,username,password,create_datetime,nickname,roles,status)values(?,?,?,?,?,?,?)", 
					userid,
					"SMS#"+group+"_"+phone+"_"+RandomStringUtils.randomNumeric(6),
					passwordEncoder.encode(UUID.randomUUID().toString()),
					new Date(),
					phone,
					role,
					1);
			if(count == 2){
				return userid;
			}
		}
		return null;
	}

	
}
