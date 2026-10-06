package vn.iotstar.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import vn.iotstar.model.User;
import vn.iotstar.util.Constant;

/** Redirects the logged-in user to the right home page according to the role. */
@WebServlet(urlPatterns = "/waiting")
public class WaitingController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User u = session == null ? null : (User) session.getAttribute(Constant.SESSION_ACCOUNT);
        String ctx = req.getContextPath();
        if (u == null) {
            resp.sendRedirect(ctx + "/login");
        } else if (u.getRoleid() == Constant.ROLE_ADMIN) {
            resp.sendRedirect(ctx + "/admin/home");
        } else if (u.getRoleid() == Constant.ROLE_MANAGER) {
            resp.sendRedirect(ctx + "/manager/home");
        } else {
            resp.sendRedirect(ctx + "/home");
        }
    }
}
