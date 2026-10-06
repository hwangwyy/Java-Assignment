<%@ include file="/WEB-INF/common/taglib.jsp" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Đăng ký tài khoản</title>
  <link rel="stylesheet" href="<c:url value='/static/css/style.css'/>">
</head>
<body>
<jsp:include page="/WEB-INF/common/topbar.jsp"/>
<div class="card narrow">
  <h2 class="center">Tạo tài khoản mới</h2>

  <c:if test="${not empty alert}">
    <div class="alert danger"><c:out value="${alert}"/></div>
  </c:if>

  <form action="<c:url value='/register'/>" method="post">
    <label for="username">Tài khoản</label>
    <input type="text" id="username" name="username" placeholder="Tài khoản" value="<c:out value='${param.username}'/>">

    <label for="fullname">Họ tên</label>
    <input type="text" id="fullname" name="fullname" placeholder="Họ tên" value="<c:out value='${param.fullname}'/>">

    <label for="email">Email</label>
    <input type="email" id="email" name="email" placeholder="Nhập Email" value="<c:out value='${param.email}'/>">

    <label for="phone">Số điện thoại</label>
    <input type="text" id="phone" name="phone" placeholder="Số điện thoại" value="<c:out value='${param.phone}'/>">

    <label for="password">Mật khẩu</label>
    <input type="password" id="password" name="password" placeholder="Mật khẩu (tối thiểu 6 ký tự)">

    <label for="repassword">Nhập lại mật khẩu</label>
    <input type="password" id="repassword" name="repassword" placeholder="Nhập lại mật khẩu">

    <button type="submit" class="btn" style="width:100%">Tạo tài khoản</button>
  </form>
  <p class="center muted">Nếu bạn đã có tài khoản? <a href="<c:url value='/login'/>">Đăng nhập</a></p>
</div>
</body>
</html>
