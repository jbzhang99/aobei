package com.aobei.trainconsole.controller.datastatistics;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 数据统计 <br>
 * dashboard概况
 *
 */
@Controller
@RequestMapping("/data_statistics/dashboard")
public class DataStatisticsDashboardController {


    @GetMapping("/index")
    public String index(){

        return "data_statistics/dashboard";
    }
}
