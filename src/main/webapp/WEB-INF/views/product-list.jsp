<%@ include file="/WEB-INF/common/taglib.jsp" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Danh sách sản phẩm</title>
  <link rel="stylesheet" href="<c:url value='/static/css/style.css'/>">
</head>
<body>
<jsp:include page="/WEB-INF/common/topbar.jsp"/>
<div class="container">
  <div class="card">
    <div class="toolbar">
      <div>
        <h1 style="margin-bottom:4px">Tất cả sản phẩm</h1>
        <span class="muted">Hiển thị 6 sản phẩm / trang &bull; Tổng cộng <b>${totalItems}</b> sản phẩm</span>
      </div>
      <form action="<c:url value='/product'/>" method="get">
        <select name="cateId">
          <option value="">-- Tất cả danh mục --</option>
          <c:forEach items="${cateList}" var="c">
            <option value="${c.id}" <c:if test="${cateId == c.id}">selected</c:if>>
              <c:out value="${c.name}"/>
            </option>
          </c:forEach>
        </select>
        <input type="text" name="q" placeholder="Tìm sản phẩm..." value="<c:out value='${q}'/>">
        <button class="btn" type="submit">Tìm kiếm</button>
      </form>
    </div>

    <c:if test="${empty productList}">
      <p class="muted center" style="padding:24px 0">Không tìm thấy sản phẩm nào.</p>
    </c:if>

    <div class="product-grid">
      <c:forEach items="${productList}" var="p">
        <c:url value="/product/detail" var="detailUrl">
          <c:param name="id" value="${p.id}"/>
        </c:url>
        <a class="product-card" href="${detailUrl}">
          <c:choose>
            <c:when test="${not empty p.image}">
              <c:url value="/image" var="imgUrl"><c:param name="fname" value="${p.image}"/></c:url>
              <img class="thumb" src="${imgUrl}" alt="">
            </c:when>
            <c:otherwise><span class="noimg">Không có ảnh</span></c:otherwise>
          </c:choose>
          <div class="body">
            <span class="badge"><c:out value="${p.categoryName}"/></span>
            <p class="title"><c:out value="${p.name}"/></p>
            <span class="price"><fmt:formatNumber value="${p.price}" type="number" maxFractionDigits="0"/> đ</span>
          </div>
        </a>
      </c:forEach>
    </div>

    <c:if test="${totalPages > 1}">
      <div class="pagination">
        <c:choose>
          <c:when test="${currentPage > 1}">
            <c:url value="/product" var="prevUrl">
              <c:param name="page" value="${currentPage - 1}"/>
              <c:if test="${not empty cateId}"><c:param name="cateId" value="${cateId}"/></c:if>
              <c:if test="${not empty q}"><c:param name="q" value="${q}"/></c:if>
            </c:url>
            <a href="${prevUrl}">&laquo; Trước</a>
          </c:when>
          <c:otherwise><span class="disabled">&laquo; Trước</span></c:otherwise>
        </c:choose>

        <c:forEach begin="1" end="${totalPages}" var="i">
          <c:choose>
            <c:when test="${i == currentPage}">
              <span class="active">${i}</span>
            </c:when>
            <c:otherwise>
              <c:url value="/product" var="pageUrl">
                <c:param name="page" value="${i}"/>
                <c:if test="${not empty cateId}"><c:param name="cateId" value="${cateId}"/></c:if>
                <c:if test="${not empty q}"><c:param name="q" value="${q}"/></c:if>
              </c:url>
              <a href="${pageUrl}">${i}</a>
            </c:otherwise>
          </c:choose>
        </c:forEach>

        <c:choose>
          <c:when test="${currentPage < totalPages}">
            <c:url value="/product" var="nextUrl">
              <c:param name="page" value="${currentPage + 1}"/>
              <c:if test="${not empty cateId}"><c:param name="cateId" value="${cateId}"/></c:if>
              <c:if test="${not empty q}"><c:param name="q" value="${q}"/></c:if>
            </c:url>
            <a href="${nextUrl}">Sau &raquo;</a>
          </c:when>
          <c:otherwise><span class="disabled">Sau &raquo;</span></c:otherwise>
        </c:choose>
      </div>
    </c:if>
  </div>
</div>
</body>
</html>
