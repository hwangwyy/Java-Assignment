package vn.iotstar.controller;

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

@WebServlet(urlPatterns = { "/product", "/products" })
public class ProductController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final ProductService productService = new ProductServiceImpl();
    private final CategoryService cateService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // If 'id' parameter is passed to /product?id=..., show the product detail view
        String idParam = req.getParameter("id");
        if (idParam != null && !idParam.trim().isEmpty()) {
            try {
                Product p = productService.get(Integer.parseInt(idParam.trim()));
                if (p != null) {
                    req.setAttribute("product", p);
                    req.getRequestDispatcher(Constant.Path.PRODUCT_DETAIL).forward(req, resp);
                    return;
                }
            } catch (NumberFormatException ignored) {
                // fall through to product list
            }
        }

        int pageSize = Constant.PRODUCT_PAGE_SIZE;
        Integer cateId = parsePositiveInt(req.getParameter("cateId"));
        String q = req.getParameter("q") == null ? "" : req.getParameter("q").trim();

        int totalItems = productService.count(cateId, q);
        int totalPages = Math.max(1, (int) Math.ceil((double) totalItems / pageSize));

        int currentPage = 1;
        Integer parsedPage = parsePositiveInt(req.getParameter("page"));
        if (parsedPage != null) {
            currentPage = Math.min(parsedPage, totalPages);
        }

        List<Product> productList = productService.getPage(cateId, q, currentPage, pageSize);

        req.setAttribute("productList", productList);
        req.setAttribute("cateList", cateService.getAll());
        req.setAttribute("currentPage", currentPage);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalItems", totalItems);
        req.setAttribute("cateId", cateId);
        req.setAttribute("q", q);
        req.getRequestDispatcher(Constant.Path.PRODUCT_LIST).forward(req, resp);
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
