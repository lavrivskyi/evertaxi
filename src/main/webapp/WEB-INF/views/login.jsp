<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<style>
    <%@include file='/WEB-INF/views/css/main.css' %>
    <%@include file='/WEB-INF/views/css/authorization.css' %>
</style>
<html>
<head>
    <title>Login</title>
</head>
<body>
<div class="user">
    <header class="user__header">
        <img src="https://user-images.githubusercontent.com/85931447/139277578-ecc770e8-e296-4305-ba24-9cff46c4f0a0.png" alt="" />
    </header>
    <h1 class="message error__message">${error_message}</h1>
    <h1 class="message success__message">${success_message}</h1>
    <form class="form" method="post" id="driver" action="${pageContext.request.contextPath}/login">
        <div class="form__group">
            <input type="text" placeholder="Login" class="form__input" name="login" form="driver" required />
        </div>
        <div class="form__group">
            <input type="password" placeholder="Password" class="form__input" name="password" form="driver" required />
        </div>
        <button class="btn" type="submit">Login</button>
        <button id="register" class="btn" type="button">Register</button>
    </form>
</div>

<script type="text/javascript">
    document.getElementById("register").onclick = function () {
        location.href = "${pageContext.request.contextPath}/drivers/add";
    };
</script>
</body>
</html>
