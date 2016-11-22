package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.SystemConfig;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.ISystemConfigService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ydc on 16-8-12.
 */
@Controller
@RequestMapping(value = "/admin")
public class SystemConfigController extends SupportController {

    @Resource(name = "systemConfigService")
    private ISystemConfigService systemConfigService;

    /**
     * 系统设置页面
     *
     * @return
     */
    @RequestMapping(value = "/systemConfig")
    public String systemConfigPage() {
        return "admin/systemConfig";
    }

    /**
     * 系统设置数据列表
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/systemConfig/list")
    public PageData<SystemConfig> getSystemConfigList(HttpServletRequest request) {
        return this.systemConfigService.getPageData(this.createPager(request), this.createQueryParams(request));
    }

    /**
     * 系统设置
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/systemConfig/edit")
    public ResponseMessage addChatRoomPersonCountConfig(String systemConfigId, String configValue, String configDesc) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("configId", systemConfigId);
        Map<String, Object> paramAndValue = new HashMap<>();
        paramAndValue.put("configValue", configValue);
        paramAndValue.put("configDesc", configDesc);
        int count = this.systemConfigService.executeUpdate(paramAndValue, queryParams);
        if (count <= 0) {
            throw new SystemException("更新该条数据失败，请刷新后再试！");
        }
        return executeResult();
    }


}
