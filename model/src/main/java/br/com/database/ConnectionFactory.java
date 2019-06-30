package br.com.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
    
    private static Connection conn;
    private static String url = null;
    private static String driver = null; 
    private static String user = null; 
    private static String pass = null; 
    
    private static Properties properties = null;
    
    static{
    	loadProperties();
    	initDatabaseConfig();
    }
    
    private static void initDatabaseConfig()    {
    	String database_type = properties.getProperty("database.type");
    	url = properties.getProperty("database." + database_type + ".url");
    	driver = properties.getProperty("database." + database_type + ".driver");
    	user = properties.getProperty("database." + database_type + ".user");
    	pass = properties.getProperty("database." + database_type + ".pass");
    }
    /**
      * Load a Properties File
      * @param propsFile
      * @return Properties
      * @throws IOException
      */
    private static void loadProperties(){
        try {
        	//"database-config.properties"
            URL path_url = ConnectionFactory.class.getResource( "/" );
            if(path_url==null){
                throw new NullPointerException("invalid properties file path" + path_url);
            }
            URI path_uri = new URI(path_url.toString() + "database-config.properties");
            File file = new File(path_uri);
            if(file!=null && file.exists() && file.isFile()){
                 FileInputStream fis = new FileInputStream(file);
                 properties = new Properties();
                 properties.load(fis);    
                 fis.close();
            }else{
                throw new NullPointerException("invalid properties file path");
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e){
            e.printStackTrace();    
        } catch (IOException e){
            e.printStackTrace();        
        }
    }
    
    private static void connect() throws SQLException, ClassNotFoundException{
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pass);
            
    }
    
    public static void closeConnection(){
        if(conn != null) {
            try {
                conn.close();
            } catch(SQLException e){
                e.printStackTrace();
            } finally {
                conn = null;
            }
        }
    }
    
    public static Connection getConnection() throws SQLException, ClassNotFoundException{
        //if(conn == null){
            connect();
        //}
        return conn;
    }

}
