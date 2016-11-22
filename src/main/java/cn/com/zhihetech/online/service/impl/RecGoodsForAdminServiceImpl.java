package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.bean.ImgInfo;
import cn.com.zhihetech.online.bean.Integral;
import cn.com.zhihetech.online.bean.RecommendGoodsForAdmin;
import cn.com.zhihetech.online.commons.AppConfig;
import cn.com.zhihetech.online.dao.IRecGoodsForAdminDao;
import cn.com.zhihetech.online.service.IRecGoodsForAdminService;
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
@Service(value = "recGoodsForAdminService")
public class RecGoodsForAdminServiceImpl implements IRecGoodsForAdminService {

    @Resource(name = "recGoodsForAdminDao")
    private IRecGoodsForAdminDao redGoodsDao;

    @Override
    public RecommendGoodsForAdmin getById(String id) {
        return this.redGoodsDao.findEntityById(id);
    }

    @Override
    public void delete(RecommendGoodsForAdmin recommendGoodsForAdmin) {
        this.redGoodsDao.deleteEntity(recommendGoodsForAdmin);
    }

    @Override
    public RecommendGoodsForAdmin add(RecommendGoodsForAdmin recommendGoodsForAdmin) {
        return this.redGoodsDao.saveEntity(recommendGoodsForAdmin);
    }

    @Override
    public void update(RecommendGoodsForAdmin recommendGoodsForAdmin) {
        this.redGoodsDao.updateEntity(recommendGoodsForAdmin);
    }

    @Override
    public List<RecommendGoodsForAdmin> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.redGoodsDao.getEntities(pager, queryParams);
    }

    @Override
    public PageData<RecommendGoodsForAdmin> getPageData(Pager pager, IQueryParams queryParams) {
        return this.redGoodsDao.getPageData(pager, queryParams);
    }

    @Override
    public long getRecTotal(GeneralQueryParams generalQueryParams) {
        return this.redGoodsDao.getRecordTotal(generalQueryParams);
    }

    @Override
    public List<RecommendGoodsForAdmin> getRecGoodses() {
        String[] selectors = new String[]{"recName", "goods.goodsId", "goods.goodsName", "coverImg.key", "recOrder"};
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.sort("recOrder", Order.ASC);
        List<Object[]> oRecGoodses = this.redGoodsDao.getProperties(selectors, null, queryParams);
        List<RecommendGoodsForAdmin> recGoodses = new ArrayList<>();
        for (Object[] oRecGoods : oRecGoodses) {
            RecommendGoodsForAdmin recGoods = new RecommendGoodsForAdmin();
            recGoods.setRecName((String) oRecGoods[0]);
            Goods goods = new Goods((String) oRecGoods[1]);
            goods.setGoodsName((String) oRecGoods[2]);
            recGoods.setGoods(goods);
            ImgInfo coverImg = new ImgInfo();
            coverImg.setKey((String) oRecGoods[3]);
            coverImg.setUrl(AppConfig.QiNiuConfig.DOMIN + oRecGoods[3]);
            recGoods.setCoverImg(coverImg);
            recGoods.setRecOrder(Integer.parseInt(String.valueOf(oRecGoods[4])));
            recGoodses.add(recGoods);
        }
        return recGoodses;
    }

    @Override
    public List<Object> getProperty(String s, Pager pager, GeneralQueryParams queryParams) {
        return this.redGoodsDao.getProperty(s, pager, queryParams);
    }
}
