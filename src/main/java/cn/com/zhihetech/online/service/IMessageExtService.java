package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.MessageExt;

import java.util.List;
import java.util.Map;

/**
 * Created by ShenYunjie on 2016/4/26.
 */
public interface IMessageExtService extends UpgradedService<MessageExt> {
    /**
     * 批量添加消息扩展
     *
     * @param messageExts
     */
    void executeBatchAdd(List<MessageExt> messageExts);

    Map<String, String> getMessageExtByMessageId(String messageId);
}
