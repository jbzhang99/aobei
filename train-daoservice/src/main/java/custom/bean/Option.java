package custom.bean;
/**
 * 封装题数据对象
 * @author adminL
 *
 */
public class Option {
	//选项名
	private String t;
	//选项内容
	private String o;
	//是否正确
	private int r;
	
	public String getT() {
		return t;
	}
	public void setT(String t) {
		this.t = t;
	}
	public String getO() {
		return o;
	}
	public void setO(String o) {
		this.o = o;
	}
	public int getR() {
		return r;
	}
	public void setR(int r) {
		this.r = r;
	}
	public Option(String t, String o, int r) {
		super();
		this.t = t;
		this.o = o;
		this.r = r;
	}
	public Option() {
		super();
	}
	
	

}
