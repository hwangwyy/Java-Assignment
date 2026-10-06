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

@WebServlet(urlPatterns = { "/admin/category/edit" })
public class CategoryEditController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final CategoryService cateService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Category category = null;
        try {
            category = cateService.get(Integer.parseInt(req.getParameter("id")));
        } catch (NumberFormatException ignored) {
            // falls through to redirect
        }
        if (category == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/category/list");
            return;
        }
        req.setAttribute("category", category);
        req.getRequestDispatcher(Constant.Path.CATEGORY_EDIT).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!ServletFileUpload.isMultipartContent(req)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try {
            UploadUtil.Result form = UploadUtil.parse(req, Constant.CATEGORY_FOLDER, "icon");
            int id;
            try {
                id = Integer.parseInt(form.field("id"));
            } catch (NumberFormatException e) {
                UploadUtil.deleteQuietly(form.savedFile);
                resp.sendRedirect(req.getContextPath() + "/admin/category/list");
                return;
            }

            String name = form.field("name");
            String error = form.error;
            if (error == null && name.isEmpty()) {
                error = "Tên danh mục không được rỗng";
            }
            if (error != null) {
                UploadUtil.deleteQuietly(form.savedFile);
                Category current = cateService.get(id);
                if (current == null) {
                    resp.sendRedirect(req.getContextPath() + "/admin/category/list");
                    return;
                }
                req.setAttribute("alert", error);
                req.setAttribute("category", current);
                req.getRequestDispatcher(Constant.Path.CATEGORY_EDIT).forward(req, resp);
                return;
            }

            // icon == null  ->  keep the old image
            cateService.edit(new Category(id, name, form.savedFile));
            resp.sendRedirect(req.getContextPath() + "/admin/category/list");
        } catch (FileUploadException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Không thể tải tệp lên: " + e.getMessage());
        }
    }
}
