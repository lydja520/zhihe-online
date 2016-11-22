package cn.com.zhihetech.online.controller.v1.api;

import cn.com.zhihetech.online.bean.GoodsScore;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IGoodsScoreService;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import com.sun.istack.internal.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by ShenYunjie on 2016/3/16.
 */
@Controller
public class GoodsScoreApiController extends ApiController {

    @Resource(name = "goodsScoreService")
    private IGoodsScoreService goodsScoreService;

    /**
     * <h3>获取商品评价</h3>
     * URL:api/goods/{goodsId}/goodsScores  <br>
     *
     * @param request
     * @param goodsId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "goods/{goodsId}/goodsScores", method = RequestMethod.GET)
    public PageData<GoodsScore> getGoodsScoreByGoodsId(HttpServletRequest request, @PathVariable("goodsId") String goodsId) {
        if (StringUtils.isEmpty(goodsId)) {
            throw new SystemException("传递参数错误");
        }
        return this.goodsScoreService.getGoodsScoresByGoodsId(goodsId, createPager(request));
    }
}
