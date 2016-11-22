package cn.com.zhihetech.online.controller.v2.api;


import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.exception.StatusCodeException;
import cn.com.zhihetech.online.service.ISkuService;
import cn.com.zhihetech.online.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by ShenYunjie on 2016/7/8.
 */
@Controller
public class GoodsAttrSkuInfoApiController extends V2ApiController {

    @Resource(name = "skuService")
    private ISkuService skuService;

    /**
     * 根据商品ID获取商品属性和Sku列表
     * URL:api/v2/goods/{goodsId}/attrsAndSkus/info
     *
     * @param goodsId 商品ID
     * @return
     */
    @ResponseBody
    @RequestMapping("goods/{goodsId}/attrsAndSkus/info")
    public ResponseMessage getAttrsAndSkuInfo(@PathVariable("goodsId") String goodsId) {
        if (StringUtils.isEmpty(goodsId)) {
            throw new StatusCodeException("找不到对应的商品信息！", ResponseStatusCode.PAGE_NOT_FOUND_CODE);
        }
        ResponseMessage responseMessage = executeResult();
        responseMessage.setAttribute(this.skuService.getGoodsAttrSkuInfoByGoodsId(goodsId));
        return responseMessage;
    }
}
