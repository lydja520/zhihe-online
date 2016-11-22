package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.ReceivedGoodsAddress;
import cn.com.zhihetech.online.dao.IReceivedGoodsAddressDao;
import cn.com.zhihetech.online.service.IReceivedGoodsAddressService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by YangDaiChun on 2016/1/25.
 */
@Service(value = "receivedGoodsAddressService")
public class ReceivedGoodsAddressServiceImpl implements IReceivedGoodsAddressService {

    @Resource(name = "receivedGoodsAddressDao")
    private IReceivedGoodsAddressDao receivedGoodsAddressDao;


    @Override
    public ReceivedGoodsAddress getById(String id) {
        return null;
    }

    @Override
    public void delete(ReceivedGoodsAddress receivedGoodsAddress) {
        ReceivedGoodsAddress _receivedGoodsAddress = this.receivedGoodsAddressDao.findEntityById(receivedGoodsAddress.getAddressId());
        this.receivedGoodsAddressDao.deleteEntity(_receivedGoodsAddress);
    }

    @Override
    public ReceivedGoodsAddress add(ReceivedGoodsAddress receivedGoodsAddress) {
        return this.receivedGoodsAddressDao.saveEntity(receivedGoodsAddress);
    }

    @Override
    public void update(ReceivedGoodsAddress receivedGoodsAddress) {

    }

    @Override
    public List<ReceivedGoodsAddress> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.receivedGoodsAddressDao.getEntities(pager,queryParams);
    }

    @Override
    public PageData<ReceivedGoodsAddress> getPageData(Pager pager, IQueryParams queryParams) {
        PageData<ReceivedGoodsAddress> receivedGoodsAddressPageData = this.receivedGoodsAddressDao.getPageData(pager,queryParams);
        List<ReceivedGoodsAddress> receivedGoodsAddressList = receivedGoodsAddressPageData.getRows();
        for(ReceivedGoodsAddress receivedGoodsAddress : receivedGoodsAddressList){
            receivedGoodsAddress.setUser(null);
        }
        return receivedGoodsAddressPageData;
    }

    @Override
    public ReceivedGoodsAddress saveOrUpdate(ReceivedGoodsAddress receivedGoodsAddress) {
        if(receivedGoodsAddress.isDefaultAddress()){
            IQueryParams queryParams = new GeneralQueryParams();
            queryParams.andEqual("user",receivedGoodsAddress.getUser()).andEqual("defaultAddress",true);
            Map<String,Object> paramAndValue = new HashMap<>();
            paramAndValue.put("defaultAddress",false);
            this.receivedGoodsAddressDao.executeUpdate(paramAndValue,queryParams);
        }
        return this.receivedGoodsAddressDao.saveOrUpdate(receivedGoodsAddress);
    }
}
