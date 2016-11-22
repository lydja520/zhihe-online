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
    <title></title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <script type="text/javascript" src="${basePath}/static/core/app.js"></script>
    <script type="text/javascript">
        app.setBasePath('${basePath}');
        app.setup();
    </script>
    <style>
        table {
            border: solid #add9c0;
            border-width: 1px;
            width:700px;
        }

        table img {
            width: 100%;
            height: auto;;
        }

        td {
            width: 50%;
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
            background-color: #67b168;
            color: #FFFFFF;
            font-size: 15px;
            font-weight: bold;
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
<table align="center" cellspacing="0px" cellpadding="0px">
    <div style="width:700px;margin: 5px auto 0px auto">
        <img src="${requestScope.merchant.coverImg.url}"
             style="display:block;float:left;width:50px;height: 50px;border: none;border-width:1px;border-radius: 50%">

        <div style="float: left;height: 50px;line-height: 50px;font-size: 18px;color:green;">
            &nbsp;&nbsp;${requestScope.merchant.merchName}</div>
        <div style="clear: both;"></div>
    </div>
    <tr>
        <td colspan="2" class="info_title">基本信息</td>
    </tr>
    <tr class="title">
        <td>商家名</td>
        <td>商家电话号码</td>
    </tr>
    <tr>
        <td>${requestScope.merchant.merchName}</td>
        <td>${requestScope.merchant.merchTell}</td>
    </tr>
    <tr class="title">
        <td>是否启用</td>
        <td>商家地址</td>
    </tr>
    <tr>
        <td>
            <c:choose>
                <c:when test="${requestScope.merchant.permit}">
                    已启用
                </c:when>
                <c:when test="${!requestScope.merchant.permit}">
                    <span style="color:red;">未启用</span>
                </c:when>
            </c:choose>
        </td>
        <td>${requestScope.merchant.address}</td>
    </tr>
    <tr class="title">
        <td>商家支付宝账号</td>
        <td>商家排列顺序</td>
    </tr>
    <tr>
        <td>${requestScope.merchant.alipayCode}</td>
        <td>${requestScope.merchant.merchOrder}</td>
    </tr>
    <tr class="title">
        <td>商家经营范围</td>
        <td>企业规模（员工人数）</td>
    </tr>
    <tr>
        <td>
            <c:forEach items="${requestScope.merchant.categories}" var="category">
                <span style="padding: 10px;">${category.goodsAttSetName}</span>
            </c:forEach>
        </td>
        <td>${requestScope.merchant.emplyCount}人</td>
    </tr>
    <tr class="title">
        <td>工商执照注册码</td>
        <td>税务登记证号</td>
    </tr>
    <tr>
        <td>
            <c:choose>
                <c:when test="${requestScope.merchant.licenseCode==''}">
                    <span style="color:silver">未填写</span>
                </c:when>
                <c:otherwise>
                    ${requestScope.merchant.licenseCode}
                </c:otherwise>
            </c:choose>

        </td>
        <td>
            <c:choose>
                <c:when test="${requestScope.merchant.taxRegCode==''}">
                    <span style="color:silver">未填写</span>
                </c:when>
                <c:otherwise>
                    ${requestScope.merchant.taxRegCode}
                </c:otherwise>
            </c:choose>
        </td>
    </tr>
    <tr class="title">
        <td>组织机构代码</td>
        <td>入驻时间</td>
    </tr>
    <tr>
        <td>
            <c:choose>
                <c:when test="${requestScope.merchant.orgCode==''}">
                    <span style="color:silver">未填写</span>
                </c:when>
                <c:otherwise>
                    ${requestScope.merchant.orgCode}
                </c:otherwise>
            </c:choose>
        </td>
        <td>${requestScope.merchant.createDate}</td>
    </tr>
    <tr class="title">
        <td colspan="2">商家类型</td>
    </tr>
    <tr>
        <td colspan="2">
            <c:choose>
                <c:when test="${requestScope.merchant.storeType eq 'shoppingCenter'}">
                    <span>购物中心:${requestScope.merchant.shoppingCenter.scName}</span>
                </c:when>
                <c:when test="${requestScope.merchant.storeType eq 'featuredBlock'}">
                    <span>特色街区:${requestScope.merchant.featuredBlock.fbName}</span>
                </c:when>
                <c:when test="${requestScope.merchant.storeType eq 'featuredStore'}">
                    <span>特色店</span>
                </c:when>
                <c:when test="${requestScope.merchant.storeType eq 'vipStore'}">
                    <span>vip商家</span>
                </c:when>
            </c:choose>
        </td>
    </tr>
    <tr>
        <td colspan="2" class="info_title">运营者（联系人）信息</td>
    </tr>
    <tr class="title">
        <td>商家联系人姓名</td>
        <td>联系人部门与职位</td>
    </tr>
    <tr>
        <td>${requestScope.merchant.contactName}</td>
        <td>${requestScope.merchant.contactPartAndPositon}</td>
    </tr>
    <tr class="title">
        <td>联系人手机号码</td>
        <td>联系人电子邮箱</td>
    </tr>
    <tr>
        <td>${requestScope.merchant.contactMobileNO}</td>
        <td>${requestScope.merchant.contactEmail}</td>
    </tr>
    <tr class="title">
        <td>联系人身份证号</td>
        <td></td>
    </tr>
    <tr>
        <td>${requestScope.merchant.contactID}</td>
        <td></td>
    </tr>
    <tr>
        <td colspan="2" class="info_title">注册时提供的相关材料(图片)</td>
    </tr>
    <tr class="title">
        <td colspan="2">商家顶部图片</td>
    </tr>
    <tr>
        <td colspan="2"><img src="${requestScope.merchant.headerImg.url}" style="width: 500px;"></td>
    </tr>
    <c:if test="${requestScope.merchant.opraterIDPhoto != null }">
        <tr class="title">
            <td colspan="2">运营者手持身份证照片</td>
        </tr>
        <tr>
            <td colspan="2"><img src="${requestScope.merchant.opraterIDPhoto.url}" style="width: 700px"></td>
        </tr>
    </c:if>
    <c:if test="${requestScope.merchant.orgPhoto !=null }">
        <tr class="title">
            <td colspan="2">组织机构代码证原件照片</td>
        </tr>
        <tr>
            <td colspan="2"><img src="${requestScope.merchant.orgPhoto.url}" style="width: 700px"></td>
        </tr>
    </c:if>
    <c:if test="${requestScope.merchant.busLicePhoto !=null }">
        <tr class="title">
            <td colspan="2">工商营业执照原件照片</td>
        </tr>
        <tr>
            <td colspan="2"><img src="${requestScope.merchant.busLicePhoto.url}" style="width: 700px"></td>
        </tr>
    </c:if>
    <c:if test="${requestScope.merchant.applyLetterPhoto !=null }">
        <tr class="title">
            <td colspan="2">加盖公章的申请认证公函</td>
        </tr>
        <tr>
            <td colspan="2"><img src="${requestScope.merchant.applyLetterPhoto.url}" style="width: 700px"></td>
        </tr>
    </c:if>
</table>
</body>
</html>
