<%--
  Created by IntelliJ IDEA.
  User: LiangYang
  Date: 2021/7/14
  Time: 8:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>测试</title>

</head>
<body>
<jsp:include page="header.jsp"/>
<%
    int i = 0;
    for (int j = 0; j <100; j++) {
        i+=j;
    }
    out.println("<h1>Sum="+i+"</h1>");
%>
<jsp:include page="bottom.jsp"/>

</body>
</html>
