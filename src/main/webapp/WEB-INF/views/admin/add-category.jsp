<%@ include file="/WEB-INF/common/taglib.jsp" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Thêm danh mục</title>
  <link rel="stylesheet" href="<c:url value='/static/css/style.css'/>">
</head>
<body>
<jsp:include page="/WEB-INF/common/topbar.jsp"/>
<div class="container">
  <div class="card">
    <h1>Thêm danh mục mới</h1>
    <c:if test="${not empty alert}">
      <div class="alert danger"><c:out value="${alert}"/></div>
    </c:if>

    <form action="<c:url value='/admin/category/add'/>" method="post" enctype="multipart/form-data">
      <label for="name">Tên danh mục</label>
      <input type="text" id="name" name="name" placeholder="Nhập tên danh mục" value="<c:out value='${name}'/>">

      <label for="icon">Ảnh đại diện (png, jpg, gif, webp - tối đa 5MB)</label>
      <input type="file" id="icon" name="icon" accept="image/*">

      <button type="submit" class="btn">Thêm</button>
      <button type="reset" class="btn secondary">Hủy</button>
      <a class="btn secondary" href="<c:url value='/admin/category/list'/>">Quay lại</a>
    </form>
  </div>
</div>
</body>
</html>
