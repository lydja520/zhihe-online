package cn.com.zhihetech.online.controller.v1.api;

import cn.com.zhihetech.online.bean.GoodsBrowse;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.service.IGoodsBrowseService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 商品浏览记录
 * Created by ShenYunjie on 2016/4/18.
 */
@Controller
public class GoodsBrowseController extends ApiController {

    @Resource(name = "goodsBrowseService")
    private IGoodsBrowseService goodsBrowseService;

    /**
     * goods.goodsId:浏览的商品ID;
     * user.userId:浏览商品的用户ID;
     *
     * @param goodsBrowse
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "goodsBrowse/add", method = RequestMethod.POST)
    public ResponseMessage addGoodsBrowse(GoodsBrowse goodsBrowse) {
        this.goodsBrowseService.add(goodsBrowse);
        return executeResult();
    }
}
