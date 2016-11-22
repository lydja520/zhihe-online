package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.*;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.dao.*;
import cn.com.zhihetech.online.exception.NotFoundException;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IActivityService;
import cn.com.zhihetech.online.service.IRedEnvelopService;
import cn.com.zhihetech.online.util.PropertiesUtils;
import cn.com.zhihetech.online.util.RedEnvelopUtils;
import cn.com.zhihetech.online.vo.ExportRedEnvelopStatistics;
import cn.com.zhihetech.util.common.GeneratedNRandom;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by ShenYunjie on 2015/12/11.
 */
@Service("redEnvelopService")
public class RedEnvelopServiceImpl implements IRedEnvelopService {
    @Resource(name = "redEnvelopDao")
    private IRedEnvelopDao redEnvelopDao;
    @Resource(name = "adminDao")
    private IAdminDao adminDao;
    @Resource(name = "redEnvelopItemDao")
    private IRedEnvelopItemServiceDao redEnvelopItemServiceDao;
    @Resource(name = "activityService")
    private IActivityService activityService;
    @Resource(name = "focusMerchantDao")
    private IFocusMerchantDao focusMerchantDao;
    @Resource(name = "activityOrderDao")
    private IActivityOrderDao activityOrderDao;
    @Resource(name = "activityOrderDetailDao")
    private IActivityOrderDetailDao activityOrderDetailDao;

    /**
     * 根据ID获取持久化对象
     *
     * @param id 对象ID
     * @return
     */
    @Override
    public RedEnvelop getById(String id) {
        return this.redEnvelopDao.findEntityById(id);
    }

    /**
     * 删除持久化对象
     *
     * @param redEnvelop 需要删除的持久化对象
     */
    //TODO:删除一个红包，同时也删除掉对应的红包订单详细
    @Override
    public void delete(RedEnvelop redEnvelop) {
/*        IQueryParams queryParams = new GeneralQueryParams().andEqual("envelopId", redEnvelop.getEnvelopId());
        List<Object> objects = this.redEnvelopDao.getProperty("activity.activitId", null, queryParams);*/
        redEnvelop = this.redEnvelopDao.findEntityById(redEnvelop.getEnvelopId());
        if (redEnvelop == null) {
            throw new NotFoundException("红包已被删除或不存在！");
        } else if (!this.activityService.isEditAble(new Activity(redEnvelop.getActivity().getActivitId()))) {
            throw new SystemException("活动已提交或已开始不能删除");
        } else if (redEnvelop.isPayState()) {
            throw new SystemException("该活动红包已经支付，不支持删除操作！");
        }
        IQueryParams queryParams = new GeneralQueryParams().andEqual("redEnvelop", redEnvelop);
        this.redEnvelopItemServiceDao.executeDelete(queryParams);
        queryParams = new GeneralQueryParams().andEqual("envelopId", redEnvelop.getEnvelopId());
        this.redEnvelopDao.executeDelete(queryParams);
        queryParams = new GeneralQueryParams();
        queryParams.andEqual("activityId", redEnvelop.getActivity().getActivitId())
                .andEqual("merchantId", redEnvelop.getMerchant().getMerchantId())
                .andEqual("orderType", ActivityOrderDetail.OrderType.redEvelop)
                .andEqual("orderTypeId", redEnvelop.getEnvelopId());
        this.activityOrderDetailDao.executeDelete(queryParams);
    }

    /**
     * 添加一个对象到数据库
     *
     * @param redEnvelop 需要持久化的对象
     * @return
     */
    //TODO:添加一个红包的同时，为红包生成一个对应的订单详情
    @Override
    public RedEnvelop add(RedEnvelop redEnvelop) {
        String activitId = redEnvelop.getActivity().getActivitId();
        Activity activity = this.activityService.getById(activitId);
        if (activity == null) {
            throw new SystemException("活动不存在！");
        }
        if (!activityService.isEditAble(activity)) {
            throw new SystemException("当前活动状态不支持添加操作");
        }
        Date createDate = new Date();
        redEnvelop.setCreateDate(createDate);
        redEnvelop.setSended(false);
        redEnvelop.setPayState(false);
        this.redEnvelopDao.saveEntity(redEnvelop);
        ActivityOrderDetail activityOrderDetail = new ActivityOrderDetail();
        activityOrderDetail.setActivityOrder(null);
        activityOrderDetail.setMoney(redEnvelop.getTotalMoney());
        activityOrderDetail.setPayState(false);
        activityOrderDetail.setOrderType(ActivityOrderDetail.OrderType.redEvelop);
        activityOrderDetail.setOrderTypeId(redEnvelop.getEnvelopId());
        activityOrderDetail.setMerchantId(redEnvelop.getMerchant().getMerchantId());
        activityOrderDetail.setActivityId(redEnvelop.getActivity().getActivitId());
        this.activityOrderDetailDao.saveEntity(activityOrderDetail);
        List<RedEnvelopItem> redEnvelopItems = this.redEnvelopArithmetic(redEnvelop);
        for (RedEnvelopItem redEnvelopItem : redEnvelopItems) {
            this.redEnvelopItemServiceDao.saveEntity(redEnvelopItem);
        }
        return redEnvelop;
    }

    /**
     * 红包算法
     *
     * @param redEnvelop
     * @return
     */
    private List<RedEnvelopItem> redEnvelopArithmetic(RedEnvelop redEnvelop) {
        List<RedEnvelopItem> redEnvelopItems = new LinkedList<>();
        float totalMoney = redEnvelop.getTotalMoney();
        int number = redEnvelop.getNumbers();
        List<Float> redItems = RedEnvelopUtils.getRedMoneies(totalMoney, number);
        for (int i = 0; i < number; i++) {
            RedEnvelopItem redEnvelopItem = new RedEnvelopItem();
            redEnvelopItem.setRedEnvelop(redEnvelop);
            redEnvelopItem.setAmountOfMoney(redItems.get(i));
            redEnvelopItem.setReceived(false);
            redEnvelopItem.setUser(null);
            redEnvelopItem.setExtractState(false);
            redEnvelopItems.add(redEnvelopItem);
        }
        return redEnvelopItems;
    }

    /**
     * 红包修改
     *
     * @param envelopId  红包ID
     * @param totalMoney 红包金额
     * @param numbers    红包发放个数
     * @param envelopMsg 红包留言
     */
    @Override
    public void updateBaseInfo(String envelopId, float totalMoney, int numbers, String envelopMsg) {
        RedEnvelop redEnvelop = this.redEnvelopDao.findEntityById(envelopId);

        if (redEnvelop == null) {
            throw new NotFoundException("红包不存在或已被删除");
        }
        if (!this.activityService.isEditAble(redEnvelop.getActivity())) {
            throw new SystemException("当前活动已提交或已审核，不能修改！");
        }
        if (redEnvelop.isPayState()) {
            throw new SystemException("该活动红包已经支付，不支持修改操作！");
        }
        redEnvelop.setEnvelopMsg(envelopMsg);
        redEnvelop.setTotalMoney(totalMoney);
        redEnvelop.setNumbers(numbers);
        this.redEnvelopDao.updateEntity(redEnvelop);
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("activityId", redEnvelop.getActivity().getActivitId())
                .andEqual("merchantId", redEnvelop.getMerchant().getMerchantId())
                .andEqual("orderType", ActivityOrderDetail.OrderType.redEvelop)
                .andEqual("orderTypeId", redEnvelop.getEnvelopId());
        Map<String, Object> paramAndValue = new HashMap<>();
        paramAndValue.put("money", redEnvelop.getTotalMoney());
        this.activityOrderDetailDao.executeUpdate(paramAndValue, queryParams);
        queryParams = new GeneralQueryParams().andEqual("redEnvelop.envelopId", envelopId);
        this.redEnvelopItemServiceDao.executeDelete(queryParams);
        List<RedEnvelopItem> redEnvelopItems = this.redEnvelopArithmetic(redEnvelop);
        for (RedEnvelopItem redEnvelopItem : redEnvelopItems) {
            this.redEnvelopItemServiceDao.saveEntity(redEnvelopItem);
        }
    }


    /**
     * 更新一个持久化对象
     *
     * @param redEnvelop
     */
    @Override
    public void update(RedEnvelop redEnvelop) {

    }

    /**
     * 根据分页参数和查询条件查询持久化对象
     *
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return
     */
    @Override
    public List<RedEnvelop> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.redEnvelopDao.getEntities(pager, queryParams);
    }

    /**
     * 根据分页参数和查询条件查询持久化对象
     *
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return 经过分页的数据
     */
    @Override
    public PageData<RedEnvelop> getPageData(Pager pager, IQueryParams queryParams) {
        return this.redEnvelopDao.getPageData(pager, queryParams);
    }

    /**
     * 获取指定用户对应商家的红包
     *
     * @param pager
     * @param queryParams
     * @param currentAdmin
     */
    @Override
    public PageData<RedEnvelop> getPageDataByAdmin(Pager pager, GeneralQueryParams queryParams, Admin currentAdmin) {
        IQueryParams query = new GeneralQueryParams().andEqual("adminId", currentAdmin.getAdminId());
        List<Object> merchIds = this.adminDao.getProperty("merchant.merchantId", null, query);
        if (!queryParams.queryContainsKey("merchant.merchantId")) {
            queryParams.andEqual("merchant.merchantId", merchIds.get(0));
        }
        return this.redEnvelopDao.getPageData(pager, queryParams);
    }

    @Override
    public boolean isEditable(RedEnvelop redEnvelop) {
        return false;
    }

    @Override
    public Map<String, List<RedEnvelop>> getAbleRedEnvelopByMerch(Pager pager, List<Merchant> merchants, String activityId) {
        //TODO:得到每一个商家所添加的全部红包
        Map<String, List<RedEnvelop>> allMerchentRedEnvelop = new HashMap<String, List<RedEnvelop>>();
        for (Merchant merchant : merchants) {
            IQueryParams queryParams = new GeneralQueryParams();
            List<RedEnvelop> oneMerchentRedEnvelop = new ArrayList<RedEnvelop>(
                    this.redEnvelopDao.getEntities(queryParams.andEqual("merchant.merchantId", merchant.getMerchantId()).andEqual("activity.activitId", activityId))
            );
            allMerchentRedEnvelop.put(merchant.getMerchantId(), oneMerchentRedEnvelop);
        }
        return allMerchentRedEnvelop;
    }

    @Override
    public void executeUpdate(Map<String, Object> paramAndValue, IQueryParams queryParams) {
        this.redEnvelopDao.executeUpdate(paramAndValue, queryParams);
    }

    /**
     * 用户抢红包算法
     *
     * @param envelopId 红包ID
     * @param userId    用户ID
     * @return
     */
    @Override
    public ResponseMessage updateGrabRedEnvelop(String envelopId, String userId) {
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("envelopId", envelopId);
        List<Object> objects = this.redEnvelopDao.getProperty("sended", null, queryParams);
        if (objects == null || objects.size() < 1) {
            return new ResponseMessage(ResponseStatusCode.RED_ENVELOP_FINISHED, true, "红包还未发送、发送失败或已过期，无法领取！");
        }
        if (!Boolean.valueOf(objects.get(0).toString())) {
            return new ResponseMessage(ResponseStatusCode.RED_ENVELOP_FINISHED, true, "领取红包失败，请重试；如多次失败则此红包可能已发送失败！");
        }

        queryParams = new GeneralQueryParams()
                .andEqual("redEnvelop.envelopId", envelopId)
                .andEqual("user.userId", userId);
        List<RedEnvelopItem> redEnvelopItems1 = this.redEnvelopItemServiceDao.getEntities(null, queryParams);
        if (redEnvelopItems1.size() > 0) {
            RedEnvelopItem redEnvelopItem = redEnvelopItems1.get(0);
            ResponseMessage responseMessage = new ResponseMessage(ResponseStatusCode.RED_ENVELOP_ALREADY_RECEIVED, true, "你已经领取了红包！");
            responseMessage.setAttribute(redEnvelopItem);
            return responseMessage;
        }

        int count = this.redEnvelopItemServiceDao.executeGrab(envelopId, userId, new Date());
        if (count < 1) {
            ResponseMessage responseMessage = new ResponseMessage(ResponseStatusCode.RED_ENVELOP_FINISHED, true, "Sorry！红包已经被抢光了！");
            responseMessage.setAttribute(null);
            return responseMessage;
        }
        queryParams = new GeneralQueryParams()
                .andEqual("redEnvelop.envelopId", envelopId)
                .andEqual("user.userId", userId);
        List<RedEnvelopItem> redEnvelopItems = this.redEnvelopItemServiceDao.getEntities(queryParams);
        RedEnvelopItem redEnvelopItem = redEnvelopItems.get(0);

        ResponseMessage responseMessage = new ResponseMessage(ResponseStatusCode.SUCCESS_CODE, true, "恭喜你，成功抢到红包！");
        responseMessage.setAttribute(redEnvelopItem);
        return responseMessage;
    }

    /**
     * 通过查询条件得到所有红包统计ToExcel
     *
     * @param queryParams 商家名，活动名，红包创建时间段
     * @return
     */
    @Override
    public List<ExportRedEnvelopStatistics> getExportRedEnvelopList(IQueryParams queryParams) {

        long total = this.redEnvelopDao.getRecordTotal(queryParams);
        if (total > Integer.parseInt(PropertiesUtils.getProperties().get("EXPORT_DATA_MAX_RECORD").toString())) {
            throw new SystemException("生成excel失败！原因：根据当前查询条件查询出来的数据太多，无法无法生成excel表格！");
        }

        List<ExportRedEnvelopStatistics> exportRedEnvelopStatisticsesList = new ArrayList<>();

        List<RedEnvelop> redEnvelopList = this.redEnvelopDao.getEntities(queryParams);
        for (RedEnvelop redEnvelop : redEnvelopList) {
            IQueryParams redEnvelopItemQueryParams = new GeneralQueryParams();
            redEnvelopItemQueryParams.andEqual("redEnvelop.envelopId", redEnvelop.getEnvelopId());
            redEnvelopItemQueryParams.andEqual("received", true);
            List<RedEnvelopItem> redEnvelopItemList = this.redEnvelopItemServiceDao.getEntities(redEnvelopItemQueryParams);

            float receivedTotalMoney = 0;//领取红包金额
            int receivedTotal=0;//领取红包个数
            float surplusMoney = 0;//剩余红包金额
            int  surplusTotal = 0;//剩余红包个数

            /*==========得到领取的红包个数和金额==========*/
            if (redEnvelopItemList.size()!=0) {
                receivedTotal = redEnvelopItemList.size();
                for (RedEnvelopItem redEnvelopItem : redEnvelopItemList) {
                    BigDecimal b1 = new BigDecimal(Float.toString(receivedTotalMoney));
                    BigDecimal b2 = new BigDecimal(Float.toString(redEnvelopItem.getAmountOfMoney()));
                    receivedTotalMoney = b1.add(b2).floatValue();
                }
            }
            /*========得到剩余红包金额=========*/
            BigDecimal b3 = new BigDecimal(Float.toString(redEnvelop.getTotalMoney()));
            BigDecimal b4 = new BigDecimal(Float.toString(receivedTotalMoney));
            surplusMoney = b3.subtract(b4).floatValue();
            surplusMoney = new BigDecimal(surplusMoney).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();

            surplusTotal = redEnvelop.getNumbers()-receivedTotal;


            /*=================设置针对Excel的红包统计对象================*/
            ExportRedEnvelopStatistics exportRedEnvelopStatistics = new ExportRedEnvelopStatistics();

            exportRedEnvelopStatistics.setMerchName(redEnvelop.getMerchant().getMerchName());
            exportRedEnvelopStatistics.setActivitName(redEnvelop.getActivity().getActivitName());
            exportRedEnvelopStatistics.setCreateDate(redEnvelop.getCreateDate());
            String payState = null;
            if(redEnvelop.isPayState()){
                payState = "是";
            }else{
                payState = "否";
            }
            String sended = null;
            if(redEnvelop.isSended()){
                sended = "是";
            }else{
                sended = "否";
            }
            exportRedEnvelopStatistics.setPayState(payState);
            exportRedEnvelopStatistics.setSended(sended);
            exportRedEnvelopStatistics.setTotalMoney(redEnvelop.getTotalMoney());
            exportRedEnvelopStatistics.setNumbers(redEnvelop.getNumbers());
            exportRedEnvelopStatistics.setReceivedTotalMoney(receivedTotalMoney);
            exportRedEnvelopStatistics.setReceivedTotal(receivedTotal);
            exportRedEnvelopStatistics.setSurplusMoney(surplusMoney);
            exportRedEnvelopStatistics.setSurplusTotal(surplusTotal);
            exportRedEnvelopStatisticsesList.add(exportRedEnvelopStatistics);
        }
        return exportRedEnvelopStatisticsesList;
    }
}
