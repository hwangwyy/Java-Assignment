package vn.iotstar.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import vn.iotstar.util.AppConfig;

/** Datasource configuration (values come from app.properties). DAO classes extend this class. */
public class DBConnection {
    private final String driver = AppConfig.get("db.driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
    private final String url = AppConfig.get("db.url", "jdbc:sqlserver://localhost:1433;databaseName=ShoppingServiceMVC");
    private final String userID = AppConfig.get("db.user", "sa");
    private final String password = AppConfig.get("db.password", "");

    public Connection getConnection() throws Exception {
        Class.forName(driver);
        return DriverManager.getConnection(url, userID, password);
    }
}
