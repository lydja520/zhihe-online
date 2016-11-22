package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.*;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.dao.IActivityDao;
import cn.com.zhihetech.online.dao.IActivityInvitationDao;
import cn.com.zhihetech.online.dao.IMerchantAllianceDao;
import cn.com.zhihetech.online.dao.INotificationDao;
import cn.com.zhihetech.online.exception.NotFoundException;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.*;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.SubQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ShenYunjie on 2015/12/8.
 */
@Service("merchantAllianceService")
public class MerchantAllianceServiceImpl implements IMerchantAllianceService {
    @Resource(name = "merchantAllianceDao")
    private IMerchantAllianceDao merchantAllianceDao;
    @Resource(name = "merchantService")
    private IMerchantService merchantService;
    @Resource(name = "adminService")
    private IAdminService adminService;
    @Resource(name = "activityService")
    private IActivityService activityService;
    @Resource(name = "redEnvelopService")
    private IRedEnvelopService redEnvelopService;
    @Resource(name = "activityDao")
    private IActivityDao activityDao;
    @Resource(name = "notificationDao")
    private INotificationDao notificationDao;
    @Resource(name = "activityInvitationDao")
    private IActivityInvitationDao activityInvitationDao;

    /**
     * 根据ID获取持久化对象
     *
     * @param id 对象ID
     * @return
     */
    @Override
    public MerchantAlliance getById(String id) {
        return this.merchantAllianceDao.findEntityById(id);
    }

    /**
     * 删除持久化对象
     *
     * @param merchantAlliance 需要删除的持久化对象
     */
    @Override
    public void delete(MerchantAlliance merchantAlliance) {
        IQueryParams queryParams = new GeneralQueryParams().andEqual("allianceId", merchantAlliance.getAllianceId());
        List<Object> objects = this.merchantAllianceDao.getProperty("activity.activitId", null, queryParams);
        if (objects.size() < 1) {
            throw new NotFoundException("活动不存在！");
        }
        boolean editAble = this.activityService.isEditAble(new Activity(objects.get(0).toString()));
        if (!editAble) {
            throw new SystemException("当前活动，不可编辑");
        }
        if (StringUtils.isEmpty(merchantAlliance.getAllianceId())) {
            throw new SystemException("需要删除的成员ID不能为空");
        }
        queryParams = new GeneralQueryParams().andEqual("allianceId", merchantAlliance.getAllianceId())
                .andProParam("merchant.merchantId = activity.activityPromoter.merchantId");
        List<Object> tmpList = this.merchantAllianceDao.getProperty("allianceId", null, queryParams);
        if (tmpList != null && tmpList.size() > 0) {
            throw new SystemException("活动发起人不能取消");
        }

        queryParams = new GeneralQueryParams().andEqual("alliance.allianceId", merchantAlliance.getAllianceId());
        this.activityInvitationDao.executeDelete(queryParams);
        queryParams = new GeneralQueryParams().andEqual("allianceId", merchantAlliance.getAllianceId());
        this.merchantAllianceDao.executeDelete(queryParams);
    }

    /**
     * 添加一个对象到数据库
     *
     * @param merchantAlliance 需要持久化的对象
     * @return
     */
    @Override
    public MerchantAlliance add(MerchantAlliance merchantAlliance) {
        IQueryParams queryParams = new GeneralQueryParams().andEqual("activity.activitId", merchantAlliance.getActivity().getActivitId());
        List<Object> objects = this.merchantAllianceDao.getProperty("activity.activitId", null, queryParams);
        if (objects.size() < 1) {
            throw new NotFoundException("活动已删除或不存在！");
        }
        boolean editAble = this.activityService.isEditAble(new Activity(objects.get(0).toString()));
        if (!editAble) {
            throw new SystemException("当前活动已提交不能修改添加商家");
        }
        queryParams = new GeneralQueryParams().andEqual("merchant.merchantId", merchantAlliance.getMerchant().getMerchantId())
                .andEqual("activity.activitId", merchantAlliance.getActivity().getActivitId());
        List<Object> list = this.merchantAllianceDao.getProperty("allianceId", null, queryParams);
        if (list != null && list.size() > 0) {
            throw new SystemException("此商家已经参加了本次活动请勿重复操作");
        }
        this.merchantAllianceDao.saveEntity(merchantAlliance);

        return merchantAlliance;
    }

    /**
     * 更新一个持久化对象
     *
     * @param merchantAlliance
     */
    @Override
    public void update(MerchantAlliance merchantAlliance) {
        this.merchantAllianceDao.updateEntity(merchantAlliance);
    }

    /**
     * 根据分页参数和查询条件查询持久化对象
     *
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return
     */
    @Override
    public List<MerchantAlliance> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.merchantAllianceDao.getEntities(pager, queryParams);
    }

    /**
     * 根据分页参数和查询条件查询持久化对象
     *
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return 经过分页的数据
     */
    @Override
    public PageData<MerchantAlliance> getPageData(Pager pager, IQueryParams queryParams) {
        return this.merchantAllianceDao.getPageData(pager, queryParams);
    }

    @Override
    public PageData<MerchantAlliance> getMerchAllianceByActivity(Pager pager, GeneralQueryParams queryParams, String activitId) {
        if (StringUtils.isEmpty(activitId)) {
            throw new SystemException("活动不能为空");
        }
        if (!queryParams.queryContainsKey("activity.activitId")) {
            queryParams.andEqual("activity.activitId", activitId);
        }
        if (!queryParams.sortContainsKey("allianceState")) {
            queryParams.sort("allianceState", Order.ASC);
        }
        if (!queryParams.sortContainsKey("createDate")) {
            queryParams.sort("createDate", Order.DESC);
        }
        return this.merchantAllianceDao.getPageData(pager, queryParams);
    }

    /**
     * 根据活动ID获取受邀本次活动的所有商户
     *
     * @param pager
     * @param queryParams
     * @param activitId   活动ID
     * @param agreed      商家是否同意参加本次活动
     * @return
     */
    @Override
    public PageData<MerchantAlliance> getMerchAllianceByActivity(Pager pager, GeneralQueryParams queryParams, String activitId, boolean agreed) {
        if (StringUtils.isEmpty(activitId)) {
            throw new SystemException("活动不能为空");
        }
        if (!queryParams.queryContainsKey("activity.activitId")) {
            queryParams.andEqual("activity.activitId", activitId);
        }
        if (!queryParams.queryContainsKey("agreed")) {
            queryParams.andEqual("agreed", agreed);
        }
        if (!queryParams.sortContainsKey("agreed")) {
            queryParams.sort("agreed", Order.DESC);
        }
        if (!queryParams.sortContainsKey("agreed")) {
            queryParams.sort("createDate", Order.DESC);
        }
        return this.merchantAllianceDao.getPageData(pager, queryParams);
    }

    @Override
    public PageData<Merchant> getAbleMerchByActivity(Pager pager, GeneralQueryParams queryParams, String activitId) {
        if (StringUtils.isEmpty(activitId)) {
            throw new SystemException("活动不能为空");
        }
        IQueryParams _queryParam = new GeneralQueryParams().andEqual("activity.activitId", activitId);
        List<Object> merchIds = this.merchantAllianceDao.getProperty("merchant.merchantId", null, _queryParam);
        if (!queryParams.queryContainsKey("merchantId")) {
            queryParams.andNotIn("merchantId", merchIds);
        }
        if (!queryParams.queryContainsKey("examinState")) {
            queryParams.andEqual("examinState", Constant.ACTIVITY_STATE_EXAMINED_OK);
        }
        if (!queryParams.queryContainsKey("permit")) {
            queryParams.andEqual("permit", true);
        }

        return this.merchantService.getPageData(pager, queryParams);
    }

    @Override
    public PageData<Activity> getActivityListByAdmin(Pager pager, GeneralQueryParams queryParams, Admin admin) {
        Merchant merchant = this.adminService.getMerchant(admin);
        if (merchant == null) {
            return new PageData<>(0, pager);
        }
        IQueryParams query = new GeneralQueryParams().andEqual("merchant", merchant)
                .andNotEq("allianceState", MerchantAlliance.ALLIANCE_UNEXECUTED_STATE)
                .andNotEq("allianceState", MerchantAlliance.ALLIANCE_REFUSED_STATE);
        List<Object> activits = this.merchantAllianceDao.getProperty("distinct activity.activitId", null, query);
        if (activits == null || activits.size() < 1) {
            return new PageData(0, pager);
        }
        queryParams.andIn("activitId", activits);
        return this.activityService.getPageData(pager, queryParams);
    }

    @Override
    public PageData<Activity> getValidActivitiesByMerchantId(Pager pager, IQueryParams queryParams, String merchantId) {
        if (StringUtils.isEmpty(merchantId)) {
            return new PageData<>(0, pager);
        }
        List<Object> states = new ArrayList<>();
        states.add(MerchantAlliance.ALLIANCE_AGREED_STATE);
        states.add(MerchantAlliance.ALLIANCE_READINESS_STATE);
        IQueryParams query = new GeneralQueryParams()
                .andEqual("merchant.merchantId", merchantId)
                .andIn("allianceState", states);
        List<Object> activits = this.merchantAllianceDao.getProperty("distinct activity.activitId", null, query);
        if (activits == null || activits.isEmpty()) {
            return new PageData<>(0, pager);   //如果不满足以上条件，则说明商家没有有效的活动
        }
        queryParams.andIn("activitId", activits);
        return this.activityService.getPageData(pager, queryParams);
    }

    @Override
    public void updateAgreeState(String allianceId, int state) {
        IQueryParams queryParams = new GeneralQueryParams().andEqual("allianceId", allianceId);
        Map<String, Object> values = new HashMap<>();
        values.put("allianceState", state);
        this.merchantAllianceDao.executeUpdate(values, queryParams);
    }

    /**
     * 删除无效的活动商家联盟
     *
     * @param activityId 活动ID
     */
    @Override
    public void deleteInvalidAlliance(String activityId) {
        List<Object> deleteSates = new ArrayList<>();
        deleteSates.add(MerchantAlliance.ALLIANCE_UNEXECUTED_STATE);
        deleteSates.add(MerchantAlliance.ALLIANCE_REFUSED_STATE);

        IQueryParams queryParams = new GeneralQueryParams().andEqual("activity.activitId", activityId)
                .andIn("allianceState", deleteSates);

        this.merchantAllianceDao.executeDelete(queryParams);
    }

    @Override
    public void executeCommit(String adminId, String activitId) {
        boolean editAble = this.activityService.isEditAble(new Activity(activitId));
        if (!editAble) {
            throw new SystemException("当前活动不能编辑");
        }
        String merchantId = this.adminService.getMerchantId(new Admin(adminId));
        IQueryParams queryParams = new GeneralQueryParams().andEqual("activity.activitId", activitId)
                .andEqual("merchant.merchantId", merchantId);
        Map<String, Object> values = new HashMap<>();
        values.put("allianceState", MerchantAlliance.ALLIANCE_READINESS_STATE);
        this.merchantAllianceDao.executeUpdate(values, queryParams);
    }

    /**
     * 获取商家正在开始的活动
     *
     * @param pager
     * @param merchantId
     * @return
     */
    @Override
    public PageData<Activity> getMerchantStartedActivities(Pager pager, String merchantId) {
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("allianceState", MerchantAlliance.ALLIANCE_READINESS_STATE)
                .andEqual("merchant.merchantId", merchantId)
                .andEqual("activity.currentState", Constant.ACTIVITY_STATE_STARTED);
        List<Object> activityIds = this.merchantAllianceDao.getProperty("activity.activitId", null, queryParams);
        //如果不满足以上条件则说明商家没有正在进行的活动
        if (activityIds == null || activityIds.isEmpty()) {
            return new PageData<>(0, pager);
        }
        queryParams = new GeneralQueryParams()
                .andIn("activitId", activityIds)
                .andEqual("currentState", Constant.ACTIVITY_STATE_STARTED);
        return this.activityDao.getPageData(pager, queryParams);
    }


    @Override
    public PageData<Merchant> getJoinMerchByActivity(Pager pager, IQueryParams iQueryParams) {
        List<MerchantAlliance> merchantAlliances = this.getAllByParams(pager, iQueryParams);
        PageData<Merchant> pageData = new PageData();
        List<Merchant> merchants = new ArrayList<Merchant>();
        for (MerchantAlliance merchantAlliance : merchantAlliances) {
            merchants.add(merchantAlliance.getMerchant());
        }
        pageData.setRows(merchants);
        return pageData;
    }
}
