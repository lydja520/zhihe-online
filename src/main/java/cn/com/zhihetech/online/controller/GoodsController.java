package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.*;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.dao.IGoodsBannerDao;
import cn.com.zhihetech.online.dao.IGoodsDetailDao;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.*;
import cn.com.zhihetech.online.vo.GoodsAndSku;
import cn.com.zhihetech.online.vo.SkuListInfo;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.*;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by YangDaiChun on 2015/12/14.
 */
@Controller
public class GoodsController extends SupportController {

    @Resource(name = "goodsService")
    private IGoodsService goodsService;
    @Resource(name = "goodsDetailService")
    private IGoodsDetailService goodsDetailService;
    @Resource(name = "goodsBannerService")
    private IGoodsBannerService goodsBannerService;
    @Resource(name = "merchantService")
    private IMerchantService merchantService;
    @Resource(name = "goodsScoreService")
    private IGoodsScoreService goodsScoreService;
    @Resource(name = "skuService")
    private ISkuService skuService;

    /**
     * 根据登录的管理员返回对应的商家
     *
     * @param request
     * @return
     */
    private Merchant adminToMerchant(HttpServletRequest request) {
        Admin admin = this.getCurrentAdmin(request);
        Merchant merchant = this.merchantService.getById(admin.getMerchant().getMerchantId());
        return merchant;
    }

    /**
     * 商家的所有商品列表页面
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "admin/goods")
    public String indexPage(HttpServletRequest request) {
        return "admin/goods";
    }

    /**
     * 所有商品列表统计
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "admin/goodsStatistics")
    public String allGoodsPage(HttpServletRequest request) {
        return "admin/goodsStatistics";
    }

    /**
     * 商家的所有商品列表
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/goods/list")
    public PageData<Goods> getGoodsPageDataByCurrentAdmin(HttpServletRequest request, @RequestParam(name = "id", defaultValue = "") String goodsId) {
        Merchant merchant = this.adminToMerchant(request);
        IQueryParams queryParams = this.createQueryParams(request);
        queryParams.andEqual("merchant", merchant).andEqual("deleteState", false);
/*        PageData<Goods> goodsPageData = this.goodsService.getPageData(this.createPager(request), queryParams);
        List<Goods> goodses = goodsPageData.getRows();
        List<GoodsAndSku> goodsAndSkus = this.goodsService.getGoodsAndSkus(goodses,goodsId);
        PageData<GoodsAndSku> goodsAndSkuPageData = new PageData<>();
        goodsAndSkuPageData.setPage(goodsPageData.getPage());
        goodsAndSkuPageData.setPageSize(goodsPageData.getPageSize());
        goodsAndSkuPageData.setTotalPage(goodsPageData.getTotalPage());
        goodsAndSkuPageData.setTotal(goodsPageData.getTotal());
        goodsAndSkuPageData.setRows(goodsAndSkus);*/

        return this.goodsService.getPageData(this.createPager(request), queryParams);
    }

    /**
     * 添加商品页面
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "admin/goods/addView")
    public ModelAndView addGoodsView(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("admin/goodsAdd");
        Merchant merchant = this.adminToMerchant(request);
        List<GoodsAttributeSet> goodsAttributeSets = new ArrayList<>(merchant.getCategories());
        modelAndView.addObject("goodsAttributeSets", goodsAttributeSets);
        return modelAndView;
    }

    /**
     * 添加或修改商品
     *
     * @param goods
     * @param goodsBanners
     * @param goodsDetails
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/goods/addOrUpdate")
    public ResponseMessage addGoodsOrUpdate(Goods goods, String[] goodsBanners, String[] goodsDetails, HttpServletRequest request) {
        if (goods.getCoverImg() == null || StringUtils.isEmpty(goods.getCoverImg().getImgInfoId())) {
            throw new SystemException("请上传一张商品封面图");
        }
        if (goods.getGoodsAttributeSet() == null) {
            throw new SystemException("请选择一个商品分类");
        }
        if (goodsBanners == null || goodsBanners.length == 0) {
            throw new SystemException("请至少上传一张商品轮播图！");
        }
        if (goodsDetails == null || goodsDetails.length == 0) {
            throw new SystemException("请上传至少一张商品详情图！");
        }
        Merchant merchant = this.adminToMerchant(request);
        List<GoodsBanner> goodsBanners1 = new LinkedList<GoodsBanner>();
        for (String goodsBanner : goodsBanners) {
            String imgInfoId = goodsBanner.substring(0, goodsBanner.indexOf("#"));
            String bannerOrder = goodsBanner.substring(goodsBanner.indexOf("#") + 1, goodsBanner.length());
            if (StringUtils.isEmpty(imgInfoId)) {
                continue;
            }
            if (StringUtils.isEmpty(bannerOrder)) {
                throw new SystemException("请输入完整商品轮播图的顺序！");
            }
            GoodsBanner goodsBanner1 = new GoodsBanner();
            ImgInfo imgInfo = new ImgInfo();
            imgInfo.setImgInfoId(imgInfoId);
            goodsBanner1.setImgInfo(imgInfo);
            goodsBanner1.setBannerOrder(Integer.parseInt(bannerOrder));
            goodsBanner1.setCreateDate(new Date());
            goodsBanners1.add(goodsBanner1);
        }

        List<GoodsDetail> goodsDetails1 = new LinkedList<GoodsDetail>();
        for (String goodsDetail : goodsDetails) {
            String imgInfoId = goodsDetail.substring(0, goodsDetail.indexOf("#"));
            String detailOrder = goodsDetail.substring(goodsDetail.indexOf("#") + 1, goodsDetail.length());
            if (StringUtils.isEmpty(imgInfoId)) {
                continue;
            }
            if (StringUtils.isEmpty(detailOrder)) {
                throw new SystemException("请输入完整商品详情图的顺序！");
            }
            GoodsDetail goodsDetail1 = new GoodsDetail();
            ImgInfo imgInfo = new ImgInfo();
            imgInfo.setImgInfoId(imgInfoId);
            goodsDetail1.setImgInfo(imgInfo);
            goodsDetail1.setViewTarget(null);
            goodsDetail1.setViewType(0);
            goodsDetail1.setDetailOrder(Integer.parseInt(detailOrder));
            goodsDetails1.add(goodsDetail1);
        }
        if (goodsBanners1.size() == 0) {
            throw new SystemException("请至少上传一张商品轮播图！");
        }
        if (goodsDetails1.size() == 0) {
            throw new SystemException("请上传至少一张商品详情图！");
        }
        ResponseMessage responseMessage = null;
        responseMessage = this.goodsService.addOrUpdateGoods(goods, goodsBanners1, goodsDetails1, merchant);
        String goodsId = (String) responseMessage.getAttribute();
        ModelAndView mv = new ModelAndView("admin/goods/goodsSkuList");
        List<SkuListInfo> skuListInfos = this.skuService.getSkuListInfoByGoodsId(goodsId);
        mv.addObject("skuList", skuListInfos);
        mv.addObject("goodsId", goodsId);
        return responseMessage;
    }

    /**
     * 删除商品
     *
     * @param goodsId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/goods/updateDeleteState", method = RequestMethod.POST)
    public ResponseMessage updateDeleteState(String goodsId) {
        this.goodsService.updateDeleteState(goodsId);
        return executeResult();
    }

    @RequestMapping(value = "admin/onsalGoods")
    public String onsalGoodsPage() {
        return "admin/goodsOnsal";
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/onsalGoods/list")
    public PageData<Goods> getOnsalGoodsPageData(HttpServletRequest request) {
        Merchant merchant = this.adminToMerchant(request);
        IQueryParams queryParams = this.createQueryParams(request);
        queryParams.andEqual("merchant", merchant).andEqual("deleteState", false).andEqual("onsale", true).andEqual("examinState", Constant.EXAMINE_STATE_EXAMINED_OK);
        return this.goodsService.getPageData(this.createPager(request), queryParams);
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/Goods/updateOnsalState")
    public ResponseMessage updateOnsalState(String goodsId, boolean onsale) {
        this.goodsService.updateOnsalState(goodsId, onsale);
        return executeResult();
    }


    @RequestMapping(value = "admin/api/goods/edit/{goodsId}")
    public ModelAndView editGoods(@PathVariable(value = "goodsId") String goodsId, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("admin/goodsEdit");
        Goods goods = this.goodsService.getById(goodsId);
        Merchant merchant = this.adminToMerchant(request);
        List<GoodsAttributeSet> goodsAttributeSets = new ArrayList<>(merchant.getCategories());

        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("goods", goods).sort("bannerOrder", Order.ASC);
        List<GoodsBanner> goodsBanners = this.goodsBannerService.getAllByParams(null, queryParams);

        queryParams = new GeneralQueryParams();
        queryParams.andEqual("goods", goods).sort("detailOrder", Order.ASC);
        List<GoodsDetail> goodsDetails = this.goodsDetailService.getAllByParams(null, queryParams);

        modelAndView.addObject("goods", goods);
        modelAndView.addObject("goodsAttributeSets", goodsAttributeSets);
        modelAndView.addObject("goodsBanners", goodsBanners);
        modelAndView.addObject("goodsDetails", goodsDetails);
        return modelAndView;
    }

    /**
     * 商品详细页面
     *
     * @param goodsId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/goods/goodsDetail/{goodsId}")
    public ModelAndView goodsDetailInfo(@PathVariable(value = "goodsId") String goodsId) {
        Goods goods = this.goodsService.getById(goodsId);
        ModelAndView modelAndView = new ModelAndView("admin/goods/goodsDetailInfo");
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("goods.goodsId", goodsId);
        queryParams.sort("bannerOrder", Order.ASC);
        List<GoodsBanner> goodsBanners = this.goodsBannerService.getAllByParams(null, queryParams);
        queryParams = new GeneralQueryParams();
        queryParams.andEqual("goods.goodsId", goodsId);
        queryParams.sort("detailOrder", Order.ASC);
        List<GoodsDetail> goodsDetails = this.goodsDetailService.getAllByParams(null, queryParams);
        modelAndView.addObject("goods", goods);
        modelAndView.addObject("goodsBanners", goodsBanners);
        modelAndView.addObject("goodsDetails", goodsDetails);
        return modelAndView;
    }

    /**
     * 商品详细页面测试
     *
     * @return
     */
    @RequestMapping(value = "goods/goodsDetail")
    public String goodsDetailInfo2() {
        return "admin/goods/goodsDetailInfo";
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/goods/{goodsId}/goodsScores")
    public PageData<GoodsScore> getGoodsScoreById(@PathVariable(value = "goodsId") String goodsId, HttpServletRequest request) {
        IQueryParams queryParams = this.createQueryParams(request);
        queryParams.andEqual("goods.goodsId", goodsId);
        return this.goodsScoreService.getPageData(this.createPager(request), queryParams);
    }

    /**
     * 获取已所有已上架的商品
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/onsale/goodses")
    public PageData<Goods> getOnsaleGoodses(HttpServletRequest request) {
        IQueryParams queryParams = createQueryParams(request).andEqual("onsale", true)
                .andEqual("deleteState", false);
        return this.goodsService.getPageData(createPager(request), queryParams);
    }

    /**
     * 所有商品分页查询
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/all/goodses")
    public PageData<Goods> getAllGoodsesBySearch(HttpServletRequest request,
                                                 String goodsName, String onsale, String examinState, String merchName, String merchTell) {
        IQueryParams queryParams = this.createQueryParams(request);
        if (!StringUtils.isEmpty(merchName)) {
            queryParams.andAllLike("merchant.merchName", merchName);
        }
        if (!StringUtils.isEmpty(merchTell)) {
            queryParams.andAllLike("merchant.merchTell", merchTell);
        }
        if (!StringUtils.isEmpty(goodsName)) {
            queryParams.andAllLike("goodsName", goodsName);
        }
        if (!StringUtils.isEmpty(examinState)) {
            queryParams.andEqual("examinState", Integer.parseInt(examinState));
        }
        if (!StringUtils.isEmpty(onsale)) {
            if (onsale.equals("true")) {
                queryParams.andEqual("onsale", true);
            } else {
                queryParams.andEqual("onsale", false);
            }
        }

        return this.goodsService.getPageData(createPager(request), queryParams);
    }
}
