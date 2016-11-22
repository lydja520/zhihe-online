package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.ActivityInvitation;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import cn.com.zhihetech.util.hibernate.service.SupportService;

/**
 * Created by ShenYunjie on 2016/3/21.
 */
public interface IActivityInvitationService extends SupportService<ActivityInvitation> {
    /**
     * 获取指定管理员对应商家收到的有效活动邀请
     *
     * @param pager
     * @param currentAdminId
     */
    PageData<ActivityInvitation> getInvitationsPageDataByAdminId(Pager pager, String currentAdminId);

    /**
     * 同意邀请
     *
     * @param invitationId
     */
    void updateAgreeInvitation(String invitationId);

    /**
     * 拒绝邀请
     *
     * @param invitationId
     */
    void updateRefuseInvitation(String invitationId);

    /**
     * 删除指定活动的无效邀请和商家联盟
     *
     * @param activityId 活动ID
     */
    void deleteActivityInvalidInvitation(String activityId);
}
