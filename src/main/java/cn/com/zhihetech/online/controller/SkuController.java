package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.*;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.dao.IActivityGoodsDao;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IGoodsAttributeService;
import cn.com.zhihetech.online.service.IGoodsService;
import cn.com.zhihetech.online.service.ISkuAttributeService;
import cn.com.zhihetech.online.service.ISkuService;
import cn.com.zhihetech.online.util.NumberUtils;
import cn.com.zhihetech.online.util.StringUtils;
import cn.com.zhihetech.online.vo.SkuListInfo;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by YangDaiChun on 2015/12/2.
 */
@Controller
public class SkuController extends SupportController {

    @Resource(name = "skuService")
    private ISkuService skuService;
    @Resource(name = "goodsService")
    private IGoodsService goodsService;
    @Resource(name = "skuAttributeService")
    private ISkuAttributeService skuAttributeService;
    @Resource(name = "goodsAttributeService")
    private IGoodsAttributeService goodsAttributeService;

    @ResponseBody
    @RequestMapping(value = "admin/api/goods/{goodsId}/sku/add", method = RequestMethod.POST)
    public ResponseMessage addGoodsSku(@PathVariable(value = "goodsId") String goodsId, @RequestParam(value = "skuAttId", required = false) List<String> attrIds,
                                       @RequestParam(value = "skuAttValue", required = false) List<String> attrValues, String imgInfoId, float price, int currentStock) {
        if (StringUtils.isEmpty(goodsId)) {
            throw new SystemException("商品不能为空！");
        }
        if (currentStock <= 0) {
            throw new SystemException("库存必须大于0");
        }
        List<GoodsAttribute> goodsAttrs = new ArrayList<>();
        if (attrIds != null && !attrIds.isEmpty()) {
            int len = attrIds.size();
            Goods goods = new Goods(goodsId);
            for (int i = 0; i < len; i++) {
                GoodsAttribute goodsAttr = new GoodsAttribute();
                goodsAttr.setAttribute(new SkuAttribute(attrIds.get(i)));
                goodsAttr.setGoods(goods);
                goodsAttr.setAttrValue(attrValues.get(i).trim());
                goodsAttrs.add(goodsAttr);
            }
        }
        Sku sku = new Sku();
        sku.setGoodsId(goodsId);
        if (!StringUtils.isEmpty(imgInfoId)) {
            sku.setCoverImg(new ImgInfo(imgInfoId));
        }
        sku.setVolume(0);
        sku.setStock(currentStock);
        sku.setPrice(NumberUtils.doubleScale(2, price));
        sku.setMixCode(goodsAttrs.isEmpty() ? goodsId : Sku.getGoodsAttrMixCode(goodsId, goodsAttrs));
        this.skuService.addSkuByGoodsId(goodsId, goodsAttrs, sku);
        return executeResult();
    }

    /**
     * 编辑商品属性（sku)
     *
     * @param goodsId      商品ID
     * @param goodsAttrIds 商品与商品属性组合ID（当为新增属性时为空）
     * @param skuAttIds    商品属性ID
     * @param attrValues   商品属性值
     * @param sku          此属性组合组成的唯一sku
     * @param coverImgId   此sku组合对应的封面图
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/goods/{goodsId}/sku/edit", method = RequestMethod.POST)
    public ResponseMessage editGoodsSku(@PathVariable(value = "goodsId") String goodsId, @RequestParam(required = false) String[] goodsAttrIds,
                                        @RequestParam(required = false) String[] skuAttIds, @RequestParam(required = false) String[] attrValues,
                                        Sku sku, String coverImgId, long currentStock) {
        if (StringUtils.isEmpty(goodsId)) {
            throw new SystemException("编辑的商品不能为空！");
        }

        List<GoodsAttribute> goodsAttrs = new ArrayList<>();
        if (goodsAttrIds != null && goodsAttrIds.length > 0) {
            int len = goodsAttrIds.length;
            Goods goods = new Goods(goodsId);
            for (int i = 0; i < len; i++) {
                GoodsAttribute goodsAttr = new GoodsAttribute(goodsAttrIds[i]);
                goodsAttr.setAttribute(new SkuAttribute(skuAttIds[i]));
                goodsAttr.setGoods(goods);
                goodsAttr.setAttrValue(attrValues[i].trim());
                goodsAttr.setSku(sku);
                goodsAttrs.add(goodsAttr);
            }
        }
        sku.setMixCode(goodsAttrs.isEmpty() ? goodsId : Sku.getGoodsAttrMixCode(goodsId, goodsAttrs));
        sku.setGoodsId(goodsId);
        sku.setCoverImg(StringUtils.isEmpty(coverImgId) ? null : new ImgInfo(coverImgId));
        sku.setPrice(NumberUtils.doubleScale(2, sku.getPrice()));
        this.skuService.updateGoodsAttrAndSku(goodsAttrs, sku, currentStock);
        return executeResult();
    }

    /**
     * 添加商品的sku价格和库存 页面
     *
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "admin/api/goods/{id}/addGoodsSkuPage")
    public ModelAndView addGoodsSkuPage(@PathVariable(value = "id") String goodsId) {
        ModelAndView mv = mv = new ModelAndView("admin/goods/goodsAddSku");
        Goods goods = this.goodsService.getById(goodsId);
        String goodsAttSetId = goods.getGoodsAttributeSet().getGoodsAttSetId(); //商品分类的Id
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("goodsAttributeSet.goodsAttSetId", goodsAttSetId).andEqual("permit", true);
        String[] skuAttInfo = new String[2];
        skuAttInfo[0] = "skuAttId";
        skuAttInfo[1] = "skuAttName";
        List<Object[]> objectArrays = this.skuAttributeService.getProperties(skuAttInfo, null, queryParams);
        if (objectArrays.size() > 0) {
            List<SkuAttribute> skuAttributes = new LinkedList<>();
            for (Object[] objectArray : objectArrays) {
                SkuAttribute skuAttribute = new SkuAttribute();
                skuAttribute.setSkuAttId((String) objectArray[0]);
                skuAttribute.setSkuAttName((String) objectArray[1]);
                skuAttributes.add(skuAttribute);
            }
            mv.addObject("skuAtts", skuAttributes);
        } else {
            mv.addObject("skuAtts", null);
        }
        mv.addObject("goods", goods);
        return mv;
    }

    /**
     * 根据商家id在页面上列出满足条件的商品的sku列表 带修改操作
     *
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "admin/api/goods/{id}/sku/list")
    public ModelAndView goodsSkuList(@PathVariable(value = "id") String goodsId) {
        ModelAndView mv = new ModelAndView("admin/goods/goodsSkuList");
        List<SkuListInfo> skuListInfos = this.skuService.getSkuListInfoByGoodsId(goodsId);
        mv.addObject("skuList", skuListInfos);
        mv.addObject("goodsId", goodsId);
        return mv;
    }

    /**
     * 根据商家id在页面上列出满足条件的商品的sku列表2 不带任何操作
     *
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "admin/api/goods/{id}/sku/list2")
    public ModelAndView goodsSkuList2(@PathVariable(value = "id") String goodsId) {
        ModelAndView mv = new ModelAndView("admin/goods/goodsSkuList2");
        List<SkuListInfo> skuListInfos = this.skuService.getSkuListInfoByGoodsId(goodsId);
        mv.addObject("skuList", skuListInfos);
        mv.addObject("goodsId", goodsId);
        return mv;
    }

    /**
     * 修改商品sku页面
     *
     * @param skuId
     * @return
     */
    @RequestMapping(value = "admin/api/sku/{id}/editPage")
    public ModelAndView editGoodsSkuPage(@PathVariable(value = "id") String skuId) {
        ModelAndView mv = new ModelAndView("/admin/goods/goodsSkuEdit");
        Sku sku = this.skuService.getById(skuId);
        mv.addObject("sku", sku);
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("goodsId", sku.getGoodsId());
        List<Object> oGoodsAttIds = this.goodsService.getProperty("goodsAttributeSet.goodsAttSetId", new Pager(), queryParams);
        String goodAttSetId = (String) oGoodsAttIds.get(0);
        queryParams = new GeneralQueryParams();
        queryParams.andEqual("goodsAttributeSet.goodsAttSetId", goodAttSetId).andEqual("permit", true);
        String[] skuAttInfo = new String[2];
        skuAttInfo[0] = "skuAttId";
        skuAttInfo[1] = "skuAttName";
        List<Object[]> oSkuAtts = this.skuAttributeService.getProperties(skuAttInfo, null, queryParams);
        Map<String, String> skuAttIds = new HashMap<>();
        for (Object[] oSkuAtt : oSkuAtts) {
            skuAttIds.put((String) oSkuAtt[0], (String) oSkuAtt[1]);
        }

        List<GoodsAttribute> goodsAttributes = new ArrayList<>();
        queryParams = new GeneralQueryParams();
        queryParams.andEqual("sku.skuId", skuId);
        String[] selectors = new String[4];
        selectors[0] = "goodsAttrId";
        selectors[1] = "attribute.skuAttId";
        selectors[2] = "attribute.skuAttName";
        selectors[3] = "attrValue";
        List<Object[]> oGoodsAtts = this.goodsAttributeService.getProperties(selectors, null, queryParams);
        Map<String, String> _skuAttIds = new HashMap<>();
        for (Object[] oGoodsAtt : oGoodsAtts) {
            GoodsAttribute goodsAttribute = new GoodsAttribute();
            goodsAttribute.setGoodsAttrId((String) oGoodsAtt[0]);
            _skuAttIds.put((String) oGoodsAtt[1], (String) oGoodsAtt[2]);
            SkuAttribute skuAttribute = new SkuAttribute();
            skuAttribute.setSkuAttId((String) oGoodsAtt[1]);
            skuAttribute.setSkuAttName((String) oGoodsAtt[2]);
            goodsAttribute.setAttribute(skuAttribute);
            goodsAttribute.setAttrValue((String) oGoodsAtt[3]);
            goodsAttributes.add(goodsAttribute);
        }

        for (String _key : _skuAttIds.keySet()) {
            skuAttIds.remove(_key);
        }

        for (String skuAttId : skuAttIds.keySet()) {
            GoodsAttribute goodsAttribute = new GoodsAttribute();
            goodsAttribute.setGoodsAttrId(null);
            SkuAttribute skuAttribute = new SkuAttribute();
            skuAttribute.setSkuAttId(skuAttId);
            skuAttribute.setSkuAttName(skuAttIds.get(skuAttId));
            goodsAttribute.setAttribute(skuAttribute);
            goodsAttribute.setAttrValue(null);
            goodsAttributes.add(goodsAttribute);
        }

        mv.addObject("goodsAtts", goodsAttributes);
        return mv;
    }
}
