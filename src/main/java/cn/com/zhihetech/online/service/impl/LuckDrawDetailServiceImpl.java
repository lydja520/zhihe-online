package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.*;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.dao.ILuckDrawUserRecordDao;
import cn.com.zhihetech.online.dao.ILuckyDrawActivityDao;
import cn.com.zhihetech.online.dao.ILuckyDrawDao;
import cn.com.zhihetech.online.dao.ILuckyDrawDetailDao;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.ILuckDrawDetailService;
import cn.com.zhihetech.online.util.DateUtils;
import cn.com.zhihetech.util.common.GeneratedNRandom;
import cn.com.zhihetech.util.hibernate.*;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.apache.commons.httpclient.util.DateUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ydc on 16-4-28.
 */
@Service(value = "luckDrawDetailService")
public class LuckDrawDetailServiceImpl implements ILuckDrawDetailService {

    @Resource(name = "luckDrawDetailDao")
    private ILuckyDrawDetailDao luckyDrawDetailDao;
    @Resource(name = "luckDrawUserRecordDao")
    private ILuckDrawUserRecordDao luckDrawUserRecordDao;
    @Resource(name = "luckyDrawActivityDao")
    private ILuckyDrawActivityDao luckyDrawActivityDao;
    @Resource(name = "luckyDrawDao")
    private ILuckyDrawDao luckyDrawDao;

    @Override
    public LuckyDrawDetail getById(String id) {
        return null;
    }

    @Override
    public void delete(LuckyDrawDetail luckyDrawDetail) {

    }

    @Override
    public LuckyDrawDetail add(LuckyDrawDetail luckyDrawDetail) {
        return null;
    }

    @Override
    public void update(LuckyDrawDetail luckyDrawDetail) {

    }

    @Override
    public List<LuckyDrawDetail> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.luckyDrawDetailDao.getEntities(pager, queryParams);
    }

    @Override
    public PageData<LuckyDrawDetail> getPageData(Pager pager, IQueryParams queryParams) {
        return this.luckyDrawDetailDao.getPageData(pager, queryParams);
    }

    @Override
    public ResponseMessage executeluckDraw(User user) {
        Date now = new Date();
        ResponseMessage responseMessage = null;
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("permit", true);
        List<LuckyDrawActivity> luckyDrawActivities = this.luckyDrawActivityDao.getEntities(queryParams);
        if (luckyDrawActivities.size() == 0) {
            throw new SystemException("目前没有任何抽奖活动！");
        }
        LuckyDrawActivity luckyDrawActivity = luckyDrawActivities.get(0);

        queryParams = new GeneralQueryParams();
        queryParams.andEqual("luckyDrawActivity", luckyDrawActivity);
        long alreadyLkDrawNum = this.luckDrawUserRecordDao.getRecordTotal(queryParams);

        queryParams.andEqual("user", user);
        Date startDate = DateUtils.thisDay().getStartDate();
        Date endDate = DateUtils.thisDay().getEndDate();
        queryParams.andMoreAndEq("drawDate", startDate).andLessAndEq("drawDate", endDate);
        long total = this.luckDrawUserRecordDao.getRecordTotal(queryParams);
        if (total > 0) {
            responseMessage = new ResponseMessage(ResponseStatusCode.NOTLUCK, true, "Sorry！亲，你今天已经抽过奖咯，明天再来哦！");
            return responseMessage;
        }
        queryParams = new GeneralQueryParams();
        queryParams.andEqual("luckyDraw.luckyDrawActivity", luckyDrawActivity);
        List<Object> totals = this.luckyDrawDetailDao.getProperty("max(whoNumber)", null, queryParams);
        if (totals == null ||totals.isEmpty() || totals.get(0) == null) {
            queryParams = new GeneralQueryParams();
            queryParams.andEqual("luckyDrawActivity", luckyDrawActivity).andEqual("ldOrder", 0).andEqual("delState", false);
            List<LuckyDraw> luckyDraws = this.luckyDrawDao.getEntities(queryParams);
            if (luckyDraws.size() == 0) {
                throw new SystemException("系统错误，请与管理员联系！");
            }
            LuckyDraw luckyDraw = luckyDraws.get(0);
            LuckyDrawDetail luckyDrawDetail = new LuckyDrawDetail();
            luckyDrawDetail.setLuckyDraw(luckyDraw);

            long whoNum = alreadyLkDrawNum + 1;
            LuckDrawUserRecord luckDrawUserRecord = new LuckDrawUserRecord();
            luckDrawUserRecord.setUser(user);
            luckDrawUserRecord.setDrawDate(now);
            luckDrawUserRecord.setLuckyDrawActivity(luckyDrawActivity);
            luckDrawUserRecord.setPersonNumber(whoNum);
            this.luckDrawUserRecordDao.saveEntity(luckDrawUserRecord);

            responseMessage = new ResponseMessage(ResponseStatusCode.SUCCESS_CODE, true, "Sorry！你未中奖！");
            responseMessage.setAttribute(luckyDrawDetail);
            return responseMessage;
        }
        if ((long) totals.get(0) == alreadyLkDrawNum) {
            responseMessage = new ResponseMessage(ResponseStatusCode.NOTLUCK, true, "Sorry！你来晚了！奖品已经被抽完了！");
            return responseMessage;
        }

        long whoNum = alreadyLkDrawNum + 1;
        LuckDrawUserRecord luckDrawUserRecord = new LuckDrawUserRecord();
        luckDrawUserRecord.setUser(user);
        luckDrawUserRecord.setDrawDate(now);
        luckDrawUserRecord.setLuckyDrawActivity(luckyDrawActivity);
        luckDrawUserRecord.setPersonNumber(whoNum);
        this.luckDrawUserRecordDao.saveEntity(luckDrawUserRecord);
        queryParams = new GeneralQueryParams();
        queryParams.andEqual("whoNumber", whoNum);
        List<LuckyDrawDetail> luckyDrawDetails = this.luckyDrawDetailDao.getEntities(queryParams);
        if (luckyDrawDetails.size() == 0) {
            queryParams = new GeneralQueryParams();
            queryParams.andEqual("luckyDrawActivity", luckyDrawActivity).andEqual("ldOrder", 0).andEqual("delState", false);
            List<LuckyDraw> luckyDraws = this.luckyDrawDao.getEntities(queryParams);
            if (luckyDraws.size() == 0) {
                throw new SystemException("系统错误，请与管理员联系！");
            }
            LuckyDraw luckyDraw = luckyDraws.get(0);
            LuckyDrawDetail luckyDrawDetail = new LuckyDrawDetail();
            luckyDrawDetail.setLuckyDraw(luckyDraw);
            responseMessage = new ResponseMessage(ResponseStatusCode.SUCCESS_CODE, true, "Sorry！你未中奖！");
            responseMessage.setAttribute(luckyDrawDetail);
        } else {
            LuckyDrawDetail luckyDrawDetail = luckyDrawDetails.get(0);
            LuckyDraw luckyDraw = luckyDrawDetail.getLuckyDraw();
            luckyDraw.setSurplus(luckyDraw.getSurplus() - 1);
            luckyDrawDetail.setUser(user);
            luckyDrawDetail.setDrawDate(now);
            luckyDrawDetail.setUseState(false);
            luckyDrawDetail.setLukyDrawCode(Constant.BEGIN_CODE_LUCKY_DRAW + GeneratedNRandom.generated(2) + System.currentTimeMillis());
            this.luckyDrawDao.updateEntity(luckyDraw);
            this.luckyDrawDetailDao.updateEntity(luckyDrawDetail);
            responseMessage = new ResponseMessage(ResponseStatusCode.SUCCESS_CODE, true, "恭喜你，中奖了！");
            responseMessage.setAttribute(luckyDrawDetail);
        }
        return responseMessage;
    }

    @Override
    public ResponseMessage getLuckDrawList() {
        ResponseMessage responseMessage = null;
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("permit", true);
        List<LuckyDrawActivity> luckyDrawActivities = this.luckyDrawActivityDao.getEntities(queryParams);
        if (luckyDrawActivities.size() == 0) {
            responseMessage = new ResponseMessage(ResponseStatusCode.HAVE_NO_LUCKY_DRAW, true, "不存在抽奖活动!");
            return responseMessage;
        }
        LuckyDrawActivity luckyDrawActivity = luckyDrawActivities.get(0);
        queryParams = new GeneralQueryParams();
        queryParams.andEqual("luckyDrawActivity", luckyDrawActivity).andEqual("delState", false).sort("ldOrder", Order.ASC);
        List<LuckyDraw> luckyDraws = this.luckyDrawDao.getEntities(queryParams);
        Iterator<LuckyDraw> iterator = luckyDraws.iterator();
        while (iterator.hasNext()) {
            LuckyDraw luckyDraw = iterator.next();
            if (luckyDraw.getLdOrder() == 0 && luckyDraw.getPercentage() <= 0) {
                iterator.remove();
            }
        }
        if (luckyDraws.size() == 0) {
            throw new SystemException("系统错误,请与管理员联系！");
        }
        responseMessage = new ResponseMessage(ResponseStatusCode.SUCCESS_CODE, true, "获取抽奖活动成功");
        responseMessage.setAttribute(luckyDraws);
        return responseMessage;
    }

    /**
     * 奖品验证（使用）
     *
     * @param luckCode 奖品号码
     * @param mobileNo 中奖手机号
     * @param admin    验证人
     */
    @Override
    public void executeUseLuckyDrawDetail(String luckCode, String mobileNo, Admin admin) {
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("lukyDrawCode", luckCode)
                .andEqual("user.userPhone", mobileNo);
        if (admin.getMerchant() != null) {
            queryParams.andEqual("luckyDraw.merchant", admin.getMerchant());
        }
        List<LuckyDrawDetail> luckyDrawDetails = this.luckyDrawDetailDao.getEntities(null, queryParams);
        if (luckyDrawDetails == null || luckyDrawDetails.isEmpty()) {
            throw new SystemException("奖品号码或手机号不正确，请检查是否输入正确！");
        }
        LuckyDrawDetail detail = luckyDrawDetails.get(0);
        if (detail.isUseState()) {
            throw new SystemException("奖品已领用，请勿重复操作！");
        }
        detail.setUseState(true);
        detail.setUseDate(new Date());
        this.luckyDrawDetailDao.updateEntity(detail);
    }
}
