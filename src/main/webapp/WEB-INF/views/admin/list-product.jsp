<%@ include file="/WEB-INF/common/taglib.jsp" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Quản lý sản phẩm</title>
  <link rel="stylesheet" href="<c:url value='/static/css/style.css'/>">
</head>
<body>
<jsp:include page="/WEB-INF/common/topbar.jsp"/>
<div class="container">
  <div class="card">
    <h1>Quản lý sản phẩm</h1>
    <div class="toolbar">
      <a class="btn" href="<c:url value='/admin/product/add'/>">+ Thêm sản phẩm mới</a>
      <form action="<c:url value='/admin/product/list'/>" method="get">
        <select name="cateId">
          <option value="">-- Tất cả danh mục --</option>
          <c:forEach items="${cateList}" var="c">
            <option value="${c.id}" <c:if test="${cateId == c.id}">selected</c:if>>
              <c:out value="${c.name}"/>
            </option>
          </c:forEach>
        </select>
        <input type="text" name="q" placeholder="Tìm theo tên sản phẩm..." value="<c:out value='${q}'/>">
        <button class="btn" type="submit">Lọc</button>
      </form>
    </div>

    <table>
      <thead>
        <tr>
          <th>STT</th>
          <th>Hình ảnh</th>
          <th>Tên sản phẩm</th>
          <th>Danh mục</th>
          <th>Giá</th>
          <th>Ngày tạo</th>
          <th>Hành động</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach items="${productList}" var="p" varStatus="STT">
          <tr>
            <td>${STT.index + 1}</td>
            <td>
              <c:choose>
                <c:when test="${not empty p.image}">
                  <c:url value="/image" var="imgUrl"><c:param name="fname" value="${p.image}"/></c:url>
                  <img class="thumb" src="${imgUrl}" alt="">
                </c:when>
                <c:otherwise><span class="noimg">Không có ảnh</span></c:otherwise>
              </c:choose>
            </td>
            <td>
              <a href="<c:url value='/product/detail'><c:param name='id' value='${p.id}'/></c:url>">
                <b><c:out value="${p.name}"/></b>
              </a>
            </td>
            <td><span class="badge"><c:out value="${p.categoryName}"/></span></td>
            <td class="price"><fmt:formatNumber value="${p.price}" type="number" maxFractionDigits="0"/> đ</td>
            <td class="muted"><fmt:formatDate value="${p.createdDate}" pattern="dd/MM/yyyy HH:mm"/></td>
            <td>
              <a class="btn small" href="<c:url value='/admin/product/edit'><c:param name='id' value='${p.id}'/></c:url>">Sửa</a>
              <form class="inline" action="<c:url value='/admin/product/delete'/>" method="post"
                    onsubmit="return confirm('Xóa sản phẩm này?');">
                <input type="hidden" name="id" value="${p.id}">
                <button class="btn small danger" type="submit">Xóa</button>
              </form>
            </td>
          </tr>
        </c:forEach>
        <c:if test="${empty productList}">
          <tr><td colspan="7" class="muted center">Không có sản phẩm nào.</td></tr>
        </c:if>
      </tbody>
    </table>
  </div>
</div>
</body>
</html>
