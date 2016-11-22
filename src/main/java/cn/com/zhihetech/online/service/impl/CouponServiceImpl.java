package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.*;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.dao.ICouponDao;
import cn.com.zhihetech.online.dao.ICouponItemDao;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IActivityService;
import cn.com.zhihetech.online.service.ICouponService;
import cn.com.zhihetech.online.util.DateUtils;
import cn.com.zhihetech.util.common.GeneratedNRandom;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import cn.jpush.api.report.UsersResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ShenYunjie on 2015/12/15.
 */
@Service("couponService")
public class CouponServiceImpl implements ICouponService {
    @Resource(name = "couponDao")
    private ICouponDao couponDao;
    @Resource(name = "activityService")
    private IActivityService activityService;
    @Resource(name = "couponItemDao")
    private ICouponItemDao couponItemDao;

    /**
     * 根据ID获取持久化对象
     *
     * @param id 对象ID
     * @return
     */
    @Override
    public Coupon getById(String id) {
        return this.couponDao.findEntityById(id);
    }

    /**
     * 删除持久化对象(逻辑删除)
     *
     * @param coupon 需要删除的持久化对象
     */
    @Override
    public void delete(Coupon coupon) {
        if (!isEditAble(coupon)) {
            throw new SystemException("当前优惠券不可删除");
        }
        Map values = new HashMap<>();
        values.put("deleted", true);
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("couponId", coupon.getCouponId());
        this.couponDao.executeUpdate(values, queryParams);
    }

    /**
     * 添加一个对象到数据库
     *
     * @param coupon 需要持久化的对象
     * @return
     */
    @Override
    public Coupon add(Coupon coupon) {
        return this.couponDao.saveEntity(coupon);
    }

    /**
     * 更新一个持久化对象
     *
     * @param coupon
     */
    @Override
    public void update(Coupon coupon) {
        this.couponDao.updateEntity(coupon);
    }

    /**
     * 根据分页参数和查询条件查询持久化对象
     *
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return
     */
    @Override
    public List<Coupon> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.couponDao.getEntities(pager, queryParams);
    }

    /**
     * 根据分页参数和查询条件查询持久化对象
     *
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return 经过分页的数据
     */
    @Override
    public PageData<Coupon> getPageData(Pager pager, IQueryParams queryParams) {
        return this.couponDao.getPageData(pager, queryParams);
    }

    /**
     * 判断优惠券信息是否可编辑
     *
     * @param coupon
     * @return
     */
    @Override
    public boolean isEditAble(Coupon coupon) {
        String activitId = coupon.getActivity().getActivitId();
        Activity activity = this.activityService.getById(activitId);
        if (activity == null) {
            throw new SystemException("不存在该活动！");
        }
        return this.activityService.isEditAble(activity);
    }

    @Override
    public void updateBaseInfo(Coupon coupon, Date beginValidity, Date validity) {
        if (beginValidity == null || validity == null) {
            throw new SystemException("有效期开始和结束时间不能为空！");
        }
        if (validity.before(beginValidity)) {
            throw new SystemException("有效期结束时间不能再有效期开始时间之前！");
        } else if (validity.before(new Date())) {
            throw new SystemException("有效期结束时间不能再小于当前时间");
        }
        if (!isEditAble(coupon)) {
            throw new SystemException("当前优惠券不可编辑");
        }

        Map<String, Object> values = new HashMap<>();
        IQueryParams queryParams = new GeneralQueryParams();
        values.put("total", coupon.getTotal());
        values.put("couponMsg", coupon.getCouponMsg());
        values.put("startValidity", beginValidity);
        values.put("endValidity", validity);
        if (coupon.getCouponType() == Constant.COUPON_VOUCHER_TYPE) {
            values.put("couponName", "优惠券");
        } else {
            values.put("couponName", "代金券");
        }
        values.put("faceValue", coupon.getFaceValue());
        values.put("couponType", coupon.getCouponType());
        queryParams = new GeneralQueryParams();
        queryParams.andEqual("couponId", coupon.getCouponId());
        this.couponDao.executeUpdate(values, queryParams);
        this.couponDao.executeUpdate(values, queryParams);

        values.clear();
        if (coupon.getCouponType() == Constant.COUPON_VOUCHER_TYPE) {
            values.put("couponName", "优惠券");
        } else {
            values.put("couponName", "代金券");
        }
        queryParams = new GeneralQueryParams();
        queryParams.andEqual("coupon.couponId", coupon.getCouponId());
        values.put("faceValue", coupon.getFaceValue());
        values.put("couponType", coupon.getCouponType());
        values.put("beginValidity", beginValidity);
        this.couponItemDao.executeUpdate(values, queryParams);
    }

    @Override
    public void addCoupon(Coupon coupon, Date beginValidity, Date validity) {
        if (beginValidity == null || validity == null) {
            throw new SystemException("有效期开始和结束时间不能为空！");
        }
        if (validity.before(beginValidity)) {
            throw new SystemException("有效期结束时间不能再有效期开始时间之前！");
        } else if (validity.before(new Date())) {
            throw new SystemException("有效期结束时间不能再小于当前时间");
        }
        if (!isEditAble(coupon)) {
            throw new SystemException("当前优惠券不可编辑");
        }
        if (coupon.getActivity() != null && StringUtils.isEmpty(coupon.getActivity().getActivitId())) {
            coupon.setActivity(null);
        }
        coupon.setCreateDate(new Date());
        if (coupon.getCouponType() == Constant.COUPON_VOUCHER_TYPE) {
            coupon.setCouponName("优惠券");
        }
        coupon.setTotalReceived(0);
        coupon.setStartValidity(beginValidity);
        coupon.setEndValidity(validity);
        this.couponDao.saveEntity(coupon);
        for (int i = 1; i <= coupon.getTotal(); i++) {
            CouponItem couponItem = new CouponItem();
            couponItem.setCoupon(coupon);
            couponItem.setCouponType(coupon.getCouponType());
            couponItem.setFaceValue(coupon.getFaceValue());
            couponItem.setUseState(false);
            couponItem.setValidity(validity);
            couponItem.setBeginValidity(beginValidity);
            couponItem.setCouponName(coupon.getCouponName());
            this.couponItemDao.saveEntity(couponItem);
        }
    }

    @Override
    public ResponseMessage updateCouponItem(String couponId, String userId) {
        ResponseMessage responseMessage = null;
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("couponId", couponId);
        List<Object> activityCurrentStates = this.couponDao.getProperty("activity.currentState", null, queryParams);
        if (activityCurrentStates.size() == 0) {
            throw new SystemException("不存在该活动");
        }
        int activityCurrentState = (int) activityCurrentStates.get(0);
        if (activityCurrentState != Constant.ACTIVITY_STATE_STARTED) {
            throw new SystemException("活动未开始或已结束！");
        }
        queryParams = new GeneralQueryParams()
                .andEqual("coupon.couponId", couponId)
                .andEqual("user.userId", userId);
        List<Object> couponItemIds = this.couponItemDao.getProperty("couponItemId", null, queryParams);
        if (couponItemIds.size() > 0) {
            responseMessage = new ResponseMessage(ResponseStatusCode.COUPON_ALREADY_GRAB, false, "你已经抢到过一张此类优惠券");
            responseMessage.setAttribute(couponItemIds.get(0));
            return responseMessage;
        }
        queryParams = new GeneralQueryParams()
                .andEqual("coupon.couponId", couponId)
                .andIsNull("user");
        long total = this.couponItemDao.getRecordTotal(queryParams);
        if (total <= 0) {
            responseMessage = new ResponseMessage(ResponseStatusCode.COUPON_ALREADY_OVER, false, "太火爆了，优惠券已经被抢光！");
            return responseMessage;
        }
        List<CouponItem> couponItems = this.couponItemDao.getEntities(null, queryParams);
        Collections.shuffle(couponItems);
        CouponItem couponItem = couponItems.get(0);
        User user = new User();
        user.setUserId(userId);
        couponItem.setUser(user);
        couponItem.setReceivedDate(new Date());
        String couponItemCode = Constant.BEGIN_CODE_COUPON + GeneratedNRandom.generated(2) + System.currentTimeMillis();
        couponItem.setCode(couponItemCode);
        this.couponItemDao.updateEntity(couponItem);

        /*更新优惠券被领取数*/
        queryParams = new GeneralQueryParams().andEqual("couponId", couponId);
        List<Object> totalReceiveds = this.couponDao.getProperty("totalReceived", null, queryParams);
        int totalReceived = (Integer) totalReceiveds.get(0);
        Map<String, Object> values = new HashMap<>();
        values.put("totalReceived", ++totalReceived);
        this.couponDao.executeUpdate(values, queryParams);
        /*更新优惠券领取数量完成*/

        responseMessage = new ResponseMessage(ResponseStatusCode.SUCCESS_CODE, true, "成功抢到优惠券");
        responseMessage.setAttribute(couponItem);
        return responseMessage;
    }

    @Override
    public Map<String,List<Coupon>> getActivityCoupon(Pager pager, List<Merchant> merchants, String activityId) {
        //TODO:得到每一个商家所添加的全部奖券
        Map<String,List<Coupon>> allMerchantCoupon = new HashMap<String,List<Coupon>>();
        for(Merchant merchant : merchants){
            IQueryParams queryParams = new GeneralQueryParams();
            List<Coupon>  oneMerchantCoupon =  new ArrayList<Coupon>(
                    this.couponDao.getEntities(queryParams.andEqual("merchant.merchantId", merchant.getMerchantId()).andEqual("activity.activitId", activityId))
            );
            allMerchantCoupon.put(merchant.getMerchantId(),oneMerchantCoupon);
        }
        return allMerchantCoupon;
    }
}
