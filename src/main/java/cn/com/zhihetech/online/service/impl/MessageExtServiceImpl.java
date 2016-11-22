package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.MessageExt;
import cn.com.zhihetech.online.dao.IMessageExtDao;
import cn.com.zhihetech.online.service.AbstractService;
import cn.com.zhihetech.online.service.IMessageExtService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ShenYunjie on 2016/4/26.
 */
@Service("messageExtService")
public class MessageExtServiceImpl extends AbstractService<MessageExt> implements IMessageExtService {

    @Resource(name = "messageExtDao")
    private IMessageExtDao messageExtDao;

    /**
     * 根据ID获取持久化对象
     *
     * @param id 对象ID
     * @return
     */
    @Override
    public MessageExt getById(String id) {
        return null;
    }

    /**
     * 删除持久化对象
     *
     * @param messageExt 需要删除的持久化对象
     */
    @Override
    public void delete(MessageExt messageExt) {

    }

    /**
     * 添加一个对象到数据库
     *
     * @param messageExt 需要持久化的对象
     * @return
     */
    @Override
    public MessageExt add(MessageExt messageExt) {
        return null;
    }

    /**
     * 更新一个持久化对象
     *
     * @param messageExt
     */
    @Override
    public void update(MessageExt messageExt) {

    }

    /**
     * 根据分页参数和查询条件查询持久化对象
     *
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return
     */
    @Override
    public List<MessageExt> getAllByParams(Pager pager, IQueryParams queryParams) {
        return null;
    }

    /**
     * 根据分页参数和查询条件查询持久化对象
     *
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return 经过分页的数据
     */
    @Override
    public PageData<MessageExt> getPageData(Pager pager, IQueryParams queryParams) {
        return null;
    }

    @Override
    public void executeBatchAdd(List<MessageExt> messageExts) {
        if (messageExts == null || messageExts.isEmpty()) {
            return;
        }
        for (MessageExt messageExt : messageExts) {
            this.messageExtDao.saveEntity(messageExt);
        }
    }

    @Override
    public Map<String, String> getMessageExtByMessageId(String messageId) {
        Map<String, String> ext = new HashMap<>();
        IQueryParams queryParams = new GeneralQueryParams().andEqual("message.messageId", messageId);
        List<Object[]> objects = this.messageExtDao.getProperties(new String[]{"key", "value"}, null, queryParams);
        if (objects != null && objects.size() > 0) {
            for (Object[] array : objects) {
                ext.put(String.valueOf(array[0]), String.valueOf(array[1]));
            }
        }
        return ext;
    }
}
