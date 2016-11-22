package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.Admin;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.bean.RedEnvelop;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.vo.ExportRedEnvelopStatistics;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import cn.com.zhihetech.util.hibernate.service.SupportService;

import java.util.List;
import java.util.Map;

/**
 * Created by ShenYunjie on 2015/12/11.
 */
public interface IRedEnvelopService extends SupportService<RedEnvelop> {
    /**
     * 获取指定用户对应商家的红包
     *
     * @param pager
     * @param queryParams
     * @param currentAdmin
     */
    PageData<RedEnvelop> getPageDataByAdmin(Pager pager, GeneralQueryParams queryParams, Admin currentAdmin);

    /**
     * 判断红包是否可添加、编辑或删除
     *
     * @param redEnvelop
     * @return
     */
    boolean isEditable(RedEnvelop redEnvelop);

    Map<String, List<RedEnvelop>> getAbleRedEnvelopByMerch(Pager pager, List<Merchant> merchants, String activityId);

    void executeUpdate(Map<String, Object> paramAndValue, IQueryParams queryParams);

    ResponseMessage updateGrabRedEnvelop(String envelopId, String userId);

    /**
     * 修改红包的基本信息
     *
     * @param envelopId  红包ID
     * @param totalMoney 红包金额
     * @param numbers    红包发放个数
     * @param envelopMsg 红包留言
     */
    void updateBaseInfo(String envelopId, float totalMoney, int numbers, String envelopMsg);

    /**
     * 根据查询参数得到所有的红包统计ToExcel
     * @param queryParams 商家名，活动名，红包创建时间段
     * @return
     */
    List<ExportRedEnvelopStatistics> getExportRedEnvelopList(IQueryParams queryParams);
}
