package vn.iotstar.controller;

import java.io.IOException;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import vn.iotstar.model.User;
import vn.iotstar.service.OtpService;
import vn.iotstar.service.UserService;
import vn.iotstar.service.impl.OtpServiceImpl;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.util.Constant;

@WebServlet(urlPatterns = "/forgot-password")
public class ForgotPasswordController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private final UserService userService = new UserServiceImpl();
    private final OtpService otpService = new OtpServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(Constant.Path.FORGOT).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email") == null ? "" : req.getParameter("email").trim();

        if (email.isEmpty() || !EMAIL.matcher(email).matches()) {
            req.setAttribute("alert", "Vui lòng nhập địa chỉ email hợp lệ");
            req.getRequestDispatcher(Constant.Path.FORGOT).forward(req, resp);
            return;
        }

        User user = userService.getByEmail(email);
        if (user == null) {
            req.setAttribute("alert", "Không tìm thấy tài khoản với email này");
            req.getRequestDispatcher(Constant.Path.FORGOT).forward(req, resp);
            return;
        }

        OtpService.IssueResult r = otpService.issue(email, Constant.OTP_RESET);
        req.getSession(true).setAttribute(Constant.SESSION_OTP_EMAIL, email);
        String flag = "";
        if (r == OtpService.IssueResult.MAIL_FAILED) {
            flag = "?mailFailed=1";
        } else if (r == OtpService.IssueResult.THROTTLED) {
            flag = "?throttled=1";
        }
        resp.sendRedirect(req.getContextPath() + "/reset-password" + flag);
    }
}
