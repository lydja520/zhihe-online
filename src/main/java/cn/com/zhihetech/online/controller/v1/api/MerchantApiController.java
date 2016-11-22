package cn.com.zhihetech.online.controller.v1.api;

import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IMerchantService;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by YangDaiChun on 2015/12/26.
 */
@Controller
public class MerchantApiController extends ApiController {

    @Resource(name = "merchantService")
    private IMerchantService merchantService;

    /**
     * 获取商家详细信息
     * <p>
     * <p>
     * URL : /api/merchant/{id}
     * <p>
     * {id}商家ID
     *
     * @param merchantId
     * @return
     */
    @Deprecated
    @ResponseBody
    @RequestMapping(value = "merchant/{merchantId}")
    public Merchant getMerchantDetails(@PathVariable(value = "merchantId") String merchantId, HttpServletResponse response) throws IOException {
        if (StringUtils.isEmpty(merchantId)) {
            throw new SystemException("传递参数错误");
        }
        Merchant merchant = this.merchantService.getById(merchantId);
        if (merchant == null) {
            throw new SystemException("未找到对应的商家");
        }
        return merchant;
    }

    /**
     * 获取商家详细信息
     * <p>
     * <p>
     * URL : /api/merchant/{id}/detail
     * <p>
     * {id}商家ID
     *
     * @param merchantId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "merchant/{merchantId}/info")
    public ResponseMessage getMerchantById(@PathVariable(value = "merchantId") String merchantId, HttpServletResponse response) throws IOException {
        if (StringUtils.isEmpty(merchantId)) {
            throw new SystemException("传递参数错误");
        }
        Merchant merchant = this.merchantService.getById(merchantId);
        if (merchant == null) {
            return executeResult(ResponseStatusCode.PAGE_NOT_FOUND_CODE, false, "商家不存在");
        }
        return executeResult(ResponseStatusCode.SUCCESS_CODE, null, merchant);
    }

    /**
     * <h2>获取所有商家</h2>
     * <p>
     * URL: api/merchants<br>
     * <p>
     * 参数： <br>
     * searchName: 搜索的属性名,如merchantName
     * searchValue:   搜索的属性值，如 '"挚合"
     * page: 第几页  <br>
     * rows:每页多少条  <br>
     * 不传参数默认为获取第一页的20条数据  <br>
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "merchants")
    public PageData<Merchant> getAllMerchants(HttpServletRequest request, @RequestParam(value = "goodsNum", required = false, defaultValue = "3") int goodsNum) {
        IQueryParams queryParams = this.createQueryParams(request);
        queryParams.andEqual("examinState", Constant.EXAMINE_STATE_EXAMINED_OK)
                .andEqual("permit", true);
        if (!queryParams.sortContainsKey("merchOrder")) {
            queryParams.sort("merchOrder", Order.DESC);
        }
        if (!queryParams.sortContainsKey("updateDate")) {
            queryParams.sort("updateDate", Order.DESC);
        }
        if (!queryParams.sortContainsKey("createDate")) {
            queryParams.sort("createDate", Order.DESC);
        }
        return this.merchantService.getMerchantAndGoods(this.createPager(request), queryParams, goodsNum);
    }


    /**
     * <h2>根据商品分类获取对应的商家</h2>
     * <p>
     * URL: api/goodsAttributeSet/{goodsAttSetId}/Merchants  <br>
     * {goodsAttSetId}:商品分类Id   <br>
     *
     * @param goodsAttSetId
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "goodsAttributeSet/{goodsAttSetId}/Merchants")
    public PageData<Merchant> getMerchantsByGoodsAttSet(@PathVariable(value = "goodsAttSetId") String goodsAttSetId, HttpServletRequest request) {
        return this.merchantService.getMerchantsByGoodsAttSet(goodsAttSetId, this.createPager(request));
    }

    /**
     * <h3>获取商家列表</h3>
     * <p>
     * URL: api/merchantOrGoods/list
     * <p>
     * 参数：
     * page: 第几页
     * rows:每页多少条
     * 不传page和rows参数默认为获取第一页的20条数据
     * <p>
     * goodsNum:每个商家需要几条上新的商品
     * 不传goodsNum参数默认每个商家传3个推荐商品
     * <p>
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "merchantOrGoods/list")
    public PageData<Merchant> getDailyNew(HttpServletRequest request, @RequestParam(value = "goodsNum", required = false, defaultValue = "3") int goodsNum) {
        IQueryParams queryParams = this.createQueryParams(request);
        queryParams.andEqual("examinState", Constant.EXAMINE_STATE_EXAMINED_OK).andEqual("permit", true);
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
