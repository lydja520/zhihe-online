package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.GoodsAttributeSet;
import cn.com.zhihetech.online.bean.ReceptionRoom;

import java.util.List;

/**
 * Created by ShenYunjie on 2016/4/5.
 */
public interface IReceptionRoomService extends UpgradedService<ReceptionRoom> {
    /**
     * 获取会客厅的所有类别
     *
     * @return
     */
    List<GoodsAttributeSet> getAllRoomAttrIdAndName();
}
