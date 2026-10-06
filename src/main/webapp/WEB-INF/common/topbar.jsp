<%@ include file="/WEB-INF/common/taglib.jsp" %>
<header class="topbar">
  <a class="brand" href="<c:url value='/waiting'/>">Servlet CRUD MVC</a>
  <nav>
    <c:choose>
      <c:when test="${empty sessionScope.account}">
        <a href="<c:url value='/login'/>">Đăng nhập</a>
        <a href="<c:url value='/register'/>">Đăng ký</a>
      </c:when>
      <c:otherwise>
        <c:if test="${sessionScope.account.roleid == 1}">
          <a href="<c:url value='/admin/home'/>">Admin</a>
          <a href="<c:url value='/admin/category/list'/>">Danh mục</a>
        </c:if>
        <c:if test="${sessionScope.account.roleid == 2}">
          <a href="<c:url value='/manager/home'/>">Manager</a>
        </c:if>
        <span class="who">Xin chào, <c:out value="${sessionScope.account.fullName}"/></span>
        <a href="<c:url value='/logout'/>">Đăng xuất</a>
      </c:otherwise>
    </c:choose>
  </nav>
</header>
