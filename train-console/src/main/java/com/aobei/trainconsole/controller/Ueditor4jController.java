package com.aobei.trainconsole.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baidu.ueditor.ActionEnter;
import com.baidu.ueditor.ConfigManager;

@Controller
public class Ueditor4jController {
	
	@Autowired
	private Environment env;

	/**
	 * 成绩信息列表展示
	 * 
	 * @param model
	 * @param p
	 * @param partnerId
	 * @param course
	 * @param conditions
	 * @param ps
	 * @return
	 */
	@RequestMapping(path = { "/ueditorUpload" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String ueditor(HttpServletRequest request) {
		//生产环境
		boolean isPro = env.acceptsProfiles("pro");
		return new ActionEnter(request, getConfigManager(request, isPro?"product":"temp/product")).exec();
	}

	private ConfigManager getConfigManager(HttpServletRequest request, String fileStorePath) {
		ServletContext application = request.getSession().getServletContext();
		Object object = application.getAttribute("UEDITOR_configManager");
		if (object != null) {
			return (ConfigManager) object;
		} else {
			String contextPath = request.getContextPath();
			String uri = request.getRequestURI();
			String rootPath = application.getRealPath("/");
			ConfigManager configManager = ConfigManager.getInstance(fileStorePath, rootPath, contextPath, uri);
			request.getSession().getServletContext().setAttribute("UEDITOR_configManager", configManager);
			return configManager;
		}
	}
}
