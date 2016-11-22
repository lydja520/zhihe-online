package cn.com.zhihetech.online.controller.v1.api;

import cn.com.zhihetech.online.bean.MerchantBrowse;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.service.IMerchantBrowseService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by ShenYunjie on 2016/4/18.
 */
@Controller
public class MerchantBrowseController extends ApiController {

    @Resource(name = "merchantBrowseService")
    private IMerchantBrowseService merchantBrowseService;

    /**
     * user.userId  用户ID
     * merchant.merchantId  商家ID
     *
     * @param merchantBrowse
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "merchantBrowse/add", method = RequestMethod.POST)
    public ResponseMessage addMerchantBrowse(MerchantBrowse merchantBrowse) {
        this.merchantBrowseService.add(merchantBrowse);
        return executeResult();
    }
}
