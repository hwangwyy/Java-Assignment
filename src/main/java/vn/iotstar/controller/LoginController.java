package vn.iotstar.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import vn.iotstar.model.User;
import vn.iotstar.service.UserService;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.util.Constant;
import vn.iotstar.util.RememberMeUtil;

@WebServlet(urlPatterns = "/login")
public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserService service = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute(Constant.SESSION_ACCOUNT) != null) {
            resp.sendRedirect(req.getContextPath() + "/waiting");
            return;
        }

        // Auto login with a valid "remember me" cookie
        String username = RememberMeUtil.read(req);
        if (username != null) {
            User user = service.get(username);
            if (user != null) {
                startSession(req, user);
                resp.sendRedirect(req.getContextPath() + "/waiting");
                return;
            }
            RememberMeUtil.clear(req, resp);
        }
        req.getRequestDispatcher(Constant.Path.LOGIN).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        boolean isRememberMe = "on".equals(req.getParameter("remember"));

        if (username == null || password == null || username.trim().isEmpty() || password.isEmpty()) {
            req.setAttribute("alert", "Tài khoản hoặc mật khẩu không được rỗng");
            req.getRequestDispatcher(Constant.Path.LOGIN).forward(req, resp);
            return;
        }

        User user = service.login(username.trim(), password);
        if (user != null) {
            startSession(req, user);
            if (isRememberMe) {
                RememberMeUtil.save(req, resp, user.getUserName());
            } else {
                RememberMeUtil.clear(req, resp);
            }
            resp.sendRedirect(req.getContextPath() + "/waiting");
        } else {
            req.setAttribute("alert", "Tài khoản hoặc mật khẩu không đúng");
            req.getRequestDispatcher(Constant.Path.LOGIN).forward(req, resp);
        }
    }

    private void startSession(HttpServletRequest req, User user) {
        HttpSession old = req.getSession(false);
        if (old != null) {
            req.changeSessionId(); // prevent session fixation
        }
        HttpSession session = req.getSession(true);
        session.setAttribute(Constant.SESSION_ACCOUNT, user);
    }
}
