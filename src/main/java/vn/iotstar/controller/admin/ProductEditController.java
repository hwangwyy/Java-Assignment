package vn.iotstar.controller.admin;

import java.io.IOException;
import java.math.BigDecimal;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import vn.iotstar.model.Product;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.ProductService;
import vn.iotstar.service.impl.CategoryServiceImpl;
import vn.iotstar.service.impl.ProductServiceImpl;
import vn.iotstar.util.Constant;
import vn.iotstar.util.UploadUtil;

@WebServlet(urlPatterns = { "/admin/product/edit" })
public class ProductEditController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final ProductService productService = new ProductServiceImpl();
    private final CategoryService cateService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Product product = null;
        try {
            product = productService.get(Integer.parseInt(req.getParameter("id")));
        } catch (NumberFormatException ignored) {
            // falls through to redirect
        }
        if (product == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/product/list");
            return;
        }
        req.setAttribute("product", product);
        req.setAttribute("cateList", cateService.getAll());
        req.getRequestDispatcher(Constant.Path.ADMIN_PRODUCT_FORM).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!ServletFileUpload.isMultipartContent(req)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try {
            UploadUtil.Result form = UploadUtil.parse(req, Constant.PRODUCT_FOLDER, "image");
            int id;
            try {
                id = Integer.parseInt(form.field("id"));
            } catch (NumberFormatException e) {
                UploadUtil.deleteQuietly(form.savedFile);
                resp.sendRedirect(req.getContextPath() + "/admin/product/list");
                return;
            }

            Product current = productService.get(id);
            if (current == null) {
                UploadUtil.deleteQuietly(form.savedFile);
                resp.sendRedirect(req.getContextPath() + "/admin/product/list");
                return;
            }

            String name = form.field("name");
            String description = form.field("description");
            String priceStr = form.field("price");
            String cateIdStr = form.field("categoryId");

            String error = form.error;
            BigDecimal price = null;
            int cateId = current.getCategoryId();

            if (error == null && name.isEmpty()) {
                error = "Tên sản phẩm không được rỗng";
            }
            if (error == null) {
                try {
                    price = new BigDecimal(priceStr);
                    if (price.compareTo(BigDecimal.ZERO) < 0) {
                        error = "Giá sản phẩm không được âm";
                    }
                } catch (NumberFormatException e) {
                    error = "Giá sản phẩm không hợp lệ";
                }
            }
            if (error == null) {
                try {
                    cateId = Integer.parseInt(cateIdStr);
                    if (cateService.get(cateId) == null) {
                        error = "Danh mục không tồn tại";
                    }
                } catch (NumberFormatException e) {
                    error = "Vui lòng chọn danh mục";
                }
            }

            if (error != null) {
                UploadUtil.deleteQuietly(form.savedFile);
                current.setName(name);
                current.setDescription(description);
                if (price != null) {
                    current.setPrice(price);
                }
                current.setCategoryId(cateId);
                req.setAttribute("alert", error);
                req.setAttribute("product", current);
                req.setAttribute("priceInput", priceStr);
                req.setAttribute("cateList", cateService.getAll());
                req.getRequestDispatcher(Constant.Path.ADMIN_PRODUCT_FORM).forward(req, resp);
                return;
            }

            // form.savedFile == null -> keep existing image
            productService.edit(new Product(id, name, description, price, form.savedFile, cateId));
            resp.sendRedirect(req.getContextPath() + "/admin/product/list");
        } catch (FileUploadException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Không thể tải tệp lên: " + e.getMessage());
        }
    }
}
