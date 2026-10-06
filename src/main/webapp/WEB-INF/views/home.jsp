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
    <h1>Trang chủ</h1>
    <p>Xin chào <b><c:out value="${sessionScope.account.fullName}"/></b>. Dưới đây là các danh mục sản phẩm:</p>
  </div>
  <div class="card">
    <c:if test="${empty cateList}"><p class="muted">Chưa có danh mục nào.</p></c:if>
    <div class="grid">
      <c:forEach items="${cateList}" var="cate">
        <div class="item">
          <c:choose>
            <c:when test="${not empty cate.icon}">
              <c:url value="/image" var="imgUrl"><c:param name="fname" value="${cate.icon}"/></c:url>
              <img class="thumb" src="${imgUrl}" alt="">
            </c:when>
            <c:otherwise><span class="noimg" style="width:100%">Không có ảnh</span></c:otherwise>
          </c:choose>
          <p><c:out value="${cate.name}"/></p>
        </div>
      </c:forEach>
    </div>
  </div>
</div>
</body>
</html>
