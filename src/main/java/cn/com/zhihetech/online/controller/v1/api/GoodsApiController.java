package cn.com.zhihetech.online.controller.v1.api;

import cn.com.zhihetech.online.bean.*;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.service.*;
import cn.com.zhihetech.online.v2.service.IGoodsService;
import cn.com.zhihetech.online.vo.GoodsParams;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * Created by YangDaiChun on 2016/1/1.
 *
 * @version 2.0
 */
@Controller
public class GoodsApiController extends ApiController {

    @Resource(name = "merchantService")
    private IMerchantService merchantService;

    @Resource(name = "goodsService")
    private cn.com.zhihetech.online.service.IGoodsService goodsService;

    @Resource(name = "goodsDetailService")
    private IGoodsDetailService goodsDetailService;

    @Resource(name = "goodsBannerService")
    private IGoodsBannerService goodsBannerService;

    @Resource(name = "goodsScoreService")
    private IGoodsScoreService goodsScoreService;

    @Resource(name = "v2GoodsService")
    private IGoodsService v2GoodsService;

    /**
     * 获取商家的商品列表
     * URL ：api/goodses/{merchantId}
     * <p>
     * {merchantId}:商家id
     * <p>
     * 参数：
     * page: 第几页
     * rows:每页多少条
     * 不传参数默认为获取第一页的20条数据
     *
     * @param merchantId
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "goodses/{merchantId}")
    public PageData<Goods> getGoodListByMerchantId(@PathVariable(value = "merchantId") String merchantId, HttpServletRequest request, GoodsParams params) throws IOException {
        params.setMerchantId(merchantId);
        Pager pager = createPager(request);
        return this.v2GoodsService.getPageDataByParams(pager, params);
    }

    /**
     * <h2>获取所有商品</h2>
     * <p>
     * URL: api/goodses <br>
     * <p>
     * 参数： <br>
     * searchName: 搜索的属性名,如goodsName
     * searchValue:   搜索的属性值，如 '"沙发"
     * page: 第几页  <br>
     * rows:每页多少条  <br>
     * 不传参数默认为获取第一页的20条数据  <br>
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "goodses")
    public PageData<Goods> getAllGoods(HttpServletRequest request, GoodsParams params) {
        return this.v2GoodsService.getPageDataByParams(createPager(request), params);
    }


    /**
     * 根据商品ID获取商品的基本信息
     * <p>
     * URL:  api/goods/{goodsId}
     * <p>
     * {goodsId}:商品id
     *
     * @param goodsId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "goods/{goodsId}")
    public ResponseMessage getGoodsByGoodsId(@PathVariable(value = "goodsId") String goodsId) {
        Goods goods = this.goodsService.getById(goodsId);
        if ((goods == null) || (goods.getExaminState() != Constant.EXAMINE_STATE_EXAMINED_OK)) {
            return executeResult(ResponseStatusCode.PAGE_NOT_FOUND_CODE, "对不起，找不到对应的商品!");
        }

        if (goods.getDeleteState() || !goods.isOnsale()) {
            return executeResult(ResponseStatusCode.NOT_ONSAL, "对不起！该商品已经下架！");
        }

        return executeResult(ResponseStatusCode.SUCCESS_CODE, "", goods);
    }

    /**
     * 根据商品Id获取商品的轮播图
     * URL: api/goodsBanners/{goodsId}
     * {goodsId}:商品Id
     *
     * @param goodsId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "goodsBanners/{goodsId}")
    public List<GoodsBanner> getGoodsBannersByGoosId(@PathVariable(value = "goodsId") String goodsId) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("goods.goodsId", goodsId).sort("bannerOrder", Order.ASC);
        return this.goodsBannerService.getAllByParams(null, queryParams);
    }

    /**
     * 根据商品Id获取商品的详情图
     * URL: api/goodsDetails/{goodsId}
     * {goodsId}:商品Id
     *
     * @param goodsId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "goodsDetails/{goodsId}")
    public List<GoodsDetail> getGoodsDetailsByGoodsId(@PathVariable(value = "goodsId") String goodsId) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("goods.goodsId", goodsId).sort("detailOrder", Order.ASC);
        return this.goodsDetailService.getAllByParams(null, queryParams);
    }


    @ResponseBody
    @RequestMapping(value = "goods/{goodsId}/goodsScores")
    public PageData<GoodsScore> getGoodsScoreByGoodsId(@PathVariable(value = "goodsId") String goodsId, HttpServletRequest request) {
        IQueryParams queryParams = this.createQueryParams(request);
        queryParams.andEqual("goods.goodsId", goodsId);
        return this.goodsScoreService.getPageData(this.createPager(request), queryParams);
    }

    /**
     * 根据商品的类别获取对应的商品
     * URL：api/goodsAttributeSet/{goodsAttSetId}/goodses
     * <p>
     * {goodsAttSetId}：商品分类Id
     *
     * @param goodsAttSetId
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "goodsAttributeSet/{goodsAttSetId}/goodses")
    public PageData<Goods> getGoodsesByGoodsAttSet(@PathVariable(value = "goodsAttSetId") String goodsAttSetId, HttpServletRequest request, GoodsParams params) {
        params.setGoodsAttrSetId(goodsAttSetId);
        return this.v2GoodsService.getPageDataByParams(createPager(request), params);
    }

}
