package custom.bean;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
/**
 * 封装试卷中每个题
 * @author adminL
 *
 */
public class QuestionPaperJson {
	
	private long id;
	
	private String topic;
	
	private List<Option> option_json;
	
	private String answer;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public List<Option> getOption_json() {
		return option_json;
	}

	public void setOption_json(List<Option> option_json) {
		this.option_json = option_json;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public QuestionPaperJson(long id, String topic, List<Option> option_json, String answer) {
		super();
		this.id = id;
		this.topic = topic;
		this.option_json = option_json;
		this.answer = answer;
	}

	public QuestionPaperJson() {
		super();
	}
	
	
}
