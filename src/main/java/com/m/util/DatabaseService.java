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
	private static Logger logger = LoggerFactory
			.getLogger(DatabaseService.class);

	private static String driver = "org.apache.derby.jdbc.EmbeddedDriver";
//	private static String protocol = "";
//	private static String connectionString = "derbyDB;create=true;user=root;password=12345";
	private static String connectionString = "jdbc:derby:derbyDB;create=true;user=root;password=12345";
	private static Connection conn;
	private static final String MONSTER_URL_TABLE = "URL";

	private static Connection connect() throws SQLException {
//		return DriverManager.getConnection(protocol + connectionString);
//		return DriverManager.getConnection(protocol + connectionString);
		return DriverManager.getConnection(connectionString);
	}

	private static void createDB() {
//		try {
//			Class.forName(driver).newInstance();
//		} catch (InstantiationException | IllegalAccessException
//				| ClassNotFoundException e) {
//			e.printStackTrace();
//		}
		try (Connection conn = connect();
				Statement stmt = conn.createStatement()) {

			stmt.execute("CREATE TABLE "
					+ MONSTER_URL_TABLE
					+ "(id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),url VARCHAR(500) NOT NULL)");
			logger.info("Table URL created");
			stmt.execute("ALTER TABLE " + MONSTER_URL_TABLE
					+ " ADD UNIQUE (URL)");
			logger.info("Unique Constraint added to table URL");
		} catch (SQLException ex) {
			logger.error("Errro on execution recreation of database.");
			ex.printStackTrace();
		}
	}

	private static void cleanDB() {
		try (Connection conn = connect();
				Statement stmt = conn.createStatement()) {
			stmt.execute("TRUNCATE TABLE " + MONSTER_URL_TABLE);
			logger.info("Table URL cleaned");
		} catch (SQLException ex) {
			logger.error("Errro on execution sql 'TRUNCATE TABLE "
					+ MONSTER_URL_TABLE + "'");
			ex.printStackTrace();
		}
	}
	
	private static void removeTable(){
		try (Connection conn = connect();
				Statement stmt = conn.createStatement()) {
			stmt.execute("DROP TABLE " + MONSTER_URL_TABLE);
			logger.info("Table URL dropped");
		} catch (SQLException ex) {
			logger.error("Errro on execution sql 'TRUNCATE TABLE "
					+ MONSTER_URL_TABLE + "'");
			ex.printStackTrace();
		}

	}

	// Create database table url
	private static void initializeDB() {
		createDB();
//		cleanDB();
		// insertUrl("https://jobberbjudande.monster.se/Test-Test-Management-Test-automation-Stockholm-Stockholm-STHM-Sweden-%C3%85F/11/197542963");
	}

	public long insertUrl(String url) {
		// String url =
		// "https://jobberbjudande.monster.se/Test-Test-Management-Test-automation-Stockholm-Stockholm-STHM-Sweden-%C3%85F/11/197542963";
		String SQL = "insert into " + MONSTER_URL_TABLE + "(url) values(?)";

		long id = 0;

		try (Connection conn = DriverManager.getConnection(connectionString);
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
					logger.warn("SQL Exception 1: " + ex.getMessage());
				}
			}
		} catch (SQLException ex) {
			logger.warn("SQL Exception 2: " + ex.getMessage());
		}
		logger.debug("id: " + id);
		return id;
	}

	private static void createSchema() {
		// Check if db exists in db folder, if doesn't create one.
		logger.info("Creating schema");

		try {
			Class.forName(driver).newInstance();

			// This should be executed if not credentials are to be used.
			conn = DriverManager
					.getConnection("jdbc:derby:derbyDB;create=true");

//			conn = DriverManager.getConnection(protocol + connectionString);

//			logger.info("Database connection:" + conn.toString());

			// Setup security
			conn.setSchema("APP");
			Statement s = conn.createStatement();
			s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(\n"
					+ "    'derby.connection.requireAuthentication', 'true')");
			s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(\n"
					+ "    'derby.authentication.provider', 'BUILTIN')");
			s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(\n"
					+ "    'derby.user.root', '12345')");
			s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(\n"
					+ "    'derby.database.propertiesOnly', 'true')");
			
			conn.commit();
			conn.close();
			
			logger.info("Finished schema creation: ");

		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public static void closeDB() {
		try {
			logger.info("Closing database connection");
			DriverManager.getConnection("jdbc:derby:;shutdown=true");
		} catch (SQLException e) {
//			e.printStackTrace();
			logger.info("Expected exception on shutdown: " + e.getMessage());
		}
	}

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class.forName(driver).newInstance();
//		To remove schema manually delete the folder from OS.
//		1
//		createSchema();
//		initializeDB();
//		closeDB();
		
//		2
		cleanDB();
		closeDB();
//		removeTable();
		
//		logger.info();
	}

}
