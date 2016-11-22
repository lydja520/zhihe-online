package cn.com.zhihetech.online.controller.v2.api;

import cn.com.zhihetech.online.bean.RecommendGoodsForAdmin;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.service.IRecGoodsForAdminService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by YangDaiChun on 2016/8/15.
 */
@Controller
public class RecGoodsForAdminApiController extends V2ApiController {

    @Resource(name = "recGoodsForAdminService")
    private IRecGoodsForAdminService recGoodsService;

    /**
     * <h3>获取首页推荐商品列表</h3>
     * url: api/v2/recommendGoods/list
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "recommendGoods/list")
    public ResponseMessage getRecGoodsForAdmin() {
        List<RecommendGoodsForAdmin> recommendGoodsForAdmins = recGoodsService.getRecGoodses();
        return this.executeResult(ResponseStatusCode.SUCCESS_CODE, "数据获取成功", recommendGoodsForAdmins);
    }
}
