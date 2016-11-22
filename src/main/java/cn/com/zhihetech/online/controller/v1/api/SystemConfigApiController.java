package cn.com.zhihetech.online.controller.v1.api;

import cn.com.zhihetech.online.bean.ImgInfo;
import cn.com.zhihetech.online.bean.SystemConfig;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.service.ISystemConfigService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ShenYunjie on 2016/3/18.
 */
@Controller
public class SystemConfigApiController extends ApiController {

    @Resource(name = "systemConfigService")
    private ISystemConfigService systemConfigService;

    /**
     * 获取App启动页图片
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "app/startImg")
    public ResponseMessage getAppStartImg() {
        ImgInfo imgInfo = this.systemConfigService.getAppStartImg();
        if (imgInfo == null) {
            return executeResult(ResponseStatusCode.PAGE_NOT_FOUND_CODE, "启动图片未设置");
        }
        return executeResult(ResponseStatusCode.SUCCESS_CODE, null, imgInfo);
    }

    /**
     * <h3>聊天人数最小基数</h3>
     * <p>
     * url:/api/app/chatRoomPersonCountConfig
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "app/chatRoomPersonCountConfig")
    public ResponseMessage getChatRoomPersonCountConfig() {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("configType", Constant.WEB_CHINESE_CHATROOM_PERSON_COUNT);
        List<SystemConfig> systemConfigs = this.systemConfigService.getAllByParams(null, queryParams);
        SystemConfig systemConfig = null;
        if (systemConfigs == null || systemConfigs.isEmpty()) {
            systemConfig = new SystemConfig();
            systemConfig.setConfigType(Constant.WEB_CHINESE_CHATROOM_PERSON_COUNT);
            systemConfig.setConfigName("聊天室人数设置");
            systemConfig.setConfigValue("30");
            systemConfig.setConfigDesc("当聊天室人数少于该值时，该值有效");
        } else {
            systemConfig = systemConfigs.get(0);
        }
        return executeResult(ResponseStatusCode.SUCCESS_CODE, "获取数据成功", systemConfig);

    }
}
