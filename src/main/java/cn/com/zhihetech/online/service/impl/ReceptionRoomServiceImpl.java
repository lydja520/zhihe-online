package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.GoodsAttributeSet;
import cn.com.zhihetech.online.bean.ReceptionRoom;
import cn.com.zhihetech.online.dao.IReceptionRoomDao;
import cn.com.zhihetech.online.service.AbstractService;
import cn.com.zhihetech.online.service.IReceptionRoomService;
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
 * Created by ShenYunjie on 2016/4/5.
 */
@Service("receptionRoomService")
public class ReceptionRoomServiceImpl extends AbstractService<ReceptionRoom> implements IReceptionRoomService {

    @Resource(name = "receptionRoomDao")
    private IReceptionRoomDao receptionRoomDao;

    /**
     * 根据ID获取持久化对象
     *
     * @param id 对象ID
     * @return
     */
    @Override
    public ReceptionRoom getById(String id) {
        return this.receptionRoomDao.findEntityById(id);
    }

    @Override
    public void delete(ReceptionRoom room) {
        Map<String, Object> values = new HashMap<>();
        values.put("deleted", true);
        IQueryParams queryParams = new GeneralQueryParams().andEqual("roomId", room.getRoomId());
        executeUpdate(values, queryParams);
    }

    @Override
    public int executeDelete(IQueryParams queryParams) {
        return this.receptionRoomDao.executeDelete(queryParams);
    }

    @Override
    public int executeUpdate(Map<String, Object> values, IQueryParams queryParams) {
        return this.receptionRoomDao.executeUpdate(values, queryParams);
    }

    @Override
    public ReceptionRoom add(ReceptionRoom receptionRoom) {
        return this.receptionRoomDao.saveEntity(receptionRoom);
    }

    @Override
    public void update(ReceptionRoom receptionRoom) {
        this.receptionRoomDao.updateEntity(receptionRoom);
    }

    @Override
    public PageData<ReceptionRoom> getPageData(Pager pager, IQueryParams queryParams) {
        return receptionRoomDao.getPageData(pager, queryParams);
    }

    @Override
    public List<ReceptionRoom> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.receptionRoomDao.getEntities(pager, queryParams);
    }

    @Override
    public List<GoodsAttributeSet> getAllRoomAttrIdAndName() {
        IQueryParams queryParams = new GeneralQueryParams().andEqual("attributeSet.permit", true)
                .andEqual("deleted", false);
        String[] selectors = new String[]{"attributeSet.goodsAttSetId", "attributeSet.goodsAttSetId"};
        List<Object[]> objects = this.receptionRoomDao.getProperties(selectors, null, queryParams);
        List<GoodsAttributeSet> attributeSets = new ArrayList<>();
        for (Object[] array : objects) {
            GoodsAttributeSet attributeSet = new GoodsAttributeSet();
            attributeSet.setGoodsAttSetId((String) array[0]);
            attributeSet.setGoodsAttSetName((String) array[1]);
            attributeSet.setPermit(true);
            attributeSets.add(attributeSet);
        }
        objects.clear();
        return attributeSets;
    }
}
