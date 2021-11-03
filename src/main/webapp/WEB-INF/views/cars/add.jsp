<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<style>
    <%@include file='/WEB-INF/views/css/main.css' %>
    <%@include file='/WEB-INF/views/css/authorization.css' %>
</style>
<html>
<head>
    <title>Add car</title>
</head>
<body>
<div class="user">
    <header class="user__header">
        <a href="${pageContext.request.contextPath}/"><img style="width: 70px;" src="https://cdn2.iconfinder.com/data/icons/basics-1/100/Home-512.png" alt="" /></a>
    </header>
    <h1 class="message success__message">${success_message}</h1>
    <form class="form" method="post" id="car" action="${pageContext.request.contextPath}/cars/add">
        <div class="form__group">
            <input type="text" placeholder="Model" class="form__input" name="model" form="car" required>
        </div>
        <div class="form__group">
            <input type="number" placeholder="Manufacturer ID" class="form__input" name="manufacturer_id" form="car" required>
        </div>
        <button class="btn" type="submit">SUBMIT</button>
    </form>
</div>
</body>
</html>
