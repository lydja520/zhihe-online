package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.Admin;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.service.ILuckDrawDetailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by ShenYunjie on 2016/5/12.
 */
@Controller
public class LuckDrawUseController extends SupportController {

    @Resource(name = "luckDrawDetailService")
    private ILuckDrawDetailService luckDrawDetailService;

    @RequestMapping("admin/luckDraws/use")
    public String pageIndex() {
        return "admin/luckyDraw/useLuckyDraw";
    }

    /**
     * 奖品验证（使用）
     *
     * @param request
     * @param luckCode 奖品号码
     * @param mobileNo 中奖手机号
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/luckyDraws/use", method = RequestMethod.POST)
    public ResponseMessage useLuckyDrawItem(HttpServletRequest request, String luckCode, String mobileNo) {
        Admin admin = getCurrentAdmin(request);
        this.luckDrawDetailService.executeUseLuckyDrawDetail(luckCode, mobileNo, admin);
        return executeResult();
    }
}
