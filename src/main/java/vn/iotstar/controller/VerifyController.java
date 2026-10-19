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

/** Account activation: the user types the OTP that was emailed after registration. */
@WebServlet(urlPatterns = "/verify")
public class VerifyController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserService userService = new UserServiceImpl();
    private final OtpService otpService = new OtpServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (email(req) == null) {
            resp.sendRedirect(req.getContextPath() + "/register");
            return;
        }
        if ("1".equals(req.getParameter("mailFailed"))) {
            req.setAttribute("alert", "Không gửi được email OTP. Vui lòng bấm \"Gửi lại mã\".");
        } else if ("1".equals(req.getParameter("inactive"))) {
            req.setAttribute("info", "Tài khoản chưa được kích hoạt. Chúng tôi đã gửi mã OTP tới email của bạn.");
        } else {
            req.setAttribute("info", "Chúng tôi đã gửi mã OTP gồm 6 chữ số tới email của bạn.");
        }
        show(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = email(req);
        if (email == null) {
            resp.sendRedirect(req.getContextPath() + "/register");
            return;
        }

        if ("resend".equals(req.getParameter("action"))) {
            OtpService.IssueResult r = otpService.issue(email, Constant.OTP_ACTIVATE);
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

        OtpService.VerifyResult r = otpService.verify(email, Constant.OTP_ACTIVATE, req.getParameter("otp"));
        if (r == OtpService.VerifyResult.OK) {
            userService.activate(email);
            req.getSession().removeAttribute(Constant.SESSION_OTP_EMAIL);
            resp.sendRedirect(req.getContextPath() + "/login?activated=1");
            return;
        }
        req.setAttribute("alert", message(r));
        show(req, resp);
    }

    static String message(OtpService.VerifyResult r) {
        switch (r) {
        case INVALID:
            return "Mã OTP không đúng";
        case TOO_MANY_ATTEMPTS:
            return "Bạn đã nhập sai quá nhiều lần. Hãy yêu cầu gửi lại mã mới.";
        default:
            return "Mã OTP đã hết hạn hoặc không tồn tại. Hãy yêu cầu gửi lại mã mới.";
        }
    }

    private static String email(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        return s == null ? null : (String) s.getAttribute(Constant.SESSION_OTP_EMAIL);
    }

    private void show(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("otpEmail", email(req));
        req.getRequestDispatcher(Constant.Path.VERIFY).forward(req, resp);
    }
}
