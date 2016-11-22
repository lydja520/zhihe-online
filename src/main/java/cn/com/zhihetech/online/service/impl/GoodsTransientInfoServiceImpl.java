package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.dao.ISkuDao;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IGoodsTransientInfoService;
import cn.com.zhihetech.online.util.NumberUtils;
import cn.com.zhihetech.online.util.StringUtils;
import cn.com.zhihetech.online.vo.GoodsTransientInfo;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ShenYunjie on 2016/7/12.
 */
@Service("goodsTransientInfoService")
public class GoodsTransientInfoServiceImpl implements IGoodsTransientInfoService {

    @Resource(name = "skuDao")
    protected ISkuDao skuDao;

    /**
     * @param goodsId
     * @return
     */
    @Override
    public GoodsTransientInfo getGoodsTransientInfoByGoodsId(String goodsId) {
        if (StringUtils.isEmpty(goodsId)) {
            throw new SystemException("商品ID不能为空！");
        }
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("goodsId", goodsId);
        List<Object> objects = this.skuDao.getProperty("skuId", null, queryParams);
        if (objects == null || objects.isEmpty()) {
            return null;
        }

        String[] values = new String[]{"MIN(price)", "MAX(price)", "SUM(stock)", "SUM(volume)"};
        queryParams = new GeneralQueryParams()
                .andEqual("goodsId", goodsId);
        List<Object[]> tmps = this.skuDao.getProperties(values, null, queryParams);
        GoodsTransientInfo transientInfo = new GoodsTransientInfo(goodsId);

        Object[] arraies = tmps.get(0);
        double minPrice = arraies[0] == null ? 0 : NumberUtils.floatScale(2, Float.parseFloat(arraies[0].toString()));
        double maxPrice = arraies[1] == null ? 0 : NumberUtils.floatScale(2, Float.parseFloat(arraies[1].toString()));
        long stock = arraies[2] == null ? 0 : Long.parseLong(arraies[2].toString());
        long volume = arraies[3] == null ? 0 : Long.parseLong(arraies[3].toString());
        transientInfo.setMinPrice(minPrice);
        transientInfo.setMaxPrice(maxPrice);
        transientInfo.setPrice(minPrice);
        transientInfo.setStock(stock);
        transientInfo.setVolume(volume);
        transientInfo.setCurrentStock(transientInfo.getStock() > transientInfo.getVolume() ? transientInfo.getStock() - transientInfo.getVolume() : 0); //为了避免库存显示负数
        return transientInfo;
    }
}
