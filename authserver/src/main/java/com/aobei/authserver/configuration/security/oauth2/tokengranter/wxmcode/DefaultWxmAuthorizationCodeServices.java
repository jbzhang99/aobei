package com.aobei.authserver.configuration.security.oauth2.tokengranter.wxmcode;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.aobei.authserver.configuration.security.BCryptPasswordEncoderExt;
import com.aobei.authserver.configuration.security.LoginInterceptor;
import com.aobei.authserver.configuration.security.oauth2.tokengranter.ExAuthenticationToken;
import com.aobei.authserver.configuration.security.userdetails.CustomUserDetails;
import com.aobei.authserver.model.User;
import com.aobei.authserver.model.UserWx;
import com.aobei.authserver.model.UserWxInfo;
import com.aobei.authserver.model.Wxu;
import com.aobei.authserver.otherapi.WeixinAPI;
import com.aobei.authserver.repository.UserRepository;
import com.aobei.authserver.util.IdGenerator;

import weixin.popular.bean.sns.Jscode2sessionResult;
import weixin.popular.bean.wxa.WxaDUserInfo;
import weixin.popular.bean.wxa.WxaUserInfoResult;
import weixin.popular.util.WxaUtil;

@Component
public class DefaultWxmAuthorizationCodeServices implements WxmAuthorizationCodeServices {

	private static Logger logger = LoggerFactory.getLogger(DefaultWxmAuthorizationCodeServices.class);

	@Autowired
	private WeixinAPI weixinAPI;

	@Autowired
	private UserRepository userRepository;

	@Resource(name = "passwordEncoder")
	private BCryptPasswordEncoderExt passwordEncoder;

	@Override
	public Authentication consumeAuthorizationCode(String appid, String code, String userinfo)
			throws OAuth2Exception {
		Jscode2sessionResult codeResult = weixinAPI.jscode2session(appid, code);
		Wxu wxu = null;
		if (codeResult.isSuccess()) {
			if (StringUtils.hasLength(codeResult.getUnionid())) {
				wxu = new Wxu();
				wxu.setAppid(appid);
				wxu.setUnionid(codeResult.getUnionid());
				wxu.setOpenid(codeResult.getOpenid());
			} else if (StringUtils.hasLength(userinfo)) {
				// 有小程序用户信息数据
				// 处理WXM data
				wxu = handlerWXMData(codeResult, appid, userinfo);
			} else if (!StringUtils.hasLength(codeResult.getUnionid())) {
				wxu = new Wxu();
				wxu.setAppid(appid);
				wxu.setUnionid(String.format("%s#%s", appid, codeResult.getOpenid()));
				wxu.setOpenid(codeResult.getOpenid());
			}

		} else {
			throw new InvalidRequestException("wxm authorization code error");
		}
		//处理微信用户数据
		UserWx userWx = handlerUserWxData(wxu);
		// 处理用户数据
		User user = handlerUserData(wxu, userWx);
		//登录日志标记
		LoginInterceptor.storeLoginLog(user.getUser_id());
		return new ExAuthenticationToken(new CustomUserDetails(user));
	}

	/**
	 * 处理WXM 用户加密数据
	 * 
	 * @param codeResult
	 * @param appid
	 * @param userinfo
	 * @return WXM data
	 */
	private Wxu handlerWXMData(Jscode2sessionResult codeResult, String appid, String userinfo) {
		logger.info("has wxm data");
		WxaUserInfoResult wxaUserInfoResult = null;
		if(StringUtils.hasLength(userinfo)){
			wxaUserInfoResult = JSON.parseObject(userinfo, WxaUserInfoResult.class);
		}
				
		if (wxaUserInfoResult != null && "getUserInfo:ok".equals(wxaUserInfoResult.getErrMsg())) {
			// 解密小程序用户数据
			WxaDUserInfo wxaDUserInfo = WxaUtil.decryptUserInfo(codeResult.getSession_key(),
					wxaUserInfoResult.getEncryptedData(), wxaUserInfoResult.getIv());
			if (wxaDUserInfo != null && codeResult.getOpenid().equals(wxaDUserInfo.getOpenId())) {
				Wxu wxu = new Wxu();
				wxu.setUnionid(wxaDUserInfo.getUnionId());
				wxu.setNickname(wxaDUserInfo.getNickName());
				wxu.setAppid(appid);
				wxu.setOpenid(wxaDUserInfo.getOpenId());
				if (!StringUtils.hasLength(wxu.getUnionid())) {
					wxu.setUnionid(String.format("%s#%s", appid, wxu.getOpenid()));
					wxaDUserInfo.setUnionId(wxu.getUnionid());
				} else {
					// 删除微信用户数据
					userRepository.deleteUserWxInfoByUnionId(String.format("%s#%s", appid, wxu.getOpenid()));
				}
				UserWxInfo userWxInfo = new UserWxInfo();
				BeanUtils.copyProperties(wxaDUserInfo, userWxInfo);
				userWxInfo.setCreate_datetime(new Date());
				// 保存微信用户信息
				userRepository.saveUserWxInfo(userWxInfo);
				return wxu;
			} else {
				throw new InvalidRequestException("WXM data error");
			}
		} else {
			throw new InvalidRequestException("WXM data error");
		}
	}

	/**
	 * 处理微信用户数据
	 * 
	 * @param wxu
	 * @return
	 */
	private UserWx handlerUserWxData(Wxu wxu) {
		UserWx userWx = userRepository.findUserWx(wxu.getAppid(), wxu.getOpenid());
		if (userWx == null) {
			// 不存在用户数据记录
			userWx = new UserWx();
			userWx.setOpenid(wxu.getOpenid());
			userWx.setAppid(wxu.getAppid());
			userWx.setWx_id(wxu.getUnionid());
			userWx.setCreate_datetime(new Date());
			// 保存记录
			userRepository.save(userWx);
		} else if (wxu.getUnionid().matches("[^#]*") && !wxu.getUnionid().equals(userWx.getWx_id())) {
			// 小程序接口中已返回unionid，但userWx 无unionid 不一致
			int ucount = userRepository.updateUserWxWxid(wxu.getUnionid(), userWx.getWx_id());
			if (ucount == 1) {
				// 新开放平台
				if(!userWx.getWx_id().matches(".*#.*")){
					userRepository.updateWxid(wxu.getUnionid(), userWx.getWx_id());
				}
				userWx.setWx_id(wxu.getUnionid());
			}
		}

		return userWx;
	}

	/**
	 * 处理用户数据
	 * 
	 * @param wxu
	 * @param userWx
	 * @return
	 */
	private User handlerUserData(Wxu wxu, UserWx userWx) {
		User user;
		user = userRepository.findByWxid(userWx.getWx_id());
		// 更新用户数据
		if (userWx.getWx_id().matches("[^#]*")) {
			User oldUser = userRepository.findByWxid(String.format("%s#%s", userWx.getAppid(), userWx.getOpenid()));
			if (oldUser != null) {
				// 更新id
				userRepository.updateWxid(userWx.getWx_id(),
						String.format("%s#%s", userWx.getAppid(), userWx.getOpenid()));
			}
			if (user == null && oldUser != null) {
				user = oldUser;
				user.setWx_id(userWx.getWx_id());
			} else if (user != null && oldUser != null) {
				// 合并用户数据
				mergeUser(user, oldUser);
			}
		}

		if (user == null) {
			// 添加用户
			user = new User();
			user.setUser_id(IdGenerator.generateId());
			user.setUsername("WX#" + userWx.getWx_id());
			user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString().substring(20)));
			user.setStatus(1);
			user.setWx_id(userWx.getWx_id());
			user.setNickname(wxu.getNickname());
			user.setCreate_datetime(new Date());
			int count = userRepository.save(user);
			if (count != 1) {
				throw new InvalidRequestException("Bad credentials forwx");
			}
		} else if (StringUtils.hasLength(wxu.getNickname()) && !StringUtils.hasLength(user.getNickname())) {
			// 更新用户昵称
			userRepository.updateNickName(wxu.getNickname(), user.getUser_id());
		}
		return user;
	}

	/**
	 * 合并用户数据
	 * 
	 * @param user
	 * @param oldUser
	 */
	private void mergeUser(User user, User oldUser) {
		// 重复的数据，删除本条用户数据,并将系统中的相关联的用户数据id替换
		long oldUserid = oldUser.getUser_id();
		// 合并用户角色信息
		String rolesStr = null;
		if (StringUtils.hasLength(oldUser.getRoles())) {
			Set<String> roles = new java.util.LinkedHashSet<>();
			if (StringUtils.hasLength(user.getRoles())) {
				for (String r : user.getRoles().split(",")) {
					roles.add(r);
				}
			}
			for (String r : oldUser.getRoles().split(",")) {
				roles.add(r);
			}
			rolesStr = StringUtils.collectionToDelimitedString(roles, ",");
		}

		// 合并老用户数据
		int count = userRepository.updateOldUserData(oldUserid, user.getUser_id(), rolesStr);
		if (count < 1) {
			throw new InvalidRequestException("user merge error");
		}
		logger.info("WXM delete user {} to {}", oldUserid, user.getUser_id());
	}

}
