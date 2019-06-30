package br.com.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.com.util.Logger;


public class DatabaseDAOImpl implements DatabaseDAO{
	static Logger log = Logger.getLogger(DatabaseDAOImpl.class);
	
    private String url;
    private String driver;
    private String user;
    private String pass;
    private Connection connection;
    /**
     * TODO commentary
     */
	public void close() throws SQLException {
        try {
            if ( getConnection() != null && ! getConnection().isClosed() ) {
                log.info(this+ " :Closing: "+getConnection());
                getConnection().close();
            }
        } catch (Exception e) {
            log.error("Error - " + e.getMessage());
            e.printStackTrace();
        }
	}

	public void commit() throws SQLException {
	       try {
	            if ( getConnection() != null && ! getConnection().getAutoCommit() ) {
	                log.info(this+ " :Commiting: "+ getConnection());
	                getConnection().commit();
	            }
	        } catch (Exception e) {
	            log.error("Error - " + e.getMessage());
	            e.printStackTrace();
	        }
	}

	public boolean connect(boolean commit) throws Exception {
		Connection connection = ConnectionFactory.getConnection();
		setConnection(connection);
        return true;
	}

	public void initialize(String driver, String url, String user, String pass)
			throws Exception {
	        log.info("Initialing DatabaseDAOImpl: "+this);
	        Class.forName( driver );
	        this.setUrl(url);
	        this.setPass(pass);
	        this.setUser(user);
	}

	public void rollback() throws SQLException {
        try {
            if ( getConnection() != null && ! getConnection().getAutoCommit() ) {
                log.info(this+ " :Rolling Back: "+ getConnection());
                getConnection().rollback();
            }
        } catch (Exception e) {
            log.error("Error - " + e.getMessage());
            e.printStackTrace();
        }
		
	}

	public PreparedStatement prepareStatement(String statement) {
        try {
			return getConnection().prepareStatement( statement );
		} catch (SQLException e) {
			log.error("Error - " + e.getMessage());
		}
		return null;
	}
    public final String nativeSQL(String sql) throws SQLException {
        if ( getConnection() != null ) {
            return getConnection().nativeSQL(sql);
        }
        return null;
    }
    
    public final void setAutoCommit(boolean value){
        try {
            if ( getConnection() != null) {
                log.info(this+ " :Setting auto commit : " + value);
                getConnection().setAutoCommit(value);
            }
        } catch (Exception e) {
            log.error("Error - " + e.getMessage());
            e.printStackTrace();
        }
    }
	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getUser() {
		return user;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getPass() {
		return pass;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getDriver() {
		return driver;
	}

}
