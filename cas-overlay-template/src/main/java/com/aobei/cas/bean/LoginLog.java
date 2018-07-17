package com.aobei.cas.bean;

import java.util.Date;

public class LoginLog {

	private Long login_log_id;

	private Long user_id;

	private Date create_datetime;

	private String ip;

	private String duuid;

	private String user_agent;

	private Integer client_type;

	private Integer login_type;

	private Integer logout;

	private Integer active_time;

	private String country;

	private String city;

	private String province;

	public Long getLogin_log_id() {
		return login_log_id;
	}

	public void setLogin_log_id(Long login_log_id) {
		this.login_log_id = login_log_id;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Date getCreate_datetime() {
		return create_datetime;
	}

	public void setCreate_datetime(Date create_datetime) {
		this.create_datetime = create_datetime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDuuid() {
		return duuid;
	}

	public void setDuuid(String duuid) {
		this.duuid = duuid;
	}

	public String getUser_agent() {
		return user_agent;
	}

	public void setUser_agent(String user_agent) {
		this.user_agent = user_agent;
	}

	public Integer getClient_type() {
		return client_type;
	}

	public void setClient_type(Integer client_type) {
		this.client_type = client_type;
	}

	public Integer getLogin_type() {
		return login_type;
	}

	public void setLogin_type(Integer login_type) {
		this.login_type = login_type;
	}

	public Integer getLogout() {
		return logout;
	}

	public void setLogout(Integer logout) {
		this.logout = logout;
	}

	public Integer getActive_time() {
		return active_time;
	}

	public void setActive_time(Integer active_time) {
		this.active_time = active_time;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

}
