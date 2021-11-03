<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<style>
    <%@include file='/WEB-INF/views/css/main.css' %>
    <%@include file='/WEB-INF/views/css/authorization.css' %>
</style>
<html>
<head>
    <title>Register driver</title>
</head>
<body>
<div class="user">
    <header class="user__header">
        <a href="${pageContext.request.contextPath}/"><img style="width: 70px;" src="https://cdn2.iconfinder.com/data/icons/basics-1/100/Home-512.png" alt="" /></a>
    </header>
    <form class="form" method="post" id="driver" action="${pageContext.request.contextPath}/drivers/add">
        <div class="form__group">
            <input type="text" placeholder="Name" class="form__input" name="name" form="driver" required>
        </div>
        <div class="form__group">
            <input type="text" placeholder="License number" class="form__input" name="license_number" form="driver" required>
        </div>
        <div class="form__group">
            <input type="text" placeholder="Login" class="form__input" name="login" form="driver" required />
        </div>
        <div class="form__group">
            <input type="password" placeholder="Password" class="form__input" name="password" form="driver" required />
        </div>
        <button class="btn" type="submit">Register</button>
        <button id="register" class="btn" type="button">Login</button>
    </form>
</div>

<script type="text/javascript">
    document.getElementById("register").onclick = function () {
        location.href = "${pageContext.request.contextPath}/login";
    };
</script>
</body>
</html>
