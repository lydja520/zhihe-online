<%@ page import="cn.com.zhihetech.online.bean.Merchant" %>
<%@ page import="cn.com.zhihetech.online.commons.Constant" %>
<%@ page import="cn.com.zhihetech.online.bean.GoodsAttributeSet" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="cn.com.zhihetech.online.bean.ShoppingCenter" %>
<%@ page import="cn.com.zhihetech.online.bean.FeaturedBlock" %>
<%@ page import="java.util.Set" %>
<%--
  Created by IntelliJ IDEA.
  User: ydc
  Date: 16-6-8
  Time: 下午4:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>商家信息修改</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <script type="text/javascript" src="${basePath}/static/core/app.js"></script>
    <script type="text/javascript">
        app.setBasePath('${basePath}');
        app.setup();
    </script>
    <link rel="stylesheet" href="${basePath}/static/bootstrap/css/bootstrapValidator.css">
    <link rel="stylesheet" href="${basePath}/static/bootstrap/css/bootstrap.min.css">

    <script type="text/javascript" src="${basePath}/static/core/jquery.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/core/jquery.form.js"></script>
    <script type="text/javascript" src="${basePath}/static/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/bootstrap/js/bootstrapValidator.min.js"></script>
    <script>
        $(function () {
            $('#submit_btn').click(function () {
                $('#editForm').ajaxSubmit({
                    url: '${basePath}/admin/api/merchant/editMerchantRegisterInfoFormByAdmin',
                    type: 'post',
                    dataType: 'text',
                    success: function (info) {
                        info = $.parseJSON(info);
                        if (info.success) {
                            $('#alert-msg').html("修改成功");
                            $('#alert-modal').modal({});
                        }
                        if (!info.success) {
                            $('#alert-msg').html(info.msg);
                            $('#alert-modal').modal({});
                        }
                    },
                    timeout: 3000
                });
            });
        });

        function chooseOne(value) {
            if (value != 'featuredBlock') {
                $('#featuredBlock').attr('disabled', 'true');
            } else {
                $('#featuredBlock').removeAttr('disabled');
            }
        }
    </script>
    <style type="text/css">
        ul, li {
            margin: 0px;
            padding: 0px;
            list-style: none;
        }
    </style>
</head>
<body>
<div class="container">
    <h2 id="edit_title"></h2>
    <!--基本信息表单-->
    <form role="form" id="editForm" method="post">
        <%
            Merchant merchant = (Merchant) request.getAttribute("merchant");
        %>
        <!--商家基本信息-->
        <fieldset style="margin-top: 30px">
            <input name="merchantId" type="hidden" value="${merchant.merchantId}">
            <legend><span style="font-size:14px;color: #67B168;font-weight: bold">商家基本信息</span></legend>
            <div class="row">
                <div class="col-xs-6">
                    <div class="form-group">
                        <label for="merchName"><span style="color: red;font-weight: normal">* </span>商家名称</label>
                        <input type="text" class="form-control" id="merchName" name="merchName"
                               value="${requestScope.merchant.merchName}">
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="form-group">
                        <label for="merchTell"><span
                                style="color: red;font-weight: normal">* </span>企业联系电话（固话请加区号）</label>
                        <input type="text" class="form-control" id="merchTell" name="merchTell"
                               value="${requestScope.merchant.merchTell}">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-6">
                    <div class="form-group">
                        <label for="address"><span style="color: red;font-weight: normal">* </span>详细地址</label>
                        <input type="text" class="form-control" id="address" name="address"
                               value="${requestScope.merchant.address}">
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="form-group">
                        <label for="emplyCount"><span style="color: red;font-weight: normal">* </span>企业规模</label>
                        <input type="number" class="form-control" id="emplyCount" name="emplyCount"
                               value="${requestScope.merchant.emplyCount}">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-6">
                    <div class="form-group">
                        <label for="licenseCode">工商执照注册码<span
                                style="color: red;font-weight: normal"> (选填)</span></label>
                        <input type="text" class="form-control" id="licenseCode" name="licenseCode"
                               value="${requestScope.merchant.licenseCode}">
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="form-group">
                        <label for="alipayCode">支付宝账号<span style="color: red;font-weight: normal"> (选填)</span></label>
                        <input type="text" class="form-control" id="alipayCode" name="alipayCode"
                               value="${requestScope.merchant.alipayCode}">
                    </div>
                </div>
            </div>
        </fieldset>
        <!--商家经营范围-->
        <fieldset>
            <legend><span style="font-weight: bold;font-size:14px;color: #67B168;">经营范围</span></legend>
            <div class="row">
                <div class="col-md-12">
                    <div class="form-group">
                        <label><span style="color: red;font-weight: normal">* </span>经营范围（可多选）</label>

                        <div style="border: solid thin #CCCCCC;max-height: 100px;overflow-y: auto;">
                            <ul>
                                <%
                                    List<GoodsAttributeSet> goodsAttributeSets = (List<GoodsAttributeSet>) request.getAttribute("goodsAttSets");
                                    Set<GoodsAttributeSet> goodsAttributeSetSets = merchant.getCategories();
                                    List<GoodsAttributeSet> _goodsAttributeSets = new ArrayList<>(goodsAttributeSetSets);
                                    for (GoodsAttributeSet goodsAttributeSet : goodsAttributeSets) {
                                        boolean flag = false;
                                        for (GoodsAttributeSet _goodsAttributeSet : _goodsAttributeSets) {
                                            if (goodsAttributeSet.getGoodsAttSetId().equals(_goodsAttributeSet.getGoodsAttSetId())) {
                                                flag = true;
                                                break;
                                            }
                                        }
                                        if (flag) {
                                %>
                                <li class="col-md-3 col-sm-6">
                                    <input type="checkbox" value="<%=goodsAttributeSet.getGoodsAttSetId()%>" checked
                                           name="goodsAttSet"><%=goodsAttributeSet.getGoodsAttSetName()%>
                                </li>
                                <%
                                } else {
                                %>
                                <li class="col-md-3 col-sm-6">
                                    <input type="checkbox" value="<%=goodsAttributeSet.getGoodsAttSetId()%>"
                                           name="goodsAttSet"><%=goodsAttributeSet.getGoodsAttSetName()%>
                                </li>
                                <%
                                        }
                                    }
                                %>
                                <div class="clearfix"></div>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </fieldset>
        <!--特色街区，购物中心，特色店-->
        <fieldset style="margin-top: 20px">
            <legend><span style="font-weight: bold;font-size:14px;color: #67B168;">实体店所属类型</span></legend>
            <%
                List<ShoppingCenter> shoppingCenterList = (List<ShoppingCenter>) request.getAttribute("shoppingCenterList");
                List<FeaturedBlock> featuredBlockList = (List<FeaturedBlock>) request.getAttribute("featuredBlockList");
                /*if (merchant.getShoppingCenter() != null && !StringUtils.isEmpty(merchant.getShoppingCenter().getScId())) {
                    String shoppingCenterId = merchant.getShoppingCenter().getScId();*/
            %>
            <%--
            <div class="row">
                <div class="col-xs-6">
                    <div class="btn-group" style="margin: 0px auto 15px auto">
                        <input type="radio" name="chooseAdressType" value="inShoppingMall"
                               onclick="chooseOne('button1')"
                               checked/><label>购物中心</label>
                        <input type="radio" name="chooseAdressType" value="inStreet"
                               onclick="chooseOne('button2')"/><label>特色街区</label>
                        <input type="radio" name="chooseAdressType" value="specialtyStores"
                               onclick="chooseOne('button3')"/><label>特色店</label>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-6">
                    <div class="form-group">
                        <label><span style="color: red;font-weight: normal">* </span>选择所在的购物中心</label>
                        <select id="inShoppingMall" class="form-control" style="font-size: 12px"
                                name="shoppingCenter.scId">
                            <%
                                for (ShoppingCenter shoppingCenter : shoppingCenterList) {
                                    if (shoppingCenter.getScId().equals(shoppingCenterId)) {
                            %>
                            <option value="<%=shoppingCenter.getScId()%>" selected><%=shoppingCenter.getScName()%>
                            </option>
                            <%
                                    continue;
                                }
                            %>
                            <option value="<%=shoppingCenter.getScId()%>"><%=shoppingCenter.getScName()%>
                            </option>
                            <%
                                }
                            %>
                        </select>
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="form-group">
                        <label><span style="color: red;font-weight: normal">* </span>选择所在的街区</label>
                        <select id="inStreet" class="form-control" style="font-size: 12px" name="featuredBlock.fbId"
                                disabled>
                            <c:forEach items="${featuredBlockList}" var="featuredBlock">
                                <option value="${featuredBlock.fbId}">${featuredBlock.fbName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>
            --%>
            <%
                if (merchant.getStoreType() == Merchant.StoreType.featuredBlock) {
                    String featuredBlockId = merchant.getFeaturedBlock().getFbId();
            %>
            <div class="row">
                <div class="col-xs-6">
                    <div class="btn-group" style="margin: 0px auto 15px auto">
                        <%--<input type="radio" name="chooseAdressType" value="inShoppingMall"
                               onclick="chooseOne('button1')"/><label>购物中心</label>--%>
                        <label>
                            <input type="radio" name="storeType" value="featuredBlock" checked
                                   onclick="chooseOne(this.value)"/>特色街区
                        </label>
                        &nbsp;&nbsp;
                        <label>
                            <input type="radio" name="storeType" value="featuredStore"
                                   onclick="chooseOne(this.value)"/>特色店
                        </label>
                        &nbsp;&nbsp;
                        <label>
                            <input type="radio" name="storeType" value="vipStore"
                                   onclick="chooseOne(this.value)"/>VIP专区
                        </label>
                    </div>
                </div>
            </div>
            <div class="row">
                <%--
                <div class="col-xs-6">
                    <div class="form-group">
                        <label><span style="color: red;font-weight: normal">* </span>选择所在的购物中心</label>
                        <select id="inShoppingMall" class="form-control" style="font-size: 12px" disabled
                                name="shoppingCenter.scId">
                            <c:forEach items="${shoppingCenterList}" var="shoppingCenter">
                                <option value="${shoppingCenter.scId}">${shoppingCenter.scName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                --%>
                <div class="col-xs-6">
                    <div class="form-group">
                        <label><span style="color: red;font-weight: normal">* </span>选择所在的街区</label>
                        <select id="featuredBlock" class="form-control" style="font-size: 12px"
                                name="featuredBlock.fbId">
                            <%
                                for (FeaturedBlock featuredBlock : featuredBlockList) {
                                    if (featuredBlock.getFbId().equals(featuredBlockId)) {
                            %>
                            <option value="<%=featuredBlock.getFbId()%>" selected><%=featuredBlock.getFbName()%>
                            </option>
                            <%
                                    continue;
                                }
                            %>
                            <option value="<%=featuredBlock.getFbId()%>"><%=featuredBlock.getFbName()%>
                            </option>
                            <%
                                }
                            %>
                        </select>
                    </div>
                </div>
            </div>
            <%
            } else if (merchant.getStoreType() == Merchant.StoreType.featuredStore) {
            %>
            <div class="row">
                <div class="col-xs-6">
                    <div class="btn-group" style="margin: 0px auto 15px auto">
                        <%--<input type="radio" name="chooseAdressType" value="inShoppingMall"
                               onclick="chooseOne('button1')"/><label>购物中心</label>--%>
                        <label>
                            <input type="radio" name="storeType" value="featuredBlock"
                                   onclick="chooseOne(this.value)"/>特色街区
                        </label>
                        &nbsp;&nbsp;
                        <label>
                            <input type="radio" name="storeType" value="featuredStore" checked
                                   onclick="chooseOne(this.value)"/>特色店
                        </label>
                        &nbsp;&nbsp;
                        <label>
                            <input type="radio" name="storeType" value="vipStore"
                                   onclick="chooseOne(this.value)"/>VIP店
                        </label>
                    </div>
                </div>
            </div>
            <div class="row">
                <%--
                <div class="col-xs-6">
                    <div class="form-group">
                        <label><span style="color: red;font-weight: normal">* </span>选择所在的购物中心</label>
                        <select id="inShoppingMall" class="form-control" style="font-size: 12px" disabled
                                name="shoppingCenter.scId">
                            <c:forEach items="${shoppingCenterList}" var="shoppingCenter">
                                <option value="${shoppingCenter.scId}">${shoppingCenter.scName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                --%>
                <div class="col-xs-6">
                    <div class="form-group">
                        <label><span style="color: red;font-weight: normal">* </span>选择所在的街区</label>
                        <select id="featuredBlock" class="form-control" style="font-size: 12px"
                                name="featuredBlock.fbId" disabled>
                            <c:forEach items="${featuredBlockList}" var="featuredBlock">
                                <option value="${featuredBlock.fbId}">${featuredBlock.fbName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>
            <%
            } else if (merchant.getStoreType() == Merchant.StoreType.vipStore) {
            %>
            <div class="row">
                <div class="col-xs-6">
                    <div class="btn-group" style="margin: 0px auto 15px auto">
                        <%--<input type="radio" name="chooseAdressType" value="inShoppingMall"
                               onclick="chooseOne('button1')"/><label>购物中心</label>--%>
                        <label>
                            <input type="radio" name="storeType" value="featuredBlock"
                                   onclick="chooseOne(this.value)"/>特色街区
                        </label>
                        &nbsp;&nbsp;
                        <label>
                            <input type="radio" name="storeType" value="featuredStore"
                                   onclick="chooseOne(this.value)"/>特色店
                        </label>
                        &nbsp;&nbsp;
                        <label>
                            <input type="radio" name="storeType" value="vipStore" checked
                                   onclick="chooseOne(this.value)"/>VIP店
                        </label>
                    </div>
                </div>
            </div>
            <div class="row">
                <%--
                <div class="col-xs-6">
                    <div class="form-group">
                        <label><span style="color: red;font-weight: normal">* </span>选择所在的购物中心</label>
                        <select id="inShoppingMall" class="form-control" style="font-size: 12px" disabled
                                name="shoppingCenter.scId">
                            <c:forEach items="${shoppingCenterList}" var="shoppingCenter">
                                <option value="${shoppingCenter.scId}">${shoppingCenter.scName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                --%>
                <div class="col-xs-6">
                    <div class="form-group">
                        <label><span style="color: red;font-weight: normal">* </span>选择所在的街区</label>
                        <select id="featuredBlock" class="form-control" style="font-size: 12px"
                                name="featuredBlock.fbId" disabled>
                            <c:forEach items="${featuredBlockList}" var="featuredBlock">
                                <option value="${featuredBlock.fbId}">${featuredBlock.fbName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>
            <%
                }
            %>
        </fieldset>
        <!--商家详细信息-->
        <fieldset>
            <legend><span style="font-size:14px;color: #67B168;font-weight: bold">详细信息</span></legend>
            <div class="row">
                <div class="col-md-12">
                    <div class="form-group">
                        <label for="merchantDetails">商家简介(建议100字以内)<span
                                style="color: red;font-weight: normal">(选填)</span></label>
                        <textarea class="form-control"
                                  id="merchantDetails"
                                  name="merchantDetails"
                                  rows="3">${requestScope.merchant.merchantDetails}</textarea>
                    </div>
                </div>
            </div>
        </fieldset>
        <!--联系人信息-->
        <fieldset>
            <legend><span style="font-size:14px;color: #67B168;font-weight: bold">联系人信息</span></legend>
            <div class="row">
                <div class="col-xs-6">
                    <div class="form-group">
                        <label for="contactName"><span style="color: red;font-weight: normal">* </span>联系人</label>
                        <input type="text" class="form-control" id="contactName" name="contactName"
                               value="${requestScope.merchant.contactName}">
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="form-group">
                        <label for="contactID">身份证号<span style="color: red;font-weight: normal">(选填)</span></label>
                        <input type="text" class="form-control" id="contactID" name="contactID"
                               value="${requestScope.merchant.contactID}">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-6">
                    <div class="form-group">
                        <label for="contactMobileNO"><span style="color: red;font-weight: normal">* </span>手机号码</label>
                        <input type="text" class="form-control" id="contactMobileNO" name="contactMobileNO"
                               value="${requestScope.merchant.contactMobileNO}">
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="form-group">
                        <label for="contactEmail">邮箱<span style="color: red;font-weight: normal">(选填)</span></label>
                        <input type="email" class="form-control" id="contactEmail" name="contactEmail"
                               value="${requestScope.merchant.contactEmail}">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-6">
                    <div class="form-group">
                        <label for="contactPartAndPositon"><span
                                style="color: red;font-weight: normal">* </span>联系人部门与职位</label>
                        <input type="text" class="form-control" id="contactPartAndPositon"
                               name="contactPartAndPositon"
                               value="${requestScope.merchant.contactPartAndPositon}">
                    </div>
                </div>
            </div>
        </fieldset>
    </form>
    <div style="margin-bottom: 20px;text-align: center">
        <button id="submit_btn" type="submit" class="btn btn-primary">确认修改</button>
    </div>
</div>

<div id="alert-modal" class="modal fade" data-title-target="#alert-title" data-msg-target="#alert-msg">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <p id="alert-msg"></p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">确定</button>
            </div>
        </div>
    </div>
</div>

</body>
</html>
