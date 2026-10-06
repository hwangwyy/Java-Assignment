<%@ include file="/WEB-INF/common/taglib.jsp" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Dashboard - Admin</title>
  <link rel="stylesheet" href="<c:url value='/static/css/style.css'/>">
</head>
<body>
<jsp:include page="/WEB-INF/common/topbar.jsp"/>
<div class="container">
  <div class="card">
    <h1>Dashboard</h1>
    <p>Bạn là <b>Admin</b>: <c:out value="${sessionScope.account.fullName}"/></p>
    <a class="btn" href="<c:url value='/admin/category/list'/>">Quản lý danh mục</a>
    <a class="btn secondary" href="<c:url value='/admin/category/add'/>">Thêm danh mục mới</a>
  </div>
</div>
</body>
</html>
