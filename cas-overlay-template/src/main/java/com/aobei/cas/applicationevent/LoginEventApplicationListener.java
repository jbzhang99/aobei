package com.aobei.cas.applicationevent;

import java.util.Date;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.aobei.cas.bean.LoginLog;
import com.aobei.cas.util.IPUtil;
import com.aobei.cas.util.IPUtil.IPData;
import com.aobei.cas.util.IdGenerator;

@Component
public class LoginEventApplicationListener implements ApplicationListener<LoginEvent> {

	@Autowired
	private DataSource dataSource;

	@Autowired(required = false)
	private JdbcTemplate jdbcTemplate;

	@Override
	public void onApplicationEvent(LoginEvent event) {
		if (jdbcTemplate == null) {
			jdbcTemplate = new JdbcTemplate(dataSource);
		}
		try {
			LoginLog loginLog = new LoginLog();
			String ip = event.getxRealIP();
			boolean localhost = false;
			if (!StringUtils.hasLength(ip) || !ip.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
				ip = event.getIp();
				if ("0:0:0:0:0:0:0:1".equals(ip)) {
					ip = "127.0.0.1";
					localhost = true;
				}
			}
			if (!localhost) {
				IPData ipData = IPUtil.parseIP(ip);
				if (ipData != null) {
					loginLog.setCountry(ipData.getCountry());
					loginLog.setProvince(ipData.getProvince());
					loginLog.setCity(ipData.getCity());
				}
			}
			loginLog.setLogin_log_id(IdGenerator.generateId());
			loginLog.setIp(ip);
			loginLog.setUser_agent(event.getUserAgent());
			loginLog.setDuuid(event.getDuuid());
			loginLog.setCreate_datetime(new Date());
			loginLog.setActive_time(0);
			loginLog.setLogout(0);
			loginLog.setClient_type(1);
			loginLog.setLogin_type(1);
			Long userid = jdbcTemplate.queryForObject("select user_id from users where username=?", Long.class,
					event.getUsername());
			loginLog.setUser_id(userid);

			//添加登录日志
			String sql = "insert into login_log(login_log_id,user_id,create_datetime,ip,duuid,user_agent,client_type,login_type,logout,active_time,country,province,city) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
			jdbcTemplate.update(sql, loginLog.getLogin_log_id(), loginLog.getUser_id(), loginLog.getCreate_datetime(),
					loginLog.getIp(), loginLog.getDuuid(), loginLog.getUser_agent(), loginLog.getClient_type(),
					loginLog.getLogin_type(), loginLog.getLogout(), loginLog.getActive_time(), loginLog.getCountry(),
					loginLog.getProvince(), loginLog.getCity());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
