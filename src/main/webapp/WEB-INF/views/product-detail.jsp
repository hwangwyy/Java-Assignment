<%@ include file="/WEB-INF/common/taglib.jsp" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title><c:out value="${product.name}"/> - Chi tiết sản phẩm</title>
  <link rel="stylesheet" href="<c:url value='/static/css/style.css'/>">
</head>
<body>
<jsp:include page="/WEB-INF/common/topbar.jsp"/>
<div class="container">
  <div class="card">
    <div class="detail-grid">
      <div>
        <c:choose>
          <c:when test="${not empty product.image}">
            <c:url value="/image" var="imgUrl"><c:param name="fname" value="${product.image}"/></c:url>
            <img class="detail-img" src="${imgUrl}" alt="<c:out value='${product.name}'/>">
          </c:when>
          <c:otherwise>
            <span class="noimg" style="width:100%;height:260px;line-height:260px">Không có ảnh sản phẩm</span>
          </c:otherwise>
        </c:choose>
      </div>

      <div>
        <span class="badge"><c:out value="${product.categoryName}"/></span>
        <h1 style="margin:8px 0"><c:out value="${product.name}"/></h1>
        <p class="price" style="font-size:1.5rem;margin:8px 0">
          <fmt:formatNumber value="${product.price}" type="number" maxFractionDigits="0"/> đ
        </p>
        <p class="muted">
          Mã sản phẩm: #${product.id}
          <c:if test="${not empty product.createdDate}">
            &bull; Ngày đăng: <fmt:formatDate value="${product.createdDate}" pattern="dd/MM/yyyy HH:mm"/>
          </c:if>
        </p>

        <h3>Mô tả chi tiết</h3>
        <c:choose>
          <c:when test="${not empty product.description}">
            <p style="white-space:pre-line;line-height:1.6"><c:out value="${product.description}"/></p>
          </c:when>
          <c:otherwise>
            <p class="muted">Chưa có mô tả cho sản phẩm này.</p>
          </c:otherwise>
        </c:choose>

        <div style="display:flex;gap:10px;flex-wrap:wrap;margin-top:16px">
          <a class="btn secondary" href="<c:url value='/product'/>">&larr; Danh sách sản phẩm</a>
          <a class="btn secondary" href="<c:url value='/home'/>">Về trang chủ</a>
          <c:if test="${sessionScope.account.roleid == 1}">
            <a class="btn" href="<c:url value='/admin/product/edit'><c:param name='id' value='${product.id}'/></c:url>">
              Chỉnh sửa sản phẩm
            </a>
          </c:if>
        </div>
      </div>
    </div>
  </div>
</div>
</body>
</html>
