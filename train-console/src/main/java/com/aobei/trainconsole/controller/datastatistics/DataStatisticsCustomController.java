package com.aobei.trainconsole.controller.datastatistics;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 数据统计 <br>
 * 顾客数据统计
 *
 */
@Controller
@RequestMapping("/data_statistics/custom")
public class DataStatisticsCustomController {


    @GetMapping("/index")
    public String index(){

        return "data_statistics/custom";
    }
}
