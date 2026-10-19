package vn.iotstar.controller.admin;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import vn.iotstar.model.Product;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.ProductService;
import vn.iotstar.service.impl.CategoryServiceImpl;
import vn.iotstar.service.impl.ProductServiceImpl;
import vn.iotstar.util.Constant;

@WebServlet(urlPatterns = { "/admin/product/list" })
public class ProductListController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final ProductService productService = new ProductServiceImpl();
    private final CategoryService cateService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String q = req.getParameter("q");
        Integer cateId = parsePositiveInt(req.getParameter("cateId"));
        List<Product> productList = productService.search(q, cateId);

        req.setAttribute("productList", productList);
        req.setAttribute("cateList", cateService.getAll());
        req.setAttribute("q", q == null ? "" : q.trim());
        req.setAttribute("cateId", cateId);
        req.getRequestDispatcher(Constant.Path.ADMIN_PRODUCT_LIST).forward(req, resp);
    }

    private static Integer parsePositiveInt(String s) {
        if (s == null || s.trim().isEmpty()) {
            return null;
        }
        try {
            int v = Integer.parseInt(s.trim());
            return v > 0 ? v : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
