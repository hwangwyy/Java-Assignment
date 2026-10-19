package vn.iotstar.controller.admin;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.ProductService;
import vn.iotstar.service.impl.CategoryServiceImpl;
import vn.iotstar.service.impl.ProductServiceImpl;

/** Delete is POST-only so a crawler / prefetched link can never delete data. */
@WebServlet(urlPatterns = { "/admin/category/delete" })
public class CategoryDeleteController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final CategoryService cateService = new CategoryServiceImpl();
    private final ProductService productService = new ProductServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            if (productService.countByCategory(id) > 0) {
                resp.sendRedirect(req.getContextPath() + "/admin/category/list?error=hasProducts");
                return;
            }
            cateService.delete(id);
        } catch (NumberFormatException ignored) {
            // invalid id: nothing to delete
        }
        resp.sendRedirect(req.getContextPath() + "/admin/category/list");
    }
}
