<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<style>
    <%@include file='/WEB-INF/views/css/table/main.css' %>
</style>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>All cars</title>
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
                        <th class="column2">Model</th>
                        <th class="column3">Manufacturer name</th>
                        <th class="column4">Manufacturer country</th>
                        <th class="column5">Drivers</th>
                        <th class="column6"></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="car" items="${cars}">
                        <tr>
                            <td class="column1"><c:out value="${car.id}"/></td>
                            <td class="column2"><c:out value="${car.model}"/></td>
                            <td class="column3"><c:out value="${car.manufacturer.name}"/></td>
                            <td class="column4"><c:out value="${car.manufacturer.country}"/></td>
                            <td class="column5">
                                <c:forEach var="driver" items="${car.drivers}">
                                    ${driver.id} ${driver.name} ${driver.licenseNumber} <br>
                                </c:forEach>
                            </td>
                            <td class="column6" style="padding-right: 40px;"><a onclick="myFunction('${pageContext.request.contextPath}/cars/delete?id=${car.id}')">DELETE</a></td>
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
