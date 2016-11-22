package cn.com.zhihetech.online.controller.v1.api;

import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.service.IMerchantService;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by YangDaiChun on 2016/1/1.
 */
@Controller
public class BuyKmOrBuyPreApiController extends ApiController {

    @Resource(name = "merchantService")
    private IMerchantService merchantService;

    /**
     * 买昆明和购地州
     * <p>
     * URL: /api/buyKmOrBuyPre/list
     * <p>
     * 参数：
     * page: 第几页
     * rows:每页多少条
     * 不传page和rows参数默认为获取第一页的20条数据
     * <p>
     * goodsNum:每个商家需要几条上新的商品
     * 不传goodsNum参数默认每个商家传3个最新上架的商品
     * <p>
     * isKunmingMerchant:布尔类型（传true或者false）true 为获取昆明的商家,false 为获取地州的商家，不传参数默认为 false
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "buyKmOrBuyPre/list")
    public PageData<Merchant> getBuyKunmingOrBuyPre(HttpServletRequest request, @RequestParam(value = "goodsNum", required = false, defaultValue = "3") int goodsNum, boolean isKunmingMerchant) {
        IQueryParams queryParams = this.createQueryParams(request);
        queryParams.andEqual("examinState", Constant.EXAMINE_STATE_EXAMINED_OK)
                .andEqual("permit", true)
                .andEqual("kunmingFlag", isKunmingMerchant).sort("merchOrder", Order.ASC);
        if (!queryParams.sortContainsKey("merchOrder")) {
            queryParams.sort("merchOrder", Order.DESC);
        }
        if (!queryParams.sortContainsKey("updateDate")) {
            queryParams.sort("updateDate", Order.DESC);
        }
        if (!queryParams.sortContainsKey("createDate")) {
            queryParams.sort("createDate", Order.DESC);
        }
        PageData<Merchant> merchantPageData = this.merchantService.getMerchantAndGoods(this.createPager(request), queryParams, goodsNum);
        return merchantPageData;
    }
}
