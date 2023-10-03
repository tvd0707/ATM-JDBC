package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConn {
	private static DbConn instance;
	private static final String DB_URL = "jdbc:mySQL://localhost:3306/atm";
	private static final String username = "root";
	private static final String password = "";
	private Connection connection;

	private DbConn() {
		// Khởi tạo kết nối cơ sở dữ liệu ở đây
		try {
			connection = DriverManager.getConnection(DB_URL, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static synchronized DbConn getInstance() {
		if (instance == null) {
			instance = new DbConn();
		}
		return instance;
	}

	public Connection getConnection() {
		return connection;
	}

	public void closeDB() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}