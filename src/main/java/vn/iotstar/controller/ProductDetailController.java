package vn.iotstar.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import vn.iotstar.model.Product;
import vn.iotstar.service.ProductService;
import vn.iotstar.service.impl.ProductServiceImpl;
import vn.iotstar.util.Constant;

@WebServlet(urlPatterns = { "/product/detail" })
public class ProductDetailController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final ProductService productService = new ProductServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Product product = null;
        try {
            product = productService.get(Integer.parseInt(req.getParameter("id")));
        } catch (NumberFormatException ignored) {
            // falls through to redirect
        }
        if (product == null) {
            resp.sendRedirect(req.getContextPath() + "/product");
            return;
        }
        req.setAttribute("product", product);
        req.getRequestDispatcher(Constant.Path.PRODUCT_DETAIL).forward(req, resp);
    }
}
