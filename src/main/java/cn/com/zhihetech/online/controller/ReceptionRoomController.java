package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.GoodsAttributeSet;
import cn.com.zhihetech.online.bean.ReceptionRoom;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.service.IReceptionRoomService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by ShenYunjie on 2016/4/5.
 */
@Controller
public class ReceptionRoomController extends SupportController {

    @Resource(name = "receptionRoomService")
    private IReceptionRoomService receptionRoomService;

    @RequestMapping("admin/receptionRoom/index")
    public String indexPage() {
        return "admin/receptionRoom";
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/receptionRoom/add", method = RequestMethod.POST)
    public ResponseMessage addRoom(ReceptionRoom receptionRoom) {
        this.receptionRoomService.add(receptionRoom);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/receptionRooms")
    public PageData<ReceptionRoom> getPageData(HttpServletRequest request) {
        IQueryParams queryParams = createQueryParams(request).andEqual("deleted", false);
        return this.receptionRoomService.getPageData(createPager(request), queryParams);
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/receptionRoom/update", method = RequestMethod.POST)
    public ResponseMessage updateRoomInfo(ReceptionRoom room) {
        this.receptionRoomService.update(room);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/receptionRoom/delete", method = RequestMethod.POST)
    public ResponseMessage deleteReceptionRoom(HttpServletRequest request, ReceptionRoom room) {
        this.receptionRoomService.delete(room);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/receptionRoom/list")
    public List<ReceptionRoom> getAllAbleRooms(HttpServletRequest request) {
        IQueryParams queryParams = new GeneralQueryParams().andEqual("deleted", false);
        return this.receptionRoomService.getAllByParams(null, queryParams);
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/receptionRoom/attributes")
    public List<GoodsAttributeSet> getAllRoomAttrList() {
        return this.receptionRoomService.getAllRoomAttrIdAndName();
    }
}
