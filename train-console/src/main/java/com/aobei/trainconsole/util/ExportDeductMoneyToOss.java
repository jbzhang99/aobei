package com.aobei.trainconsole.util;

import com.aobei.train.IdGenerator;
import com.aobei.train.model.*;
import com.aobei.train.service.*;
import custom.bean.DeductMoneyInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by mr_bl on 2018/7/2.
 */
@Component
public class ExportDeductMoneyToOss implements Callable<Integer> {

    private Long data_download_id;

    private DeductMoneyExample deductMoneyExample;

    public Long getData_download_id() {
        return data_download_id;
    }

    public void setData_download_id(Long data_download_id) {
        this.data_download_id = data_download_id;
    }

    public DeductMoneyExample getDeductMoneyExample() {
        return deductMoneyExample;
    }

    public void setDeductMoneyExample(DeductMoneyExample deductMoneyExample) {
        this.deductMoneyExample = deductMoneyExample;
    }

    @Autowired
    private DeductMoneyService deductMoneyService;

    @Autowired
    private MyFileHandleUtil myFileHandleUtil;

    @Autowired
    private DataDownloadService dataDownloadService;

    @Autowired
    private OssFileService ossFileService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ServiceUnitService serviceUnitService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private PartnerService partnerService;

    @Override
    public Integer call() throws Exception {
        List<DeductMoney> deductMoneyList = deductMoneyService.selectByExample(deductMoneyExample);
        List<Partner> partners = partnerService.selectByExample(new PartnerExample());
        Map<Long, Partner> partnerMap = partners.stream().collect(Collectors.toMap(Partner::getPartner_id, Function.identity()));
        List<DeductMoneyInfo> infos = deductMoneyList.stream().map(n -> {
            Order order = orderService.selectByPrimaryKey(n.getPay_order_id());
            ServiceUnit unit = serviceUnitService.selectByPrimaryKey(n.getServiceunit_id());
            Users users = usersService.selectByPrimaryKey(n.getDeduct_operator());
            DeductMoneyInfo deductMoneyInfo = new DeductMoneyInfo();
            if (n.getConfirm_operator()!= null){
                deductMoneyInfo.setConfirm_operators(usersService.selectByPrimaryKey(n.getConfirm_operator()));
            }
            deductMoneyInfo.setOrder(order);
            deductMoneyInfo.setUnit(unit);
            deductMoneyInfo.setApply_operator(users);
            BeanUtils.copyProperties(n, deductMoneyInfo);
            return deductMoneyInfo;
        }).collect(Collectors.toList());

        String[] rowsName = new String[]{"扣款单号", "订单号", "订单状态", "服务名称","用户姓名",
                                    "顾客电话","服务地址","服务价格","实付金额","扣款金额",
                                    "扣款原因","合伙人公司名","服务人员","扣款申请人","扣款申请时间",
                                    "扣款申请状态","确认时间","确认人"};
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        List<Object[]> dataList = infos.stream().map(n ->{
            String order_status = null;
            Integer o_status_active = n.getOrder().getStatus_active();
            Integer r_status = n.getOrder().getR_status();
            Integer u_status_active = n.getUnit().getStatus_active();
            if (o_status_active != null){
                switch (o_status_active){
                    case 1:
                        order_status = "待付款";
                        break;
                    case 2:
                        if (u_status_active != null && u_status_active.equals(new Integer(6))){
                            order_status = "已拒单";
                        }else{
                            order_status = "待确认";
                        }
                        break;
                    case 3:
                        order_status = "待服务";
                        break;
                    case 4:
                        if (r_status != null && r_status.equals(new Integer(2))){
                            order_status = "已退款";
                        }else{
                            order_status = "已取消";
                        }
                        break;
                    case 5:
                        order_status = "已完成";
                        break;
                }
            }
            Integer deduct_status = n.getDeduct_status();
            String deduct_status_describe = null;
            switch (deduct_status) {
                case 1:
                    deduct_status_describe = "待处理";
                    break;
                case 2:
                    deduct_status_describe = "完成";
                    break;
                case 3:
                    deduct_status_describe = "驳回";
                    break;
                default:
                    deduct_status_describe = "";
                    break;
            }
            Object[] obj = {n.getDeduct_money_id(),n.getPay_order_id(),order_status,n.getOrder().getName(),n.getOrder().getCus_username(),
                            n.getOrder().getCus_phone(),n.getOrder().getCus_address(),n.getOrder().getPrice_total()/100d + "",n.getOrder().getPrice_pay()/100d + "",
                            n.getDeduct_amount()/100d + "",n.getDeduct_info(),partnerMap.get(n.getUnit().getPartner_id()).getName(),n.getUnit().getStudent_name(),
                            n.getApply_operator().getUsername(),n.getCreate_datetime() == null ? "" : sdf.format(n.getCreate_datetime()),
                            deduct_status_describe,n.getConfirm_datetime() == null ? "" : sdf.format(n.getConfirm_datetime()),
                            n.getConfirm_operators() == null ? "":n.getConfirm_operators().getUsername()};
            return obj;
        }).collect(Collectors.toList());

        WriteExcel ex = new WriteExcel(rowsName, dataList);
        Map<String, String> file_map = myFileHandleUtil.file_upload(ex.export(), PathUtil.PathType.file_order_excel);
        OssFile ossFile = new OssFile();
        ossFile.setOss_file_id(IdGenerator.generateId());
        ossFile.setName(file_map.get("file_name"));
        ossFile.setUrl(file_map.get("file_url"));
        ossFile.setFormat(file_map.get("file_format"));
        ossFile.setCreate_time(new Date());
        ossFile.setBucket(file_map.get("bucketName"));
        ossFile.setAccess_permissions("private");
        ossFileService.insertSelective(ossFile);
//		Thread.sleep(30000);
        DataDownload dataDownload = new DataDownload();
        dataDownload.setData_download_id(data_download_id);
        dataDownload.setOss_file(file_map.get("file_url"));
        dataDownload.setStatus(1);
        int i = dataDownloadService.updateByPrimaryKeySelective(dataDownload);
        return i;
    }
}
