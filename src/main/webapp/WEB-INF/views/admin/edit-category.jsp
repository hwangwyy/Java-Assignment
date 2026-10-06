<%@ include file="/WEB-INF/common/taglib.jsp" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Chỉnh sửa danh mục</title>
  <link rel="stylesheet" href="<c:url value='/static/css/style.css'/>">
</head>
<body>
<jsp:include page="/WEB-INF/common/topbar.jsp"/>
<div class="container">
  <div class="card">
    <h1>Chỉnh sửa danh mục</h1>
    <c:if test="${not empty alert}">
      <div class="alert danger"><c:out value="${alert}"/></div>
    </c:if>

    <form action="<c:url value='/admin/category/edit'/>" method="post" enctype="multipart/form-data">
      <input type="hidden" name="id" value="${category.id}">

      <label for="name">Tên danh mục</label>
      <input type="text" id="name" name="name" value="<c:out value='${category.name}'/>">

      <c:if test="${not empty category.icon}">
        <label>Ảnh hiện tại</label>
        <c:url value="/image" var="imgUrl"><c:param name="fname" value="${category.icon}"/></c:url>
        <img class="thumb" src="${imgUrl}" alt="">
      </c:if>

      <label for="icon">Ảnh đại diện mới (bỏ trống để giữ ảnh cũ)</label>
      <input type="file" id="icon" name="icon" accept="image/*">

      <button type="submit" class="btn">Lưu</button>
      <button type="reset" class="btn secondary">Reset</button>
      <a class="btn secondary" href="<c:url value='/admin/category/list'/>">Quay lại</a>
    </form>
  </div>
</div>
</body>
</html>
