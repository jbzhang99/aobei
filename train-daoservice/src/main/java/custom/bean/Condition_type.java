package custom.bean;

import java.util.List;

public class Condition_type {
	
	private String title;
	private Integer value;
	private List<Long> list_product;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public List<Long> getList_product() {
		return list_product;
	}
	public void setList_product(List<Long> list_product) {
		this.list_product = list_product;
	}
	
}
