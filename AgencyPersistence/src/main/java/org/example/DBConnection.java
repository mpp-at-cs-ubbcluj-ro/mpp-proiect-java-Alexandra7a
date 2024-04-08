package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private Properties jdbcProps;
    private static final Logger logger= LogManager.getLogger(); // for logging
    private Connection instance=null;

    public DBConnection(Properties props){
        jdbcProps=props;
    }

    /**
     * this method cannot be accessed and is used, and it is used only to get a connection
     * when there is none*/
    private Connection getNewConnection(){
        logger.traceEntry();

        String url=jdbcProps.getProperty("jdbc.url");
        String user=jdbcProps.getProperty("jdbc.user");
        String pass=jdbcProps.getProperty("jdbc.pass");
        logger.info("trying to connect to database ... {}",url);
        logger.info("user: {}",user);
        logger.info("pass: {}", pass);

        Connection con=null;
        try {

            // prepared fore more types of dbs and their
            // properties(postgres(url,pass,user),sqlite(url), so on...)
            if (user!=null && pass!=null)
                con= DriverManager.getConnection(url,user,pass);
            else
            {
                /*SQLiteConfig config = new SQLiteConfig();
                config.enforceForeignKeys(true);
                con=DriverManager.getConnection(url,config.toProperties());  */
                con=DriverManager.getConnection(url);
            }

        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error getting connection "+e);
        }
        return con;
    }
    /**
     * the below method check if there is a connection, if not make one, else
     * reuse the existing one. PUBLICLY AVAILABLE*/

    public Connection getConnection(){
        logger.traceEntry();
        try {
            if (instance==null || instance.isClosed())
                instance=getNewConnection();

        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(instance);
        return instance;
    }
}
