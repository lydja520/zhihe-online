package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.Activity;
import cn.com.zhihetech.online.bean.Admin;
import cn.com.zhihetech.online.bean.RedEnvelop;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import cn.com.zhihetech.util.hibernate.service.SupportService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by ShenYunjie on 2015/12/7.
 */
public interface IActivityService extends SupportService<Activity> {
    Activity saveOrUpdate(Activity activity);

    /**
     * 根据当前登录用于，查询活动。（如果当前用户为系统用于则显示所用活动，否则只显示与当前用户所属商家参加的活动）
     *
     * @param pager
     * @param queryParams
     * @param currentAdmin
     * @return
     */
    PageData<Activity> getPageDataByAdmin(Pager pager, GeneralQueryParams queryParams, Admin currentAdmin);

    /**
     * 商家发起一个新活动或修改自己发起的现有活动
     *
     * @param activity
     * @param activity
     * @param admin
     * @param admin
     */
    void saveByAdmin(Activity activity, Admin admin);

    /**
     * 查询活动是否可编辑
     *
     * @param activity
     * @return
     */
    boolean isEditAble(Activity activity);

    /**
     * 根据活动ID获取活动的当前状态
     *
     * @param activitId
     * @return
     */
    int getCurrentSateById(String activitId);

    ResponseMessage updateActivityExaminState(String activityId, String currentState, String examinMsg);

    void updateSubmitState(String activityId);

    ResponseMessage addSingleUserToActivityChatRoom(String activityId, String userId);

    ResponseMessage getChatRoomInfo(String activityId);

    void executeUpdate(Map<String, Object> paramAndValue, IQueryParams queryParams);

    /**
     * 获取已审核通过或已开始的活动
     *
     * @param pager
     * @param queryParams
     * @return
     */
    PageData<Activity> getExaminedAndStartedActivities(Pager pager, IQueryParams queryParams);

    /**
     * 活动开始执行
     *
     * @param activitId
     */
    void executeActivityStarted(String activitId);

    /**
     * 活动结束执行
     *
     * @param activitId
     */
    void executeActivityOver(String activitId) throws IOException;

    /**
     * 更新活动信息
     *
     * @param activity    对应活动
     * @param attributIds 活动对应的商品类别
     */
    void executeActivity(Activity activity, List<String> attributIds);

    /**
     * 根据商品类别获取活动
     *
     * @param goodsAttSetId
     * @param pager
     * @return
     */
    PageData<Activity> getActivitiesByGoodsAttSetId(String goodsAttSetId, Pager pager);
}
