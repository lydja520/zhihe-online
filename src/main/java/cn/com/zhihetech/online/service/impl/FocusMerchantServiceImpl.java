package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.FocusMerchant;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.bean.User;
import cn.com.zhihetech.online.dao.IFocusMerchantDao;
import cn.com.zhihetech.online.dao.IMerchantDao;
import cn.com.zhihetech.online.service.IFocusMerchantService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by ShenYunjie on 2015/12/15.
 */
@Service("focusMerchantService")
public class FocusMerchantServiceImpl implements IFocusMerchantService {
    @Resource(name="focusMerchantDao")
    private IFocusMerchantDao focusMerchantDao;

    @Resource(name = "merchantDao")
    private IMerchantDao merchantDao;
    /**
     * 根据ID获取持久化对象
     *
     * @param id 对象ID
     * @return
     */
    @Override
    public FocusMerchant getById(String id) {
        return null;
    }

    /**
     * 删除持久化对象
     *
     * @param focusMerchant 需要删除的持久化对象
     */
    @Override
    public void delete(FocusMerchant focusMerchant) {
        this.focusMerchantDao.deleteEntity(focusMerchant);
    }

    /**
     * 添加一个对象到数据库
     *
     * @param focusMerchant 需要持久化的对象
     * @return
     */
    @Override
    public FocusMerchant add(FocusMerchant focusMerchant) {
        return this.focusMerchantDao.saveEntity(focusMerchant);
    }

    /**
     * 更新一个持久化对象
     *
     * @param focusMerchant
     */
    @Override
    public void update(FocusMerchant focusMerchant) {

    }

    /**
     * 根据分页参数和查询条件查询持久化对象
     *
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return
     */
    @Override
    public List<FocusMerchant> getAllByParams(Pager pager, IQueryParams queryParams) {
       return this.focusMerchantDao.getEntities(pager,queryParams);

    }

    /**
     * 根据分页参数和查询条件查询持久化对象
     *
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return 经过分页的数据
     */
    @Override
    public PageData<FocusMerchant> getPageData(Pager pager, IQueryParams queryParams) {
        return  this.focusMerchantDao.getPageData(pager,queryParams);
    }

    @Override
    public PageData<Merchant> getMerchantPageData(Pager pager,IQueryParams queryParams){
        PageData<FocusMerchant> focusMerchantPageData = this.focusMerchantDao.getPageData(pager,queryParams);
        List<FocusMerchant> focusMerchants = focusMerchantPageData.getRows();
        List<Merchant> merchants = new LinkedList<>();
        for(FocusMerchant focusMerchant : focusMerchants){
            Merchant merchant = focusMerchant.getMerchant();
            merchants.add(merchant);
        }
        PageData<Merchant> merchantPageData = new PageData<>();
        merchantPageData.setRows(merchants);
        merchantPageData.setTotal(focusMerchantPageData.getTotal());
        merchantPageData.setPage(focusMerchantPageData.getPage());
        merchantPageData.setPageSize(focusMerchantPageData.getPageSize());
        merchantPageData.setTotalPage(focusMerchantPageData.getTotalPage());
        return merchantPageData;
    }

    @Override
    public Map<String, List<FocusMerchant>> getMerchantFocusUser(Pager pager, List<Merchant> merchantId, String activityId) {
        Map<String,List<FocusMerchant>> focusMerchantUser = new HashMap<String,List<FocusMerchant>>();
        for(Merchant merchant : merchantId){
            IQueryParams queryParams = new GeneralQueryParams();
            List<FocusMerchant>  onefocusMerchants =  new ArrayList<FocusMerchant>(
                    this.focusMerchantDao.getEntities(queryParams.andEqual("merchant.merchantId", merchant.getMerchantId()))
            );
            focusMerchantUser.put(merchant.getMerchantId(),onefocusMerchants);
        }
        return focusMerchantUser;
    }
}
