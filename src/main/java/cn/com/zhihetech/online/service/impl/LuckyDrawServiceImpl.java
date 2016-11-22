package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.LuckyDraw;
import cn.com.zhihetech.online.bean.LuckyDrawActivity;
import cn.com.zhihetech.online.dao.ILuckyDrawActivityDao;
import cn.com.zhihetech.online.dao.ILuckyDrawDao;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.ILuckyDrawActivityService;
import cn.com.zhihetech.online.service.ILuckyDrawService;
import cn.com.zhihetech.online.util.StringUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by ydc on 16-4-22.
 */
@Service(value = "luckyDrawService")
public class LuckyDrawServiceImpl implements ILuckyDrawService {

    @Resource(name = "luckyDrawDao")
    private ILuckyDrawDao luckyDrawDao;
    @Resource(name = "luckyDrawActivityDao")
    private ILuckyDrawActivityDao luckyDrawActivityDao;

    @Override
    public LuckyDraw getById(String id) {
        return null;
    }

    @Override
    public void delete(LuckyDraw luckyDraw) {

    }

    @Override
    public LuckyDraw add(LuckyDraw luckyDraw) {
        String lkDrawActId = luckyDraw.getLuckyDrawActivity().getActivityId();
        if (this.getLkDrawActSubmitState(lkDrawActId)) {
            throw new SystemException("已经上过线的抽奖活动就不支持添加操作！");
        }
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("luckyDrawActivity.activityId", lkDrawActId)
                .andEqual("ldOrder", luckyDraw.getLdOrder()).andEqual("delState", false);
        long total = this.luckyDrawDao.getRecordTotal(queryParams);
        if (total > 0) {
            throw new SystemException("顺序存在冲突!");
        }
        BigDecimal bigDecimal = new BigDecimal(luckyDraw.getPercentage()).setScale(2, BigDecimal.ROUND_HALF_UP);
        queryParams = new GeneralQueryParams();
        queryParams.andEqual("luckyDrawActivity.activityId", lkDrawActId).andEqual("delState", false);
        BigDecimal bigDecimal1 = new BigDecimal(this.getCurrentPercentage("sum(percentage)", null, null, queryParams))
                .setScale(2, BigDecimal.ROUND_HALF_UP);
        if(bigDecimal1.intValue() ==1){
            throw new SystemException("抽奖概率已经为100%！");
        }
        float percentage = bigDecimal.add(bigDecimal1).floatValue();
        if (percentage > 1) {
            throw new SystemException("抽奖概率之和超过了100%！");
        }
        luckyDraw.setPercentage(bigDecimal.floatValue());
        luckyDraw.setDelState(false);
        luckyDraw.setLuckState(true);
        luckyDraw.setSurplus(luckyDraw.getAmount());
        return this.luckyDrawDao.saveEntity(luckyDraw);
    }

    @Override
    public void update(LuckyDraw luckyDraw) {
        String lkDrawActId = luckyDraw.getLuckyDrawActivity().getActivityId();
        if (this.getLkDrawActSubmitState(lkDrawActId)) {
            throw new SystemException("已经上过线的抽奖活动就不支持添加操作！");
        }
        BigDecimal bigDecimal = new BigDecimal(luckyDraw.getPercentage()).setScale(2, BigDecimal.ROUND_HALF_UP);
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("luckyDrawActivity.activityId", lkDrawActId).andEqual("delState", false);
        float _p = (float) this.getCurrentPercentage("sum(percentage)", luckyDraw.getLuckyDrawId(), null, queryParams);
        BigDecimal bigDecimal1 = new BigDecimal(_p).setScale(2, BigDecimal.ROUND_HALF_UP);
        float percentage = bigDecimal.add(bigDecimal1).floatValue();
        if (percentage > 1) {
            throw new SystemException("抽奖概率之和超过了100%！");
        }
        LuckyDraw _luckyDraw = this.luckyDrawDao.findEntityById(luckyDraw.getLuckyDrawId());
        if (_luckyDraw == null) {
            throw new SystemException("不存在该奖项!");
        }
        if (luckyDraw.getLdOrder() == 0) {
            _luckyDraw.setPercentage(bigDecimal.floatValue());
        } else {
            _luckyDraw.setDesc(luckyDraw.getDesc());
            _luckyDraw.setMerchant(luckyDraw.getMerchant());
            _luckyDraw.setPercentage(bigDecimal.floatValue());
            _luckyDraw.setLdOrder(luckyDraw.getLdOrder());
            _luckyDraw.setAmount(luckyDraw.getAmount());
            _luckyDraw.setLuckyDrawActivity(luckyDraw.getLuckyDrawActivity());
            _luckyDraw.setSurplus(luckyDraw.getSurplus());
        }
        this.luckyDrawDao.updateEntity(_luckyDraw);
    }


    @Override
    public List<LuckyDraw> getAllByParams(Pager pager, IQueryParams queryParams) {
        return null;
    }

    @Override
    public PageData<LuckyDraw> getPageData(Pager pager, IQueryParams queryParams) {
        return this.luckyDrawDao.getPageData(pager, queryParams);
    }

    @Override
    public void addNotWinLuckDraw(String lkDrawActId) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("luckyDrawActivity.activityId", lkDrawActId)
                .andEqual("ldOrder", 0);
        long total = this.luckyDrawDao.getRecordTotal(queryParams);
        if (total > 0) {
            return;
        }
        LuckyDraw luckyDraw = new LuckyDraw();
        luckyDraw.setDesc("只能设置未中奖的概率，默认为中奖概率为60%");
        luckyDraw.setLdOrder(0);
        luckyDraw.setDelState(false);
        LuckyDrawActivity luckyDrawActivity = new LuckyDrawActivity();
        luckyDrawActivity.setActivityId(lkDrawActId);
        luckyDraw.setLuckyDrawActivity(luckyDrawActivity);
        luckyDraw.setAmount(0);
        luckyDraw.setPercentage(0.60f);
        luckyDraw.setMerchant(null);
        luckyDraw.setTitle("未中奖");
        luckyDraw.setLuckState(false);
        this.luckyDrawDao.saveEntity(luckyDraw);
    }

    @Override
    public double getCurrentPercentage(String selector, String property, Pager pager, IQueryParams queryParams) {
        if (!StringUtils.isEmpty(property)) {
            queryParams.andNotEq("luckyDrawId", property);
        }
        List<Object> percentages = this.luckyDrawDao.getProperty(selector, pager, queryParams);
        if(percentages.get(0) == null){
            return 0f;
        }
        BigDecimal percentage = new BigDecimal(0f);
        for (Object o : percentages) {
            BigDecimal _o = new BigDecimal((double) o).setScale(2, BigDecimal.ROUND_HALF_UP);
            percentage = percentage.add(_o);
        }
        return percentage.doubleValue();
    }

    @Override
    public boolean getLkDrawActSubmitState(String lkDrawActId) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("activityId", lkDrawActId);
        List<Object> states = this.luckyDrawActivityDao.getProperty("submitState", null, queryParams);
        if (states.size() == 0) {
            throw new SystemException("不存在该抽奖活动");
        }
        return (boolean) states.get(0);
    }

    @Override
    public void executeUpdate(Map<String, Object> paramAndValue, IQueryParams queryParams) {
        this.luckyDrawDao.executeUpdate(paramAndValue,queryParams);
    }
}
