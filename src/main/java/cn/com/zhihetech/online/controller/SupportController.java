package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.Admin;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.exception.StatusCodeException;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.exception.SystemRuntimeException;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.LikeType;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 控制器基类，所有控制器都应继承与此类
 * Created by ShenYunjie on 2015/11/17.
 */
public abstract class SupportController implements Serializable {

    protected final static String PAGE_KEY = "page";
    protected final static String ROWS_KEY = "rows";

    protected final static String SEARCH_NAME_KEY = "searchName";
    protected final static String SEARCH_VALUE_KEY = "searchValue";
    protected final static String SORT_KEY = "sort";
    protected final static String ORDER_KEY = "order";

    public final static String CURRENT_ADMIN = "currentAdmin";
    public final static String CURRENT_ADMIN_ID = "currentAdminId";
    public final static String CURRENT_MERCHANT_ID = "currentMerchantId";

    protected Log log;

    public SupportController() {
        log = LogFactory.getLog(this.getClass());
    }

    /**
     * 根据请求获取当前登录的管理员
     *
     * @param request 用户请求
     * @return Admin当前登录管理员
     */
    public Admin getCurrentAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute(CURRENT_ADMIN) == null) {
            return null;
        }
        return (Admin) session.getAttribute(CURRENT_ADMIN);
    }

    /**
     * 设置当前登录管理员
     *
     * @param request 用户请求
     * @param admin   需要设置的管理员
     */
    public void setCurrentAdmin(HttpServletRequest request, Admin admin) {
        if (admin == null) {
            throw new SystemException("current loged admin not able null");
        }
        HttpSession session = request.getSession();
        session.setAttribute(CURRENT_ADMIN, admin);
        setCurrentAdminId(request, admin.getAdminId());
    }

    /**
     * 设置当前登录人员的ID
     *
     * @param request
     * @param adminId 当前登录管理人员ID
     */
    public void setCurrentAdminId(HttpServletRequest request, String adminId) {
        HttpSession session = request.getSession();
        session.setAttribute(CURRENT_ADMIN_ID, adminId);
    }

    /**
     * 获取当前登录人员的ID
     *
     * @param request
     * @return
     */
    public String getCurrentAdminId(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute(CURRENT_ADMIN_ID) == null) {
            return null;
        }
        return (String) session.getAttribute(CURRENT_ADMIN_ID);
    }

    /**
     * 设置当前登录商家的ID
     *
     * @param request
     * @param merchantId
     */
    public void setCurrentMerchatId(HttpServletRequest request, String merchantId) {
        HttpSession session = request.getSession();
        session.setAttribute(CURRENT_MERCHANT_ID, merchantId);
    }

    /**
     * 获取当前登录商家的ID
     *
     * @param request
     * @return
     */
    public String getCurrentMerchatId(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute(CURRENT_MERCHANT_ID) == null) {
            return null;
        }
        return (String) session.getAttribute(CURRENT_MERCHANT_ID);
    }

    /**
     * @param binder
     */
    @InitBinder
    public final void initBinder(WebDataBinder binder) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constant.DEFAULT_DATE_TIME_FORMAT);
        simpleDateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(simpleDateFormat, true));
    }

    /**
     * 异常处理
     *
     * @param request
     * @param response
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler
    public ResponseMessage handlerException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        Throwable throwable = ex.getCause() == null ? ex : ex.getCause();
        log.error(ex.getMessage(), throwable);
        if (ex instanceof StatusCodeException) {
            StatusCodeException exception = (StatusCodeException) ex;
            response.setStatus(200);
            return executeResult(exception.getCode(), false, ex.getMessage());
        }
        if (ex instanceof SystemRuntimeException) {
            response.setStatus(200);
            return executeResult(500, false, ex.getMessage());
        }
        response.setStatus(500);
        return new ResponseMessage(500, false, ex.getMessage());
    }

    /**
     * 根据一个RequestParam对象创建一个GenralQueryParams对象，并初始化查询对象值，并默认左右匹配
     *
     * @param request
     * @return
     */
    protected GeneralQueryParams createQueryParams(HttpServletRequest request) {
        return createQueryParams(request, LikeType.BOTH_LIKE);
    }

    protected GeneralQueryParams createQueryParams(HttpServletRequest request, GeneralQueryParams queryParams) {
        return createQueryParams(request, queryParams, LikeType.BOTH_LIKE);
    }

    protected GeneralQueryParams createQueryParams(HttpServletRequest request, GeneralQueryParams queryParams, LikeType likeType) {
        if (request == null) {
            return queryParams;
        }
        if (!StringUtils.isEmpty(request.getParameter(SEARCH_NAME_KEY))) {
            switch (likeType) {
                case LEFT_LIKE:
                    queryParams.andLLike(request.getParameter(SEARCH_NAME_KEY), StringUtils.ObjectToString(request.getParameter(SEARCH_VALUE_KEY)));
                    break;
                case RIGHT_LIKE:
                    queryParams.andRLike(request.getParameter(SEARCH_NAME_KEY), StringUtils.ObjectToString(request.getParameter(SEARCH_VALUE_KEY)));
                    break;
                default:
                    queryParams.andAllLike(request.getParameter(SEARCH_NAME_KEY), StringUtils.ObjectToString(request.getParameter(SEARCH_VALUE_KEY)));
            }
        }
        if (!StringUtils.isEmpty(request.getParameter(SORT_KEY))) {
            if (request.getParameter(ORDER_KEY).equalsIgnoreCase(Order.DESC.toString())) {
                queryParams.sort(request.getParameter(SORT_KEY), Order.DESC);
            } else {
                queryParams.sort(request.getParameter(SORT_KEY), Order.ASC);
            }
        }
        return queryParams;

    }

    /**
     * 根据一个RequestParam对象创建一个GenralQueryParams对象，并初始化查询对象值
     *
     * @param request
     * @param likeType 模糊查询模式（左匹配，右匹配，左右匹配）
     * @return
     */

    protected GeneralQueryParams createQueryParams(HttpServletRequest request, LikeType likeType) {
        GeneralQueryParams queryParams = new GeneralQueryParams();

        if (request == null) {
            return queryParams;
        }
        if (!StringUtils.isEmpty(request.getParameter(SEARCH_NAME_KEY))) {
            switch (likeType) {
                case LEFT_LIKE:
                    queryParams.andLLike(request.getParameter(SEARCH_NAME_KEY), StringUtils.ObjectToString(request.getParameter(SEARCH_VALUE_KEY)));
                    break;
                case RIGHT_LIKE:
                    queryParams.andRLike(request.getParameter(SEARCH_NAME_KEY), StringUtils.ObjectToString(request.getParameter(SEARCH_VALUE_KEY)));
                    break;
                default:
                    queryParams.andAllLike(request.getParameter(SEARCH_NAME_KEY), StringUtils.ObjectToString(request.getParameter(SEARCH_VALUE_KEY)));
            }
        }
        if (!StringUtils.isEmpty(request.getParameter(SORT_KEY))) {
            if (request.getParameter(ORDER_KEY).equalsIgnoreCase(Order.DESC.toString())) {
                queryParams.sort(request.getParameter(SORT_KEY), Order.DESC);
            } else {
                queryParams.sort(request.getParameter(SORT_KEY), Order.ASC);
            }
        }
        return queryParams;
    }

    /**
     * 根据用户请求对象创建一个Pager对象
     *
     * @param request
     * @return
     */
    protected Pager createPager(HttpServletRequest request) {
        Pager pager = new Pager();
        if (request == null) {
            return pager;
        }
        try {
            pager.setPage(Integer.parseInt(request.getParameter(PAGE_KEY)));
        } catch (Exception e) {
        }
        try {
            pager.setRows(Integer.parseInt(request.getParameter(ROWS_KEY)));
        } catch (Exception e) {
        }

        return pager;
    }

    protected ResponseMessage executeResult() {
        return executeResult("操作成功");
    }

    protected ResponseMessage executeResult(String msg) {
        return executeResult(ResponseStatusCode.SUCCESS_CODE, msg);
    }

    protected ResponseMessage executeResult(int code, String msg) {
        return executeResult(code, code == ResponseStatusCode.SUCCESS_CODE, msg);
    }

    protected ResponseMessage executeResult(int code, String msg, Object object) {
        ResponseMessage responseMessage = this.executeResult(code, msg);
        responseMessage.setAttribute(object);
        return responseMessage;
    }

    protected ResponseMessage executeResult(int code, boolean success, String msg) {
        ResponseMessage responseMessage = new ResponseMessage(code, success, msg);
        return responseMessage;
    }

}
