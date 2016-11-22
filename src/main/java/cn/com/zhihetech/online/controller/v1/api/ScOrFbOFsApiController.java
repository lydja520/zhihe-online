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
public class ScOrFbOFsApiController extends ApiController {

    @Resource(name = "merchantService")
    private IMerchantService merchantService;

    /**
     * <h3>购物中心，特色街区，特色店中的商家列表</h3>
     * url:  api/scOrFbOrFs/list  <br>
     * <p>
     * <p>
     * <p>参数：</p>
     * type : 传“1”则表示获取购物中心得商家，“2" 则表示特色街区，不传则表示特色店  <br>
     * typeId ： 如果type传“1”则typeId应传购物中心的id,如果type传“2”则typeId则应该传特色街区的ID,若果type传“3”,则不用传该参数
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "scOrFbOrFs/list")
    public PageData<Merchant> getDailyNew(HttpServletRequest request, @RequestParam(value = "goodsNum", required = false, defaultValue = "3") int goodsNum, @RequestParam(value = "typeId", required = false) String typeId, @RequestParam(value = "type", required = false, defaultValue = "3") int type) {
        IQueryParams queryParams = this.createQueryParams(request);
        queryParams.andEqual("examinState", Constant.EXAMINE_STATE_EXAMINED_OK).andEqual("permit", true);
        if (type == 1) {
            queryParams.andEqual("shoppingCenter.scId", typeId);
        } else if (type == 2) {
            queryParams.andEqual("featuredBlock.fbId", typeId);
        } else {
            queryParams.andEqual("storeType", Merchant.StoreType.featuredStore);
        }

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
