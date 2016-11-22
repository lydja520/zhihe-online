<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>商家活动信息详情页</title>
    <style>

        html, body {
            font-size: 12px;
            font-family: "Microsoft Yahei";
            margin: 0px;
            padding: 0px;
        }

        table {
            border-collapse: collapse;
            width: 90%;
            margin: 30px auto;
            table-layout: fixed;
            border-color: #a0c6e5;
        }

        td {
            text-align: center;
            font-size: 12px;
            height: 20px;
            word-wrap: break-word;
        }

        .info_title td {
            color: orangered;
            font-weight: bolder;
            background-color: #edf3ff;
        }

        .title td {
            color: #255625;
            font-weight: bolder;
            background-color: #edf3ff;
        }

    </style>
</head>
<body>
<table border="1" align="center">
    <tr class="info_title">
        <td colspan="4">活动基本信息</td>
    </tr>
    <tr class="title">
        <td>活动名称:</td>
        <td>发起商家:</td>
        <td>活动类别:</td>
        <td>商品类别:</td>
    </tr>
    <tr>
        <td>${requestScope.activity.activitName}</td>
        <td>${requestScope.activity.activityPromoter.merchName}</td>
        <td>${requestScope.activity.category.categName}</td>
        <td>
            <c:forEach items="${activity.attributeSets}" var="attributeSet">
                ${attributeSet.goodsAttSetName},
            </c:forEach>
        </td>
    </tr>
    <tr class="title">
        <td>活动联系人:</td>
        <td>联系电话:</td>
        <td>活动规则:</td>
        <td>当前状态内容描述:</td>
    </tr>
    <tr>
        <td>${requestScope.activity.contacterName}</td>
        <td>${requestScope.activity.contactTell}</td>
        <td>${requestScope.activity.activitDetail}</td>
        <td>${requestScope.activity.displayState}</td>
    </tr>
    <tr class="title">
        <td>活动创建时间:</td>
        <td>开始时间:</td>
        <td>结束时间:</td>
        <td></td>
    </tr>
    <tr>
        <td>${requestScope.activity.createDate}</td>
        <td>${requestScope.activity.beginDate}</td>
        <td>${requestScope.activity.endDate}</td>
        <td></td>
    </tr>
</table>

<c:set var="index" value="0"></c:set>
<c:forEach items="${requestScope.merchants.rows}" var="item">
    <table border="1" align="center" id="merchantInfo">
        <tr class="info_title">
            <td colspan="2"><img style="width: 50px;height: 50px;border-radius: 25px" src="${item.coverImg.url}"></td>
            <td colspan="4">
                商家${index = index+1} （${item.merchName}）
            </td>
            <td colspan="2"></td>
        </tr>
        <tr class="title">
            <td colspan="2">商家名称</td>
            <td colspan="2">联系电话</td>
            <td colspan="2">入驻时间</td>
            <td colspan="2">详细地址</td>
        </tr>
        <tr>
            <td colspan="2">${item.merchName}</td>
            <td colspan="2">${item.merchTell}</td>
            <td colspan="2">${item.createDate}</td>
            <td colspan="2">${item.address}</td>
        </tr>

        <tr class="title">
            <td colspan="8">活动红包</td>
        </tr>
        <tr class="title">
            <td colspan="2">红包总额</td>
            <td colspan="2">红包个数</td>
            <td colspan="2">红包信息</td>
            <td colspan="2">红包创建时间</td>
        </tr>
        <c:set var="merchantId" value="${item.merchantId}"></c:set>
        <c:set var="_redEnvelops" value="${redEnvelops[merchantId]}"></c:set>
        <c:if test="${_redEnvelops.size() <= 0}">
            <tr>
                <td colspan="8">未添加活动红包</td>
            </tr>
        </c:if>
        <c:if test="${_redEnvelops.size() > 0}">
            <c:forEach items="${_redEnvelops}" var="redEnvelop">
                <tr>
                    <td colspan="2">${redEnvelop.totalMoney}</td>
                    <td colspan="2">${redEnvelop.numbers}</td>
                    <td colspan="2">${redEnvelop.envelopMsg}</td>
                    <td colspan="2">${redEnvelop.createDate}</td>
                </tr>
            </c:forEach>
        </c:if>

        <tr class="title">
            <td colspan="8">活动优惠劵</td>
        </tr>
        <tr class="title">
            <td colspan="2">优惠类型</td>
            <td colspan="2">优惠面值</td>
            <td colspan="2">数量</td>
            <td colspan="2">有效期</td>
        </tr>
        <c:set var="_coupon" value="${coupon[merchantId]}"></c:set>
        <c:if test="${_coupon.size() <= 0}">
            <tr>
                <td colspan="8">未添加优惠券</td>
            </tr>
        </c:if>
        <c:if test="${_coupon.size() > 0}">
            <c:forEach items="${_coupon}" var="coupon">
                <tr>
                    <td colspan="2">${coupon.couponName}</td>
                    <td colspan="2">${coupon.faceValue}</td>
                    <td colspan="2">${coupon.total}</td>
                    <td colspan="2">${coupon.startValidity.toString()}<br>至<br>${coupon.endValidity.toString()}
                    </td>
                </tr>
                <tr>
                    <td colspan="8">
                        <span style=" color:grey;">使用规则:&nbsp&nbsp&nbsp&nbsp${coupon.couponMsg}</span>
                    </td>
                </tr>
            </c:forEach>
        </c:if>

        <tr class="title">
            <td colspan="8">活动商品</td>
        </tr>
        <tr class="title">
            <td colspan="2">活动商品</td>
            <td colspan="2">商品原价</td>
            <td colspan="2">活动价格</td>
            <td colspan="2">商品数量</td>
        </tr>
        <c:set var="_activityGoods" value="${activityGoods[merchantId]}"></c:set>
        <c:if test="${_activityGoods.size() <= 0}">
            <tr>
                <td colspan="8">未添加活动商品</td>
            </tr>
        </c:if>
        <c:if test="${_activityGoods.size() > 0}">
            <c:forEach items="${_activityGoods}" var="activityGoods">
                <tr>
                    <td colspan="2">${activityGoods.goods.goodsName}</td>
                    <td colspan="2">${activityGoods.goods.price}元</td>
                    <td colspan="2">${activityGoods.activityPrice}元</td>
                    <td colspan="2">${activityGoods.agCount}</td>
                </tr>
            </c:forEach>
        </c:if>

        <tr class="title">
            <td colspan="8">邀请会员</td>
        </tr>
        <tr class="title">
            <td>用户名称</td>
            <td>用户电话</td>
            <td>用户年龄</td>
            <td>用户性别</td>
            <td>用户职业</td>
            <td>用户收入</td>
            <td>用户所属区域</td>
            <td>用户生日</td>
        </tr>
        <c:set var="_activityFans" value="${activityFans[merchantId]}"></c:set>
        <c:if test="${_activityFans.size() <= 0}">
            <tr>
                <td colspan="8">未邀请会员</td>
            </tr>
        </c:if>
        <c:if test="${_activityFans.size() > 0}">
            <c:forEach items="${_activityFans}" var="activityFan">
                <tr>
                    <td>${activityFan.fans.userName}</td>
                    <td>${activityFan.fans.userPhone}</td>
                    <td>${activityFan.fans.age}</td>
                    <td>
                        <c:choose>
                            <c:when test="${activityFan.fans.sex==true}">
                                男
                            </c:when>
                            <c:when test="${activityFan.fans.sex==false}">
                                女
                            </c:when>
                        </c:choose>
                    </td>
                    <td>${activityFan.fans.occupation}</td>
                    <td>${activityFan.fans.income}</td>
                    <td>${activityFan.fans.area.fullName}</td>
                    <td>${activityFan.fans.birthday}</td>
                </tr>
            </c:forEach>
        </c:if>
    </table>
</c:forEach>
</body>
</html>
