package cn.com.zhihetech.online.controller.v1.api;

import cn.com.zhihetech.online.bean.FocusGoods;
import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.bean.User;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.service.IFocusGoodsService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by YangDaiChun on 2016/1/8.
 */
@Controller
public class FocusGoodsApiController extends ApiController {

    @Resource(name = "focusGoodsService")
    private IFocusGoodsService focusGoodsService;

    /**
     * <h3>添加商品收藏 </h3>
     * <p>
     * URL:api/focusGoods/add   <br>
     * <p>
     * 参数：<br>
     * userId:用户Id  <br>
     * goodsId:商品Id  <br>
     * <p>
     * 返回状态码：<br>
     * 200：关注成功 <br>
     * 710 ：已经收藏过该商品  <br>
     *
     * @param userId
     * @param goodsId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "focusGoods/add", method = RequestMethod.GET)
    public ResponseMessage addFocusGoods(String userId, String goodsId) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("user.userId", userId).andEqual("goods.goodsId", goodsId);
        if (this.focusGoodsService.getFocusGoodsCount(queryParams) > 0) {
            return executeResult(ResponseStatusCode.ALREADY_FOCUS, "你已经关注过了该商品，无需重复关注！");
        }

        FocusGoods focusGoods = new FocusGoods();
        Goods goods = new Goods();
        goods.setGoodsId(goodsId);
        User user = new User();
        user.setUserId(userId);
        focusGoods.setGoods(goods);
        focusGoods.setUser(user);
        focusGoods.setFocusDate(new Date());
        this.focusGoodsService.add(focusGoods);
        return executeResult();
    }

    /**
     * <h3>根据用户id查收藏过的商品</h3>
     * <p>
     * URL : api/user/{userId}/focusGoodses<br>
     * {userId}:用户id  <br>
     * <p>
     * <br>返回状态码：<br>
     * 720：未关注过任何商品
     *
     * @param userId
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "user/{userId}/focusGoodses")
    public Object getFocusGoodsesByUserId(@PathVariable(value = "userId") String userId, HttpServletRequest request) {
        IQueryParams queryParams = createQueryParams(request)
                .andProParam("goods.stock > 0")
                .andEqual("deleteState", false)
                .andEqual("onsale", true)
                .andEqual("goods.merchant.permit", true)
                .andEqual("goods.merchant.examinState", Constant.EXAMINE_STATE_EXAMINED_OK)
                .andEqual("user.userId", userId).sort("focusDate", Order.DESC);
        PageData<FocusGoods> focusGoodsPageData = this.focusGoodsService.getPageData(this.createPager(request), queryParams);
        return focusGoodsPageData;
    }

    /**
     * 检查是否收藏过该商品 <br>
     * URL: api/checkFocusGoods  <br>
     * <p>
     * 参数：<br>
     * userId:用户Id  <br>
     * goodsId:商品Id  <br>
     *
     * @param userId
     * @param goodsId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "checkFocusGoods")
    public ResponseMessage checkFoucusGoods(String userId, String goodsId) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("user.userId", userId).andEqual("goods.goodsId", goodsId);
        if (this.focusGoodsService.getFocusGoodsCount(queryParams) > 0) {
            return executeResult(ResponseStatusCode.ALREADY_FOCUS, "你已经关注过了该商品");
        }
        return executeResult(ResponseStatusCode.HAVE_NO_FOCUS, "你未关注过了该商品");
    }

    /**
     * 取消商品收藏
     * URL : api/cancelFocusGoods
     * 参数：<br>
     * userId:用户Id  <br>
     * goodsId:商品Id  <br>
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "cancelFocusGoods", method = RequestMethod.GET)
    public ResponseMessage deleteFocusGoods(String userId, String goodsId) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("user.userId", userId).andEqual("goods.goodsId", goodsId);
        List<FocusGoods> focusGoods = this.focusGoodsService.getAllByParams(null, queryParams);
        if (focusGoods.size() == 0) {
            return executeResult(ResponseStatusCode.HAVE_NO_FOCUS, "关注已经取消，无需重复操作");
        }
        this.focusGoodsService.delete(focusGoods.get(0));
        return executeResult();
    }
}
