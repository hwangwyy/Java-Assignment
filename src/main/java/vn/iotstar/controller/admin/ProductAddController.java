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

@WebServlet(urlPatterns = { "/admin/product/add" })
public class ProductAddController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final ProductService productService = new ProductServiceImpl();
    private final CategoryService cateService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
            String name = form.field("name");
            String description = form.field("description");
            String priceStr = form.field("price");
            String cateIdStr = form.field("categoryId");

            String error = form.error;
            BigDecimal price = null;
            int cateId = 0;

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
                Product draft = new Product(0, name, description, price, null, cateId);
                req.setAttribute("alert", error);
                req.setAttribute("product", draft);
                req.setAttribute("priceInput", priceStr);
                req.setAttribute("cateList", cateService.getAll());
                req.getRequestDispatcher(Constant.Path.ADMIN_PRODUCT_FORM).forward(req, resp);
                return;
            }

            productService.insert(new Product(0, name, description, price, form.savedFile, cateId));
            resp.sendRedirect(req.getContextPath() + "/admin/product/list");
        } catch (FileUploadException e) {
            req.setAttribute("alert", "Không thể tải tệp lên: " + e.getMessage());
            req.setAttribute("cateList", cateService.getAll());
            req.getRequestDispatcher(Constant.Path.ADMIN_PRODUCT_FORM).forward(req, resp);
        }
    }
}
