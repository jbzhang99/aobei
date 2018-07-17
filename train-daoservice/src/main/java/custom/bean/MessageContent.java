package custom.bean;


import com.aobei.train.model.Message;

import java.util.List;

public class MessageContent extends Message{

	private ContentMsg contentMsg;
	private Long message_id;
	private String notifyDateTimeString;

	public String getNotifyDateTimeString() {
		return notifyDateTimeString;
	}

	public void setNotifyDateTimeString(String notifyDateTimeString) {
		this.notifyDateTimeString = notifyDateTimeString;
	}

	public Long getMessage_id() {
		return message_id=getId();
	}

	public void setMessage_id(Long message_id) {
		this.message_id = message_id;
	}

	public ContentMsg getContentMsg() {
		return contentMsg;
	}



	public void setContentMsg(ContentMsg contentMsg) {
		this.contentMsg = contentMsg;
	}



public static class ContentMsg{
	    private String msgtype;
		private String v;
		private	String content;
		private String sign_name;
		private String template;
		private String params;
		private String phone;
		private String href;
		private String url;
		private String title;
		private Integer types;
		private List<String> urlList;
		private Integer noticeTypes;

	public Integer getNoticeTypes() {
		return noticeTypes;
	}

	public void setNoticeTypes(Integer noticeTypes) {
		this.noticeTypes = noticeTypes;
	}

	public List<String> getUrlList() {
		return urlList;
	}

	public void setUrlList(List<String> urlList) {
		this.urlList = urlList;
	}

	public Integer getTypes() {
		return types;
	}

	public void setTypes(Integer types) {
		this.types = types;
	}

	public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		
		public String getMsgtype() {
			return msgtype;
		}
		public void setMsgtype(String msgtype) {
			this.msgtype = msgtype;
		}
		public String getV() {
			return v;
		}
		public void setV(String v) {
			this.v = v;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public String getSign_name() {
			return sign_name;
		}
		public void setSign_name(String sign_name) {
			this.sign_name = sign_name;
		}
		public String getTemplate() {
			return template;
		}
		public void setTemplate(String template) {
			this.template = template;
		}
		public String getParams() {
			return params;
		}
		public void setParams(String params) {
			this.params = params;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public String getHref() {
			return href;
		}
		public void setHref(String href) {
			this.href = href;
		}
   }
}
