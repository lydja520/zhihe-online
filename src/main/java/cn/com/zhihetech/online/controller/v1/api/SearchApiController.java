package cn.com.zhihetech.online.controller.v1.api;

import cn.com.zhihetech.online.bean.SerializableAndCloneable;
import cn.com.zhihetech.online.service.IGoodsService;
import cn.com.zhihetech.online.service.IMerchantService;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2015/12/22.
 */
@Controller
public class SearchApiController extends ApiController {
    @Resource(name = "goodsService")
    private IGoodsService iGoodsService;
    @Resource(name = "merchantService")
    private IMerchantService iMerchantService;

    @ResponseBody
    @RequestMapping(value = "searchResult/list")
    public PageData<? extends SerializableAndCloneable> searchResult(HttpServletRequest request, int searchType) {
        PageData<? extends SerializableAndCloneable> pageData = null;
        switch (searchType) {
            case 1:
                pageData = this.iGoodsService.getPageData(createPager(request), createQueryParams(request));
                break;
            case 2:
                pageData = this.iMerchantService.getPageData(createPager(request), createQueryParams(request));
                break;
        }
        return pageData;
    }
}
