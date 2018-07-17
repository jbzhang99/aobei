package com.github.liyiorg.mbg.autoconfigure;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = MbgProperties.MBG_PREFIX)
public class MbgProperties {

	public static final String MBG_PREFIX = "mbg";

	public List<InterceptorConfig> interceptors;

	public List<InterceptorConfig> getInterceptors() {
		return interceptors;
	}

	public void setInterceptors(List<InterceptorConfig> interceptors) {
		this.interceptors = interceptors;
	}

	public class InterceptorConfig {

		private String className;
		private boolean enabled;

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

	}
}
