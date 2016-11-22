package cn.com.zhihetech.online.controller.v1.api;

import cn.com.zhihetech.online.bean.GoodsAttributeSet;
import cn.com.zhihetech.online.service.IGoodsAttributeSetService;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by YangDaiChun on 2015/12/30.
 */
@Controller
public class GoodsAttributeSetApiController extends ApiController {

    @Resource(name = "goodsAttributeSetService")
    private IGoodsAttributeSetService goodsAttributeSetService;

    /**
     * URL: api/goodsAttributeSet/list
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "goodsAttributeSet/list")
    public PageData<GoodsAttributeSet> goodsAttributeSetList(HttpServletRequest request) {
        IQueryParams queryParams = this.createQueryParams(request);
        queryParams.andEqual("permit", true).sort("attSetOrder", Order.ASC);
        return this.goodsAttributeSetService.getPageData(this.createPager(request), queryParams);
    }
}
