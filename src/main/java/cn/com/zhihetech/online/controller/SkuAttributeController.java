package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.GoodsAttributeSet;
import cn.com.zhihetech.online.bean.SkuAttribute;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IGoodsAttributeSetService;
import cn.com.zhihetech.online.service.ISkuAttributeService;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

/**
 * Created by YangDaiChun on 2015/12/2.
 */
@Controller
public class SkuAttributeController extends SupportController {

    @Resource(name = "skuAttributeService")
    private ISkuAttributeService skuAttributeService;

    @Resource(name = "goodsAttributeSetService")
    private IGoodsAttributeSetService goodsAttributeSetService;

    @RequestMapping(value = "admin/skuAttribute")
    public ModelAndView indexPage() {
        List<GoodsAttributeSet> goodsAttributeSets = this.goodsAttributeSetService.getAllByParams(null, null);
        ModelAndView modelAndView = new ModelAndView("admin/goodsAttribute/skuAttribute");
        modelAndView.addObject("goodsAttSets", goodsAttributeSets);
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/skuAttribute/list")
    public PageData<SkuAttribute> getSkuAttributePageData(HttpServletRequest request) {
        IQueryParams queryParams = createQueryParams(request).sort("goodsAttributeSet.goodsAttSetId", Order.ASC);
        return this.skuAttributeService.getPageData(this.createPager(request), queryParams);
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/skuAttribute/add", method = RequestMethod.POST)
    public ResponseMessage addSkuAttribute(SkuAttribute skuAttribute) {
        this.skuAttributeService.add(skuAttribute);
        return this.executeResult();
    }

    /**
     * 修改指定的属性
     *
     * @param skuAttId      属性ID
     * @param skuAttName    属性名称
     * @param goodsAttSetId 对应的商品类别ID
     * @param skuAttDesc    属性描述
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/skuAttribute/params/modify", method = RequestMethod.POST)
    public ResponseMessage editSkuAttribute(String skuAttId, String skuAttName, String goodsAttSetId, String skuAttDesc) {
        this.skuAttributeService.executeUpdateParamsById(skuAttId, skuAttName, goodsAttSetId, skuAttDesc);
        return this.executeResult();
    }

    /**
     * 禁用商品属性
     *
     * @param skuAttrId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/skuAttribute/{skuAttrId}/disable", method = RequestMethod.POST)
    public ResponseMessage disableByAttrId(@PathVariable(value = "skuAttrId") String skuAttrId) {
        if (StringUtils.isEmpty(skuAttrId)) {
            throw new SystemException("skuAttributeId is not able null!");
        }
        this.skuAttributeService.executeUpdatePermit(skuAttrId, false);
        return executeResult();
    }

    /**
     * 启用商品属性
     *
     * @param skuAttrId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/skuAttribute/{skuAttrId}/enable", method = RequestMethod.POST)
    public ResponseMessage enableByAttrId(@PathVariable(value = "skuAttrId") String skuAttrId) {
        if (StringUtils.isEmpty(skuAttrId)) {
            throw new SystemException("skuAttributeId is not able null!");
        }
        this.skuAttributeService.executeUpdatePermit(skuAttrId, true);
        return executeResult();
    }
}
