package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.FocusMerchant;
import cn.com.zhihetech.online.bean.PushBrithdate;
import cn.com.zhihetech.online.dao.IFocusMerchantDao;
import cn.com.zhihetech.online.dao.IPushBrithdateDao;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IPushBrithdateService;
import cn.com.zhihetech.online.util.DateUtils;
import cn.com.zhihetech.online.util.PushMsgUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by YangDaiChun on 2016/3/24.
 */
@Service(value = "pushuBrithdateService")
    public class PushBrithdateServiceImpl implements IPushBrithdateService {

    @Resource(name = "pushBrithdateDao")
    private IPushBrithdateDao pushBrithdateDao;

    @Resource(name = "focusMerchantDao")
    private IFocusMerchantDao focusMerchantDao;

    @Override
    public PushBrithdate getById(String id) {
        return null;
    }

    @Override
    public void delete(PushBrithdate pushBrithdate) {

    }

    @Override
    public PushBrithdate add(PushBrithdate pushBrithdate) {
        return null;
    }

    @Override
    public void update(PushBrithdate pushBrithdate) {

    }

    @Override
    public List<PushBrithdate> getAllByParams(Pager pager, IQueryParams queryParams) {
        return null;
    }

    @Override
    public PageData<PushBrithdate> getPageData(Pager pager, IQueryParams queryParams) {
        return this.pushBrithdateDao.getPageData(pager,queryParams);
    }

    @Override
    public void addPushBrithdates() {
        IQueryParams queryParams = null;
        queryParams = new GeneralQueryParams();
        queryParams.andLessAndEq("nowBrithDay",new Date());
        this.pushBrithdateDao.executeDelete(queryParams);
        List<String> focusMerchantIds = this.focusMerchantDao.getInFiveDayBrithdayFocusMerchant(new Date());

        for (String focusMerchantId : focusMerchantIds) {
            FocusMerchant focusMerchant = this.focusMerchantDao.findEntityById(focusMerchantId);
            queryParams = new GeneralQueryParams();
            queryParams.andEqual("focusMerchant.focusMerchantId", focusMerchant.getFocusMerchantId()).andEqual("focusMerchant.user.userId", focusMerchant.getUser().getUserId());
            Long total = this.pushBrithdateDao.getRecordTotal(queryParams);
            if (total > 0) {
                return;
            }
            PushBrithdate pushBrithdate = new PushBrithdate();
            pushBrithdate.setFocusMerchant(focusMerchant);
            Date userBrithDate = focusMerchant.getUser().getBirthday();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-");
            StringBuffer nowYear = new StringBuffer(simpleDateFormat.format(new Date()));
            simpleDateFormat = new SimpleDateFormat("MM-dd");
            StringBuffer nowUserBrithDateStr = new StringBuffer(simpleDateFormat.format(userBrithDate));
            String nowUserBrithDate = (nowYear.append(nowUserBrithDateStr)).toString();
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date nowUserBrithday = null;
            try {
                nowUserBrithday = simpleDateFormat.parse(nowUserBrithDate);
            } catch (ParseException e) {
                throw new SystemException("日期转换错");
            }
            pushBrithdate.setNowBrithDay(nowUserBrithday);
            pushBrithdate.setPushState(false);
            this.pushBrithdateDao.saveEntity(pushBrithdate);
        }
    }

    @Override
    public void executeUpdate(Map<String, Object> propertyAndValues, IQueryParams queryParams) {
        this.executeUpdate(propertyAndValues,queryParams);
    }

    @Override
    @Transactional
    public void executePushUserbrithdayMsg(String pushBrithdateId, String userId, String pushInfo) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("pushBrithdateId",pushBrithdateId);
        Map<String,Object> paramAndValue = new HashMap<>();
        paramAndValue.put("pushState",true);
        this.pushBrithdateDao.executeUpdate(paramAndValue, queryParams);
        PushMsgUtils.PushToUserByUserId(userId,pushInfo);
    }
}
