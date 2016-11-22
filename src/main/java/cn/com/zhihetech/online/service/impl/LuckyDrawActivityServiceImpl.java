package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.LuckyDraw;
import cn.com.zhihetech.online.bean.LuckyDrawActivity;
import cn.com.zhihetech.online.bean.LuckyDrawDetail;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.dao.ILuckyDrawActivityDao;
import cn.com.zhihetech.online.dao.ILuckyDrawDao;
import cn.com.zhihetech.online.dao.ILuckyDrawDetailDao;
import cn.com.zhihetech.online.dao.IMerchantDao;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.ILuckyDrawActivityService;
import cn.com.zhihetech.online.util.LuckyDrawUtils;
import cn.com.zhihetech.online.util.StringUtils;
import cn.com.zhihetech.online.vo.LuckyDrawAlcObj;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ydc on 16-4-21.
 */
@Service(value = "luckyDrawActivityService")
public class LuckyDrawActivityServiceImpl implements ILuckyDrawActivityService {

    @Resource(name = "luckyDrawActivityDao")
    private ILuckyDrawActivityDao luckyDrawActivityDao;
    @Resource(name = "luckyDrawDao")
    private ILuckyDrawDao luckyDrawDao;
    @Resource(name = "luckDrawDetailDao")
    private ILuckyDrawDetailDao luckyDrawDetailDao;
    @Resource(name = "merchantDao")
    private IMerchantDao merchantDao;

    @Override
    public LuckyDrawActivity getById(String id) {
        return null;
    }

    @Override
    public void delete(LuckyDrawActivity luckyDrawActivity) {
        this.luckyDrawActivityDao.saveEntity(luckyDrawActivity);
    }

    @Override
    public LuckyDrawActivity add(LuckyDrawActivity luckyDrawActivity) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("permit", true);
        long total = this.luckyDrawActivityDao.getRecordTotal(null);
        if (luckyDrawActivity.isPermit() && total > 0) {
            throw new SystemException("当前已经有上线的抽奖活动，");
        }
        return this.luckyDrawActivityDao.saveEntity(luckyDrawActivity);
    }

    @Override
    public void update(LuckyDrawActivity luckyDrawActivity) {
        LuckyDrawActivity _luckyDrawActivity = this.luckyDrawActivityDao.findEntityById(luckyDrawActivity.getActivityId());
        if (_luckyDrawActivity == null) {
            throw new SystemException("不存在该活动!");
        }
        if (_luckyDrawActivity.isPermit()) {
            throw new SystemException("该活动为上线状态，不支持修改操作！");
        }
        _luckyDrawActivity.setActivityName(luckyDrawActivity.getActivityName());
        _luckyDrawActivity.setDesc(luckyDrawActivity.getDesc());
        this.luckyDrawActivityDao.updateEntity(_luckyDrawActivity);
    }

    @Override
    public List<LuckyDrawActivity> getAllByParams(Pager pager, IQueryParams queryParams) {
        return null;
    }

    @Override
    public PageData<LuckyDrawActivity> getPageData(Pager pager, IQueryParams queryParams) {
        return this.luckyDrawActivityDao.getPageData(pager, queryParams);
    }

    @Override
    public void executeOnlineOP(String activityId, boolean permit) {
        IQueryParams queryParams = null;
        LuckyDrawActivity luckyDrawActivity = this.luckyDrawActivityDao.findEntityById(activityId);
        if (luckyDrawActivity == null) {
            throw new SystemException("不存在该抽奖活动!");
        }
        if (!permit) {
            luckyDrawActivity.setPermit(false);
            this.luckyDrawActivityDao.updateEntity(luckyDrawActivity);
            return;
        }
        queryParams = new GeneralQueryParams().andEqual("permit", true);
        List objects = this.luckyDrawActivityDao.getProperty("activityId", null, queryParams);
        if (objects != null && objects.size() > 0) {
            throw new SystemException("现在还有活动未下线，不能上线活动！");
        }
       /* Map paramAndValue = new HashMap<>();
        paramAndValue.put("permit", false);
        this.luckyDrawActivityDao.executeUpdate(paramAndValue, null);*/
        luckyDrawActivity.setPermit(true);
        if (!luckyDrawActivity.isSubmitState()) {
            queryParams = new GeneralQueryParams();
            LuckyDrawAlcObj luckyDrawAlcObj = new LuckyDrawAlcObj();
            queryParams.andEqual("luckyDrawActivity.activityId", activityId).andEqual("delState", false);
            List<LuckyDraw> luckyDraws = this.luckyDrawDao.getEntities(queryParams);
            if (!(luckyDraws.size() >= 5 && luckyDraws.size() <= 13)) {
                throw new SystemException("为了更好的显示抽奖转盘，奖项的设置数量只能在4~12之间！");
            }
            Iterator<LuckyDraw> iterator = luckyDraws.iterator();
            while (iterator.hasNext()){
                LuckyDraw luckyDraw = iterator.next();
                if (luckyDraw.getPercentage() <= 0.0f || luckyDraw.getAmount() <= 0) {
                    iterator.remove();
                }
            }
            luckyDrawAlcObj.setLuckyDraws(luckyDraws);
            int notLuckyNum = 0;
            queryParams.andNotEq("ldOrder", notLuckyNum).andMoreThan("percentage", 0f);
            List<Object> allLuckAmounts = this.luckyDrawDao.getProperty("SUM(amount)", null, queryParams);
            if ((long) allLuckAmounts.get(0) == 0) {
                throw new SystemException("提交失败，此次抽奖活动中没有任何奖品");
            }
            long allLuckAmount = (long) allLuckAmounts.get(0);
            luckyDrawAlcObj.setAllLuckAmount(allLuckAmount);
            List<Object> allLuckPercentages = this.luckyDrawDao.getProperty("SUM(percentage)", null, queryParams);
            BigDecimal allLuckPercentage = new BigDecimal((double) allLuckPercentages.get(0)).setScale(2, BigDecimal.ROUND_HALF_UP);
            if (allLuckPercentage.floatValue() == (new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP)).floatValue()) {
                throw new SystemException("提交失败，此次抽奖活动百分比为0！");
            }
            luckyDrawAlcObj.setAllLuckPercentage(allLuckPercentage.floatValue());
            List<LuckyDraw> luckyDraws1 = this.luckyDrawDao.getMinLuckDraw();
            if (luckyDraws1.size() == 0) {
                throw new SystemException("系统错误，请与管理员联系！");
            }
            LuckyDraw firstLucyDraw = luckyDraws1.get(0);
            long firstPrizeAmount = firstLucyDraw.getAmount();
            float firstPrizePercentage = firstLucyDraw.getPercentage();
            luckyDrawAlcObj.setFirstPrizeAmount(firstPrizeAmount);
            luckyDrawAlcObj.setFirstPrizePercentage(firstPrizePercentage);
            List<LuckyDrawDetail> luckyDrawDetails = this.getLuckyDrawDetails(luckyDrawAlcObj);
            for (LuckyDrawDetail luckyDrawDetail : luckyDrawDetails) {
                this.luckyDrawDetailDao.saveEntity(luckyDrawDetail);
            }
            luckyDrawActivity.setSubmitState(true);
        }
        this.luckyDrawActivityDao.updateEntity(luckyDrawActivity);
    }

    /**
     * 获取此次活动的赞助商家
     *
     * @return
     */
    @Override
    public List<Merchant> getSponsorMerchants() {
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("luckyDrawActivity.permit", true)
                .andNotNull("merchant.merchantId");
        List<Object> objs = this.luckyDrawDao.getProperty("distinct merchant.merchantId", null, queryParams);
        if (objs == null || objs.isEmpty()) {
            return new ArrayList<Merchant>();
        }
        queryParams = new GeneralQueryParams().andIn("merchantId", objs);
        return this.merchantDao.getEntities(queryParams);
    }

    @Override
    public LuckyDrawActivity getCurrentLuckDrawActivity() {
        IQueryParams queryParams = new GeneralQueryParams().andEqual("permit", true);
        return this.luckyDrawActivityDao.getEntities(queryParams).get(0);
    }

    private List<LuckyDrawDetail> getLuckyDrawDetails(LuckyDrawAlcObj luckyDrawAlcObj) {
        long base = 0;  //基数
        long allAmount = 0; //所有奖品总数量（包括为中奖）
        List<LuckyDrawDetail> luckyDrawDetails = new ArrayList<>();
        List<LuckyDraw> luckyDraws = luckyDrawAlcObj.getLuckyDraws();  //抽奖奖项
        long allLuckAmount = luckyDrawAlcObj.getAllLuckAmount();  //所有奖品总数量
        float allLuckPercentage = luckyDrawAlcObj.getAllLuckPercentage(); //所有奖品所占的比例
        long firstPrizeAmount = luckyDrawAlcObj.getFirstPrizeAmount(); //占比例最小奖品数量
        float firstPrizePercentage = luckyDrawAlcObj.getFirstPrizePercentage(); //占比例最小的所占比例

        allAmount = new BigDecimal(allLuckAmount / allLuckPercentage)
                .setScale(0, BigDecimal.ROUND_HALF_UP).longValue();

        double _base = ((double) firstPrizeAmount / (double) allAmount) * firstPrizePercentage;

        BigDecimal b = new BigDecimal(String.valueOf(_base));
        BigDecimal divisor = BigDecimal.ONE;
        MathContext mc = new MathContext(1);
        String s = b.divide(divisor, mc).toPlainString();
        int position = s.length() - s.indexOf(".") - 1;
        String tmp = s.replaceAll("0", "").replaceAll("\\.", "");   //取小数点后面的有效数字
        int firstValue = StringUtils.isEmpty(tmp) ? 0 : Integer.parseInt(tmp);
        base = (long) ((double) firstPrizeAmount / (double) firstValue);
        if (base == 0) {
            base = 1;
        }
        base = (long) (base * Math.pow((int) 10, (int) position));
        base = (long) (base * (new BigDecimal(1 - allLuckPercentage).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue()));
        if (base < allAmount) {
            base = (long) (allAmount / allLuckPercentage);
        }

        /*调用抽奖算法*/
        int[] randomValue = LuckyDrawUtils.randomCommon(1, (int) base, (int) allLuckAmount);

        int x = 0;
        for (LuckyDraw luckyDraw : luckyDraws) {
            for (int i = 1; i <= luckyDraw.getAmount(); i++) {
                int value = randomValue[x];
                x++;
                LuckyDrawDetail luckyDrawDetail = new LuckyDrawDetail();
                luckyDrawDetail.setLuckyDraw(luckyDraw);
                luckyDrawDetail.setWhoNumber(value);
                luckyDrawDetails.add(luckyDrawDetail);
            }
        }
        return luckyDrawDetails;
    }

}
