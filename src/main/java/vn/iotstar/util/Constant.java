package vn.iotstar.util;

public final class Constant {
    private Constant() {
    }

    public static final String SESSION_ACCOUNT = "account";
    public static final String COOKIE_REMEMBER = "remember_me";

    public static final String CATEGORY_FOLDER = "category";
    public static final String PRODUCT_FOLDER = "product";

    public static final String OTP_ACTIVATE = "ACTIVATE";
    public static final String OTP_RESET = "RESET";
    /** Session attribute: the email an OTP was sent to (activate / reset flow in progress). */
    public static final String SESSION_OTP_EMAIL = "otpEmail";

    public static final int PRODUCT_PAGE_SIZE = 6;
    public static final int HOME_PRODUCT_COUNT = 10;

    public static final int ROLE_ADMIN = 1;
    public static final int ROLE_MANAGER = 2;
    public static final int ROLE_USER = 5;

    public static final class Path {
        private Path() {
        }

        public static final String LOGIN = "/WEB-INF/views/login.jsp";
        public static final String REGISTER = "/WEB-INF/views/register.jsp";
        public static final String HOME = "/WEB-INF/views/home.jsp";
        public static final String ADMIN_HOME = "/WEB-INF/views/admin/home.jsp";
        public static final String MANAGER_HOME = "/WEB-INF/views/manager/home.jsp";
        public static final String CATEGORY_LIST = "/WEB-INF/views/admin/list-category.jsp";
        public static final String CATEGORY_ADD = "/WEB-INF/views/admin/add-category.jsp";
        public static final String CATEGORY_EDIT = "/WEB-INF/views/admin/edit-category.jsp";
        public static final String VERIFY = "/WEB-INF/views/verify.jsp";
        public static final String FORGOT = "/WEB-INF/views/forgot-password.jsp";
        public static final String RESET = "/WEB-INF/views/reset-password.jsp";
        public static final String PRODUCT_LIST = "/WEB-INF/views/product-list.jsp";
        public static final String PRODUCT_DETAIL = "/WEB-INF/views/product-detail.jsp";
        public static final String ADMIN_PRODUCT_LIST = "/WEB-INF/views/admin/list-product.jsp";
        public static final String ADMIN_PRODUCT_FORM = "/WEB-INF/views/admin/form-product.jsp";
    }
}
