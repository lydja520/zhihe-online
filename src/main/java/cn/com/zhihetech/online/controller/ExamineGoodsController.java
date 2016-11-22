package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IGoodsService;
import cn.com.zhihetech.online.util.StringUtils;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by ShenYunjie on 2016/6/15.
 */
@Controller
public class ExamineGoodsController extends SupportController {

    @Resource(name = "goodsService")
    private IGoodsService goodsService;

    @RequestMapping("admin/goods/examine/index")
    public String indexPage() {
        return "admin/goods/waitExamineGoods";
    }

    /**
     * 获取等待审核的商品
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("admin/api/goods/waitExamine/goodses")
    public PageData<Goods> getWaitExamineGoodses(HttpServletRequest request) {
        IQueryParams queryParams = createQueryParams(request)
                .andEqual("examinState", Constant.EXAMINE_STATE_SUBMITED)
                .andEqual("deleteState", false);
        return this.goodsService.getPageData(createPager(request), queryParams);
    }

    /**
     * 审核商品
     *
     * @param goodsId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/goods/examine", method = RequestMethod.POST)
    public ResponseMessage excuteExamineGoods(String goodsId) {
        if (StringUtils.isEmpty(goodsId)) {
            throw new SystemException("商品参数不全！");
        }
        this.goodsService.executeExamineGoods(goodsId);
        return executeResult();
    }


    /**
     * 商品未审核通过
     *
     * @param goodsId    商品ID
     * @param examineMsg 未通过原因
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/goods/unExamine", method = RequestMethod.POST)
    public ResponseMessage executeUnExamineGoods(String goodsId, String examineMsg) {
        if (StringUtils.isEmpty(goodsId)) {
            throw new SystemException("商品参数不全！");
        }
        this.goodsService.executeUnExamineGoods(goodsId, examineMsg);
        return executeResult();
    }

    /**
     * 提交商品审核
     *
     * @param goodsId 商品ID
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/goods/examine/submit", method = RequestMethod.POST)
    public ResponseMessage submitExamineGoods(String goodsId) {
        if (StringUtils.isEmpty(goodsId)) {
            throw new SystemException("商品参数不全！");
        }
        this.goodsService.executeSubmitExamine(goodsId);
        return executeResult();
    }
}
