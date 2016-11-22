package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.*;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.WebChineseConfig;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.*;
import cn.com.zhihetech.online.util.ExportExcelUtil;
import cn.com.zhihetech.online.util.SMSUtils;
import cn.com.zhihetech.online.vo.ExportMerchantBill;
import cn.com.zhihetech.online.vo.ExportMerchantBillOrder;
import cn.com.zhihetech.online.vo.ExportMercahntBillOrderDetail;
import cn.com.zhihetech.online.vo.MerchantBillSearch;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by YangDaiChun on 2016/3/14.
 */
@Controller
public class BillingSystemController extends SupportController {

    @Resource(name = "userWithdrawService")
    private IUserWithdrawServie userWithdrawServie;
    @Resource(name = "merchantBillService")
    private IMerchantBillService merchantBillService;
    @Resource(name = "merchantBillDetailService")
    private IMerchantBillDetailService merchantBillDetailService;
    @Resource(name = "merchantBillErrRecordService")
    private IMerchantBillErrRecordService merchantBillErrRecordService;

    /**
     * 商家账单结算页面
     *
     * @return
     */
    @RequestMapping(value = "admin/merchantBilling")
    public String merchantBillingPage() {
        return "admin/billManager/merchantBilling";
    }

    /**
     * 商家账单列表
     *
     * @param request
     * @param billSearch
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/merchantBill/list")
    public PageData<MerchantBill> getMerchantBills(HttpServletRequest request, MerchantBillSearch billSearch) {
        IQueryParams queryParams = this.createQueryParams(request);
        billSearch.initQueryParams(queryParams);
        return this.merchantBillService.getSearchPageData(this.createPager(request), queryParams);
    }

    /**
     * 导出商家账单以及账单中包含的订单
     *
     * @param request
     * @param response
     * @param billSearch
     * @throws IOException
     */
    @RequestMapping(value = "admin/api/merchantBill/export")
    public void exportMerchantBills(HttpServletRequest request, HttpServletResponse response, MerchantBillSearch billSearch) throws IOException {
        IQueryParams queryParams = this.createQueryParams(request);
        billSearch.initQueryParams(queryParams);
        List<ExportMerchantBill> merchantBills = this.merchantBillService.getExportMerchantBill(null, queryParams);
        List<ExportMerchantBillOrder> merchantBillOrders = this.merchantBillDetailService.getExportMerchantBillOrder(billSearch);
        ExportExcelUtil<ExportMerchantBill> exp1 = new ExportExcelUtil<>();
        ExportExcelUtil<ExportMerchantBillOrder> exp2 = new ExportExcelUtil<>();
        String fileName = merchantBills.get(0).getStartAndEndDate();
        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xls");

        String[] headers1 = {"账单号", "商家名", "账单创建时间", "账单所属时间", "产生的营业额", "手续费", "实际应转账", "手续费利率", "商家支付账号", "官方是否处理该账单"};
        String[] headers2 = {"账单号", "商家名", "订单号", "订单支付时间", "支付方式", "是否是秒杀商品订单", "订单支付金额", "包含订单运费", "手续费", "实际应转账", "手续费利率"};
        OutputStream out = response.getOutputStream();
        HSSFWorkbook workbook = null;
        try {
            workbook = new HSSFWorkbook();
            exp1.exportExcel(workbook, "实淘结算单", headers1, merchantBills);
            String merchantName = null;
            List<ExportMerchantBillOrder> merchantBillOrders1 = new LinkedList<>();
            for (ExportMerchantBillOrder merchantBillOrder : merchantBillOrders) {
                if (merchantName == null) {
                    merchantName = merchantBillOrder.getMerchantName();
                }
                String _merchantName = merchantBillOrder.getMerchantName();
                if (!merchantName.equals(_merchantName)) {
                    exp2.exportExcel(workbook, merchantName, headers2, merchantBillOrders1);
                    merchantName = merchantBillOrder.getMerchantName();
                    merchantBillOrders1.clear();
                }
                merchantBillOrders1.add(merchantBillOrder);
            }
            exp2.exportExcel(workbook, merchantName, headers2, merchantBillOrders1);
            workbook.write(out);
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                workbook.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 根据结算单id导出该商家该账单下包含的所有活动订单详情
     *
     * @param billId
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "admin/api/merchantBillOrderDetails/export")
    public void exportMerchantBillOrderDetail(String billId, HttpServletResponse response) throws IOException {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("merchantBill.billId", billId);
        List<ExportMercahntBillOrderDetail> orderDetails = this.merchantBillDetailService.getExportMerchantBillOrderDetail(queryParams);
        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + "zhihe" + ".xls");
        ExportExcelUtil<ExportMercahntBillOrderDetail> orderDetailExp = new ExportExcelUtil<>();
        String[] headers = {"订单号", "买家账号", "买家名字", "购买的商品", "购买的商品数量", "商品的单价", "是否是活动商品", "快递单号"};
        OutputStream out = response.getOutputStream();
        try {
            HSSFWorkbook workbook = new HSSFWorkbook();
            orderDetailExp.exportExcel(workbook, "实淘结算单", headers, orderDetails);
            workbook.write(out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }


    /**
     * 商家账单详细页面
     *
     * @param merchantBillId
     * @param request
     * @return
     */
    @RequestMapping(value = "admin/merchantBill/{id}/merchantBillDetails")
    public ModelAndView merchantBillOrdersPage(@PathVariable(value = "id") String merchantBillId, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("admin/billManager/merchantBillDetails");
        mv.addObject("merchantBillId", merchantBillId);
        return mv;
    }

    @RequestMapping(value = "admin/merchantErrBill")
    public String merchantErrBillPage() {
        return "admin/billManager/merchantErrBillRecord";
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/merchantBillErrRd/list")
    public PageData<MerchantBillErrRecord> getMerchantBillErrRdPgData(HttpServletRequest request) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.sort("handleState", Order.ASC)
                .sort("createDate", Order.DESC);
        return this.merchantBillErrRecordService.getPageData(this.createPager(request), queryParams);
    }

    /**
     * 商家账单详细列表
     *
     * @param merchantBillId
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/merchantBill/{id}/merchantBillDetail/list")
    public PageData<MerchantBillDetail> getMerchantBillOrders(@PathVariable(value = "id") String merchantBillId, HttpServletRequest request) {
        MerchantBill merchantBill = this.merchantBillService.getById(merchantBillId);
        IQueryParams queryParams = this.createQueryParams(request);
        queryParams.andEqual("merchantBill", merchantBill).
                andMoreAndEq("createDate", merchantBill.getStartDate())
                .andLessAndEq("createDate", merchantBill.getEndDate());
        return this.merchantBillDetailService.getPageData(this.createPager(request), queryParams);
    }

    /**
     * 实淘处理商家账单
     *
     * @param merchantBill
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/merchanBill/confirmCheckOut")
    public ResponseMessage confirmCheckOut(MerchantBill merchantBill) {
        this.merchantBillService.executeHandleBill(merchantBill);
        return executeResult();
    }

    /**
     * 用户钱包提现结算页面
     *
     * @return
     */
    @RequestMapping(value = "admin/userBilling")
    public String userBillingPage() {
        return "admin/billManager/userBilling";
    }

    /**
     * 用户提现账单列表
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/user/waitWithdraw/list")
    public PageData<UserWithdraw> getWaitExminUserWithdraw(HttpServletRequest request) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("withdrawState", Constant.APPLY_WITHDRAW).sort("applyDate", Order.ASC);
        PageData<UserWithdraw> userWithdrawPageData = this.userWithdrawServie.getPageData(this.createPager(request), queryParams);
        return userWithdrawPageData;
    }

    /**
     * 用户钱包提现成功
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/userWithDrawOk")
    public ResponseMessage userWithDrawOk(String withDrawId, String aliTransactionCode) {
        UserWithdraw userWithdraw = this.userWithdrawServie.getById(withDrawId);
        if (userWithdraw == null) {
            throw new SystemException("不存在该条记录！");
        }
        if (userWithdraw.getWithdrawState() == Constant.WITHDRAW_OK) {
            throw new SystemException("该条数据已经处理过，请勿重复处理");
        }
        userWithdraw.setAliOrderNum(aliTransactionCode);
        userWithdraw.setWithdrawState(Constant.WITHDRAW_OK);
        userWithdraw.setWithdrawDate(new Date());
        userWithdraw.setOperator(Constant.ZHI_HE_TECH);
        this.userWithdrawServie.update(userWithdraw);
        User user = userWithdraw.getUser();
        this.notifyUserWithdrawOk(user.getUserPhone(), WebChineseConfig.MsgTemplate.USER_WITHDRAW_OK);
        return executeResult();
    }

    /**
     * 发送短信提醒用户提现成功
     *
     * @param mobileNo 手机号码
     * @param msgTxt   短信内容
     */
    protected void notifyUserWithdrawOk(String mobileNo, String msgTxt) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SMSUtils.sendSMS(mobileNo, msgTxt);
                }
            }).start();
        } catch (Exception e) {
            log.error("发送提醒用户提现成功失败", e);
        }
    }

    /**
     * 提现失败
     *
     * @param withDrawId
     * @param withDrawFailureReason
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/userWithDrawFailure")
    public ResponseMessage userWithDrawFailure(String withDrawId, String withDrawFailureReason) {
        this.userWithdrawServie.executeWithDrawFilure(withDrawId, withDrawFailureReason);
        return executeResult();
    }

    /**
     * 商家登录后查看自己的账单
     *
     * @return
     */
    @RequestMapping(value = "admin/financialManager")
    public String indexPage() {
        return "admin/billManager/financialManager";
    }

    @ResponseBody
    @RequestMapping(value = "admin/currentMerchant/merchantBill/list")
    public PageData<MerchantBill> getMerchantBillByCurrentMerchant(HttpServletRequest request, MerchantBillSearch billSearch) {
        String merchantId = this.getCurrentMerchatId(request);
        IQueryParams queryParams = this.createQueryParams(request);
        queryParams.andEqual("merchant.merchantId", merchantId)
                .sort("createDate", Order.DESC);
        billSearch.initQueryParams(queryParams);
        return this.merchantBillService.getSearchPageData(this.createPager(request), queryParams);
    }

}
