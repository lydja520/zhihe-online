package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.GoodsAttributeSet;
import cn.com.zhihetech.online.bean.SkuAttribute;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.service.IGoodsAttributeSetService;
import cn.com.zhihetech.online.service.ISkuAttributeService;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by YangDaiChun on 2015/12/4.
 */
@Controller
public class GoodsAttributeSetCotroller extends SupportController {

    @Resource(name = "goodsAttributeSetService")
    public IGoodsAttributeSetService goodsAttributeSetService;

    @RequestMapping(value = "admin/goodsAttSet")
    public String indexPage() {
        return "admin/goodsAttSet";
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/goodsAttSet/list")
    public PageData<GoodsAttributeSet> getAllPageData(HttpServletRequest request) {
        IQueryParams queryParams = createQueryParams(request)
                .andEqual("permit", true)
                .sort("attSetOrder", Order.ASC);
        return this.goodsAttributeSetService.getPageData(this.createPager(request), queryParams);
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/goodsAttSet/add")
    public ResponseMessage addGoodsAttSet(GoodsAttributeSet goodsAttributeSet) {
        goodsAttributeSet.setCreatDate(new Date());
        goodsAttributeSet.setPermit(true);
        this.goodsAttributeSetService.add(goodsAttributeSet);
        return this.executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/goodsAttSet/edit")
    public ResponseMessage editGoodsAttSet(GoodsAttributeSet goodsAttributeSet) {
        this.goodsAttributeSetService.update(goodsAttributeSet);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/goodsAttSet/allAtts")
    public List<GoodsAttributeSet> getAllAtts(HttpServletRequest request) {
        IQueryParams queryParams = createQueryParams(request).andEqual("permit", true);
        return this.goodsAttributeSetService.getAllByParams(null, queryParams);
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/goodsAttSet/{id}/delete", method = RequestMethod.POST)
    public ResponseMessage deleteAttSetById(@PathVariable("id") String id) {
        if (StringUtils.isEmpty(id)) {
            return executeResult(ResponseStatusCode.SYSTEM_ERROR_CODE, false, "参数不足！");
        }
        this.goodsAttributeSetService.updatePermit(id, false);
        return executeResult();
    }
}
