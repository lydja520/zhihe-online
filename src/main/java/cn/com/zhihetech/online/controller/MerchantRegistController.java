package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.*;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.exception.StatusCodeException;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.*;
import cn.com.zhihetech.online.util.VerifyRegexp;
import cn.com.zhihetech.util.common.MD5Utils;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2015/12/1.
 */
@Controller
public class MerchantRegistController extends SupportController {

    private final String REGIST_MERCHANT = "registerMerchant";
    private final String REGIST_MERCHANT_ADMIN = "admin";

    @Resource(name = "merchantService")
    private IMerchantService merchantService;
    @Resource(name = "shoppingCenterService")
    private IShoppingCenterService shoppingCenterService;
    @Resource(name = "featuredService")
    private IFeaturedBlockService featuredBlockService;
    @Resource(name = "adminService")
    private IAdminService adminService;
    @Resource(name = "goodsAttributeSetService")
    private IGoodsAttributeSetService goodsAttributeSetService;

    @RequestMapping(value = "merchant/registAccount")
    public String registMerchantAccount() {
        return "merchantRegistAccount";
    }

    @ResponseBody
    @RequestMapping(value = "merchant/registAccount/submit")
    public ResponseMessage registAccountSubmit(Admin admin, HttpServletRequest request) {
        if (StringUtils.isEmpty(admin.getAdminCode()) || StringUtils.isEmpty(admin.getAdminPwd())) {
            throw new SystemException("账号和密码不能为空!");
        }
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("adminCode", admin.getAdminCode());
        if (this.adminService.getRecordTotal(queryParams) > 0) {
            throw new SystemException("此账号已经存在，可直接登录");
        }
        request.getSession().setAttribute(REGIST_MERCHANT_ADMIN, admin);
        return executeResult();
    }

    @RequestMapping(value = "merchant/registBasicInfo")
    public ModelAndView indexPage(HttpServletRequest request) {
        ModelAndView mv = null;
        if (request.getSession().getAttribute(REGIST_MERCHANT_ADMIN) == null) {
            mv = new ModelAndView("redirect:/merchant/registAccount");
            return mv;
        }
        mv = new ModelAndView("merchantRegistBasicInfo");
        IQueryParams queryParams = new GeneralQueryParams().andEqual("permit", true);
        List<ShoppingCenter> shoppingCenterList = this.shoppingCenterService.getAllByParams(null, queryParams);
        queryParams = new GeneralQueryParams().andEqual("permit", true);
        List<FeaturedBlock> featuredBlockList = this.featuredBlockService.getAllByParams(null, queryParams);
        mv.addObject("shoppingCenterList", shoppingCenterList).addObject("featuredBlockList", featuredBlockList);
        queryParams = new GeneralQueryParams();
        queryParams.andEqual("permit", true);
        List<GoodsAttributeSet> goodsAttributeSets = this.goodsAttributeSetService.getAllByParams(null, queryParams);
        mv.addObject("goodsAttSets", goodsAttributeSets);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "merchant/registBasicInfo/submit", method = RequestMethod.POST)
    public ResponseMessage merchantData(Merchant merchant, String[] goodsAttSet, HttpServletRequest request) {
        if (request.getSession().getAttribute(REGIST_MERCHANT_ADMIN) == null) {
            throw new SystemException("请先注册商家账号！");
        }
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
        merchant.setCategories(categories);
        request.getSession().setAttribute(REGIST_MERCHANT, merchant);
        return executeResult();
    }

    @RequestMapping(value = "merchant/registImgForm")
    public String imgInfoForm(HttpServletRequest request) {
        if (request.getSession().getAttribute(REGIST_MERCHANT) != null && request.getSession().getAttribute(REGIST_MERCHANT_ADMIN) != null) {
            return "/merchantRegistFileUpload";
        }
        if (request.getSession().getAttribute(REGIST_MERCHANT) == null) {
            return "redirect:/merchant/registBasicInfo";
        }
        return "redirect:/merchant/registAccount";
    }

    @ResponseBody
    @RequestMapping(value = "merchant/registImgForm/submit", method = RequestMethod.POST)
    public ResponseMessage submitMerchantInfo(HttpServletRequest request, Merchant merchantImg) {
        if (request.getSession().getAttribute(REGIST_MERCHANT) == null || request.getSession().getAttribute(REGIST_MERCHANT_ADMIN) == null) {
            throw new SystemException("请先填完整注册信息，在进行上传审核图片操作！");
        }
        Admin admin = (Admin) request.getSession().getAttribute(REGIST_MERCHANT_ADMIN);
        Merchant merchant = (Merchant) request.getSession().getAttribute(REGIST_MERCHANT);

        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("adminCode", admin.getAdminCode());
        if (this.adminService.getRecordTotal(queryParams) > 0) {
            throw new SystemException("此账号已经存在，可直接登录");
        }

        if (StringUtils.isEmpty(merchantImg.getCoverImg().getImgInfoId())) {
            throw new StatusCodeException("商家头像不能为空", 500);
        }
        if (StringUtils.isEmpty(merchantImg.getHeaderImg().getImgInfoId())) {
            throw new StatusCodeException("店铺的顶部图不能为空", 500);
        }
        merchant.setCoverImg(merchantImg.getCoverImg());
        merchant.setHeaderImg(merchantImg.getHeaderImg());
        /*身份证照片*/
        merchant.setOpraterIDPhoto(merchantImg.getOrgPhoto());
        merchant.setOrgPhoto(merchantImg.getOrgPhoto());
        merchant.setBusLicePhoto(merchantImg.getBusLicePhoto());
        merchant.setApplyLetterPhoto(merchantImg.getApplyLetterPhoto());
        merchant.setExaminState(Constant.EXAMINE_STATE_SUBMITED);
        merchant.setCreateDate(new Date());
        merchant.setOrgCode(merchant.getLicenseCode());
        merchant.setTaxRegCode(merchant.getLicenseCode());
        merchant.setUpdateDate(new Date().getTime());

        admin.setPermit(true);
        admin.setAdminPwd(MD5Utils.getMD5Msg(admin.getAdminPwd()));
        admin.setMerchant(merchant);
        admin.setSuperAdmin(false);
        this.merchantService.addMerchantAndAdmin(merchant, admin);
        request.getSession().removeAttribute(REGIST_MERCHANT_ADMIN);
        request.getSession().removeAttribute(REGIST_MERCHANT);
        return executeResult();
    }

    @RequestMapping(value = "merchant/rigistAccount/fiveSecondsJump")
    public String fiveSecondsPage() {
        return "fiveSecondsJump";
    }

}
