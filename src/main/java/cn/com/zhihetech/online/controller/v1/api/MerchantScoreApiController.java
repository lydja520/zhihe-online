package cn.com.zhihetech.online.controller.v1.api;

import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IMerchantScoreService;
import cn.com.zhihetech.util.common.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2016/3/16.
 */
@Controller
public class MerchantScoreApiController extends ApiController {

    @Resource(name = "merchantScoreService")
    private IMerchantScoreService merchantScoreService;

    @ResponseBody
    @RequestMapping(value = "merchant/{merchantId}/score", method = RequestMethod.GET)
    public ResponseMessage getMerchantScore(@PathVariable("merchantId") String merchantId) {
        if (StringUtils.isEmpty(merchantId)) {
            throw new SystemException("参数不正确");
        }
        Float merchantScore = this.merchantScoreService.getMerchantScoreByMerchantId(merchantId);
        return executeResult(200, null, merchantScore);
    }
}
