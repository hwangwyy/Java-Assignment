<%@ include file="/WEB-INF/common/taglib.jsp" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Đặt lại mật khẩu</title>
  <link rel="stylesheet" href="<c:url value='/static/css/style.css'/>">
</head>
<body>
<jsp:include page="/WEB-INF/common/topbar.jsp"/>
<div class="card narrow">
  <h2 class="center">Đặt lại mật khẩu</h2>
  <p class="center muted">Email: <b><c:out value="${otpEmail}"/></b></p>

  <c:if test="${not empty info}">
    <div class="alert success"><c:out value="${info}"/></div>
  </c:if>
  <c:if test="${not empty alert}">
    <div class="alert danger"><c:out value="${alert}"/></div>
  </c:if>

  <form action="<c:url value='/reset-password'/>" method="post">
    <label for="otp">Mã OTP (6 chữ số)</label>
    <input type="text" id="otp" name="otp" placeholder="Nhập mã OTP gồm 6 chữ số" maxlength="6"
           value="<c:out value='${param.otp}'/>" autofocus required>

    <label for="password">Mật khẩu mới</label>
    <input type="password" id="password" name="password" placeholder="Mật khẩu mới (tối thiểu 6 ký tự)" required>

    <label for="repassword">Nhập lại mật khẩu mới</label>
    <input type="password" id="repassword" name="repassword" placeholder="Nhập lại mật khẩu mới" required>

    <button type="submit" class="btn" style="width:100%">Đặt lại mật khẩu</button>
  </form>

  <form action="<c:url value='/reset-password'/>" method="post">
    <input type="hidden" name="action" value="resend">
    <button type="submit" class="btn secondary" style="width:100%">Gửi lại mã OTP</button>
  </form>

  <p class="center muted"><a href="<c:url value='/login'/>">Quay lại đăng nhập</a></p>
</div>
</body>
</html>
