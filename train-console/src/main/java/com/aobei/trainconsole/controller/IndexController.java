package com.aobei.trainconsole.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

	@GetMapping({ "/", "/index" })
	public String index(HttpServletRequest request, Authentication authentication) {
		if(request.isUserInRole("TMANAGER")){				//培训经理端角色
			return "redirect:/courseTeam/showCourseTeam";
		}else if(request.isUserInRole("ZLJC")){				//质量监控
			return "redirect:/categorySun/showCategorySun";
		}else if(request.isUserInRole("PARTNER")){			//合伙人管理
			return "redirect:/partner/showPartner";
		}
		return "index";
	}
}
