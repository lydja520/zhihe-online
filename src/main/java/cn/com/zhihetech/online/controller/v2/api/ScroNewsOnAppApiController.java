package cn.com.zhihetech.online.controller.v2.api;

import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.service.IScroNewOnAppService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by YangDaiChun on 2016/8/22.
 */
@Controller
public class ScroNewsOnAppApiController extends V2ApiController {

    @Resource(name = "scroNewOnAppService")
    private IScroNewOnAppService scroNewOnAppService;

    /**
     * <h3>获取app首页滚动消息</h3>
     * url: api/v2/scrollNews/list
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "scrollNews/list")
    public ResponseMessage getScroNewsLisr(HttpServletRequest request) {
        List<cn.com.zhihetech.online.bean.ScroNewsOnApp> scroNewsOnApps = this.scroNewOnAppService.getAllByParams(null, this.createQueryParams(request));
        return executeResult(ResponseStatusCode.SUCCESS_CODE, "获取数据成功", scroNewsOnApps);
    }
}
