package custom.bean;

import java.util.Date;
import java.util.List;

import com.aobei.train.model.AuthRole;

/**
 * 操作日志承载数据bean
 * @author adminL
 *
 */
public class Log {

	private String name;//用户真实姓名
	private String username;//账号名称
	private List<AuthRole> rolename;//角色集合
	private String operated;//操作记录
	private Date operated_time;//操作时间
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getOperated() {
		return operated;
	}
	public void setOperated(String operated) {
		this.operated = operated;
	}
	public Date getOperated_time() {
		return operated_time;
	}
	public void setOperated_time(Date operated_time) {
		this.operated_time = operated_time;
	}
	public List<AuthRole> getRolename() {
		return rolename;
	}
	public void setRolename(List<AuthRole> rolename) {
		this.rolename = rolename;
	}
	@Override
	public String toString() {
		return "Log [name=" + name + ", username=" + username + ", rolename=" + rolename + ", operated=" + operated
				+ ", operated_time=" + operated_time + "]";
	}
	
	
	
}
