package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.service.IUtilService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by YangDaiChun on 2016/7/26.
 */
@Controller
public class UtilsController extends SupportController {

    @Resource(name = "utilService")
    private IUtilService utilService;

    @RequestMapping("utils/option")
    public String addSkuIndexPage() {
        return "admin/utils/option";
    }

    @ResponseBody
    @RequestMapping("utils/option/importSku")
    public ResponseMessage importDefaultSku() {
/*        this.utilService.initSku();*/
        return executeResult();
    }

    @ResponseBody
    @RequestMapping("utils/option/importGoodsCountInfo")
    public ResponseMessage importGoodsCountInfo() {
/*        this.utilService.initGoodsStockAndVolumeInfo();*/
        return executeResult();
    }

    @ResponseBody
    @RequestMapping("utils/option/importShoppingCartInfo")
    public ResponseMessage initShoppingCartInfo() {
/*        this.utilService.initShoppingCartInfo();*/
        return executeResult();
    }

}
