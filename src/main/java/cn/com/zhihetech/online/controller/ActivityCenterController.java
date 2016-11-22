package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.service.ILuckyDrawActivityService;
import cn.com.zhihetech.online.service.ILuckyDrawService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by YangDaiChun on 2016/3/18.
 */
@Controller
public class ActivityCenterController extends SupportController {

    @Resource(name = "luckyDrawActivityService")
    private ILuckyDrawActivityService luckyDrawActivityService;

    /**
     * <h3>活动专区</h3>
     * url : web/activityCenter <br>
     *
     * @return
     */
    @RequestMapping(value = "web/activityCenter")
    public ModelAndView activityCenterPage() {
        ModelAndView mv = new ModelAndView("mobile/activityCenter");
        mv.addObject("merchantList", this.luckyDrawActivityService.getSponsorMerchants());
        mv.addObject("lucyDrawActivity", this.luckyDrawActivityService.getCurrentLuckDrawActivity());
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "web/activityCenter/award")
    public Map turnAbleAward() {
        for (int i = 0; i < 1000; i++) {
            System.out.println(i);
        }
        Random ran = new Random();
        int code = ran.nextInt(7);
        Map map = new HashMap();
        map.put("code", code);
        return map;
    }
}
