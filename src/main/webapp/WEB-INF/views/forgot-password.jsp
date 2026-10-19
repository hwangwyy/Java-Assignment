<%@ include file="/WEB-INF/common/taglib.jsp" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Quên mật khẩu</title>
  <link rel="stylesheet" href="<c:url value='/static/css/style.css'/>">
</head>
<body>
<jsp:include page="/WEB-INF/common/topbar.jsp"/>
<div class="card narrow">
  <h2 class="center">Quên mật khẩu</h2>
  <p class="center muted">Nhập email đã đăng ký để nhận mã xác nhận OTP.</p>

  <c:if test="${not empty alert}">
    <div class="alert danger"><c:out value="${alert}"/></div>
  </c:if>

  <form action="<c:url value='/forgot-password'/>" method="post">
    <label for="email">Email</label>
    <input type="email" id="email" name="email" placeholder="Nhập địa chỉ email của bạn" autofocus
           value="<c:out value='${param.email}'/>" required>

    <button type="submit" class="btn" style="width:100%">Gửi mã OTP</button>
  </form>

  <p class="center muted"><a href="<c:url value='/login'/>">Quay lại đăng nhập</a></p>
</div>
</body>
</html>
