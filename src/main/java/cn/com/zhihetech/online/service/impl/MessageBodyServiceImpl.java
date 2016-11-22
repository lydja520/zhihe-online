package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.MessageBody;
import cn.com.zhihetech.online.dao.IMessageBodyDao;
import cn.com.zhihetech.online.service.AbstractService;
import cn.com.zhihetech.online.service.IMessageBodyService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ShenYunjie on 2016/4/26.
 */
@Service("messageBodyService")
public class MessageBodyServiceImpl extends AbstractService<MessageBody> implements IMessageBodyService {

    @Resource(name = "messageBodyDao")
    private IMessageBodyDao messageBodyDao;

    /**
     * 根据ID获取持久化对象
     *
     * @param id 对象ID
     * @return
     */
    @Override
    public MessageBody getById(String id) {
        return null;
    }

    /**
     * 删除持久化对象
     *
     * @param messageBody 需要删除的持久化对象
     */
    @Override
    public void delete(MessageBody messageBody) {

    }

    /**
     * 添加一个对象到数据库
     *
     * @param messageBody 需要持久化的对象
     * @return
     */
    @Override
    public MessageBody add(MessageBody messageBody) {
        return this.messageBodyDao.saveEntity(messageBody);
    }

    /**
     * 更新一个持久化对象
     *
     * @param messageBody
     */
    @Override
    public void update(MessageBody messageBody) {

    }

    /**
     * 根据分页参数和查询条件查询持久化对象
     *
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return
     */
    @Override
    public List<MessageBody> getAllByParams(Pager pager, IQueryParams queryParams) {
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
    public PageData<MessageBody> getPageData(Pager pager, IQueryParams queryParams) {
        return null;
    }

    @Override
    public List<MessageBody> getMessageBodiesByMessageId(CharSequence messageId) {
        IQueryParams queryParams = new GeneralQueryParams().andEqual("message.messageId", messageId);
        return this.messageBodyDao.getEntities(null, queryParams);
    }

    @Override
    public void updateMessageBodyMsg(String bodyId, String msgBody) {
        this.messageBodyDao.updateMessageBodyMsg(bodyId, msgBody);
    }
}
