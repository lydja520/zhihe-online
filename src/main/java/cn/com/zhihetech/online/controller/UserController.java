package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.LoginJournal;
import cn.com.zhihetech.online.bean.User;
import cn.com.zhihetech.online.service.ILoginJournalService;
import cn.com.zhihetech.online.service.IUserService;
import cn.com.zhihetech.online.util.DateUtils;
import cn.com.zhihetech.online.vo.UserSearch;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/19.
 */
@Controller
public class UserController extends SupportController {
    @Resource(name = "userService")
    private IUserService userService;
    @Resource(name = "loginJournalService")
    private ILoginJournalService loginJournalService;

    @RequestMapping(value = "admin/user")
    public String userManagementPage() {
        return "admin/user";
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/user/list")
    public PageData<User> getUserPageData(HttpServletRequest request, UserSearch userSearch) throws ParseException {
        IQueryParams queryParams = this.createQueryParams(request);
        return this.userService.getRelationSearchEntitys(this.createPager(request), queryParams, userSearch);
    }

    /*==========用户统计===========*/
    @ResponseBody
    @RequestMapping(value = "admin/api/user/statistic")
    public Map<String, Integer> userStatistic() {
        Map<String, Integer> statList = new HashMap();
        IQueryParams queryParams = new GeneralQueryParams();
        IQueryParams queryParams1 = new GeneralQueryParams();

        //获取当天查询参数
        Date stratDate = DateUtils.getStartDateTimeWithDate(new Date());
        Date endDate = DateUtils.getEndDateTimeWithDate(new Date());

        queryParams.andMoreAndEq("createDate", stratDate)
                .andLessAndEq("createDate", endDate);
        queryParams1.andEqual("loginType", LoginJournal.LoginType.user)
                .andMoreAndEq("loginDate", stratDate)
                .andLessAndEq("loginDate", endDate);

        Integer userTotal = this.userService.getProperty("distinct userPhone", null, null).size();
        Integer newUserTotal = this.userService.getProperty("distinct userPhone", null, queryParams).size();
        Integer activeUser = this.loginJournalService.getProperty("distinct userCode", null, queryParams1).size();
        statList.put("userTotal", userTotal);
        statList.put("newUserTotal", newUserTotal);
        statList.put("activeUser", activeUser);
        return statList;

    }


}
