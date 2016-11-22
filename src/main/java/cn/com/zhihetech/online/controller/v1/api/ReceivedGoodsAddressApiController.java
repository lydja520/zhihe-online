package cn.com.zhihetech.online.controller.v1.api;

import cn.com.zhihetech.online.bean.ReceivedGoodsAddress;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.service.IReceivedGoodsAddressService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by YangDaiChun on 2016/1/25.
 */
@Controller
public class ReceivedGoodsAddressApiController extends ApiController{


    @Resource(name = "receivedGoodsAddressService")
    private IReceivedGoodsAddressService receivedGoodsAddressService;

    /**
     * <h3>添加或者更新收货地址</h3>
     * url: api/receivedGoodsAddress/addOrUpdate  <br>
     *
     *  <h4>参数</h4>
     *
     * addressId : 不传值则添加新的收货地址，传值为更新对应的收货信息
     *  user.userId  : 用户ID  <br>
     *  receiverName ：收货人姓名  <br>
     *  receiverPhone : 收货人电话号码  <br>
     *  detailAddress : 收货详细地址  <br>
     *  defaultAddress : 是否是默认地址 参数传 ： true 或 false
     *
     * @param receivedGoodsAddress
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "receivedGoodsAddress/addOrUpdate",method = RequestMethod.POST)
    public ResponseMessage addReceivedGoodsAddress(ReceivedGoodsAddress receivedGoodsAddress){
        ReceivedGoodsAddress _receivedGoodsAddress = this.receivedGoodsAddressService.saveOrUpdate(receivedGoodsAddress);
        if(_receivedGoodsAddress != null){
            return this.executeResult(ResponseStatusCode.SUCCESS_CODE,"操作成功",_receivedGoodsAddress);
        }
        return executeResult();
    }

    /**
     * <h3>根据用户ID获取收货地址信息</h3>
     *
     * url: api/user/{userId}/receivedGoodsAddresses  <br>
     *  {userId}为用户的id  <br>
     *
     * @param userId
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "user/{userId}/receivedGoodsAddresses")
    public PageData<ReceivedGoodsAddress> getPageData(@PathVariable(value = "userId")String userId,HttpServletRequest request){
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("user.userId",userId);
        return this.receivedGoodsAddressService.getPageData(this.createPager(request),queryParams);
    }

    /**
     * <h3>获取默认收货地址</h3>
     * url : user/{userId}/defaultReceivedAddress
     * {userId} : 用户ID
     *
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "user/{userId}/defaultReceivedAddress")
    public ResponseMessage getDefaultAddress(@PathVariable(value = "userId")String userId){
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("user.userId",userId).andEqual("defaultAddress",true);
        List<ReceivedGoodsAddress> receivedGoodsAddressList = this.receivedGoodsAddressService.getAllByParams(null,queryParams);
        if(receivedGoodsAddressList.size() > 0){
            ReceivedGoodsAddress receivedGoodsAddress = receivedGoodsAddressList.get(0);
            receivedGoodsAddress.setUser(null);
            return executeResult(ResponseStatusCode.SUCCESS_CODE,"获取默认地址成功",receivedGoodsAddress);
        }else {
            IQueryParams queryParams1 = new GeneralQueryParams();
            queryParams1.andEqual("user.userId",userId);
            List<ReceivedGoodsAddress> receivedGoodsAddressList1 = this.receivedGoodsAddressService.getAllByParams(null,queryParams1);
            if(receivedGoodsAddressList1.size() > 0){
                ReceivedGoodsAddress receivedGoodsAddress = receivedGoodsAddressList1.get(0);
                receivedGoodsAddress.setUser(null);
                return executeResult(ResponseStatusCode.SUCCESS_CODE,"获取默认地址成功",receivedGoodsAddress);
            }else {
                return executeResult(ResponseStatusCode.PAGE_NOT_FOUND_CODE,"无收货地址");
            }
        }
    }

    /**
     * <h3>删除收货地址</h3>
     * url : api/receivedGoodsAddress/{addressId}/delete  <br>
     *  {addressId} : 收货地址Id
     *
     * @param addressId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "receivedGoodsAddress/{addressId}/delete")
    public ResponseMessage deleteReceivedGoodsAddress(@PathVariable(value = "addressId")String addressId){
        ReceivedGoodsAddress receivedGoodsAddress = new ReceivedGoodsAddress();
        receivedGoodsAddress.setAddressId(addressId);
        this.receivedGoodsAddressService.delete(receivedGoodsAddress);
        return executeResult();
    }
}
