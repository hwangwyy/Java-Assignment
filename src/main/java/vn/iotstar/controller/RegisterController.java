package vn.iotstar.controller;

import java.io.IOException;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import vn.iotstar.service.UserService;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.util.Constant;

@WebServlet(urlPatterns = "/register")
public class RegisterController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private static final Pattern USERNAME = Pattern.compile("^[A-Za-z0-9_.-]{3,50}$");
    private final UserService service = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute(Constant.SESSION_ACCOUNT) != null) {
            resp.sendRedirect(req.getContextPath() + "/waiting");
            return;
        }
        req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = trim(req.getParameter("username"));
        String fullname = trim(req.getParameter("fullname"));
        String email = trim(req.getParameter("email"));
        String phone = trim(req.getParameter("phone"));
        String password = req.getParameter("password") == null ? "" : req.getParameter("password");
        String repassword = req.getParameter("repassword") == null ? "" : req.getParameter("repassword");

        String error = null;
        if (username.isEmpty() || fullname.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            error = "Vui lòng nhập đầy đủ thông tin";
        } else if (!USERNAME.matcher(username).matches()) {
            error = "Tài khoản gồm 3-50 ký tự chữ, số, '_', '.', '-'";
        } else if (!EMAIL.matcher(email).matches()) {
            error = "Email không hợp lệ";
        } else if (password.length() < 6) {
            error = "Mật khẩu phải có ít nhất 6 ký tự";
        } else if (!password.equals(repassword)) {
            error = "Mật khẩu nhập lại không khớp";
        } else if (service.checkExistEmail(email)) {
            error = "Email đã tồn tại!";
        } else if (service.checkExistUsername(username)) {
            error = "Tài khoản đã tồn tại!";
        } else if (service.checkExistPhone(phone)) {
            error = "Số điện thoại đã tồn tại!";
        }

        if (error != null) {
            req.setAttribute("alert", error);
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }

        if (service.register(username, password, email, fullname, phone)) {
            resp.sendRedirect(req.getContextPath() + "/login?registered=1");
        } else {
            req.setAttribute("alert", "System error!");
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
        }
    }

    private static String trim(String s) {
        return s == null ? "" : s.trim();
    }
}
