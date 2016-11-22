package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.service.ILuckDrawDetailService;
import cn.com.zhihetech.online.service.ILuckyDrawActivityService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * Created by ShenYunjie on 2016/5/3.
 */
@Controller
public class UserPrizeController extends SupportController {

    @Resource(name = "luckDrawDetailService")
    private ILuckDrawDetailService luckDrawDetailService;

    @RequestMapping("web/user/{userId}/prizes")
    public ModelAndView indexPage(@PathVariable("userId") String userId) {
        ModelAndView mv = new ModelAndView("mobile/prize/index");
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("user.userId", userId)
                .andEqual("useState", false)
                .sort("drawDate", Order.DESC);
        mv.addObject("prizes", this.luckDrawDetailService.getAllByParams(null, queryParams));
        return mv;
    }
}
