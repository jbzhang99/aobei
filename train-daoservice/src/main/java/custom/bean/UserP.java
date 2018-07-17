package custom.bean;
import java.util.List;

import com.aobei.train.model.SysUsers;

public class UserP extends SysUsers {

	private String username;
	
	private List<String> roleList;

	public List<String> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<String> roleList) {
		this.roleList = roleList;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
