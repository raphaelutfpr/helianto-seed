package org.helianto.core.social;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * Create required tables if do not exist.
 * 
 * @author mauriciofernandesdecastro
 */
public class OAuthConnectionDatabaseBootstrap 
	implements InitializingBean
{
	
	private static final Logger logger = LoggerFactory.getLogger(OAuthConnectionDatabaseBootstrap.class);
	
	private DataSource dataSource;
	
	/**
	 * Data source constructor.
	 * 
	 * @param dataSource
	 */
	public OAuthConnectionDatabaseBootstrap(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}
	
	private String userConnectionTableDdl = "create table core_UserConnection (userId varchar(255) not null,"
			+ "providerId varchar(255) not null,"
			+ "providerUserId varchar(255),"
			+ "rank int not null,"
			+ "displayName varchar(255),"
			+ "profileUrl varchar(512),"
			+ "imageUrl varchar(512),"
			+ "accessToken varchar(255) not null,"
			+ "secret varchar(255),"
			+ "refreshToken varchar(255),"
			+ "expireTime bigint,"
			+ "primary key (userId, providerId, providerUserId));";
	
	private String userConnectionIndexDdl = "create unique index UserConnectionRank on UserConnection(userId, providerId, rank);";

	private String remoteUserTableDdl = "create table core_RemoteUser (id int not null,"
			+ "userKey varchar(255) not null,"
			+ "password varchar(255) not null,"
			+ "firstName varchar(255),"
			+ "lastName varchar(255),"
			+ "displayName varchar(255),"
			+ "profileUrl varchar(512),"
			+ "imageUrl varchar(512),"
			+ "roles varchar(255),"
			+ "providerType varchar(32),"
			+ "primary key (id));";
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			DatabaseMetaData metadata = connection.getMetaData();
			if (!exists(metadata, "core_UserConnection")) {
				logger.info("core_UserConnection does not exist, creating...");
				create(connection, userConnectionTableDdl, userConnectionIndexDdl);
				logger.info("Done");
			}
			if (!exists(metadata, "core_RemoteUser")) {
				create(connection, remoteUserTableDdl);
			}
		} catch (SQLException e) {
			throw new RuntimeException("Unable to create table", e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					
				}
			}
		}
	}
	
	/**
	 * Helper method to check for the table existence.
	 * 
	 * @param metadata
	 * @param tableName
	 * 
	 * @throws SQLException
	 */
	protected boolean exists(DatabaseMetaData metadata, String tableName) throws SQLException {
		boolean exists;
		ResultSet result = metadata.getTables(null, null, tableName, null);
		try {
			// checks if the table exists
			if (result.next()) {
				logger.info("{} table found.", tableName);
				exists = true;
			}
			else {
				result = metadata.getTables(null, null, tableName.toUpperCase(), null);
				if (result.next()) {
					logger.info("{} table found.", tableName.toUpperCase());
					exists = true;
				}
				else {
					exists = false;
				}
			}
		} finally {
			result.close();
		}
		return exists;
	}
	
	/**
	 * Helper method to execute create statements.
	 * 
	 * @param connection
	 * @param ddl
	 * 
	 * @throws SQLException
	 */
	protected void create(Connection connection, String... ddl) throws SQLException {
		Statement stmt = connection.createStatement();
		try {
			for (String d: ddl) {
				stmt.execute(d);
			}
			logger.info("Created.");
		} finally {
			stmt.close();
		}
	}

}
