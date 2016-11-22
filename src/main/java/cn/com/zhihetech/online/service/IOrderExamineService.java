package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.OrderExamine;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

/**
 * Created by ShenYunjie on 2016/6/29.
 */
public interface IOrderExamineService extends UpgradedService<OrderExamine> {

    /**
     * 生成对账单数据
     *
     * @param workbook  微信（支付宝）账单文件
     * @param batchCode 平台账单批次
     * @param payType   支付方式（微信支付或支付宝支付）
     */
    void executeCreateExamineBill(Workbook workbook, String batchCode, String payType);
}
