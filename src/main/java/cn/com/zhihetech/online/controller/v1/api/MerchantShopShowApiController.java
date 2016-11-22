package cn.com.zhihetech.online.controller.v1.api;

import cn.com.zhihetech.online.bean.ShopShow;
import cn.com.zhihetech.online.service.IShopShowService;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ShenYunjie on 2016/3/16.
 */
@Controller
public class MerchantShopShowApiController extends ApiController {

    @Resource(name = "shopShowService")
    private IShopShowService shopShowService;

    /**
     * 获取指定商家的门店照（最多只能获取到20张）
     * url:api/merchant/{merchantId}/shopShows
     *
     * @param merchantId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "merchant/{merchantId}/shopShows")
    public List<ShopShow> getMerchantShopShows(@PathVariable("merchantId") String merchantId) {
        return this.shopShowService.getShopShowsByMerchantId(merchantId);
    }
}
