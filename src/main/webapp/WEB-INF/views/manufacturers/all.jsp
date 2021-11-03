<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<style>
    <%@include file='/WEB-INF/views/css/table/main.css' %>
</style>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>All manufacturers</title>
</head>
<body>
<div class="limiter">
    <div class="container-table100">
        <div class="wrap-table100">
            <div class="home_button_div">
                <a href="${pageContext.request.contextPath}/"><img style="width: 70px; margin-left: 50%;" src="https://cdn2.iconfinder.com/data/icons/basics-1/100/Home-512.png" alt="" /></a>
            </div>
            <div class="table100">
                <table>
                    <thead>
                    <tr class="table100-head">
                        <th class="column1">ID</th>
                        <th class="column2">Name</th>
                        <th class="column3">Country</th>
                        <th class="column4"></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="manufacturer" items="${manufacturers}">
                        <tr>
                            <td class="column1"><c:out value="${manufacturer.id}"/></td>
                            <td class="column2"><c:out value="${manufacturer.name}"/></td>
                            <td class="column3"><c:out value="${manufacturer.country}"/></td>
                            <td class="column4" style="padding-right: 40px;"><a onclick="myFunction('${pageContext.request.contextPath}/manufacturers/delete?id=${manufacturer.id}')">DELETE</a></td>
                        </tr></c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    <%@include file='/WEB-INF/views/js/delete_item.js' %>
</script>
</body>
</html>
