package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.*;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.dao.IShopShowDao;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.*;
import cn.com.zhihetech.online.util.VerifyRegexp;
import cn.com.zhihetech.online.vo.MerchantImg;
import cn.com.zhihetech.online.vo.MerchantSearch;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ShenYunjie on 2015/11/20.
 */
@Controller
public class MerchantController extends SupportController {

    private final String REGIST_MERCHANT = "registerMerchant";

    @Resource(name = "merchantService")
    private IMerchantService merchantService;
    @Resource(name = "imgInfoService")
    private IImgInfoService imgInfoService;
    @Resource(name = "roleService")
    private IRoleService roleService;
    @Resource(name = "adminService")
    private IAdminService adminService;
    @Resource(name = "shopShowService")
    private IShopShowService shopShowService;
    @Resource(name = "featuredService")
    private IFeaturedBlockService featuredBlockService;
    @Resource(name = "shoppingCenterService")
    private IShoppingCenterService shoppingCenterService;
    @Resource(name = "goodsAttributeSetService")
    private IGoodsAttributeSetService goodsAttributeSetService;


    @RequestMapping("admin/merchant")
    public String merchantPage() {
        return "admin/merchant";
    }

    @RequestMapping(value = "admin/merchantInfoPage")
    public ModelAndView infoPage(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("admin/merchantInfoPage");
        String merchantId = this.getCurrentMerchatId(request);
        Merchant merchant = this.merchantService.getById(merchantId);
        modelAndView.addObject("merchant", merchant);
        return modelAndView;
    }

    /**
     * 商家审核页面
     *
     * @return
     */
    @RequestMapping("admin/examinMerchant")
    public ModelAndView examinMerchantPage() {
        ModelAndView modelAndView = new ModelAndView("admin/merchantExamin");
        List<Role> roles = this.roleService.getAllByParams(null, null);
        modelAndView.addObject("roleList", roles);
        return modelAndView;
    }

    /**
     * 商家详细信息页面
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "admin/merchant/info/{id}")
    public ModelAndView merchantInfo(@PathVariable(value = "id") String id) {
        ModelAndView modelAndView = new ModelAndView("admin/merchantInfo");
        Merchant merchant = this.merchantService.getById(id);
        modelAndView.addObject("merchant", merchant);
        return modelAndView;
    }

    /**
     * 所有商家列表
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/merchant/list")
    public PageData<Merchant> getMerchantPageData(HttpServletRequest request, MerchantSearch merchantSearch) {
        IQueryParams queryParams = createQueryParams(request);
        if (!StringUtils.isEmpty(merchantSearch.getMerchName())) {
            queryParams.andAllLike("merchName", merchantSearch.getMerchName());
        }
        if (!StringUtils.isEmpty(merchantSearch.getMerchTell())) {
            queryParams.andAllLike("merchTell", merchantSearch.getMerchTell());
        }
        if (!StringUtils.isEmpty(merchantSearch.getPermit())) {
            queryParams.andEqual("permit", merchantSearch.getPermit().equals("1") ? true : false);
        }
        if (!StringUtils.isEmpty(merchantSearch.getExaminState())) {
            queryParams.andEqual("examinState", Integer.parseInt(merchantSearch.getExaminState()));
        }
        if (!queryParams.sortContainsKey("permit")) {
            queryParams.sort("permit", Order.DESC);
        }
        if (!queryParams.sortContainsKey("merchOrder")) {
            queryParams.sort("merchOrder", Order.ASC);
        }
        return this.merchantService.getPageData(createPager(request), queryParams);
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/merchant/add", method = RequestMethod.POST)
    public ResponseMessage addMerchant(Merchant merchant) {
        merchant.setCreateDate(new Date());
        this.merchantService.add(merchant);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/merchant/edit", method = RequestMethod.POST)
    public ResponseMessage editMerchant(Merchant merchant) {
        this.merchantService.update(merchant);
        return executeResult();
    }


    /**
     * 待审核商家列表
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/merchant/examinList")
    public PageData<Merchant> getExaminMerchant(HttpServletRequest request) {
        IQueryParams queryParams = this.createQueryParams(request);
        queryParams.andEqual("examinState", Constant.EXAMINE_STATE_SUBMITED);
        return this.merchantService.getPageData(this.createPager(request), queryParams);
    }

    /**
     * 商家审核通过
     *
     * @param merchant
     * @param admin
     * @param roleIds
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/merchant/updateExaminStateOk")
    public ResponseMessage updateExaminStateOk(Merchant merchant, Admin admin, String[] roleIds) {
        if (roleIds == null || roleIds.length < 1) {
            return executeResult(500, false, "商家角色不能为空！");
        }
        if (roleIds != null) {
            for (String id : roleIds) {
                if (StringUtils.isEmpty(id)) {
                    continue;
                }
                Role role = new Role();
                role.setRoleId(id);
                admin.getRoles().add(role);
            }
        }
        this.merchantService.updateExaminStateOk(merchant, admin);
        return executeResult();
    }

    /**
     * 商家审核不通过
     *
     * @param merchantId
     * @param examinMsg
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/merchant/updateExaminStateDissmiss")
    public ResponseMessage updateExaminStateDissmiss(String merchantId, String examinMsg) {
        this.merchantService.updateExaminStateDissmiss(merchantId, examinMsg);
        return executeResult();
    }

    /**
     * 商家的启用和禁用
     *
     * @param merchantId
     * @param permit
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/merchant/updatePermit")
    public ResponseMessage updatePermit(String merchantId, boolean permit, String permitMsg) {
        this.merchantService.updatePermit(merchantId, permit, permitMsg);
        return executeResult();
    }

    /**
     * 修改注册信息页面
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "admin/api/merchant/editMerchantRegisterInfo")
    public ModelAndView editMerchantInfo(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("admin/merchantRegisterInfoEdit");
        Merchant merchant = this.merchantService.getById(this.getCurrentMerchatId(request));
        modelAndView.addObject("merchant", merchant);
        IQueryParams queryParams = new GeneralQueryParams().andEqual("permit", true);
        List<ShoppingCenter> shoppingCenterList = this.shoppingCenterService.getAllByParams(null, queryParams);
        queryParams = new GeneralQueryParams().andEqual("permit", true);
        List<FeaturedBlock> featuredBlockList = this.featuredBlockService.getAllByParams(null, queryParams);
        modelAndView.addObject("shoppingCenterList", shoppingCenterList).addObject("featuredBlockList", featuredBlockList);
        queryParams = new GeneralQueryParams();
        queryParams.andEqual("permit", true);
        List<GoodsAttributeSet> goodsAttributeSets = this.goodsAttributeSetService.getAllByParams(null, queryParams);
        modelAndView.addObject("goodsAttSets", goodsAttributeSets);
        return modelAndView;
    }

    /**
     * 修改商家基本信息页面（管理员权限）
     */
    @RequestMapping(value = "admin/api/merchant/{merchantId}/editMerchantInfo")
    public ModelAndView editMerchantInfoById(@PathVariable(value = "merchantId") String merchantId, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("admin/merchantInfoEdit");
        Merchant merchant = this.merchantService.getById(merchantId);
        modelAndView.addObject("merchant", merchant);
        IQueryParams queryParams = new GeneralQueryParams().andEqual("permit", true);
        List<ShoppingCenter> shoppingCenterList = this.shoppingCenterService.getAllByParams(null, queryParams);
        List<FeaturedBlock> featuredBlockList = this.featuredBlockService.getAllByParams(null, queryParams);
        modelAndView.addObject("shoppingCenterList", shoppingCenterList).addObject("featuredBlockList", featuredBlockList);
        List<GoodsAttributeSet> goodsAttributeSets = this.goodsAttributeSetService.getAllByParams(null, queryParams);
        modelAndView.addObject("goodsAttSets", goodsAttributeSets);
        return modelAndView;
    }


    /**
     * 修改注册信息
     *
     * @param merchant
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/merchant/editMerchantRegisterInfoForm", method = RequestMethod.POST)
    public ResponseMessage merchantData(Merchant merchant, String[] goodsAttSet, HttpServletRequest request) {
        Merchant _merchant = this.merchantService.getById(this.getCurrentMerchatId(request));
        if (_merchant.getExaminState() == Constant.EXAMINE_STATE_EXAMINED_OK) {
            _merchant.setMerchantDetails(merchant.getMerchantDetails());
        } else {
            if (goodsAttSet == null || goodsAttSet.length == 0) {
                throw new SystemException("商家的经营范围不能为空");
            }
            if (!VerifyRegexp.isMobileNum(merchant.getContactMobileNO())) {
                throw new SystemException("请填入正确的联系人电话号码！");
            }
            Set<GoodsAttributeSet> categories = new HashSet<>();
            for (int i = 0; i < goodsAttSet.length; i++) {
                String categorieId = goodsAttSet[i];
                GoodsAttributeSet goodsAttributeSet = new GoodsAttributeSet();
                goodsAttributeSet.setGoodsAttSetId(categorieId);
                categories.add(goodsAttributeSet);
            }
            _merchant.setMerchName(merchant.getMerchName());
            _merchant.setMerchTell(merchant.getMerchTell());
            _merchant.setAddress(merchant.getAddress());
            _merchant.setAlipayCode(merchant.getAlipayCode());
            _merchant.setLicenseCode(merchant.getLicenseCode());
            _merchant.setEmplyCount(merchant.getEmplyCount());

            _merchant.setCategories(categories);
            _merchant.setShoppingCenter(merchant.getShoppingCenter());
            _merchant.setFeaturedBlock(merchant.getFeaturedBlock());
            _merchant.setStoreType(merchant.getStoreType());

            _merchant.setMerchantDetails(merchant.getMerchantDetails());

            _merchant.setContactID(merchant.getContactID());
            _merchant.setContactMobileNO(merchant.getContactMobileNO());
            _merchant.setContactName(merchant.getContactName());
            _merchant.setContactEmail(merchant.getContactEmail());
            _merchant.setContactPartAndPositon(merchant.getContactPartAndPositon());
        }
        this.merchantService.update(_merchant);
        return executeResult();
    }

    /**
     * 修改注册信息（管理员权限）
     *
     * @param merchant
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/merchant/editMerchantRegisterInfoFormByAdmin", method = RequestMethod.POST)
    public ResponseMessage editMerchantData(Merchant merchant, String[] goodsAttSet, HttpServletRequest request) {
        Merchant _merchant = this.merchantService.getById(merchant.getMerchantId());
        if (goodsAttSet == null || goodsAttSet.length == 0) {
            throw new SystemException("商家的经营范围不能为空");
        }
        if (!VerifyRegexp.isMobileNum(merchant.getContactMobileNO())) {
            throw new SystemException("请填入正确的联系人电话号码！");
        }
        Set<GoodsAttributeSet> categories = new HashSet<>();
        for (int i = 0; i < goodsAttSet.length; i++) {
            String categorieId = goodsAttSet[i];
            GoodsAttributeSet goodsAttributeSet = new GoodsAttributeSet();
            goodsAttributeSet.setGoodsAttSetId(categorieId);
            categories.add(goodsAttributeSet);
        }
        _merchant.setMerchName(merchant.getMerchName());
        _merchant.setMerchTell(merchant.getMerchTell());
        _merchant.setAddress(merchant.getAddress());
        _merchant.setAlipayCode(merchant.getAlipayCode());
        _merchant.setLicenseCode(merchant.getLicenseCode());
        _merchant.setEmplyCount(merchant.getEmplyCount());

        _merchant.setCategories(categories);
        _merchant.setShoppingCenter(merchant.getShoppingCenter());
        _merchant.setFeaturedBlock(merchant.getFeaturedBlock());
        _merchant.setStoreType(merchant.getStoreType());

        _merchant.setMerchantDetails(merchant.getMerchantDetails());

        _merchant.setContactID(merchant.getContactID());
        _merchant.setContactMobileNO(merchant.getContactMobileNO());
        _merchant.setContactName(merchant.getContactName());
        _merchant.setContactEmail(merchant.getContactEmail());
        _merchant.setContactPartAndPositon(merchant.getContactPartAndPositon());
        this.merchantService.update(_merchant);
        return executeResult();
    }

    /**
     * 修改有关审核图片页面
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "admin/api/merchant/editMerchantRegisterUpload")
    public ModelAndView imgInfoForm(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("/admin/merchantRegisterUploadEdit");
        Merchant merchant = this.merchantService.getById(this.getCurrentMerchatId(request));
        mv.addObject("merchant", merchant);
        return mv;
    }

    /**
     * 修改有关审核图片（审核通过后就不能修改）
     *
     * @param request
     * @param merchantImg
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/merchant/editMerchantRegisterUploadForm", method = RequestMethod.POST)
    public ResponseMessage submitMerchantInfo(HttpServletRequest request, MerchantImg merchantImg) {
        Merchant merchant = this.merchantService.getById(this.getCurrentMerchatId(request));
        if (merchant.getExaminState() == Constant.EXAMINE_STATE_EXAMINED_OK) {
            throw new SystemException("审核通过的商家不能进行修改审核图片操作！");
        }
        ImgInfo opraterIDPhoto = merchantImg.getOpraterIDPhoto();
        ImgInfo orgPhoto = merchantImg.getOrgPhoto();
        ImgInfo applyLetterPhoto = merchantImg.getApplyLetterPhoto();
        ImgInfo busLicePhoto = merchantImg.getBusLicePhoto();
        if (opraterIDPhoto == null && orgPhoto == null && applyLetterPhoto == null && busLicePhoto == null) {
        } else {
            if (opraterIDPhoto == null) {
                throw new SystemException("运营者手持身份证照片没有上传！");
            }
            if (orgPhoto == null) {
                throw new SystemException("组织机构代码证原件照片没有上传！");
            }
            if (busLicePhoto == null) {
                throw new SystemException("工商营业执照原件照片没有上传！");
            }
            if (applyLetterPhoto == null) {
                throw new SystemException("加盖公章的申请认证公函(与商家纠纷事件裁定等)照片没有上传！");
            }
            merchant.setOpraterIDPhoto(opraterIDPhoto);
            merchant.setOrgPhoto(orgPhoto);
            merchant.setBusLicePhoto(busLicePhoto);
            merchant.setApplyLetterPhoto(applyLetterPhoto);
        }
        merchant.setExaminState(Constant.EXAMINE_STATE_SUBMITED);
        this.merchantService.update(merchant);
        return executeResult();
    }

    /**
     * 修改营业执照
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/updateMerchant/busLicePhoto", method = RequestMethod.POST)
    public ResponseMessage updateBusLicePhoto(String merchantId, String imgInfoId) {
        this.merchantService.executeUpdateBusLicePhoto(merchantId, imgInfoId);
        return executeResult();
    }

    /**
     * 修改商家有关图片页面（任何状态下都能修改）
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "admin/updateImg")
    public ModelAndView updateImgPage(HttpServletRequest request) {
        Merchant merchant = this.merchantService.getById(this.getCurrentMerchatId(request));
        ModelAndView modelAndView = new ModelAndView("admin/merchantImgEdit");
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("merchant", merchant);
        List<ShopShow> shopShows = this.shopShowService.getAllByParams(null, queryParams);
        modelAndView.addObject("shopShows", shopShows);
        modelAndView.addObject("merchant", merchant);
        return modelAndView;
    }

    /**
     * 修改商家有关图片_封面图（任何状态下都能修改）
     *
     * @param merchant
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/merchant/updateCoverImg", method = RequestMethod.POST)
    public ResponseMessage updateCoverImg(Merchant merchant) {
        this.merchantService.updateCoverImg(merchant);
        return executeResult();
    }

    /**
     * 修改商家有关图片_头像（任何状态下都能修改）
     *
     * @param merchant
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/merchant/updateHeaderImg", method = RequestMethod.POST)
    public ResponseMessage updateHeaderImg(Merchant merchant) {
        this.merchantService.updateHeadImg(merchant);
        return executeResult();
    }

}
