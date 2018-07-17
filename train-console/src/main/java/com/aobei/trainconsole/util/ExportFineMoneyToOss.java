package com.aobei.trainconsole.util;

import com.aobei.train.IdGenerator;
import com.aobei.train.model.*;
import com.aobei.train.service.*;
import custom.bean.DeductMoneyInfo;
import custom.bean.FineMoneyInfo;
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
public class ExportFineMoneyToOss implements Callable<Integer> {

    private Long data_download_id;

    private FineMoneyExample fineMoneyExample;

    public Long getData_download_id() {
        return data_download_id;
    }

    public void setData_download_id(Long data_download_id) {
        this.data_download_id = data_download_id;
    }

    public FineMoneyExample getFineMoneyExample() {
        return fineMoneyExample;
    }

    public void setFineMoneyExample(FineMoneyExample fineMoneyExample) {
        this.fineMoneyExample = fineMoneyExample;
    }

    @Autowired
    private FineMoneyService fineMoneyService;

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
        List<FineMoney> fineMoneyList = fineMoneyService.selectByExample(fineMoneyExample);
        List<Partner> partners = partnerService.selectByExample(new PartnerExample());
        Map<Long, Partner> partnerMap = partners.stream().collect(Collectors.toMap(Partner::getPartner_id, Function.identity()));
        List<FineMoneyInfo> infos = fineMoneyList.stream().map(n -> {
            Order order = orderService.selectByPrimaryKey(n.getPay_order_id());
            ServiceUnit unit = serviceUnitService.selectByPrimaryKey(n.getServiceunit_id());
            Users users = usersService.selectByPrimaryKey(n.getFine_operator());
            FineMoneyInfo fineMoneyInfo = new FineMoneyInfo();
            if (n.getConfirm_operator()!= null){
                fineMoneyInfo.setConfirm_operators(usersService.selectByPrimaryKey(n.getConfirm_operator()));
            }
            fineMoneyInfo.setOrder(order);
            fineMoneyInfo.setUnit(unit);
            fineMoneyInfo.setApply_operator(users);
            BeanUtils.copyProperties(n, fineMoneyInfo);
            return fineMoneyInfo;
        }).collect(Collectors.toList());

        String[] rowsName = new String[]{"罚款单号", "订单号", "服务名称","用户姓名","顾客电话",
                                    "服务地址","服务价格","实付金额","拒单类型", "拒单合伙人",
                                    "拒单罚款申请人","申请罚款原因","申请罚款金额","申请时间","罚款单状态",
                                    "确认时间","确认人"};
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        List<Object[]> dataList = infos.stream().map(n ->{
            Integer Fine_status = n.getFine_status();
            String Fine_status_describe = null;
            switch (Fine_status) {
                case 1:
                    Fine_status_describe = "待处理";
                    break;
                case 2:
                    Fine_status_describe = "完成";
                    break;
                case 3:
                    Fine_status_describe = "驳回";
                    break;
                default:
                    Fine_status_describe = "";
                    break;
            }
            Object[] obj = {n.getFine_money_id(),n.getPay_order_id(),n.getOrder().getName(),n.getOrder().getCus_username(),
                            n.getOrder().getCus_phone(),n.getOrder().getCus_address(),n.getOrder().getPrice_total()/100d + "",n.getOrder().getPrice_pay()/100d + "",
                            n.getUnit().getReject_type() == null ? "" : (n.getUnit().getReject_type() == 0 ? "超时拒单" : "主动拒单"),partnerMap.get(n.getUnit().getPartner_id()).getName(),
                            n.getApply_operator().getUsername(),n.getFine_info(),n.getFine_amount()/100d + "",n.getCreate_datetime() == null ? "" : sdf.format(n.getCreate_datetime()),
                            Fine_status_describe,n.getConfirm_datetime() == null ? "" : sdf.format(n.getConfirm_datetime()),
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
