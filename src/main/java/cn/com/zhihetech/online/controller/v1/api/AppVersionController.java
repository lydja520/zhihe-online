package cn.com.zhihetech.online.controller.v1.api;

import cn.com.zhihetech.online.bean.AppVersion;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.service.IAppVersionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2016/3/14.
 */
@Controller
public class AppVersionController extends ApiController {

    @Resource(name = "appVersionService")
    private IAppVersionService appVersionService;

    @ResponseBody
    @RequestMapping(value = "app/lastVersion")
    public ResponseMessage getLastAppVersion() {
        AppVersion appVersion = this.appVersionService.getLastVersion();
        if (appVersion != null) {
            return executeResult(ResponseStatusCode.SUCCESS_CODE, "find new version !", appVersion);
        }
        return executeResult(ResponseStatusCode.PAGE_NOT_FOUND_CODE, "not found app version info!");
    }
}
