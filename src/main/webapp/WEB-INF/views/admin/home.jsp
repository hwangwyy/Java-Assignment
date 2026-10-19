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
    <div style="display:flex;gap:10px;flex-wrap:wrap">
      <a class="btn" href="<c:url value='/admin/category/list'/>">Quản lý danh mục</a>
      <a class="btn secondary" href="<c:url value='/admin/category/add'/>">+ Thêm danh mục</a>
      <a class="btn" href="<c:url value='/admin/product/list'/>">Quản lý sản phẩm</a>
      <a class="btn secondary" href="<c:url value='/admin/product/add'/>">+ Thêm sản phẩm</a>
      <a class="btn secondary" href="<c:url value='/home'/>">Xem Trang chủ</a>
      <a class="btn secondary" href="<c:url value='/product'/>">Xem Trang sản phẩm</a>
    </div>
  </div>
</div>
</body>
</html>
