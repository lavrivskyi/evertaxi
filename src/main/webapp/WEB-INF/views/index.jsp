<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style>
    <%@include file='/WEB-INF/views/css/main.css' %>
    <%@include file='/WEB-INF/views/css/buttons/index.scss' %>
</style>
<html>
<head>
    <title>My team</title>
</head>
<body>
<div class="center_div">
    <h1>Hello, <span class="driver_name">${driver_name}</span></h1><br/>
    <button onclick="myFunction('${pageContext.request.contextPath}/drivers/')" class="name noselect">Display All Drivers</button>
    <button onclick="myFunction('${pageContext.request.contextPath}/cars/')" class="name noselect">Display All Cars</button>
    <button onclick="myFunction('${pageContext.request.contextPath}/cars/my')" class="name noselect">Display My Cars</button>
    <button onclick="myFunction('${pageContext.request.contextPath}/manufacturers/')" class="name noselect">Display All Manufacturers</button>
    <button onclick="myFunction('${pageContext.request.contextPath}/drivers/add')" class="name noselect">Create new Driver</button>
    <button onclick="myFunction('${pageContext.request.contextPath}/cars/add')" class="name noselect">Create new Car</button>
    <button onclick="myFunction('${pageContext.request.contextPath}/manufacturers/add')" class="name noselect">Create new Manufacturer</button>
    <button onclick="myFunction('${pageContext.request.contextPath}/cars/drivers/add')" class="name noselect">Add Driver to Car</button>
    <button onclick="myFunction('${pageContext.request.contextPath}/logout')" class="name noselect">Logout</button>
</div>


<script type="text/javascript">
    function myFunction(link) {
        window.location.href = link;
    }
</script>
</body>
</html>
