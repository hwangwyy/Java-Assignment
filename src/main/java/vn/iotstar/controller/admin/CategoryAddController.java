package vn.iotstar.controller.admin;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import vn.iotstar.model.Category;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.impl.CategoryServiceImpl;
import vn.iotstar.util.Constant;
import vn.iotstar.util.UploadUtil;

@WebServlet(urlPatterns = { "/admin/category/add" })
public class CategoryAddController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final CategoryService cateService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(Constant.Path.CATEGORY_ADD).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!ServletFileUpload.isMultipartContent(req)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try {
            UploadUtil.Result form = UploadUtil.parse(req, Constant.CATEGORY_FOLDER, "icon");
            String name = form.field("name");
            String error = form.error;
            if (error == null && name.isEmpty()) {
                error = "Tên danh mục không được rỗng";
            }
            if (error != null) {
                UploadUtil.deleteQuietly(form.savedFile);
                req.setAttribute("alert", error);
                req.setAttribute("name", name);
                req.getRequestDispatcher(Constant.Path.CATEGORY_ADD).forward(req, resp);
                return;
            }
            cateService.insert(new Category(0, name, form.savedFile));
            resp.sendRedirect(req.getContextPath() + "/admin/category/list");
        } catch (FileUploadException e) {
            req.setAttribute("alert", "Không thể tải tệp lên: " + e.getMessage());
            req.getRequestDispatcher(Constant.Path.CATEGORY_ADD).forward(req, resp);
        }
    }
}
