<%@ include file="/WEB-INF/common/taglib.jsp" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Trang chủ</title>
  <link rel="stylesheet" href="<c:url value='/static/css/style.css'/>">
</head>
<body>
<jsp:include page="/WEB-INF/common/topbar.jsp"/>
<div class="container">
  <div class="card">
    <div class="toolbar" style="margin-bottom:0">
      <div>
        <h1>Trang chủ</h1>
        <p style="margin:0">Xin chào <b><c:out value="${sessionScope.account.fullName}"/></b>!</p>
      </div>
      <a class="btn" href="<c:url value='/product'/>">Xem tất cả sản phẩm &rarr;</a>
    </div>
  </div>

  <div class="card">
    <h2>Danh mục sản phẩm</h2>
    <c:if test="${empty cateList}"><p class="muted">Chưa có danh mục nào.</p></c:if>
    <div class="grid">
      <c:forEach items="${cateList}" var="cate">
        <a class="item product-card" href="<c:url value='/product'><c:param name='cateId' value='${cate.id}'/></c:url>">
          <c:choose>
            <c:when test="${not empty cate.icon}">
              <c:url value="/image" var="imgUrl"><c:param name="fname" value="${cate.icon}"/></c:url>
              <img class="thumb" src="${imgUrl}" alt="">
            </c:when>
            <c:otherwise><span class="noimg" style="width:100%">Không có ảnh</span></c:otherwise>
          </c:choose>
          <div class="body"><p class="title"><c:out value="${cate.name}"/></p></div>
        </a>
      </c:forEach>
    </div>
  </div>

  <div class="card">
    <div class="toolbar">
      <h2 style="margin:0">10 sản phẩm mới nhất</h2>
      <a href="<c:url value='/product'/>">Xem tất cả (phân trang) &rarr;</a>
    </div>
    <c:if test="${empty latestProducts}">
      <p class="muted">Chưa có sản phẩm nào.</p>
    </c:if>
    <div class="product-grid">
      <c:forEach items="${latestProducts}" var="p">
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
  </div>
</div>
</body>
</html>
