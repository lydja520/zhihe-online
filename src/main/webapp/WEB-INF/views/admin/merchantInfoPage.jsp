<%@ page import="cn.com.zhihetech.online.bean.Merchant" %>
<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2015/12/9
  Time: 11:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>商家详细信息</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <script type="text/javascript" src="${basePath}/static/core/app.js"></script>
    <script type="text/javascript">
        app.setBasePath('${basePath}');
        app.setup();
    </script>
    <style>
        body {
            margin: 0px;
            padding: 0px;
        }

        table {
            border: solid #add9c0;
            border-width: 1px;
            margin-top: 5px;
            margin-bottom: 20px;
        }

        #container {
            width: 90%;
            margin-left: auto;
            margin-right: auto;
        }

        table img {
            width: 100%;
            height: auto;;
        }

        td {
            width: 33%;
            border-collapse: collapse;
            border: solid #add9c0;
            border-width: 0px 1px 1px 0px
        }

        table tr td {
            text-align: center;
            font-size: 12px;
            height: 26px;
        }

        .info_title {
            width: 250px;
            height: 30px;
            line-height: 30px;
            margin: 7px auto 7px auto;
            background-color: #67b168;
            color: #FFFFFF;
            font-size: 15px;
            font-weight: bold;
            border-radius: 15px;
            border: solid;
            border-color: green;
            border-width: 1px;
        }

        .title {
            color: #67b168;
            font-size: 14px;
            font-weight: bold;
            background-color: #edf4ff;
        }
    </style>
</head>
<body>
<div id="container">
    <div style="width: 80%;margin: 5px auto 0px auto">
        <img src="${requestScope.merchant.coverImg.url}"
             style="display:block;float:left;width:100px;height: 100px;border: solid;border-color: green;border-width:1px;border-radius: 50%">

        <div style="float: left;height: 100px;line-height: 100px;font-size: 18px;color:green;">
            &nbsp;&nbsp;${requestScope.merchant.merchName}</div>
        <div style="clear: both;"></div>
    </div>
    <table align="center" cellspacing="0px" cellpadding="0px" width="80%">
        <tr>
            <td colspan="3">
                <div class="info_title">基本信息</div>
            </td>
        </tr>
        <tr class="title">
            <td>商家名</td>
            <td>商家电话号码</td>
            <td>商家地址</td>
        </tr>
        <tr>
            <td>${requestScope.merchant.merchName}</td>
            <td>${requestScope.merchant.merchTell}</td>
            <td>${requestScope.merchant.address}</td>
        </tr>
        <tr class="title">
            <td>企业规模（员工人数）</td>
            <td>组织机构代码</td>
            <td>工商执照注册码</td>
        </tr>
        <tr>
            <td>${requestScope.merchant.emplyCount}人</td>
            <td>${requestScope.merchant.orgCode}</td>
            <td>${requestScope.merchant.licenseCode}</td>
        </tr>
        <tr class="title">
            <td>税务登记证号</td>
            <td>支付宝账号</td>
            <td>入驻时间</td>
        </tr>
        <tr>
            <td>${requestScope.merchant.taxRegCode}</td>
            <td>${requestScope.merchant.alipayCode}</td>
            <td>${requestScope.merchant.createDate}</td>
        </tr>
        <tr>
            <td colspan="3">
                <div class="info_title">实体店所属类型</div>
            </td>
        </tr>
        <tr>
            <td colspan="3">
                <%
                    Merchant merchant = (Merchant) request.getAttribute("merchant");
                    if (merchant.getStoreType() == Merchant.StoreType.shoppingCenter) {
                %>
                购物中心： ${requestScope.merchant.shoppingCenter.scName}
                <%
                } else if (merchant.getStoreType() == Merchant.StoreType.featuredBlock) {
                %>
                特色街区： ${requestScope.merchant.featuredBlock.fbName}
                <%
                } else if (merchant.getStoreType() == Merchant.StoreType.featuredStore) {
                %>
                优+店
                <%
                } else if (merchant.getStoreType() == Merchant.StoreType.vipStore) {
                %>
                vip店铺
                <%
                    }
                %>
            </td>
        </tr>
        <tr>
            <td colspan="3">
                <div class="info_title">经营范围</div>
            </td>
        </tr>
        <tr>
            <td colspan="3">
                <c:forEach items="${requestScope.merchant.categories}" var="categorie">
                    ${categorie.goodsAttSetName}、
                </c:forEach>
            </td>
        </tr>
        <tr>
            <td colspan="3">
                <div class="info_title">商家简介</div>
            </td>
        </tr>

        <td colspan="3">
            <p style="width: 80%;margin-left: auto;margin-right: auto;line-height: 20px;text-align: left">${requestScope.merchant.merchantDetails}</p>
        </td>
        <tr>
            <td colspan="3">
                <div class="info_title">运营者（联系人）信息</div>
            </td>
        </tr>
        <tr class="title">
            <td>商家联系人姓名</td>
            <td>联系人部门与职位</td>
            <td>联系人手机号码</td>
        </tr>
        <tr>
            <td>${requestScope.merchant.contactName}</td>
            <td>${requestScope.merchant.contactPartAndPositon}</td>
            <td>${requestScope.merchant.contactMobileNO}</td>
        </tr>
        <tr class="title">
            <td>联系人电子邮箱</td>
            <td>联系人身份证号</td>
            <td></td>
        </tr>
        <tr>
            <td>${requestScope.merchant.contactEmail}</td>
            <td>${requestScope.merchant.contactID}</td>
            <td></td>
        </tr>
        <tr>
            <td colspan="3">
                <div class="info_title">商家信息图片(图片)</div>
            </td>
        </tr>
        <tr class="title">
            <td colspan="3">商家顶部图</td>
        </tr>
        <tr style="height: 100px">
            <td colspan="3">
                <div class="img_div"
                     style="width:100%;height:150px;margin-left:auto;margin-right:auto;background-image: url('${requestScope.merchant.headerImg.url}');background-position: center">
                </div>
            </td>
        </tr>
        <tr>
            <td colspan="3">
                <div class="info_title">注册时提供的相关材料(图片)</div>
            </td>
        </tr>
<%--        <tr class="title">
            <td colspan="3">运营者手持身份证照片</td>
        </tr>
        <tr>
            <td colspan="3"><img src="${requestScope.merchant.opraterIDPhoto.url}" style="width: 700px"></td>
        </tr>--%>
        <tr class="title">
            <td colspan="3">组织机构代码证原件照片</td>
        </tr>
        <tr>
            <td colspan="3"><img src="${requestScope.merchant.orgPhoto.url}" style="width: 700px"></td>
        </tr>
        <tr class="title">
            <td colspan="3">工商营业执照原件照片</td>
        </tr>
        <tr>
            <td colspan="3"><img src="${requestScope.merchant.busLicePhoto.url}" style="width: 700px"></td>
        </tr>
        <tr class="title">
            <td colspan="3">加盖公章的申请认证公函</td>
        </tr>
        <tr>
            <td colspan="3"><img src="${requestScope.merchant.applyLetterPhoto.url}" style="width: 700px"></td>
        </tr>
    </table>
</div>
</body>
</html>
