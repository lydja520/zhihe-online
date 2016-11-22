
<%@ page contentType="text/html;charset=UTF-8" language="java"  %>
<html>
<head>
    <title></title>
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/static/easyui/themes/default/easyui.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/easyui/themes/icon.css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/core/jquery.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/static/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/link_income.js"></script>
    <style>
        form div{
            margin: 10px 5px
        }
    </style>
    <script>
        $(function(){
            IncomeLoad();
        });
    </script>
</head>
<body>
    <table id="t_income"></table>
    <div id="win">
      <form id="income_form" method="post">
          <div  >
         最低收入: <input type="text"  class="easyui-numberbox" name="incomMin" data-options="min:0,precision:0" />
          </div>
          <div >
         最高收入: <input type="text" class="easyui-numberbox" name="incomMax"  data-options="min:0,precision:0" />
          </div >
          <input type="hidden" name="incomeId"/>
      </form>
    </div>
</body>
</html>
