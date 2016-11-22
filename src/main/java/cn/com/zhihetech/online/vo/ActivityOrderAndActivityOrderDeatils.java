package cn.com.zhihetech.online.vo;

import cn.com.zhihetech.online.bean.ActivityOrder;
import cn.com.zhihetech.online.bean.ActivityOrderDetail;
import cn.com.zhihetech.online.bean.SerializableAndCloneable;

import java.util.List;

/**
 * Created by YangDaiChun on 2016/6/2.
 */
public class ActivityOrderAndActivityOrderDeatils extends SerializableAndCloneable{

    List<ActivityOrderDetail> activityOrderDetails;
    ActivityOrder activityOrder;

    public List<ActivityOrderDetail> getActivityOrderDetails() {
        return activityOrderDetails;
    }

    public void setActivityOrderDetails(List<ActivityOrderDetail> activityOrderDetails) {
        this.activityOrderDetails = activityOrderDetails;
    }

    public ActivityOrder getActivityOrder() {
        return activityOrder;
    }

    public void setActivityOrder(ActivityOrder activityOrder) {
        this.activityOrder = activityOrder;
    }
}
