package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.LoginJournal;
import cn.com.zhihetech.online.service.ILoginJournalService;
import cn.com.zhihetech.online.util.DateUtils;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by Administrator on 2016/4/25.
 */
@Controller
public class LoginJournalController extends SupportController {
    @Resource
    ILoginJournalService loginJournalService;

    @RequestMapping(value = "admin/journal")
    public String pageIndex() {
        return "admin/journalinfo";
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/user/journal")
    public PageData<LoginJournal> getLoginsJournal(HttpServletRequest request, String initTime, String endTime) {
        IQueryParams queryParams = this.createQueryParams(request);
        queryParams.andEqual("loginType", LoginJournal.LoginType.user);
        /*===========时间段参数===========*/
        if (!cn.com.zhihetech.online.util.StringUtils.isEmpty(initTime)) {
            StringBuffer startTime = new StringBuffer(initTime);
            startTime.append(" 00:00:00");
            Date startDate = DateUtils.String2DateTime(startTime.toString());
            queryParams.andMoreAndEq("loginDate", startDate);
        }
        if (!cn.com.zhihetech.online.util.StringUtils.isEmpty(endTime)) {
            StringBuffer overTime = new StringBuffer(endTime);
            overTime.append(" 23:59:59");
            Date overDate = DateUtils.String2DateTime(overTime.toString());
            queryParams.andLessAndEq("loginDate", overDate);
        }
        /*============时间排序============*/
        if (!queryParams.sortContainsKey("loginDate")) {
            queryParams.sort("loginDate", cn.com.zhihetech.util.hibernate.Order.DESC);
        }
        return this.loginJournalService.getPageData(this.createPager(request), queryParams);

    }
}
