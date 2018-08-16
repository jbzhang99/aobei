package com.aobei.trainconsole.controller;

import com.aobei.train.IdGenerator;
import com.aobei.train.model.*;
import com.aobei.train.service.*;
import com.github.liyiorg.mbg.bean.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/group_purchase")
public class GroupPurchaseController {

    private static Logger logger = LoggerFactory.getLogger(GroupPurchaseController.class);

    @Autowired
    private GroupPurchaseService groupPurchaseService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private ProSkuService proSkuService;


    @InitBinder
    protected void initBinder(WebDataBinder binder) throws Exception {
        binder.registerCustomEditor(
                Date.class,
                new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
    }

    @GetMapping("/list")
    public String list(
            @RequestParam(defaultValue = "1", required = false) Integer p,
            @RequestParam(defaultValue = "10") Integer ps,
            ModelMap modelMap) {
        GroupPurchaseExample example = new GroupPurchaseExample();
        example.setOrderByClause(GroupPurchaseExample.C.create_time + " desc");
        Page<GroupPurchase> page = groupPurchaseService.selectByExample(example, p, ps);
        modelMap.addAttribute("page", page);

        if (page.getList().size() > 0) {
            List<Long> ids = page.getList().stream().map(n -> n.getProductId()).collect(Collectors.toList());
            ProductExample productExample = new ProductExample();
            productExample.or().andProduct_idIn(ids);
            // 获取商品列表
            List<Product> products = productService.selectByExample(productExample);
            Map<Long, String> productNameMap = products.stream().collect(Collectors.toMap(Product::getProduct_id, Product::getName));
            modelMap.addAttribute("productNameMap", productNameMap);
        }
        return "group_purchase/list";
    }

    @GetMapping("/to_update")
    public String toUpdate(@RequestParam(required = false) Long id,
                           ModelMap modelMap) {
        if (id != null) {
            GroupPurchase groupPurchase = groupPurchaseService.selectByPrimaryKey(id);
            modelMap.addAttribute("groupPurchase", groupPurchase);
        }
        List<Long> ids = productService.xSelectSingleSkuProduct();
        ProductExample productExample = new ProductExample();
        productExample.or().andProduct_idIn(ids);
        // 获取商品列表
        List<Product> products = productService.selectByExample(productExample);
        modelMap.addAttribute("products", products);

        CouponExample couponExample = new CouponExample();

        couponExample.or()
                // 仅派发卷
                .andTypeEqualTo(1)
                // 已审核
                .andValidEqualTo(2)
                // 使用日期大于当前日期
                .andUse_end_datetimeGreaterThan(new Date());

        // 获取优惠卷列表
        List<Coupon> coupons = couponService.selectByExample(couponExample);
        modelMap.addAttribute("coupons", coupons);

        return "group_purchase/update";
    }

    @PostMapping("/update")
    public String update(
            Authentication authentication,
            @ModelAttribute GroupPurchase groupPurchase) {
        Users user = usersService.xSelectUserByUsername(authentication.getName());

        ProSkuExample proSkuExample = new ProSkuExample();
        proSkuExample.or().andProduct_idEqualTo(groupPurchase.getProductId());
        List<ProSku> proSkuList = proSkuService.selectByExample(proSkuExample);

        if (groupPurchase.getId() != null) {
            // 更新数据
            GroupPurchaseExample example = new GroupPurchaseExample();
            example.or()
                    .andIdEqualTo(groupPurchase.getId())
                    .andOverOnlineEqualTo(1);
            long count = groupPurchaseService.countByExample(example);
            logger.info("M[GROUP_PURCHASE]F[UPDATE]U[{}] DATA:{} OverOnline:{}", user.getUser_id(), groupPurchase, count > 0);
            if (count == 0) {
                groupPurchase.setPskuId(proSkuList.get(0).getPsku_id());
                groupPurchase.setUpdateTime(new Date());
                groupPurchaseService.updateByPrimaryKeySelective(groupPurchase);
            }
        } else {
            // 添加数据
            logger.info("M[GROUP_PURCHASE]F[INSERT]U[{}] DATA:{}", user.getUser_id(), groupPurchase);
            groupPurchase.setId(IdGenerator.generateId());
            groupPurchase.setPskuId(proSkuList.get(0).getPsku_id());
            Date date = new Date();
            groupPurchase.setCreateTime(date);
            groupPurchase.setUpdateTime(date);
            groupPurchaseService.insertSelective(groupPurchase);
        }
        return "redirect:/group_purchase/list";
    }

    /**
     * 更新在线状态
     *
     * @param id
     * @param online
     * @return
     */
    @PostMapping("/updateOnline")
    @ResponseBody
    public String updateOnline(
            Long id,
            int online) {
        GroupPurchase groupPurchase = new GroupPurchase();
        groupPurchase.setId(id);
        groupPurchase.setOnline(online);
        if (online == 1) {
            groupPurchase.setOverOnline(1);
        }
        groupPurchase.setUpdateTime(new Date());
        int count = groupPurchaseService.updateByPrimaryKeySelective(groupPurchase);
        return String.format("{\"status\":%b}", count == 1);
    }

}
