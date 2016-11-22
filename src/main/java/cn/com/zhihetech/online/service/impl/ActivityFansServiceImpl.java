package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.Activity;
import cn.com.zhihetech.online.bean.ActivityFans;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.bean.User;
import cn.com.zhihetech.online.dao.IActivityFansDao;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IActivityFansService;
import cn.com.zhihetech.online.service.IActivityService;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ShenYunjie on 2015/12/15.
 */
@Service("activityFansService")
public class ActivityFansServiceImpl implements IActivityFansService {
    @Resource(name = "activityFansDao")
    private IActivityFansDao activityFansDao;
    @Resource(name = "activityService")
    private IActivityService activityService;

    /**
     * 根据ID获取持久化对象
     *
     * @param id 对象ID
     * @return
     */
    @Override
    public ActivityFans getById(String id) {
        return this.activityFansDao.findEntityById(id);
    }

    /**
     * 删除持久化对象
     *
     * @param activityFans 需要删除的持久化对象
     */
    @Override
    public void delete(ActivityFans activityFans) {
        if (!isEditAble(activityFans)) {
            throw new SystemException("活动已提交或已审核通过，不能删除会员");
        }
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("fansId", activityFans.getFansId());
        this.activityFansDao.executeDelete(queryParams);
    }

    /**
     * 添加一个对象到数据库
     *
     * @param activityFans 需要持久化的对象
     * @return
     */
    @Override
    public ActivityFans add(ActivityFans activityFans) {
        if (!isEditAble(activityFans)) {
            throw new SystemException("活动已提交或已审核通过，不能添加会员");
        }
        IQueryParams queryParams = new GeneralQueryParams().andEqual("fans.userId", activityFans.getFans().getUserId())
                .andEqual("activity.activitId", activityFans.getActivity().getActivitId());
        List<Object> objects = this.activityFansDao.getProperty("fansId", null, queryParams);
        if (objects.size() > 0) {
            throw new SystemException("此用户已参加了本次活动，请勿重复添加！");
        }
        return this.activityFansDao.saveEntity(activityFans);
    }

    /**
     * 更新一个持久化对象
     *
     * @param activityFans
     */
    @Override
    public void update(ActivityFans activityFans) {
        this.activityFansDao.updateEntity(activityFans);
    }

    /**
     * 根据分页参数和查询条件查询持久化对象
     *
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return
     */
    @Override
    public List<ActivityFans> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.activityFansDao.getEntities(pager, queryParams);
    }

    /**
     * 根据分页参数和查询条件查询持久化对象
     *
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return 经过分页的数据
     */
    @Override
    public PageData<ActivityFans> getPageData(Pager pager, IQueryParams queryParams) {
        return this.activityFansDao.getPageData(pager, queryParams);
    }

    private boolean isEditAble(ActivityFans activityFans) {
        String activityId = null;
        if (activityFans.getActivity() != null && !StringUtils.isEmpty(activityFans.getActivity().getActivitId())) {
            activityId = activityFans.getActivity().getActivitId();
        } else if (!StringUtils.isEmpty(activityFans.getFansId())) {
            IQueryParams queryParams = new GeneralQueryParams();
            queryParams.andEqual("fansId", activityFans.getFansId());
            List<Object> tmps = this.activityFansDao.getProperty("activity.activitId", null, queryParams);
            if (tmps.size() > 0) {
                activityId = (String) tmps.get(0);
            }
        }
        if (StringUtils.isEmpty(activityId)) {
            throw new SystemException("参数不足");
        }
        Activity activity = new Activity();
        activity.setActivitId(activityId);
        return this.activityService.isEditAble(activity);
    }

    @Override
    public PageData<Object> getPageDataProperty(String property, Pager pager, IQueryParams queryParams) {
        return this.activityFansDao.getPageDataProperty(property, pager, queryParams);
    }

    //TODO:得到每一个商家邀请的活动会员
    @Override
    public Map<String, List<ActivityFans>> getFansUser(Pager pager, List<Merchant> merchants, String activityId) {
        Map<String,List<ActivityFans>> allActivityFans = new HashMap<>();
        for(Merchant merchant : merchants){
            IQueryParams queryParams = new GeneralQueryParams();
            List<ActivityFans> oneActivityFans = new ArrayList<ActivityFans>(
                    this.activityFansDao.getEntities(queryParams.andEqual("activity.activitId",activityId).andEqual("invitationMerch.merchantId",merchant.getMerchantId()))
            );
            allActivityFans.put(merchant.getMerchantId(),oneActivityFans);
        }
        return allActivityFans;
    }
}
