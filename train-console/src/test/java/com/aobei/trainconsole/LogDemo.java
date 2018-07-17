package com.aobei.trainconsole;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional // 自动回滚事务
@ActiveProfiles("dev") // 使用开发环境
public class LogDemo {

	// 声明
	private final static Logger logger = LoggerFactory.getLogger(LogDemo.class);

	/**
	 * 日志语法
	 */
	@Test
	public void test1() {

		// 常规日志
		logger.info("常规文本记录");
		// 占位符日志
		logger.info("占位符日志，{} 欢迎 {} ！", "北京", "你");

		// 代码中不要使用字符串拼接
		logger.warn("禁止使用 ”+” 字符串" + "拼接");

		try {
			Integer.valueOf("%");
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			//禁止使用 e.printStackTrace();
			logger.error("数据转换异常", e);
		}
		
		
		/**
		 * 日志  格式
		 * M[模块标识] F[操作] U[用户uid] others
		 */
		
		logger.info("M[partner] F[save_partner] U[3444353543535] log messages");
		

		// print internal state
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		StatusPrinter.print(lc);
	}

}
