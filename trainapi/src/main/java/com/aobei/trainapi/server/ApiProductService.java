package com.aobei.trainapi.server;

import com.aobei.train.model.Product;
import com.github.liyiorg.mbg.bean.Page;
import custom.bean.Evaluate;
import custom.bean.EvaluateBase;
import custom.bean.ProductWithCoupon;

import java.util.List;

public interface ApiProductService {

    /**
     * 获取首页需要进行展示的产品。
     * @return
     */
    List<ProductWithCoupon> homePageProducts(Integer pageIndex, Integer pages, String city_id, Long customer_id);

    /**
     * 获取推荐位的产品列表
     * @return
     */
    List<ProductWithCoupon> recommendProducts(String city_id,Long customer_id);

    /**
     * 获得一个带有指定条数的评价统计基本信息
     * @param num 指定条数
     * @return
     */
    EvaluateBase productEvaluateBase(Long product_id,Integer num);

    /**
     * 分页获取 一定量评价信息
     * @param pageIndex
     * @param pages
     * @return
     */
    Page<Evaluate> productEvaluates(Long product_id,Integer pageIndex, Integer pages,boolean top);

    List<Evaluate> productEvaluatesList(Long product_id, Integer pageIndex, Integer pages);

}
