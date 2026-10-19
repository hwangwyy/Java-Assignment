-- SQL Server. Chạy một lần trên database của dự án (ví dụ ShoppingService).

-- 1) Cờ kích hoạt tài khoản. Người dùng cũ được coi là đã kích hoạt (DEFAULT 1);
--    tài khoản đăng ký mới được code insert với active = 0 cho tới khi nhập đúng OTP.
IF COL_LENGTH('[User]', 'active') IS NULL
    ALTER TABLE [User] ADD active BIT NOT NULL CONSTRAINT DF_User_active DEFAULT 1;
GO

-- 2) Bảng OTP (kích hoạt tài khoản / quên mật khẩu). Chỉ lưu HMAC của mã, không lưu mã gốc.
IF OBJECT_ID('Otp') IS NULL
CREATE TABLE Otp (
    id         INT IDENTITY(1,1) PRIMARY KEY,
    email      NVARCHAR(255) NOT NULL,
    purpose    VARCHAR(20)   NOT NULL,          -- ACTIVATE | RESET
    code_hash  VARCHAR(64)   NOT NULL,
    expires_at DATETIME2     NOT NULL,
    attempts   INT           NOT NULL DEFAULT 0,
    created_at DATETIME2     NOT NULL,
    CONSTRAINT UQ_Otp_email_purpose UNIQUE (email, purpose)
);
GO

-- 3) Bảng Product: 1 Category - n Product.
--    Không dùng ON DELETE CASCADE để tránh vô tình xóa hàng loạt sản phẩm;
--    ứng dụng chặn xóa danh mục còn sản phẩm.
IF OBJECT_ID('Product') IS NULL
CREATE TABLE Product (
    product_id   INT IDENTITY(1,1) PRIMARY KEY,
    product_name NVARCHAR(255)  NOT NULL,
    description  NVARCHAR(MAX)  NULL,
    price        DECIMAL(18,2)  NOT NULL CHECK (price >= 0),
    image        NVARCHAR(255)  NULL,
    created_date DATETIME2      NOT NULL DEFAULT SYSDATETIME(),
    cate_id      INT            NOT NULL,
    CONSTRAINT FK_Product_Category FOREIGN KEY (cate_id) REFERENCES Category(cate_id)
);
CREATE INDEX IX_Product_cate ON Product(cate_id);
CREATE INDEX IX_Product_created ON Product(created_date DESC, product_id DESC);
GO
