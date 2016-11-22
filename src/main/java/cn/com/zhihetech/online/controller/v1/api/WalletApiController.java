package cn.com.zhihetech.online.controller.v1.api;

import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.service.IWalletService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by YangDaiChun on 2016/3/4.
 */
@Controller
public class WalletApiController extends ApiController {

    @Resource(name = "walletService")
    private IWalletService walletService;

    /**
     * <h3>把抢到的红包提现到用户钱包</h3>
     * url : api/wallet/putRedEnvelop  <br>
     * <p>
     * <p>参数</p>
     * userId : 用户Id  <br>
     * envelopItemId : 已抢到的红包Id  <br>
     *
     * @param userId
     * @param envelopItemId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "wallet/putRedEnvelop")
    public ResponseMessage putRedEnvelopToWallet(String userId, String envelopItemId) {
        ResponseMessage responseMessage = null;
        responseMessage = this.walletService.executePutRedEnvelopItemMoneyToWallet(userId, envelopItemId);
        if (responseMessage != null) {
            return responseMessage;
        }
        return executeResult();
    }

    /**
     * <h3>获取用户钱包的金额</h3>
     * url : api/user/{userId}/walletTotalMoney  <br>
     * {userId}  ： 用户Id <br>
     *
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "user/{userId}/walletTotalMoney")
    public ResponseMessage getWalletMoney(@PathVariable(value = "userId") String userId) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("user.userId", userId);
        List<Object> totalMoneys = this.walletService.getTotalMoney("totalMoney", queryParams);
        if (totalMoneys.size() <= 0) {
            return executeResult(ResponseStatusCode.SUCCESS_CODE, "获取钱包金额成功", 0);
        }
        float totalMoney = (float) totalMoneys.get(0);
        return executeResult(ResponseStatusCode.SUCCESS_CODE, "获取钱包金额成功", totalMoney);
    }
}
