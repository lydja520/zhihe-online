<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2016/7/26
  Time: 9:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="/static/core/jquery.min.js"></script>
    <script type="text/javascript">
        $(function () {
            $('.btn').each(function (index, element) {
                $(element).click(function () {
                    var url = $(this).data('url');
                    $.ajax({
                        url: url,
                        type: 'get',
                        dataType: 'json',
                        success: function (info) {
                            alert(info.msg);
                        }
                    });
                });
            });
        });
    </script>
</head>
<body>

<div>导入默认SKU：</div>
<button class="btn" data-url="/utils/option/importSku">导入</button>
<br><br>

<div>将商品的价格、销量、最小价格、最新价格等信息跟新为对应的sku信息：</div>
<button class="btn" data-url="/utils/option/importGoodsCountInfo">更新</button>
<br><br>

<div>初始化购物车数据对应的sku：</div>
<button class="btn" data-url="/utils/option/importShoppingCartInfo">初始化</button>

</body>
</html>
