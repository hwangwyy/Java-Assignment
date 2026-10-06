<%@ include file="/WEB-INF/common/taglib.jsp" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Quản lý danh mục</title>
  <link rel="stylesheet" href="<c:url value='/static/css/style.css'/>">
</head>
<body>
<jsp:include page="/WEB-INF/common/topbar.jsp"/>
<div class="container">
  <div class="card">
    <h1>Quản lý danh mục</h1>
    <div class="toolbar">
      <a class="btn" href="<c:url value='/admin/category/add'/>">+ Thêm danh mục mới</a>
      <form action="<c:url value='/admin/category/list'/>" method="get">
        <input type="text" name="q" placeholder="Tìm theo tên..." value="<c:out value='${q}'/>">
        <button class="btn" type="submit">Tìm</button>
      </form>
    </div>

    <table>
      <thead>
        <tr><th>STT</th><th>Hình ảnh</th><th>Tên danh mục</th><th>Hành động</th></tr>
      </thead>
      <tbody>
        <c:forEach items="${cateList}" var="cate" varStatus="STT">
          <tr>
            <td>${STT.index + 1}</td>
            <td>
              <c:choose>
                <c:when test="${not empty cate.icon}">
                  <c:url value="/image" var="imgUrl"><c:param name="fname" value="${cate.icon}"/></c:url>
                  <img class="thumb" src="${imgUrl}" alt="">
                </c:when>
                <c:otherwise><span class="noimg">Không có ảnh</span></c:otherwise>
              </c:choose>
            </td>
            <td><c:out value="${cate.name}"/></td>
            <td>
              <a class="btn small" href="<c:url value='/admin/category/edit'><c:param name='id' value='${cate.id}'/></c:url>">Sửa</a>
              <form class="inline" action="<c:url value='/admin/category/delete'/>" method="post"
                    onsubmit="return confirm('Xóa danh mục này?');">
                <input type="hidden" name="id" value="${cate.id}">
                <button class="btn small danger" type="submit">Xóa</button>
              </form>
            </td>
          </tr>
        </c:forEach>
        <c:if test="${empty cateList}">
          <tr><td colspan="4" class="muted center">Không có danh mục nào.</td></tr>
        </c:if>
      </tbody>
    </table>
  </div>
</div>
</body>
</html>
