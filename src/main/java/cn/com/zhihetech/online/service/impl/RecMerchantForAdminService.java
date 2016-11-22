package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.ImgInfo;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.bean.RecommendMerchantForAdmin;
import cn.com.zhihetech.online.commons.AppConfig;
import cn.com.zhihetech.online.dao.IRecMerchantForAdminDao;
import cn.com.zhihetech.online.service.IRecMerchantForAdminService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ydc on 16-8-15.
 */
@Service(value = "recMerchantForAdminService")
public class RecMerchantForAdminService implements IRecMerchantForAdminService {

    @Resource(name = "recMerchantForAdminDao")
    private IRecMerchantForAdminDao recMerchantForAdminDao;

    @Override
    public RecommendMerchantForAdmin getById(String id) {
        return this.recMerchantForAdminDao.findEntityById(id);
    }

    @Override
    public void delete(RecommendMerchantForAdmin recommendMerchantForAdmin) {
        this.recMerchantForAdminDao.deleteEntity(recommendMerchantForAdmin);
    }

    @Override
    public RecommendMerchantForAdmin add(RecommendMerchantForAdmin recommendMerchantForAdmin) {
        return this.recMerchantForAdminDao.saveEntity(recommendMerchantForAdmin);
    }

    @Override
    public void update(RecommendMerchantForAdmin recommendMerchantForAdmin) {
        this.recMerchantForAdminDao.updateEntity(recommendMerchantForAdmin);
    }

    @Override
    public List<RecommendMerchantForAdmin> getAllByParams(Pager pager, IQueryParams queryParams) {
        return null;
    }

    @Override
    public PageData<RecommendMerchantForAdmin> getPageData(Pager pager, IQueryParams queryParams) {
        return this.recMerchantForAdminDao.getPageData(pager, queryParams);
    }

    @Override
    public List<RecommendMerchantForAdmin> getRecMerchants() {
        String[] selectors = new String[]{"merchant.merchantId", "merchant.merchName", "coverImg.key", "recOrder"};
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.sort("recOrder", Order.ASC);
        List<Object[]> oRecMerchants = this.recMerchantForAdminDao.getProperties(selectors, null, queryParams);
        List<RecommendMerchantForAdmin> recMerchants = new ArrayList<>();
        for (Object[] oRecMerchant : oRecMerchants) {
            RecommendMerchantForAdmin recMerchant = new RecommendMerchantForAdmin();
            Merchant merchant = new Merchant((String) oRecMerchant[0]);
            merchant.setMerchName((String) oRecMerchant[1]);
            recMerchant.setMerchant(merchant);
            ImgInfo coverImg = new ImgInfo();
            coverImg.setKey((String) oRecMerchant[2]);
            coverImg.setUrl(AppConfig.QiNiuConfig.DOMIN + oRecMerchant[2]);
            recMerchant.setCoverImg(coverImg);
            recMerchant.setRecOrder(Integer.parseInt(String.valueOf(oRecMerchant[3])));
            recMerchants.add(recMerchant);
        }
        return recMerchants;
    }

    @Override
    public long getRecTotal(GeneralQueryParams generalQueryParams) {
        return this.recMerchantForAdminDao.getRecordTotal(generalQueryParams);
    }

    @Override
    public List<Object> getProperty(String selectror,Pager pager, IQueryParams queryParams) {
        return this.recMerchantForAdminDao.getProperty(selectror,pager,queryParams);
    }
}
