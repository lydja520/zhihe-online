package cn.com.zhihetech.online.controller.v2.api;

import cn.com.zhihetech.online.bean.RecommendMerchantForAdmin;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.service.IRecMerchantForAdminService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by YangDaiChun on 2016/8/16.
 */
@Controller
public class RecMerchantForAdminApiController extends V2ApiController {

    @Resource(name = "recMerchantForAdminService")
    private IRecMerchantForAdminService recMerchantService;

    /**
     * <h3>获取首页推荐商家列表</h3>
     * url: api/v2/recommendMerchant/list
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "recommendMerchant/list")
    public ResponseMessage getRecMerchantList() {
        List<RecommendMerchantForAdmin> recMerchants = this.recMerchantService.getRecMerchants();
        return executeResult(ResponseStatusCode.SUCCESS_CODE, "获取数据成功", recMerchants);
    }
}
