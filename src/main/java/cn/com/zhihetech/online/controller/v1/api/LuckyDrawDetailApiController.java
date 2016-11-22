package cn.com.zhihetech.online.controller.v1.api;

import cn.com.zhihetech.online.bean.LuckyDrawDetail;
import cn.com.zhihetech.online.bean.User;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.service.ILuckDrawDetailService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.omg.PortableInterceptor.SUCCESSFUL;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.html.HTMLTableCaptionElement;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * Created by ydc on 16-4-28.
 */
@Controller
public class LuckyDrawDetailApiController extends ApiController {

    @Resource(name = "luckDrawDetailService")
    private ILuckDrawDetailService luckDrawDetailService;


    /**
     * <h3>获取奖项列表</h3>
     * url: api/luckyDrawDetail/list  <br>
     * <p>
     * <p>返回状态码</p>
     * 910:不存在抽奖活动 <br>
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "luckyDrawDetail/list")
    public ResponseMessage getLuckyDrawList() {
        ResponseMessage responseMessage = null;
        responseMessage = this.luckDrawDetailService.getLuckDrawList();
        if (responseMessage != null) {
            return responseMessage;
        }
        return executeResult();
    }

    /**
     * <h3>进行抽奖操作</h3>
     * url: api/user/{id}/luckyDraw  <br>
     * {id}:用户Id <br>
     * <p>
     * <p>返回状态码</p>
     * 900:未中奖 <br>
     * 901:奖品已经被抽完 <br>
     * 902:今天已经抽过奖了 <br>
     *
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "user/{id}/luckyDraw")
    public ResponseMessage luckDraw(@PathVariable(value = "id") String userId) {
        ResponseMessage responseMessage = null;
        User user = new User();
        user.setUserId(userId);
        responseMessage = this.luckDrawDetailService.executeluckDraw(user);
        if (responseMessage != null) {
            return responseMessage;
        }
        return executeResult(500, false, "抽奖失败，请重试！");
    }

    /**
     * <h3>根据用户id获取该用户抽中的奖项</h3>
     * url: api/user/{id}/luckyDrawDetails  <br>
     * {id}:用户id  <br>
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "user/{id}/luckyDrawDetails")
    public ResponseMessage getLuckyDrawListByUserId(@PathVariable(value = "id") String userId, HttpServletRequest request) {
        IQueryParams queryParams = this.createQueryParams(request);
        queryParams.andEqual("user.userId", userId).andEqual("useState", false).sort("drawDate", Order.DESC);
        PageData<LuckyDrawDetail> luckyDrawDetailPageData = this.luckDrawDetailService.getPageData(this.createPager(request), queryParams);
        return executeResult(ResponseStatusCode.SUCCESS_CODE, "获取成功", luckyDrawDetailPageData);

    }

}
