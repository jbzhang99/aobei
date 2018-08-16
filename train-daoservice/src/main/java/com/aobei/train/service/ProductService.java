package com.aobei.train.service;

import java.util.List;
import java.util.Map;

import com.aobei.train.model.Partner;
import com.aobei.train.model.ProSku;
import com.aobei.train.model.Product;
import com.aobei.train.model.ProductExample;
import com.github.liyiorg.mbg.support.service.MbgReadBLOBsService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;
import com.github.liyiorg.mbg.support.service.MbgWriteBLOBsService;
import custom.bean.ProductWithCoupon;

public interface ProductService extends MbgReadBLOBsService<Long, Product, Product, ProductExample>,MbgWriteBLOBsService<Long, Product, Product, ProductExample>,MbgUpsertService<Long, Product, Product, ProductExample>{

	int xAddCommodityProduct(Product product, Map<String, String> params,  Map<String, String> littleParams,String uploadControllerList);

	int xAddSku(Long product_id, String str,String unsetStr);

	int xDownProduct(Long product_id);

	int xUpProduct(Long product_id);

	int xDelProduct(Long product_id);

	int xEditProduct(Product product, Map<String, String> params,Map<String, String> littleParams,String uploadControllerList);

	List<Map<Long, String>> xEditProductAnalysisTime(Map<String, Integer> timeUnis,List<ProSku> proSkuList);

	int xEditSku(Long product_id, String str, String delStr,String unsetStr);

	List<Map<String, Object>> xSelectEditShowTime(String p_service_times);

	List<Map<String, Object>> xSelectCategory(Long cid);

	List<Partner> xScreenPartner(String str,Long proid);

	int xAddOrderPartner(String str,Long product_id);


	List<ProductWithCoupon> xStreamProduct(List<Product> list, Long customer_id);

	/**
	 * 获取单品SKU 的商品id
	 * @return
	 */
	List<Long> xSelectSingleSkuProduct();
}