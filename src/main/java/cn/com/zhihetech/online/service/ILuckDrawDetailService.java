package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.Admin;
import cn.com.zhihetech.online.bean.LuckyDrawDetail;
import cn.com.zhihetech.online.bean.User;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.util.hibernate.service.SupportService;

/**
 * Created by ydc on 16-4-28.
 */
public interface ILuckDrawDetailService extends SupportService<LuckyDrawDetail> {
    ResponseMessage executeluckDraw(User user);

    ResponseMessage getLuckDrawList();

    /**
     * 奖品验证（使用）
     *
     * @param luckCode 奖品号码
     * @param mobileNo 中奖手机号
     * @param admin    验证人
     */
    void executeUseLuckyDrawDetail(String luckCode, String mobileNo, Admin admin);
}
