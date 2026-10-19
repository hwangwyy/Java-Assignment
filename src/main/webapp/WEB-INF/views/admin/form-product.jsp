<%@ include file="/WEB-INF/common/taglib.jsp" %>
<c:set var="isEdit" value="${not empty product and product.id > 0}"/>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>${isEdit ? 'Chỉnh sửa sản phẩm' : 'Thêm sản phẩm mới'}</title>
  <link rel="stylesheet" href="<c:url value='/static/css/style.css'/>">
</head>
<body>
<jsp:include page="/WEB-INF/common/topbar.jsp"/>
<div class="container">
  <div class="card">
    <h1>${isEdit ? 'Chỉnh sửa sản phẩm' : 'Thêm sản phẩm mới'}</h1>
    <c:if test="${not empty alert}">
      <div class="alert danger"><c:out value="${alert}"/></div>
    </c:if>
    <c:if test="${empty cateList}">
      <div class="alert danger">
        Chưa có danh mục nào! Vui lòng <a href="<c:url value='/admin/category/add'/>">thêm danh mục</a> trước khi thêm sản phẩm.
      </div>
    </c:if>

    <c:url var="formAction" value="${isEdit ? '/admin/product/edit' : '/admin/product/add'}"/>
    <form action="${formAction}" method="post" enctype="multipart/form-data">
      <c:if test="${isEdit}">
        <input type="hidden" name="id" value="${product.id}">
      </c:if>

      <label for="name">Tên sản phẩm</label>
      <input type="text" id="name" name="name" placeholder="Nhập tên sản phẩm"
             value="<c:out value='${product.name}'/>" required>

      <label for="categoryId">Danh mục</label>
      <select id="categoryId" name="categoryId" required>
        <option value="">-- Chọn danh mục --</option>
        <c:forEach items="${cateList}" var="c">
          <option value="${c.id}" <c:if test="${product.categoryId == c.id}">selected</c:if>>
            <c:out value="${c.name}"/>
          </option>
        </c:forEach>
      </select>

      <label for="price">Giá bán (VNĐ)</label>
      <input type="number" id="price" name="price" step="0.01" min="0" placeholder="Ví dụ: 150000"
             value="<c:out value='${not empty priceInput ? priceInput : product.price}'/>" required>

      <label for="description">Mô tả sản phẩm</label>
      <textarea id="description" name="description" placeholder="Nhập mô tả chi tiết sản phẩm..."><c:out value="${product.description}"/></textarea>

      <c:if test="${isEdit and not empty product.image}">
        <label>Ảnh hiện tại</label>
        <c:url value="/image" var="imgUrl"><c:param name="fname" value="${product.image}"/></c:url>
        <img class="thumb" src="${imgUrl}" alt="">
      </c:if>

      <label for="image">
        ${isEdit ? 'Ảnh sản phẩm mới (bỏ trống để giữ ảnh cũ)' : 'Ảnh sản phẩm (png, jpg, gif, webp - tối đa 5MB)'}
      </label>
      <input type="file" id="image" name="image" accept="image/*">

      <button type="submit" class="btn">${isEdit ? 'Lưu thay đổi' : 'Thêm sản phẩm'}</button>
      <button type="reset" class="btn secondary">Nhập lại</button>
      <a class="btn secondary" href="<c:url value='/admin/product/list'/>">Quay lại</a>
    </form>
  </div>
</div>
</body>
</html>
