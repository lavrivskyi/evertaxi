<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<style>
    <%@include file='/WEB-INF/views/css/main.css' %>
    <%@include file='/WEB-INF/views/css/authorization.css' %>
</style>
<html>
<head>
    <title>Manufacturers</title>
</head>
<body>
<div class="user">
    <header class="user__header">
        <a href="${pageContext.request.contextPath}/"><img style="width: 70px;" src="https://cdn2.iconfinder.com/data/icons/basics-1/100/Home-512.png" alt="" /></a>
    </header>
    <h1 class="message success__message">${success_message}</h1>
    <form class="form" method="post" id="manufacturer" action="${pageContext.request.contextPath}/manufacturers/add">
        <div class="form__group">
            <input type="text" placeholder="Name" class="form__input" name="name" form="manufacturer" required>
        </div>
        <div class="form__group">
            <input type="text" placeholder="Country" class="form__input" name="country" form="manufacturer" required>
        </div>
        <button class="btn" type="submit" name="add" form="manufacturer">SUBMIT</button>
    </form>
</div>
</body>
</html>
