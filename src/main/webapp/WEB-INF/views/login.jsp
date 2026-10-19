<%@ include file="/WEB-INF/common/taglib.jsp" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Đăng nhập</title>
  <link rel="stylesheet" href="<c:url value='/static/css/style.css'/>">
</head>
<body>
<jsp:include page="/WEB-INF/common/topbar.jsp"/>
<div class="card narrow">
  <h2 class="center">Đăng nhập vào hệ thống</h2>

  <c:if test="${not empty alert}">
    <div class="alert danger"><c:out value="${alert}"/></div>
  </c:if>
  <c:if test="${param.activated == '1'}">
    <div class="alert success">Kích hoạt tài khoản thành công! Hãy đăng nhập.</div>
  </c:if>
  <c:if test="${param.reset == '1'}">
    <div class="alert success">Đặt lại mật khẩu thành công! Hãy đăng nhập bằng mật khẩu mới.</div>
  </c:if>

  <form action="<c:url value='/login'/>" method="post">
    <label for="username">Tài khoản</label>
    <input type="text" id="username" name="username" placeholder="Tài khoản" autofocus
           value="<c:out value='${param.username}'/>">

    <label for="password">Mật khẩu</label>
    <input type="password" id="password" name="password" placeholder="Mật khẩu">

    <div class="check">
      <input type="checkbox" id="remember" name="remember">
      <label for="remember">Nhớ tôi</label>
    </div>

    <button type="submit" class="btn" style="width:100%">Đăng nhập</button>
  </form>
  <p class="center"><a href="<c:url value='/forgot-password'/>">Quên mật khẩu?</a></p>
  <p class="center muted">Nếu bạn chưa có tài khoản, hãy <a href="<c:url value='/register'/>">Đăng ký</a></p>
</div>
</body>
</html>
