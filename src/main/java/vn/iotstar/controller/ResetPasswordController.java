package vn.iotstar.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import vn.iotstar.service.OtpService;
import vn.iotstar.service.UserService;
import vn.iotstar.service.impl.OtpServiceImpl;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.util.Constant;

@WebServlet(urlPatterns = "/reset-password")
public class ResetPasswordController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserService userService = new UserServiceImpl();
    private final OtpService otpService = new OtpServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (email(req) == null) {
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }
        if ("1".equals(req.getParameter("mailFailed"))) {
            req.setAttribute("alert", "Không gửi được email OTP. Vui lòng bấm \"Gửi lại mã\".");
        } else if ("1".equals(req.getParameter("throttled"))) {
            req.setAttribute("info", "Mã OTP đã được gửi gần đây. Vui lòng kiểm tra hộp thư của bạn.");
        } else {
            req.setAttribute("info", "Mã OTP đặt lại mật khẩu đã được gửi tới email của bạn.");
        }
        show(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = email(req);
        if (email == null) {
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }

        if ("resend".equals(req.getParameter("action"))) {
            OtpService.IssueResult r = otpService.issue(email, Constant.OTP_RESET);
            switch (r) {
            case SENT:
                req.setAttribute("info", "Đã gửi lại mã OTP mới.");
                break;
            case THROTTLED:
                req.setAttribute("alert", "Vui lòng đợi một chút trước khi yêu cầu gửi lại mã.");
                break;
            default:
                req.setAttribute("alert", "Không gửi được email OTP. Vui lòng thử lại sau.");
            }
            show(req, resp);
            return;
        }

        String otp = req.getParameter("otp");
        String password = req.getParameter("password") == null ? "" : req.getParameter("password");
        String repassword = req.getParameter("repassword") == null ? "" : req.getParameter("repassword");

        if (otp == null || otp.trim().isEmpty()) {
            req.setAttribute("alert", "Vui lòng nhập mã OTP");
            show(req, resp);
            return;
        }
        if (password.length() < 6) {
            req.setAttribute("alert", "Mật khẩu mới phải có ít nhất 6 ký tự");
            show(req, resp);
            return;
        }
        if (!password.equals(repassword)) {
            req.setAttribute("alert", "Mật khẩu nhập lại không khớp");
            show(req, resp);
            return;
        }

        OtpService.VerifyResult r = otpService.verify(email, Constant.OTP_RESET, otp);
        if (r == OtpService.VerifyResult.OK) {
            userService.resetPassword(email, password);
            userService.activate(email);
            req.getSession().removeAttribute(Constant.SESSION_OTP_EMAIL);
            resp.sendRedirect(req.getContextPath() + "/login?reset=1");
            return;
        }

        req.setAttribute("alert", VerifyController.message(r));
        show(req, resp);
    }

    private static String email(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        return s == null ? null : (String) s.getAttribute(Constant.SESSION_OTP_EMAIL);
    }

    private void show(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("otpEmail", email(req));
        req.getRequestDispatcher(Constant.Path.RESET).forward(req, resp);
    }
}
