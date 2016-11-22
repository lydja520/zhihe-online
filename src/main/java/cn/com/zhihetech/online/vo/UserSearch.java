package cn.com.zhihetech.online.vo;

/**
 * Created by Administrator on 2016/4/22.
 */
public class UserSearch {
    private String searchPhone;
    private String searchInvitCode;
    private String initTime;
    private String endTime;

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getInitTime() {
        return initTime;
    }

    public void setInitTime(String initTime) {
        this.initTime = initTime;
    }

    public String getSearchInvitCode() {
        return searchInvitCode;
    }

    public void setSearchInvitCode(String searchInvitCode) {
        this.searchInvitCode = searchInvitCode;
    }

    public String getSearchPhone() {
        return searchPhone;
    }

    public void setSearchPhone(String searchPhone) {
        this.searchPhone = searchPhone;
    }
}
