package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.*;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.WebChineseConfig;
import cn.com.zhihetech.online.dao.*;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IActivityInvitationService;
import cn.com.zhihetech.online.service.IActivityService;
import cn.com.zhihetech.online.service.IAdminService;
import cn.com.zhihetech.online.service.IMerchantAllianceService;
import cn.com.zhihetech.online.util.SMSUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.SubQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.*;

/**
 * Created by ShenYunjie on 2016/3/21.
 */
@Service("activityInvitationService")
public class ActivityInvitationServiceImpl implements IActivityInvitationService {

    @Resource(name = "activityInvitationDao")
    private IActivityInvitationDao activityInvitationDao;
    @Resource(name = "activityService")
    private IActivityService activityService;
    @Resource(name = "activityDao")
    private IActivityDao activityDao;
    @Resource(name = "merchantDao")
    private IMerchantDao merchantDao;
    @Resource(name = "merchantAllianceService")
    private IMerchantAllianceService merchantAllianceService;
    @Resource(name = "adminService")
    private IAdminService adminService;
    @Resource(name = "merchantAllianceDao")
    private IMerchantAllianceDao merchantAllianceDao;
    @Resource(name = "activityOrderDao")
    private IActivityOrderDao activityOrderDao;
    @Resource(name = "activityOrderDetailDao")
    private IActivityOrderDetailDao activityOrderDetailDao;

    @Override
    public ActivityInvitation getById(String id) {
        return this.activityInvitationDao.findEntityById(id);
    }

    @Override
    public void delete(ActivityInvitation invitation) {

    }

    @Override
    public ActivityInvitation add(ActivityInvitation invitation) {
        IQueryParams queryParams;
        //boolean editAble = this.activityService.isEditAble(new Activity(invitation.getActivity().getActivitId()));
        boolean editAble = this.activityService.isEditAble(invitation.getActivity());
        if (!editAble) {
            throw new SystemException("当前活动已提交，不能再邀请商家！");
        }
        queryParams = new GeneralQueryParams().andEqual("activitId", invitation.getActivity().getActivitId());
        Object[] activitInfos = this.activityDao.getProperties(new String[]{"activitId", "activitName", "beginDate"}, null, queryParams).get(0);
        queryParams = new GeneralQueryParams().andEqual("activitId", activitInfos[0]);
        String invMerchName = this.activityDao.getProperty("activityPromoter.merchName", null, queryParams).get(0).toString();
        invitation.setCreateDate(new Date());
        String msg = "商家\"{0}\"邀请您参加\"{1}\"活动";
        msg = MessageFormat.format(msg, invMerchName, activitInfos[1]);
        invitation.setInvitedMsg(msg);
        invitation.setExpiredDate((Date) activitInfos[2]);

        MerchantAlliance alliance = new MerchantAlliance(invitation);
        this.merchantAllianceService.add(alliance);
        invitation.setAlliance(alliance);
        invitation = this.activityInvitationDao.saveEntity(invitation);

        queryParams = new GeneralQueryParams().andEqual("invitationId", invitation.getInvitationId());
        String merchantNum = this.activityInvitationDao.getProperty("merchant.contactMobileNO", null, queryParams).get(0).toString();

        queryParams = new GeneralQueryParams().andEqual("activitId", invitation.getActivity().getActivitId());
        String[] properties = new String[]{"activityPromoter.merchName", "contacterName", "contactTell"};
        Object[] infos = this.activityDao.getProperties(properties, null, queryParams).get(0);
        String smsTxt = MessageFormat.format(WebChineseConfig.MsgTemplate.ACTIVITY_INVITATION, infos[0], infos[1], infos[2]);
        SMSUtils.asyncSendSMS(merchantNum, smsTxt);
        return invitation;
    }

    @Override
    public void update(ActivityInvitation invitation) {

    }

    @Override
    public List<ActivityInvitation> getAllByParams(Pager pager, IQueryParams queryParams) {
        return null;
    }

    @Override
    public PageData<ActivityInvitation> getPageData(Pager pager, IQueryParams queryParams) {
        return null;
    }

    @Override
    public PageData<ActivityInvitation> getInvitationsPageDataByAdminId(Pager pager, String currentAdminId) {
        String merchantId = this.adminService.getMerchantId(new Admin(currentAdminId));
        IQueryParams queryParams = new GeneralQueryParams().andEqual("merchant.merchantId", merchantId)
                .andEqual("invitationState", ActivityInvitation.INVITATION_INVITATION_STATE);
        return this.activityInvitationDao.getPageData(pager, queryParams);
    }

    /**
     * 同意活动请求
     *
     * @param invitationId
     */
    //TODO:插入一个活动订单详细-商家活动支出
    @Override
    public void updateAgreeInvitation(String invitationId) {
        if (!editAble(invitationId)) {
            throw new SystemException("当前活动已提交，不可再参加活动");
        }
        IQueryParams queryParams = new GeneralQueryParams().andEqual("invitationId", invitationId);
        Map<String, Object> values = new HashMap<>();
        values.put("invitationState", ActivityInvitation.INVITATION_AGREED_STATE);
        this.activityInvitationDao.executeUpdate(values, queryParams);
        String allianceId = this.activityInvitationDao.getProperty("alliance.allianceId", null, queryParams).get(0).toString();
        this.merchantAllianceService.updateAgreeState(allianceId, MerchantAlliance.ALLIANCE_AGREED_STATE);
        queryParams = new GeneralQueryParams();
        queryParams.andEqual("allianceId", allianceId);
        String[] selectors = new String[2];
        selectors[0] = "activity.activitId";
        selectors[1] = "merchant.merchantId";
        List<Object[]> acitivityIdAndMerchantIds = this.merchantAllianceDao.getProperties(selectors, null, queryParams);
        String acitivityId = (String) acitivityIdAndMerchantIds.get(0)[0];
        String merchantId = (String) acitivityIdAndMerchantIds.get(0)[1];

        ActivityOrderDetail activityOrderDetail = new ActivityOrderDetail();
        activityOrderDetail.setOrderType(ActivityOrderDetail.OrderType.activityCost);
        activityOrderDetail.setOrderTypeId(allianceId);
        activityOrderDetail.setPayState(false);
        activityOrderDetail.setMoney(Constant.MERCHNT_ACTIVITY_BUDGET);
        activityOrderDetail.setActivityOrder(null);
        activityOrderDetail.setMerchantId(merchantId);
        activityOrderDetail.setActivityId(acitivityId);
        this.activityOrderDetailDao.saveEntity(activityOrderDetail);
    }

    @Override
    public void updateRefuseInvitation(String invitationId) {
        if (!editAble(invitationId)) {
            throw new SystemException("当前活动已提交，不可编辑");
        }
        IQueryParams queryParams = new GeneralQueryParams().andEqual("invitationId", invitationId);
        Map<String, Object> values = new HashMap<>();
        values.put("invitationState", ActivityInvitation.INVITATION_REFUSED_STATE);
        this.activityInvitationDao.executeUpdate(values, queryParams);
        String allianceId = this.activityInvitationDao.getProperty("alliance.allianceId", null, queryParams).get(0).toString();
        this.merchantAllianceService.updateAgreeState(allianceId, MerchantAlliance.ALLIANCE_REFUSED_STATE);
    }

    /**
     * 删除无效邀请和商家联盟
     *
     * @param activityId 活动ID
     */
    @Override
    public void deleteActivityInvalidInvitation(String activityId) {
        List<Object> deleteSates = new ArrayList<>();
        deleteSates.add(ActivityInvitation.INVITATION_INVITATION_STATE);
        deleteSates.add(ActivityInvitation.INVITATION_REFUSED_STATE);
        IQueryParams queryParams = new GeneralQueryParams().andEqual("activity.activitId", activityId)
                .andIn("invitationState", deleteSates);

        this.activityInvitationDao.executeDelete(queryParams);  //删除未处理或已拒绝活动邀请数据
        this.merchantAllianceService.deleteInvalidAlliance(activityId);   //删除未处理的商家联盟
    }

    /**
     * 根据邀请ID查询对应邀请是否可编辑
     *
     * @param invitationId
     * @return
     */
    protected boolean editAble(String invitationId) {
        IQueryParams queryParams = new GeneralQueryParams().andEqual("invitationId", invitationId);
        List<Object> objects = this.activityInvitationDao.getProperty("activity.activitId", null, queryParams);
        if (objects == null || objects.size() < 1) {
            throw new SystemException("活动不存在");
        }
        return this.activityService.isEditAble(new Activity(objects.get(0).toString()));
    }
}
