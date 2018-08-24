package com.m.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseService {
	private static Logger logger = LoggerFactory.getLogger(DatabaseService.class);

	public static String driver = "org.apache.derby.jdbc.EmbeddedDriver";
	public static String protocol = "jdbc:derby:";
	private static Connection conn;

	private static Connection connect() throws SQLException {
		Connection conn = DriverManager.getConnection(protocol
				+ "derbyDB;create=true;user=root;password=12345");
		return conn;
	}

	private static void createDB() {
		try (Connection conn = connect();
				Statement stmt = conn.createStatement()) {
			stmt.execute("DROP TABLE URL");
			logger.info("Table URL dropped");
			stmt.execute("CREATE TABLE URL(id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),url VARCHAR(500) NOT NULL)");
			logger.info("Table URL created");
			stmt.execute("ALTER TABLE URL ADD UNIQUE (URL)");
			logger.info("Unique Constraint added to table URL");
			
			stmt.execute("TRUNCATE TABLE URL");
			logger.info("Table URL cleaned");
		} catch (SQLException ex) {
			logger.error("Errro on execution sql");
			ex.printStackTrace();
		}
	}

	// Create database table url
	private static void initializeDB() {
		createDB();
//		insertUrl("https://jobberbjudande.monster.se/Test-Test-Management-Test-automation-Stockholm-Stockholm-STHM-Sweden-%C3%85F/11/197542963");
	}

	public static long insertUrl(String url) {
//		String url = "https://jobberbjudande.monster.se/Test-Test-Management-Test-automation-Stockholm-Stockholm-STHM-Sweden-%C3%85F/11/197542963";
		String SQL = "insert into URL(url) values(?)";

		long id = 0;

		try (Connection conn = connect();
				PreparedStatement pstmt = conn.prepareStatement(SQL,
						Statement.RETURN_GENERATED_KEYS)) {

			pstmt.setString(1, url);

			int affectedRows = pstmt.executeUpdate();
			// check the affected rows
			if (affectedRows > 0) {
				// get the ID back
				try (ResultSet rs = pstmt.getGeneratedKeys()) {
					if (rs.next()) {
						id = rs.getLong(1);
					}
				} catch (SQLException ex) {
					System.out.println(ex.getMessage());
				}
			}
		} catch (SQLException ex) {
			logger.debug(ex.getMessage());
		}
		logger.debug("id: " + id);
		return id;
	}
// Run it once to create the database
//	static {
//		// Check if db exists in db folder, if doesn't create one.
//
//		try {
//			Class.forName(driver).newInstance();
//			
//			// This should be executed if not credentials are to be used.
//			conn = DriverManager
//			.getConnection(protocol + "derbyDB;create=true");
//
//			conn = DriverManager
//					.getConnection(protocol + "derbyDB;create=true;user=root;password=12345");
//
//			logger.info("Database connection:" + conn.toString());
//
//			// Setup security
//			conn.setSchema("APP");
//			Statement s = conn.createStatement();
//			s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(\n"
//					+ "    'derby.connection.requireAuthentication', 'true')");
//			s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(\n"
//					+ "    'derby.authentication.provider', 'BUILTIN')");
//			s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(\n"
//					+ "    'derby.user.root', '12345')");
//			s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(\n"
//					+ "    'derby.database.propertiesOnly', 'true')");
//
//		} catch (InstantiationException | IllegalAccessException
//				| ClassNotFoundException | SQLException e) {
//			e.printStackTrace();
//		}
//
//	}

	public void closeDB() {
		try {
			DriverManager.getConnection("jdbc:derby:;shutdown=true");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		DatabaseService db = new DatabaseService();
		String url = "";
//		logger.info("Inserted url:" + db.inseBoortURL(url));
		initializeDB();

	}

}
