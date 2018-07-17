package custom.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 服务单备注类
 * @author mr_bl
 *
 */
public class Remark implements Serializable {
	private Date d;
	private String remark;
	private String operator_name;
	private Long user_id;
	
	public Date getD() {
		return d;
	}
	public void setD(Date d) {
		this.d = d;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getOperator_name() {
		return operator_name;
	}
	public void setOperator_name(String operator_name) {
		this.operator_name = operator_name;
	}
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	
	
}
