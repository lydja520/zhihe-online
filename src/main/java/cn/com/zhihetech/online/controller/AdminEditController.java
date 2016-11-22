package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.Admin;
import cn.com.zhihetech.online.bean.ImgInfo;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IAdminService;
import cn.com.zhihetech.util.common.MD5Utils;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/21.
 */
@Controller
public class AdminEditController extends SupportController {
    @Resource(name = "adminService")
    private IAdminService adminService;

    @RequestMapping(value = "admin/adminEdit")
    public String getAdminPage(HttpServletRequest request){
        if(!StringUtils.isEmpty(this.getCurrentMerchatId(request))){
            return "admin/adminEditToMerchant";
        }
        return "admin/adminEdit";
    }

    @RequestMapping(value = "admin/adminEditToMerchant")
    public String getAdminPageToMerch(HttpServletRequest request){
        return "admin/adminEditToMerchant";
    }

    /**
     * admin信息列表
     * @param request
     * @param adminCode
     * @param chatNickName
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/admin/portraitList")
    public PageData<Admin> getAdminPageData(HttpServletRequest request,String adminCode,String chatNickName,String merchantName) {
        IQueryParams queryParams = this.createQueryParams(request);
        String merchantId = getCurrentMerchatId(request);
        if(!StringUtils.isEmpty(merchantId)){
            queryParams.andEqual("merchant.merchantId",merchantId);
        }
        if(!StringUtils.isEmpty(adminCode)){
            queryParams.andAllLike("adminCode",adminCode);
        }
        if(!StringUtils.isEmpty(chatNickName)){
            queryParams.andAllLike("chatNickName",chatNickName);
        }
        if(!StringUtils.isEmpty(merchantName)){
            queryParams.andAllLike("merchant.merchName",merchantName);
        }
        queryParams.andEqual("merchant.examinState", Constant.EXAMINE_STATE_EXAMINED_OK);//约束已审核通过的
        queryParams.andEqual("official",false);//显示不是平台管理员账号
        return this.adminService.getPageData(this.createPager(request), queryParams);
    }

    /**
     * 修改头像和昵称
     * @param adminId
     * @param imgInfoId
     * @param chatNickName
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/admin/editPortraitName")
    public ResponseMessage adminPortraitEdit(String adminId, String imgInfoId,String chatNickName) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("adminId", adminId);
        Map<String, Object> paramAndValue = new HashMap<>();
        ImgInfo portrait = new ImgInfo();
        portrait.setImgInfoId(imgInfoId);
        paramAndValue.put("portrait", portrait);
        paramAndValue.put("chatNickName",chatNickName);
        this.adminService.executeAdminPortrait(paramAndValue, queryParams);
        return executeResult();
    }


    /**
     * 更改账号
     * @param adminId
     * @param newAdminCode
     * @return
     */
    @ResponseBody
    @RequestMapping(value ="admin/api/adminEdit/adminCodeEdit")
    public ResponseMessage adminCodeEdit(String adminId,String newAdminCode){
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("adminCode",newAdminCode);

        Admin currentAdmin = this.adminService.getById(adminId);
        if(currentAdmin.getAdminCode().equals(newAdminCode)){
            throw new SystemException("未更改账号");
        }

        List<Admin> otherAdmin = this.adminService.getAllByParams(null,queryParams);
        Admin admin = null;
        if(otherAdmin.size()>0){
            throw new SystemException("账号已存在");
        }else{
            admin = currentAdmin;
            admin.setAdminCode(newAdminCode);
        }
        this.adminService.update(admin);
        return executeResult();

    }

    /**
     * 重置admin密码为123456
     * @param adminId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/adminEdit/resetadminPwd")
    public ResponseMessage resetadminPwd(String adminId){
        Admin currentAdmin = this.adminService.getById(adminId);
        String resetPwd= MD5Utils.getMD5Msg("123456");
        currentAdmin.setAdminPwd(resetPwd);
        this.adminService.update(currentAdmin);
        return executeResult();
    }

    /**
     * admin禁用或启用
     * @param adminId
     * @param permitState
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/adminEdit/adminPermitEdit")
    public ResponseMessage permitEdit(String adminId,boolean permitState){
        Admin currentAdmin = this.adminService.getById(adminId);
        if(permitState==currentAdmin.isPermit()){
            throw new SystemException("请勿重复修改");
        }
        currentAdmin.setPermit(permitState);
        this.adminService.update(currentAdmin);
        return executeResult();
    }

}
