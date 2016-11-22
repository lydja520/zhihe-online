package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.*;
import cn.com.zhihetech.online.commons.*;
import cn.com.zhihetech.online.dao.*;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IMerchantScoreService;
import cn.com.zhihetech.online.service.IMerchantService;
import cn.com.zhihetech.online.service.IRecommendService;
import cn.com.zhihetech.online.util.DateUtils;
import cn.com.zhihetech.online.util.JerseyUtils;
import cn.com.zhihetech.online.util.SMSUtils;
import cn.com.zhihetech.online.vo.ClientSecretCredential;
import cn.com.zhihetech.online.vo.Credential;
import cn.com.zhihetech.online.vo.EndPoints;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pingplusplus.model.App;
import org.glassfish.jersey.client.JerseyWebTarget;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.*;

/**
 * Created by ShenYunjie on 2015/11/20.
 */
@Service("merchantService")
public class MerchantServiceImpl implements IMerchantService {

    @Resource(name = "merchantDao")
    private IMerchantDao merchantDao;

    @Resource(name = "adminDao")
    private IAdminDao adminDao;

    @Resource(name = "goodsDao")
    private IGoodsDao goodsDao;

    @Resource(name = "merchantAllianceDao")
    private IMerchantAllianceDao merchantAllianceDao;

    @Resource(name = "activityDao")
    private IActivityDao activityDao;

    @Resource(name = "imgInfoDao")
    private IImgInfoDao imgInfoDao;
    @Resource(name = "merchantScoreService")
    private IMerchantScoreService merchantScoreService;
    @Resource(name = "recommendService")
    private IRecommendService recommendService;
    @Resource(name = "recommendDao")
    private IRecommendDao recommendDao;

    @Override
    public Merchant getById(String id) {
        Merchant merchant = this.merchantDao.findEntityById(id);
        if (merchant != null) {
            merchant.setScore(this.merchantScoreService.getMerchantScoreByMerchantId(id));
        }
        return merchant;
    }

    @Override
    public void delete(Merchant merchant) {
        this.merchantDao.deleteEntity(merchant);
    }

    @Override
    public Merchant add(Merchant merchant) {
        return this.merchantDao.saveEntity(merchant);
    }

    @Override
    public void update(Merchant merchant) {
        this.merchantDao.updateEntity(merchant);
    }


    @Override
    public List<Merchant> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.merchantDao.getEntities(pager, queryParams);
    }

    @Override
    public PageData<Merchant> getPageData(Pager pager, IQueryParams queryParams) {
        return this.merchantDao.getPageData(pager, queryParams);
    }

    private static final String APPKEY = AppConfig.EasemobConfig.EM_APP_KEY;
    private static final JsonNodeFactory factory = new JsonNodeFactory(false);

    // 通过app的client_id和client_secret来获取app管理员token
    private static Credential credential = new ClientSecretCredential(AppConfig.EasemobConfig.APP_CLIENT_ID,
            AppConfig.EasemobConfig.APP_CLIENT_SECRET, Roles.USER_ROLE_APPADMIN);

    @Override
    public void updateCoverImg(Merchant merchant) {
        ImgInfo imgInfo = this.imgInfoDao.findEntityById(merchant.getCoverImg().getImgInfoId());
        Map paramAndValue = new HashMap();
        paramAndValue.put("coverImg", imgInfo);
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("merchantId", merchant.getMerchantId());
        this.merchantDao.executeUpdate(paramAndValue, queryParams);
    }

    @Override
    public void updateHeadImg(Merchant merchant) {
        ImgInfo imgInfo = this.imgInfoDao.findEntityById(merchant.getHeaderImg().getImgInfoId());
        Map paramAndValue = new HashMap();
        paramAndValue.put("headerImg", imgInfo);
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("merchantId", merchant.getMerchantId());
        this.merchantDao.executeUpdate(paramAndValue, queryParams);
    }

    /**
     * 初次审核
     *
     * @param merchant
     * @param admin
     */
    @Override
    public void updateExaminStateOk(Merchant merchant, Admin admin) {
        Merchant _merchant = this.merchantDao.findEntityById(merchant.getMerchantId());
        _merchant.setExaminState(Constant.EXAMINE_STATE_EXAMINED_OK);
        _merchant.setPermit(true);
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("merchant.merchantId", merchant.getMerchantId());
        List<Admin> admins = this.adminDao.getEntities(queryParams);
        Admin _admin = admins.get(0);
        _admin.setPermit(admin.isPermit());
        _admin.setRoles(admin.getRoles());
        _admin.setSuperAdmin(true); //初次审核对应的管理员为商家的超级管理员
        _admin.setOfficial(false);  //商家管理员不是平台管理员

        /**
         * 设置环信账号信息
         */
        _admin.setChatPassword(_merchant.getMerchantId());
        _admin.setChatUserName(_merchant.getMerchantId().replaceAll("-", ""));
        _admin.setChatNickName(_merchant.getMerchName());
        _admin.setPortrait(_merchant.getCoverImg());
        _admin.setSuperAdmin(true); //商家超级管理员

        ObjectNode datanode = JsonNodeFactory.instance.objectNode();
        datanode.put("username", _admin.getChatUserName());
        datanode.put("password", _admin.getChatPassword());
        ObjectNode createNewIMUserSingleNode = createNewIMUserSingle(datanode);
        if (createNewIMUserSingleNode == null) {
            throw new SystemException("审核失败，请重试！");
        }
        this.adminDao.updateEntity(_admin);
        this.merchantDao.updateEntity(_merchant);
        String smsText = MessageFormat.format(WebChineseConfig.MsgTemplate.MERCHANT_EXAMIN_OK, DateUtils.formatDateTime(new Date()));
        SMSUtils.asyncSendSMS(_merchant.getContactMobileNO(), smsText);
    }

    @Override
    public void updateExaminStateDissmiss(String merchantId, String examinMsg) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("examinMsg", examinMsg);
        param.put("examinState", Constant.EXAMINE_STATE_EXAMINED_NUOK);
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("merchantId", merchantId);
        this.merchantDao.executeUpdate(param, queryParams);
    }

    @Override
    public void addMerchantAndAdmin(Merchant merchant, Admin admin) {
        this.merchantDao.saveEntity(merchant);
        this.adminDao.saveEntity(admin);
    }

    @Override
    public void addIMMerchant() {
        List<Merchant> merchants = this.merchantDao.getEntities(null);
        for (Merchant merchant : merchants) {
            String merchantId = merchant.getMerchantId();
            ObjectNode datanode = JsonNodeFactory.instance.objectNode();
            datanode.put("username", merchantId.replaceAll("-", ""));
            datanode.put("password", merchantId);
            ObjectNode createNewIMUserSingleNode = createNewIMUserSingle(datanode);
            System.out.println(createNewIMUserSingleNode);
        }
    }

    @Override
    public void executeUpdateBusLicePhoto(String merchantId, String imgInfoId) {
        ImgInfo busLicePhoto = new ImgInfo();
        busLicePhoto.setImgInfoId(imgInfoId);
        Map<String, Object> paramAndValue = new HashMap<>();
        paramAndValue.put("busLicePhoto", busLicePhoto);
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("merchantId", merchantId);
        this.merchantDao.executeUpdate(paramAndValue, queryParams);
    }

    @Override
    public List<Object> getProperty(String selector, Pager pager, GeneralQueryParams generalQueryParams) {
        return this.merchantDao.getProperty(selector, pager, generalQueryParams);
    }

    /**
     * 注册IM用户[单个]
     * <p>
     * 给指定AppKey创建一个新的用户
     *
     * @param dataNode
     * @return
     */
    public static ObjectNode createNewIMUserSingle(ObjectNode dataNode) {

        ObjectNode objectNode = null;

        try {
            JerseyWebTarget webTarget = EndPoints.USERS_TARGET.resolveTemplate("org_name", APPKEY.split("#")[0]).resolveTemplate("app_name", APPKEY.split("#")[1]);

            objectNode = JerseyUtils.sendRequest(webTarget, dataNode, credential, HTTPMethod.METHOD_POST, null);

        } catch (Exception e) {
            throw new SystemException("系统错误，请与管理员联系！");
        }

        return objectNode;
    }

    @Override
    public void editMerchantInfoForm(Merchant merchant) {
        this.merchantDao.updateEntity(merchant);
    }

    /**
     * 获取商家列表和商家的推荐商品
     *
     * @param pager
     * @param queryParams
     * @param goodsNum
     * @return
     */
    @Override
    public PageData<Merchant> getMerchantAndGoods(Pager pager, IQueryParams queryParams, int goodsNum) {
        PageData<Merchant> merchantPageData = this.merchantDao.getPageData(pager, queryParams);
        Iterator<Merchant> iterator = merchantPageData.getRows().iterator();

        Pager _page = new Pager(1, goodsNum);//推荐商品分页

        while (iterator.hasNext()) {
            Merchant merchant = iterator.next();

            merchant.setGoodsNum(getMerchantValidGoodsCount(merchant.getMerchantId())); //设置商家有效已上架的商品数量

            merchant.setRecommendGoodses(this.recommendService.getRecommendGoodsByMerchantId(_page, merchant.getMerchantId()));//设置商家的推荐商品

            merchant.setIsActivating(hasStartedActivity(merchant.getMerchantId())); //设置商家是否有正在进行的活动
        }
        return merchantPageData;
    }

    @Override
    public void updatePermit(String merchantId, boolean permit, String permitMsg) {
        IQueryParams queryParams = new GeneralQueryParams().andEqual("merchantId", merchantId);
        List<Object> states = this.merchantDao.getProperty("examinState", null, queryParams);
        if (states == null || states.isEmpty()) {
            throw new SystemException("未找到对应的商家！");
        }
        if (Integer.parseInt(states.get(0).toString()) != Constant.EXAMINE_STATE_EXAMINED_OK) {
            throw new SystemException("不能启用或禁用未审核通过的商家！");
        }

        Map<String, Object> param = new HashMap<>();
        param.put("permit", permit);
        queryParams = new GeneralQueryParams();
        queryParams.andEqual("merchant.merchantId", merchantId);
        this.adminDao.executeUpdate(param, queryParams);

        param.put("permitMsg", permitMsg);
        queryParams = new GeneralQueryParams().andEqual("merchantId", merchantId);
        this.merchantDao.executeUpdate(param, queryParams);

    }

    /**
     * 根据商品类别获取商家列表
     *
     * @param goodsAttSetId
     * @param pager
     * @return
     */
    @Override
    public PageData<Merchant> getMerchantsByGoodsAttSet(String goodsAttSetId, Pager pager) {
        PageData<Merchant> pageData = this.merchantDao.getMerchantsByGoodsAttSet(goodsAttSetId, pager);
        Iterator<Merchant> it = pageData.getRows().iterator();
        while (it.hasNext()) {
            String merchantId = it.next().getMerchantId();
            Merchant merchant = this.merchantDao.findEntityById(merchantId);

            merchant.setGoodsNum(getMerchantValidGoodsCount(merchantId));   //设置有效已上架商品的数量

            merchant.setRecommendGoodses(this.recommendService.getRecommendGoodsByMerchantId(new Pager(1, 3), merchant.getMerchantId()));   //设置推荐商品

            merchant.setIsActivating(hasStartedActivity(merchantId));//设置是否有正在开始的活动

        }
        return pageData;
    }

    /**
     * 获取商家有有效已上架商品的总数
     *
     * @param merchantId
     * @return
     */
    protected long getMerchantValidGoodsCount(String merchantId) {
        IQueryParams query = new GeneralQueryParams()
                .andEqual("merchant.merchantId", merchantId)
                //.andProParam("stock>volume")
                .andEqual("onsale", true)
                .andEqual("deleteState", false)
                .andEqual("examinState", Constant.EXAMINE_STATE_EXAMINED_OK);
        return this.goodsDao.getRecordTotal(query);
    }

    /**
     * 判断商家是否有正在开始的活动
     *
     * @param merchantId 商家ID
     * @return true:有正在开始的活动；false:没有正在开始的活动
     */
    protected boolean hasStartedActivity(String merchantId) {
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("merchant.merchantId", merchantId)
                .andEqual("allianceState", MerchantAlliance.ALLIANCE_READINESS_STATE);
        List<Object> activits = this.merchantAllianceDao.getProperty("distinct activity.activitId", null, queryParams);
        if (activits == null || activits.isEmpty()) {
            return false;
        }
        queryParams = new GeneralQueryParams()
                .andIn("activitId", activits)
                .andEqual("currentState", Constant.ACTIVITY_STATE_STARTED);
        if (this.activityDao.getRecordTotal(queryParams) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void updateMerchantUpdateDate(String merchantId) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("merchantId", merchantId);
        Map<String, Object> paramAndValue = new HashMap<>();
        paramAndValue.put("updateDate", new Date().getTime());
        this.merchantDao.executeUpdate(paramAndValue, queryParams);
    }

}
