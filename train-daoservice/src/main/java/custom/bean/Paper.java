package custom.bean;

import java.util.List;
/**
 * 封装试卷对象
 * @author adminL
 *
 */
public class Paper {
	

	//单选题集合
	private List<QuestionPaperJson> t1;
	//多选题集合
	private List<QuestionPaperJson> t2;
	//判断题集合
	private List<QuestionPaperJson> t3;

	
	
	public List<QuestionPaperJson> getT1() {
		return t1;
	}

	public void setT1(List<QuestionPaperJson> t1) {
		this.t1 = t1;
	}

	public List<QuestionPaperJson> getT2() {
		return t2;
	}

	public void setT2(List<QuestionPaperJson> t2) {
		this.t2 = t2;
	}

	public List<QuestionPaperJson> getT3() {
		return t3;
	}

	public void setT3(List<QuestionPaperJson> t3) {
		this.t3 = t3;
	}

	public Paper(List<QuestionPaperJson> t1, List<QuestionPaperJson> t2, List<QuestionPaperJson> t3) {
		super();
		this.t1 = t1;
		this.t2 = t2;
		this.t3 = t3;
	}

	public Paper() {
		super();
	}

	
	
}
