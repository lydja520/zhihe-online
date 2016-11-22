package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.bean.RecommendGoodsForAdmin;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IGoodsService;
import cn.com.zhihetech.online.service.IRecGoodsForAdminService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by ydc on 16-8-15.
 */
@Controller
public class RecGoodsForAdminController extends SupportController {

    @Resource(name = "recGoodsForAdminService")
    private IRecGoodsForAdminService recGoodsService;
    @Resource(name = "goodsService")
    private IGoodsService goodsService;


    @RequestMapping(value = "admin/recGoodsForAdmin")
    public String indexPage() {
        return "admin/recommend/recGoodsForAdmin";
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/recGoodsForAdmin/list")
    public PageData<RecommendGoodsForAdmin> getPageData(HttpServletRequest request) {
        return this.recGoodsService.getPageData(this.createPager(request), this.createQueryParams(request));
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/recGoodsForAdmin/add")
    public ResponseMessage addRecGoodsForAdmin(RecommendGoodsForAdmin recGoods) {
        long total = this.recGoodsService.getRecTotal(null);
        if (total >= 5) {
            throw new SystemException("推荐商品最多只能添加5个，若继续推荐此商品，请删除现有的推荐！");
        }
        recGoodsService.add(recGoods);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/recGoodsForAdmin/select")
    public PageData<Goods> selectGoods(HttpServletRequest request) {
        List<Object> ids = this.recGoodsService.getProperty("goods.goodsId", null, null);
        IQueryParams queryParams = this.createQueryParams(request);
        queryParams.andEqual("onsale", true)
                .andEqual("examinState", Constant.EXAMINE_STATE_EXAMINED_OK)
                .andEqual("deleteState", false)
                .andNotIn("goodsId", ids);
        return this.goodsService.getPageData(this.createPager(request), queryParams);
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/recGoodsForAdmin/edit")
    public ResponseMessage editRecGoodsForAdmin(RecommendGoodsForAdmin recGoods) {
        this.recGoodsService.update(recGoods);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/recGoodsForAdmin/{id}/delete")
    public ResponseMessage delRecGoodsForAdmin(@PathVariable(value = "id") String recId) {
        this.recGoodsService.delete(this.recGoodsService.getById(recId));
        return executeResult();
    }
}
